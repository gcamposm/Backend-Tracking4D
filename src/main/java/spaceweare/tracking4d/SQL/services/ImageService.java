package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import spaceweare.tracking4d.Exceptions.FileDeleteException;
import spaceweare.tracking4d.Exceptions.GetObjectException;
import spaceweare.tracking4d.Exceptions.IdNotFoundException;
import spaceweare.tracking4d.Exceptions.RutNotFoundException;
import spaceweare.tracking4d.FileManagement.service.FileStorageService;
import spaceweare.tracking4d.SQL.dao.CustomerDao;
import spaceweare.tracking4d.SQL.dao.DetectionDao;
import spaceweare.tracking4d.SQL.dao.ImageDao;
import spaceweare.tracking4d.SQL.dto.responses.ImageResponse;
import spaceweare.tracking4d.SQL.models.Customer;
import spaceweare.tracking4d.SQL.models.Detection;
import spaceweare.tracking4d.SQL.models.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final CustomerDao customerDao;
    private final ImageDao imageDao;
    private final DetectionDao detectionDao;
    private final FileStorageService fileStorageService;
    public ImageService(ImageDao imageDao, FileStorageService fileStorageService, CustomerDao customerDao, DetectionDao detectionDao) {
        this.imageDao = imageDao;
        this.fileStorageService = fileStorageService;
        this.customerDao = customerDao;
        this.detectionDao = detectionDao;
    }

    public Image create(Image image){
        return imageDao.save(image);
    }

    public Image readById(Integer id){
        if(imageDao.findById(id).isPresent()){
            return imageDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Image> readAll(){
        return imageDao.findAll();
    }

    public Image update(Image image, Integer id){
        if(imageDao.findById(id).isPresent()){
            Image imageFound = imageDao.findById(id).get();
            imageFound.setName(image.getName());
            imageFound.setExtension(image.getExtension());
            imageFound.setCustomer(image.getCustomer());
            imageFound.setPrincipal(image.getPrincipal());
            imageFound.setPath(image.getPath());
            imageFound.setDetections(image.getDetections());
            return imageDao.save(imageFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(imageDao.findById(id).isPresent()){
            imageDao.delete(imageDao.findById(id).get());
            return "deleted";
        }
        else{
            return  null;
        }
    }

    public Image chargeData(List<Float> descriptorList, String path, String userName) {
        String[] data = userName.split(" ");
        String firstName = data[0];
        String lastName = data[1];
        if(!customerDao.findCustomerByFirstNameAndLastName(firstName, lastName).isPresent())
        {
            Customer customer = new Customer();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            return createDescriptorWithCustomer(customerDao.save(customer), descriptorList, path);
        }
        Customer customer = customerDao.findCustomerByFirstNameAndLastName(firstName, lastName).get();
        return createDescriptorWithCustomer(customer, descriptorList, path);
    }

    private Image createDescriptorWithCustomer(Customer customer, List<Float> descriptorList, String path) {
        Image image = new Image();
        image.setPath(path);
        image.setCustomer(customer);
        imageDao.save(image);
        for (Float descriptorFor: descriptorList
        ) {
            Detection detection = new Detection();
            detection.setImage(image);
            detection.setValue(descriptorFor);
            detectionDao.save(detection);
        }
        return imageDao.save(image);
    }

    public String chargeFaces(List<Map<Object, Object>> faces) {
        System.out.println(faces.toArray().toString());
        return faces.toArray().toString();
    }

    public Object getAllFaces() {
        List<Map<Object, Object>> faces = new ArrayList<>();
        List<Customer> customers = customerDao.findAll();
        for (Customer customer : customers
        ) {
            Map<Object, Object> face = new HashMap<>();
            List<Map<Object, Object>> descriptors = new ArrayList<>();
            List<Image> images = imageDao.findAllByCustomer(customer);
            for (Image image:images
                 ) {
                Map<Object, Object> descriptor = new HashMap<>();
                List<Float> floats = new ArrayList<>();
                List<Detection> detections = image.getDetections();
                for (Detection detection : detections
                ) {
                    floats.add(detection.getValue());
                }
                descriptor.put("descriptor", floats);
                descriptor.put("path", image.getPath());
                descriptors.add(descriptor);
            }
            face.put("descriptors", descriptors);
            face.put("user", customer.getFirstName());
            faces.add(face);
        }
        return faces;
    }

    public List<ImageResponse> getAllImagesFromCustomer(int customerId){
        List<ImageResponse> imageResponseList = new ArrayList<>();
        Customer customer = customerDao.findById(customerId).get();
        if(customer != null){
            List<Image> imageList = customer.getImages();
            for (Image image: imageList
            ) {
                ImageResponse imageResponse = new ImageResponse();
                imageResponse.setId(image.getId());
                imageResponse.setName(image.getName());
                imageResponse.setExtension(image.getExtension());
                imageResponse.setPrincipal(image.getPrincipal());
                imageResponse.setUrl("/web/get/" + customer.getRut() + "/" + imageList.indexOf(image));
                imageResponseList.add(imageResponse);
            }
            return imageResponseList;
        }
        else{
            throw new IdNotFoundException("The customer could not be found");
        }
    }

    public List<ImageResponse> uploadMultipleImages(String customerRut, MultipartFile[] fileList) throws IOException {
        Customer customer = customerDao.findByRut(customerRut);
        List<ImageResponse> imageResponseList = new ArrayList<>();
        if(customer != null) {
            for (MultipartFile file : fileList
            ) {
                System.out.println("OriginalFileName: " + file.getOriginalFilename());
                imageResponseList.add(uploadImage(customer, file.getOriginalFilename(), file.getBytes()));
            }
            return imageResponseList;
        }else{
            throw new RutNotFoundException("The customer with rut: " + customerRut + " could not be found");
        }
    }

    //get the principal image with customer rut in bytes
    public byte[] getPrincipalImageFromCustomer(String customerRut) {
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        Customer customer = customerDao.findByRut(customerRut);
        if (customer != null) {
            try {
                Image principalImage = imageDao.findImageById(imageDao.findImageByPrincipalEquals(customer));
                if(principalImage == null){
                    Path path = Paths.get(absoluteFilePath + "/nodisponible.png");
                    return Files.readAllBytes(path);
                }
                String rpath =  absoluteFilePath + "/" + principalImage.getName() + principalImage.getExtension(); // whatever path you used for storing the file
                Path path = Paths.get(rpath);
                return Files.readAllBytes(path);
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
                return new byte[]{0};
            }
        }else {
            return new byte[]{0};
        }
    }
    //GET THE IMAGE BYTE ARRAY FROM CUSTOMER RUT AND INDEX
    public byte[] getImageFromCustomerRutAndIndex(String customerRut, Integer index) {
        Customer customer = customerDao.findByRut(customerRut);
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();

        List<Image> imageList;
        if(customer != null){
            imageList = customer.getImages();
        }else{
            throw new RutNotFoundException("Could not found the customer with rut: " + customerRut);
        }
        if(index > imageList.size()){
            throw new GetObjectException("The index is bigger than the actual number of images in the customer");
        }
        try {
            Image image = imageList.get(index);
            if(image == null){
                Path path = Paths.get(absoluteFilePath + "/nodisponible.png");
                return Files.readAllBytes(path);
            }
            String rpath =  absoluteFilePath + "/" + customer.getFirstName() + " " + customer.getLastName() + "/" + image.getName() + image.getExtension(); // whatever path you used for storing the file
            Path path = Paths.get(rpath);
            return Files.readAllBytes(path);
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
            return new byte[]{0};
        }

    }

    //GET THE IMAGE BYTE ARRAY FROM CUSTOMER Name AND INDEX
    public byte[] getImageFromCustomerNameAndIndex(String customerName, Integer index) {
        System.out.println(customerName);
        String[] data = customerName.split(" ");
        String firstName = data[0];
        String lastName = data[1];
        System.out.println(firstName + " " + lastName);
        Customer customer = customerDao.findCustomerByFirstNameAndLastName(firstName, lastName).get();
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();

        List<Image> imageList;
        if(customer != null){
            imageList = customer.getImages();
        }else{
            throw new RutNotFoundException("Could not found the customer with name: " + customerName);
        }
        if(index > imageList.size()){
            throw new GetObjectException("The index is bigger than the actual number of images in the customer");
        }
        try {
            Image image = imageList.get(index-1);
            if(image == null){
                Path path = Paths.get(absoluteFilePath + "/nodisponible.png");
                return Files.readAllBytes(path);
            }
            String rpath =  absoluteFilePath + "/" + customerName + "/" + image.getName() + image.getExtension(); // whatever path you used for storing the file
            Path path = Paths.get(rpath);
            return Files.readAllBytes(path);
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
            return new byte[]{0};
        }

    }
    //SELECT THE PRINCIPAL IMAGE OF A CUSTOMER USING THE CUSTOMER RUT AND IMAGE ID
    public ImageResponse selectPrincipalImageFromCustomer(String customerRut, Integer imageId) {
        Customer customer = customerDao.findByRut(customerRut);
        if(customer != null){
            List<Image> imageList = customer.getImages();
            imageList.forEach(image -> image.setPrincipal(false));
            customerDao.save(customer);

            Image returnedImage = imageList.stream().filter(
                    image -> image.getId().equals(imageId)).collect(toSingleton());
            returnedImage.setPrincipal(true);
            return mapToImageResponse(imageDao.save(returnedImage));
        }
        else{
            throw new RutNotFoundException("Could not found the customer with rut: " + customerRut);
        }
    }

    //HELPERS METHODS
    //_______________________________________________________
    //_______________________________________________________
    private String getImageName(Customer customer){
        if(customer.getImages() != null)
        {
            Integer index = customer.getImages().size() + 1;
            return index.toString();
        }
        return "1";
    }

    //create image by customer and filename
    static Image createImageWithCustomer(Customer customerToUpdate, String ext, String fileName) {
        Image image = new Image();
        image.setCustomer(customerToUpdate);
        image.setName(fileName);
        image.setExtension(ext);
        image.setPrincipal(false);
        String path = "/data/users/"+customerToUpdate.getRut()+"/"+fileName+ext;
        image.setPath(path);
        List<Image> imageList = customerToUpdate.getImages();
        if (imageList.size() == 0) {
            image.setPrincipal(true);
        }
        imageList.add(image);
        customerToUpdate.setImages(imageList);
        return image;
    }

    //upload a single image for customer
    public ImageResponse uploadImage(Customer customerToUpdate, String imageName, byte[] fileBytes) throws IOException {
        String ext = imageName.substring(imageName.lastIndexOf("."));
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        String fileName = getImageName(customerToUpdate);
        String directory = absoluteFilePath + "/" + customerToUpdate.getRut();
        File convertFile = new File(directory + "/" + fileName + ext);
        try(FileOutputStream fos = new FileOutputStream(convertFile)) {
            fos.write(fileBytes);
            Image image = createImageWithCustomer(customerToUpdate, ext, fileName);
            customerDao.save(customerToUpdate);
            return mapToImageResponse(image);
            //return customerDao.save(customerToUpdate);
        }catch(Exception e){
            throw new IOException("The image could not be uploaded" + e.getMessage());
        }
    }

    //check if the customer have or no a principal image
    private boolean havePrincipalImage(Customer customer){
        Image principalImage = imageDao.findImageById(imageDao.findImageByPrincipalEquals(customer));
        return principalImage != null;
    }

    //get the image url for display in a web browser
    public String getImageUrl(Customer customer){
        String url = "http://104.131.15.22:8080/backend-tracking4d";
        String rut = "";
        String rutReplaced = "";
        if(havePrincipalImage(customer)){
            rut = customer.getRut();
            rutReplaced = rut.replaceAll("[^a-zA-Z0-9]", "");
            System.out.println("Rut: " +  rut + " rutReplaced: " + rutReplaced);
            return url.concat("/customers/web/image/preview/" + rutReplaced);
        }
        else{
            return "";
        }
    }

    private ImageResponse mapToImageResponse(Image image){
        ImageResponse imageResponse = new ImageResponse();
        imageResponse.setId(image.getId());
        imageResponse.setName(image.getName());
        imageResponse.setExtension(image.getExtension());
        imageResponse.setPrincipal(image.getPrincipal());
        return imageResponse;
    }

    private static <T> Collector<T, ?, T> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }

    public void deleteImage(Integer imageId) {
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        Image image = imageDao.findImageById(imageId);
        if(image != null){
            String rpath = absoluteFilePath + "/" + image.getName() + image.getExtension();
            imageDao.delete(image);
            File file = new File(rpath);
            try {
                if (file.delete()) {
                    imageDao.delete(image);
                }
            }catch (Exception e){

                throw new FileDeleteException("The file could not be deleted: " + e.getMessage());
            }
        }else{
            throw new IdNotFoundException("The image with id: " + imageId + " could not be found");
        }
    }

    public List<String> pathsByCustomer(Customer customer) {
        List<Image> images = imageDao.findAllByCustomer(customer);
        List<String> paths = new ArrayList<>();
        for (Image image:images
             ) {
            paths.add(image.getPath());
        }
        return paths;
    }

    public Object pathsWithCustomer() {
        List<Map<Object, Object>> pathWithCustomerList = new ArrayList<>();
        List<Customer> customers = customerDao.findAll();
        for (Customer customer:customers
             ) {
            Map<Object, Object> pathWithCustomer = new HashMap<>();

            List<Image> images = imageDao.findAllByCustomer(customer);

            List<String> paths = new ArrayList<>();
            for (Image image:images
            ) {
                paths.add(image.getPath());
            }
            pathWithCustomer.put("paths", paths);
            pathWithCustomer.put("customer", customer);
            pathWithCustomerList.add(pathWithCustomer);
        }
        return pathWithCustomerList;
    }
}
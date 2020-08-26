package spaceweare.tracking4d.SQL.services;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import spaceweare.tracking4d.Exceptions.FileDeleteException;
import spaceweare.tracking4d.Exceptions.GetObjectException;
import spaceweare.tracking4d.Exceptions.IdNotFoundException;
import spaceweare.tracking4d.Exceptions.RutNotFoundException;
import spaceweare.tracking4d.FileManagement.service.FileStorageService;
import spaceweare.tracking4d.SQL.dao.PersonDao;
import spaceweare.tracking4d.SQL.dao.DetectionDao;
import spaceweare.tracking4d.SQL.dao.ImageDao;
import spaceweare.tracking4d.SQL.dto.responses.ImageResponse;
import spaceweare.tracking4d.SQL.models.Person;
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

    private final PersonDao personDao;
    private final ImageDao imageDao;
    private final DetectionDao detectionDao;
    private final FileStorageService fileStorageService;
    public ImageService(ImageDao imageDao, FileStorageService fileStorageService, PersonDao personDao, DetectionDao detectionDao) {
        this.imageDao = imageDao;
        this.fileStorageService = fileStorageService;
        this.personDao = personDao;
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
            imageFound.setPerson(image.getPerson());
            imageFound.setPrincipal(image.getPrincipal());
            imageFound.setPath(image.getPath());
            imageFound.setDetections(image.getDetections());
            imageFound.setCamera(image.getCamera());
            imageFound.setDeleted(image.getDeleted());
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

    public List<String> deleteWithPath(String path){
        if(imageDao.findImageByPath(path).isPresent()){
            Image image = imageDao.findImageByPath(path).get();
            image.setDeleted(true);
            imageDao.save(image);
            Path absoluteFilePath = fileStorageService.getFileStorageLocation();
            //String fileName = getImageName(personToUpdate);
            String directory = absoluteFilePath + "/users/" + image.getPerson().getRut() + "/" + image.getName() + image.getExtension();
            File fileToDelete = new File(directory);
            fileToDelete.delete();
            return pathsOnePerson(image.getPerson());
        }
        return  null;
    }

    public Image chargeData(List<Float> descriptorList, String path, String personRut) {
        if(personDao.findPersonByRut(personRut).isPresent())
        {
            Person person = personDao.findPersonByRut(personRut).get();
            person.setToTrain(false);
            personDao.save(person);
            return createDescriptorWithPerson(person, descriptorList, path);
        }
        Person person = new Person();
        person.setRut(personRut);
        person.setToTrain(false);
        personDao.save(person);
        return createDescriptorWithPerson(personDao.save(person), descriptorList, path);
    }

    private Image createDescriptorWithPerson(Person person, List<Float> descriptorList, String path) {
        if(imageDao.findImageByPath(path).isPresent())
        {
            Image image = imageDao.findImageByPath(path).get();
            return createDescriptorWithImage(person, descriptorList, image);
        }
        else{
            Image image = new Image();
            image.setPath(path);
            return createDescriptorWithImage(person, descriptorList, image);
        }
    }

    private Image createDescriptorWithImage(Person person, List<Float> descriptorList, Image image) {
        image.setPerson(person);
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
        return faces.toArray().toString();
    }

    public Object getAllFaces() {
        List<Map<Object, Object>> faces = new ArrayList<>();
        List<Person> people = personDao.findAllByDeleted(false);
        for (Person person : people
        ) {
            Map<Object, Object> face = new HashMap<>();
            List<Map<Object, Object>> descriptors = new ArrayList<>();
            List<Image> images = imageDao.findAllByPersonAndDeleted(person, false);
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
            face.put("user", person.getRut());
            faces.add(face);
        }
        return faces;
    }

    public List<ImageResponse> getAllImagesFromPerson(int personId){
        List<ImageResponse> imageResponseList = new ArrayList<>();
        Person person = personDao.findById(personId).get();
        if(person != null){
            List<Image> imageList = person.getImages();
            for (Image image: imageList
            ) {
                ImageResponse imageResponse = new ImageResponse();
                imageResponse.setId(image.getId());
                imageResponse.setName(image.getName());
                imageResponse.setExtension(image.getExtension());
                imageResponse.setPrincipal(image.getPrincipal());
                imageResponse.setUrl("/web/get/" + person.getRut() + "/" + imageList.indexOf(image));
                imageResponseList.add(imageResponse);
            }
            return imageResponseList;
        }
        else{
            throw new IdNotFoundException("The person could not be found");
        }
    }

    public List<String> uploadMultipleImages(String personRut, MultipartFile[] fileList) throws IOException {
        //List<ImageResponse> imageResponseList = new ArrayList<>();
        if(personDao.findPersonByRut(personRut).isPresent()) {
            Person person = personDao.findPersonByRut(personRut).get();
            person.setToTrain(true);
            personDao.save(person);
            List<String> paths = new ArrayList<>();
            for (MultipartFile file : fileList
            ) {
                paths.add(uploadImage(person, file.getOriginalFilename(), file.getBytes()));
            }
            return paths;
        }else{
            throw new RutNotFoundException("The person with rut: " + personRut + " could not be found");
        }
    }

    //get the principal image with person rut in bytes
    public byte[] getPrincipalImageFromPerson(String personRut) {
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        Person person = personDao.findByRut(personRut);
        if (person != null) {
            try {
                Image principalImage = imageDao.findImageById(imageDao.findImageByPrincipalEquals(person));
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
    //GET THE IMAGE BYTE ARRAY FROM PERSON RUT AND INDEX
    public byte[] getImageFromPersonRutAndIndex(String personRut, Integer index) {
        Person person = personDao.findByRut(personRut);
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();

        List<Image> imageList;
        if(person != null){
            imageList = person.getImages();
        }else{
            throw new RutNotFoundException("Could not found the person with rut: " + personRut);
        }
        if(index > imageList.size()){
            throw new GetObjectException("The index is bigger than the actual number of images in the person");
        }
        try {
            Image image = imageList.get(index);
            if(image == null){
                Path path = Paths.get(absoluteFilePath + "/nodisponible.png");
                return Files.readAllBytes(path);
            }
            String rpath =  absoluteFilePath + "/" + person.getFirstName() + " " + person.getLastName() + "/" + image.getName() + image.getExtension(); // whatever path you used for storing the file
            Path path = Paths.get(rpath);
            return Files.readAllBytes(path);
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
            return new byte[]{0};
        }

    }

    //GET THE IMAGE BYTE ARRAY FROM PERSON Name AND INDEX
    public byte[] getImageFromPersonNameAndIndex(String personName, Integer index) {
        String[] data = personName.split(" ");
        String firstName = data[0];
        String lastName = data[1];
        Person person = personDao.findPersonByFirstNameAndLastName(firstName, lastName).get();
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();

        List<Image> imageList;
        if(person != null){
            imageList = person.getImages();
        }else{
            throw new RutNotFoundException("Could not found the person with name: " + personName);
        }
        if(index > imageList.size()){
            throw new GetObjectException("The index is bigger than the actual number of images in the person");
        }
        try {
            Image image = imageList.get(index-1);
            if(image == null){
                Path path = Paths.get(absoluteFilePath + "/nodisponible.png");
                return Files.readAllBytes(path);
            }
            String rpath =  absoluteFilePath + "/" + personName + "/" + image.getName() + image.getExtension(); // whatever path you used for storing the file
            Path path = Paths.get(rpath);
            return Files.readAllBytes(path);
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
            return new byte[]{0};
        }

    }
    //SELECT THE PRINCIPAL IMAGE OF A PERSON USING THE PERSON RUT AND IMAGE ID
    public ImageResponse selectPrincipalImageFromPerson(String personRut, Integer imageId) {
        Person person = personDao.findByRut(personRut);
        if(person != null){
            List<Image> imageList = person.getImages();
            imageList.forEach(image -> image.setPrincipal(false));
            personDao.save(person);

            Image returnedImage = imageList.stream().filter(
                    image -> image.getId().equals(imageId)).collect(toSingleton());
            returnedImage.setPrincipal(true);
            return mapToImageResponse(imageDao.save(returnedImage));
        }
        else{
            throw new RutNotFoundException("Could not found the person with rut: " + personRut);
        }
    }

    //HELPERS METHODS
    //_______________________________________________________
    //_______________________________________________________
    private String getImageName(Person person){
        if(person.getImages() != null)
        {
            Integer index = person.getImages().size() + 1;
            return person.getRut() + "_" + index.toString();
        }
        return "1";
    }

    //create image by person and filename
    static Image createImageWithPerson(Person personToUpdate, String ext, String fileName) {
        Image image = new Image();
        image.setPerson(personToUpdate);
        image.setName(fileName);
        image.setExtension(ext);
        image.setPrincipal(false);
        image.setDeleted(false);
        String path = "/data/users/"+ personToUpdate.getRut()+"/"+fileName+ext;
        image.setPath(path);
        List<Image> imageList = personToUpdate.getImages();
        if (imageList.size() == 0) {
            image.setPrincipal(true);
        }
        imageList.add(image);
        personToUpdate.setImages(imageList);
        return image;
    }

    //upload a single image for person
    public String uploadImage(Person personToUpdate, String imageName, byte[] fileBytes) throws IOException {
        String ext = imageName.substring(imageName.lastIndexOf("."));
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        String fileName = getImageName(personToUpdate);
        String directory = absoluteFilePath + "/users/" + personToUpdate.getRut();
        File convertFile = new File(directory + "/" + fileName + ext);
        try(FileOutputStream fos = new FileOutputStream(convertFile)) {
            fos.write(fileBytes);
            Image image = createImageWithPerson(personToUpdate, ext, fileName);
            personDao.save(personToUpdate);
            //return mapToImageResponse(image);
            return image.getPath();
            //return personDao.save(personToUpdate);
        }catch(Exception e){
            throw new IOException("The image could not be uploaded" + e.getMessage());
        }
    }

    public String uploadPhotos(Person personToUpdate, String imageValue) throws IOException {
        byte[] imageByte= Base64.decodeBase64(imageValue);
        String ext = ".jpg";
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        String fileName = getImageName(personToUpdate);
        String directory = absoluteFilePath + "/users/" + personToUpdate.getRut();
        File convertFile = new File(directory + "/" + fileName + ext);
        try(FileOutputStream fos = new FileOutputStream(convertFile)) {
            fos.write(imageByte);
            Image image = createImageWithPerson(personToUpdate, ext, fileName);
            personToUpdate.setToTrain(true);
            personDao.save(personToUpdate);
            return image.getPath();
        }catch(Exception e){
            throw new IOException("The image could not be uploaded" + e.getMessage());
        }
    }

    //check if the person have or no a principal image
    private boolean havePrincipalImage(Person person){
        Image principalImage = imageDao.findImageById(imageDao.findImageByPrincipalEquals(person));
        return principalImage != null;
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

    public List<String> pathsByPerson(Person person) {
        List<Image> images = imageDao.findAllByPersonAndDeleted(person, false);
        List<String> paths = new ArrayList<>();
        for (Image image:images
             ) {
            paths.add(image.getPath());
        }
        return paths;
    }

    public Object pathsWithPerson() {
        List<Map<Object, Object>> pathWithPersonList = new ArrayList<>();
        List<Person> people = personDao.findAllByUnknownAndDeleted(false, false);
        for (Person person : people
             ) {
            Map<Object, Object> pathWithPerson = new HashMap<>();

            List<Image> images = imageDao.findAllByPersonAndDeleted(person, false);

            List<String> paths = new ArrayList<>();
            for (Image image:images
            ) {
                paths.add(image.getPath());
            }
            pathWithPerson.put("paths", paths);
            pathWithPerson.put("person", person);
            pathWithPersonList.add(pathWithPerson);
        }
        return pathWithPersonList;
    }

    public Object pathsWithOnePerson(Person person) {
            Map<Object, Object> pathWithPerson = new HashMap<>();

            List<Image> images = imageDao.findAllByPersonAndDeleted(person, false);

            List<String> paths = new ArrayList<>();
            for (Image image:images
            ) {
                paths.add(image.getPath());
            }
            pathWithPerson.put("paths", paths);
            pathWithPerson.put("person", person);
        return pathWithPerson;
    }

    public List<String> pathsOnePerson(Person person) {
        List<Image> images = imageDao.findAllByPersonAndDeleted(person, false);
        List<String> paths = new ArrayList<>();
        for (Image image:images
        ) {
            paths.add(image.getPath());
        }
        return paths;
    }

    public List<Float> detectionsByPath(Image image) {
        List<Detection> detections = detectionDao.findAllByImage(image);
        List<Float> descriptors = new ArrayList<>();
        for (Detection detection:detections
             ) {
            descriptors.add(detection.getValue());
        }
        return descriptors;
    }
}
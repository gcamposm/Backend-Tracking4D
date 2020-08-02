package spaceweare.tracking4d.SQL.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spaceweare.tracking4d.Exceptions.IdNotFoundException;
import spaceweare.tracking4d.Exceptions.RutNotFoundException;
import spaceweare.tracking4d.FileManagement.service.FileStorageService;
import spaceweare.tracking4d.SQL.dao.CustomerDao;
import spaceweare.tracking4d.SQL.dao.ImageDao;
import spaceweare.tracking4d.SQL.dao.MatchDao;
import spaceweare.tracking4d.SQL.models.Customer;
import spaceweare.tracking4d.SQL.models.Image;
import spaceweare.tracking4d.SQL.models.Match;
import spaceweare.tracking4d.SQL.services.CustomerService;
import spaceweare.tracking4d.SQL.services.MatchService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final ImageDao imageDao;
    private final CustomerDao customerDao;
    private final MatchDao matchDao;
    private final MatchService matchService;
    private final FileStorageService fileStorageService;
    private final CustomerService customerService;

    public CustomerController(CustomerDao customerDao, CustomerService customerService, ImageDao imageDao, FileStorageService fileStorageService, MatchDao matchDao, MatchService matchService) {
        this.customerService = customerService;
        this.customerDao = customerDao;
        this.imageDao = imageDao;
        this.fileStorageService = fileStorageService;
        this.matchDao = matchDao;
        this.matchService = matchService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<Customer> create (@RequestBody Customer customer){
        try{
            customer.setUnknown(false);
            return ResponseEntity.ok(customerService.create(customer));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<Customer>> readAll(){
        try{
            return ResponseEntity.ok(customerService.readAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Customer> readById (@PathVariable("id") Integer id){
        try{
            return ResponseEntity.ok(customerService.readById(id));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<Customer> update (@PathVariable("id") Integer id, @RequestBody Customer customer){
        try{
            return ResponseEntity.ok(customerService.update(customer, id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> delete (@PathVariable Integer id){
        try{
            return ResponseEntity.ok(customerService.delete(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "{id}/getprincipalimage", method = RequestMethod.GET)
    public byte[] getPrincipalImage(@PathVariable Integer id, HttpServletRequest request) {
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        Customer customerToUpdate = customerDao.findById(id).get();
        if (customerToUpdate != null) {
            try {
                Image principalImage = imageDao.findImageById(imageDao.findImageByPrincipalEquals(customerToUpdate));
                if(principalImage == null){
                    Path path = Paths.get(absoluteFilePath + "/nodisponible.png");
                    byte[] data = Files.readAllBytes(path);
                    return data;
                }
                String rpath =  absoluteFilePath + "/" + principalImage.getName() + principalImage.getExtension(); // whatever path you used for storing the file
                Path path = Paths.get(rpath);
                byte[] data = Files.readAllBytes(path);
                return data;
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
                byte[] data = {0};
                return data;
            }
        }else {
            byte[] data = {0};
            return data;
        }
    }

    @RequestMapping(value = "web/{id}/getprincipalimage", method = RequestMethod.GET)
    public byte[] getPrincipalImageForWeb(@PathVariable Integer id) {
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        Customer customerToUpdate = customerDao.findById(id).get();
        if (customerToUpdate != null) {
            try {
                Image principalImage = imageDao.findImageById(imageDao.findImageByPrincipalEquals(customerToUpdate));
                if(principalImage == null){
                    Path path = Paths.get(absoluteFilePath + "/nodisponible.png");
                    byte[] data = Files.readAllBytes(path);
                    return data;
                }
                String rpath =  absoluteFilePath + "/" + principalImage.getName() + principalImage.getExtension(); // whatever path you used for storing the file
                Path path = Paths.get(rpath);
                byte[] data = Files.readAllBytes(path);
                return data;
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
                byte[] data = {0};
                return data;
            }
        }else {
            byte[] data = {0};
            return data;
        }
    }
    @GetMapping(
            value = "/web/image/preview/{customerRut}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    @ResponseBody
    public byte[] previewPrincipalImage(@PathVariable String customerRut){
        Customer customer = customerDao.findByRut(customerRut);
        if(customer != null) {
            try {
                return getPrincipalImageForWeb(customer.getId());
            }catch (Exception e){
                throw new IdNotFoundException("Could not found the customer images", e);
            }
        }else{
            throw new RutNotFoundException("The customer with rut: " + customerRut + " could not be found");
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "{id}/allImages", method = RequestMethod.GET)
    public List<byte[]> allImageByCustomer(@PathVariable Integer id, HttpServletRequest request) {

        Customer customer = customerDao.findById(id).get();
        List<byte[]> images = new ArrayList<>();
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        if (customer != null) {
            try {
                for (Image image: customer.getImages()
                ) {
                    String rpath =  absoluteFilePath+ "/" + image.getName() + image.getExtension(); // whatever path you used for storing the file
                    Path path = Paths.get(rpath);
                    byte[] data = Files.readAllBytes(path);
                    images.add(data);
                }
                return images;
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
                byte[] data = {0};
                images.add(data);
                return images;
            }
        } else {
            byte[] data = {0};
            images.add(data);
            return images;
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/deleteImage", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity deleteImage (@RequestParam("customerId") Integer customerId , @RequestParam("position") Integer position){
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        Customer customerToUpdate = customerDao.findById(customerId).get();
        if (customerToUpdate != null) {
            Image principalImage = customerToUpdate.getImages().get(position);
            if (principalImage != null) {
                String rpath = absoluteFilePath + "/" + principalImage.getName() + principalImage.getExtension();
                File file = new File(rpath);
                imageDao.deleteImage(principalImage.getId());
                return ResponseEntity.ok(file.delete());
            }
        }
        return ResponseEntity.badRequest().build();
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/write", method = RequestMethod.GET)
    public ResponseEntity<Object> writeFile() throws IOException
    {
        Date day = new Date();
        Instant firstCurrent = day.toInstant();
        Instant secondCurrent = day.toInstant();
        LocalDateTime firstLocalDate = LocalDateTime.ofInstant(firstCurrent,
                ZoneId.systemDefault());
        LocalDateTime secondLocalDate = LocalDateTime.ofInstant(secondCurrent,
                ZoneId.systemDefault()).plusDays(1);
        //List<Match> matches = matchDao.findMatchByHourBetween(firstLocalDate, secondLocalDate);
        List<Match>matches = matchDao.findAll();
        /*List<Customer> customerList = new ArrayList<>();
        for (Match match:matches
             ) {
            if(!customerList.contains(match.getCustomer())){
                customerList.add(match.getCustomer());
            }
        }*/
        if (matches.size() == 0) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body("No existen clientes");
        }
        //Path path = fileStorageService.getFileStorageLocation();
        Path filePath = fileStorageService.getFileStorageLocation().resolve("output.xlsx").normalize();
        customerService.writeXlsx(matches, filePath.toString(), day);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Se creó el archivo de salida.");

    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadProductExcel(HttpServletRequest request){
        Resource resource = fileStorageService.loadFileAsResource("output.xlsx");
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
package spaceweare.tracking4d.SQL.controllers;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spaceweare.tracking4d.FileManagement.service.FileStorageService;
import spaceweare.tracking4d.SQL.dao.PersonDao;
import spaceweare.tracking4d.SQL.dao.ImageDao;
import spaceweare.tracking4d.SQL.models.Image;
import spaceweare.tracking4d.SQL.models.Person;
import spaceweare.tracking4d.SQL.services.ImageService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;
    private final ImageDao imageDao;
    private final PersonDao personDao;
    private final FileStorageService fileStorageService;
    public ImageController(ImageService imageService, ImageDao imageDao, PersonDao personDao, FileStorageService fileStorageService) {
        this.imageService = imageService;
        this.imageDao = imageDao;
        this.personDao = personDao;
        this.fileStorageService = fileStorageService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<Image> create (@RequestBody Image image){
        try{
            return ResponseEntity.ok(imageService.create(image));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<Image>> readAll(){
        try{
            return ResponseEntity.ok(imageService.readAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Image> readById (@PathVariable("id") Integer id){
        try{
            return ResponseEntity.ok(imageService.readById(id));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<Image> update (@PathVariable("id") Integer id, @RequestBody Image image){
        try{
            return ResponseEntity.ok(imageService.update(image, id));
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
            return ResponseEntity.ok(imageService.delete(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/deleteWithPath", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> deleteWithPath (@RequestParam("path") String path){
        try{
            return ResponseEntity.ok(imageService.deleteWithPath(path));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/create/withData", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity chargeData(@RequestParam("descriptor") List<Float> descriptorList,
                                     @RequestParam("path") String path,
                                     @RequestParam("rut") String personRut){
        try{
            if(descriptorList.size() != 128)
            {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(imageService.chargeData(descriptorList, path, personRut));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/chargeFaces", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity chargeFaces(@RequestParam("descriptor") List<Map<Object, Object>> faces){
        try{
            return ResponseEntity.ok(imageService.chargeFaces(faces));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getAllFaces")
    @ResponseBody
    public ResponseEntity getAllFaces(){
        try{
            return ResponseEntity.ok(imageService.getAllFaces());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/pathsByPerson/{personId}")
    @ResponseBody
    public ResponseEntity pathsByPerson(@PathVariable Integer personId){
        try{
            if(personDao.findById(personId).isPresent())
            {
                return ResponseEntity.ok(imageService.pathsByPerson(personDao.findById(personId).get()));
            }
        else{
            return ResponseEntity.status(500).body("The person with id: " + personId + " could not be found");
        }
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/detectionsByPath", method = RequestMethod.GET, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity detectionsByPath(@RequestParam("path") String path){
        try{
            if(imageDao.findImageByPath(path).isPresent())
            {
                return ResponseEntity.ok(imageService.detectionsByPath(imageDao.findImageByPath(path).get()));
            }
            else{
                return ResponseEntity.status(500).body("The image with path: " + path + " could not be found");
            }
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/pathsWithPerson")
    @ResponseBody
    public ResponseEntity pathsWithPerson(){
        try{
            return ResponseEntity.ok(imageService.pathsWithPerson());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    //RETURN A LIST OF IMAGE RESPONSE THAT CONTAINS THE PATH
    @GetMapping("/web/allImageFromPerson/{personId}")
    @ResponseBody
    public ResponseEntity getAllImageFromPerson(@PathVariable("personId") int personId){
        try{
            return ResponseEntity.status(200).body(imageService.getAllImagesFromPerson(personId));
        }catch (Exception e){
            return ResponseEntity.status(500).body("Could not get the images " + e.getMessage());
        }
    }

    //RETURN THE IMAGE IN BYTE FORMAT USING THE PATH IN THE "getAllImageFromPerson" RESPONSE
    @GetMapping("/web/get/{personRut}/{index}")
    @ResponseBody
    public ResponseEntity getImageFromPersonRutAndIndex(@PathVariable("personRut") String personRut,
                                                          @PathVariable("index") Integer index){
        try{
            return ResponseEntity.status(200).body(imageService.getImageFromPersonRutAndIndex(personRut, index));
        }catch (Exception e){
            return ResponseEntity.status(500).body("Could not get the principal image");
        }
    }

    //RETURN THE IMAGE IN BYTE FORMAT USING THE PATH IN THE "getAllImageFromPerson" RESPONSE
    @GetMapping("/web/get/labels/{personName}/{index}")
    @ResponseBody
    public ResponseEntity getImageFromPersonNameAndIndex(@PathVariable("personName") String personName,
                                                          @PathVariable("index") Integer index){
        try{
            return ResponseEntity.status(200).body(imageService.getImageFromPersonNameAndIndex(personName, index));
        }catch (Exception e){
            return ResponseEntity.status(500).body("Could not get the principal image");
        }
    }

    //RETURN THE BYTE ARRAY OF THE PRINCIPAL IMAGE
    @GetMapping("/web/getPrincipalImageFromPerson/{personRut}")
    @ResponseBody
    public ResponseEntity getPrincipalImageFromPerson(@PathVariable("personRut") String personRut){
        try{
            return ResponseEntity.status(200).body(imageService.getPrincipalImageFromPerson(personRut));
        }catch (Exception e){
            return ResponseEntity.status(500).body("Could not get the principal image");
        }
    }

    @RequestMapping(value = "web/{personRut}/getprincipalimage", method = RequestMethod.GET)
    public byte[] getprincipalimageForWeb(@PathVariable String personRut) {
        //String relativeWebPath = "WEB-INF/classes/static/imagenes";
        //String absoluteFilePath = context.getRealPath(relativeWebPath);
        //String absoluteFilePath =  "src/main/resources/static/imagenes/";
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        Person person = personDao.findByRut(personRut);
        if (person != null) {
            try {
                Image principalImage = imageDao.findImageById(imageDao.findImageByPrincipalEquals(person));
                if(principalImage == null){
                    Path path = Paths.get(absoluteFilePath + "/nodisponible.jpg");
                    byte[] data = Files.readAllBytes(path);
                    return data;
                }
                String rpath =  absoluteFilePath + "/users/" + person.getRut() + "/" + principalImage.getName() + principalImage.getExtension(); // whatever path you used for storing the file
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

    //SELECT THE PRINCIPAL IMAGE OF A PERSON
    @PostMapping("/selectPrincipalImageFromPerson")
    @ResponseBody
    public ResponseEntity selectPrincipalImageFromPerson(@RequestParam("personRut") String personRut,
                                                          @RequestParam("imageId") Integer imageId){
        try{
            return ResponseEntity.status(200).body(imageService.selectPrincipalImageFromPerson(personRut, imageId));
        }catch (Exception e){
            return ResponseEntity.status(500).body("Could not select the principal image: " + e.getMessage());
        }
    }

    //DELETE A IMAGE USING THE ID
    @DeleteMapping("/delete/{imageId}")
    @ResponseBody
    public ResponseEntity selectPrincipalImageFromPerson(@PathVariable("imageId") Integer imageId){
        try{
            imageService.deleteImage(imageId);
            return ResponseEntity.status(200).body("The image has been deleted successfully");
        }catch (Exception e){
            return ResponseEntity.status(500).body("Could not select the principal image: " + e.getMessage());
        }
    }

    //UPLOAD MULTIPLE IMAGES USING THE PERSON RUT AND THE ARRAY OF FILES
    //RETURN A LIST OF IMAGES RESPONSE THAT'S CONTAINS THE URL AND OTHER PARAMETERS
    @PostMapping("/uploadImages/{personRut}")
    @ResponseBody
    public ResponseEntity uploadImages(@PathVariable String personRut ,
                                       @RequestParam("file") MultipartFile[] files){
        try{
            if(personDao.findPersonByRut(personRut).isPresent()) {
                return ResponseEntity.status(200).body(imageService.uploadMultipleImages(personRut, files));
            }else{
                return ResponseEntity.status(500).body("The person with rut: " + personRut + " could not be found");
            }
        }catch (Exception e){
            return ResponseEntity.status(500).body("Could not upload the images: " + e.getMessage());
        }
    }
    //UPLOAD ONE IMAGE USING THE PERSON RUT AND THE ARRAY OF FILES
    //RETURN A IMAGES RESPONSE THAT'S CONTAINS THE URL AND OTHER PARAMETERS
    @PostMapping("/uploadImage/{personRut}")
    @ResponseBody
    public ResponseEntity uploadImage(@PathVariable String personRut ,
                                      @RequestParam("file") MultipartFile file){
        try{
            if(personDao.findPersonByRut(personRut).isPresent()) {
                return ResponseEntity.status(200).body(imageService.uploadImage(personDao.findPersonByRut(personRut).get(), file.getOriginalFilename(), file.getBytes()));
            }else{
                return ResponseEntity.status(500).body("The person with rut: " + personRut + " could not be found");
            }
        }catch (Exception e){
            return ResponseEntity.status(500).body("Could not upload the images: " + e.getMessage());
        }
    }

    @PostMapping("/uploadPhotos/{personRut}")
    @ResponseBody
    public ResponseEntity uploadPhotos(@PathVariable String personRut ,
                                      @RequestParam("imageValue") String imageValue){
        try{
            if(personDao.findPersonByRut(personRut).isPresent()) {
                return ResponseEntity.status(200).body(imageService.uploadPhotos(personDao.findPersonByRut(personRut).get(), imageValue));
            }else{
                return ResponseEntity.status(500).body("The person with rut: " + personRut + " could not be found");
            }
        }catch (Exception e){
            return ResponseEntity.status(500).body("Could not upload the images: " + e.getMessage());
        }
    }

    //METHODS EXTRA
    @GetMapping("/web/get/preview/{personRut}/{index}")
    @ResponseBody
    public void getImageForPreview(@PathVariable("personRut") String personRut,
                                   @PathVariable("index") Integer index,
                                   HttpServletResponse response){
        try{
            String url = "https://localhost:8080/images/web/get/"+ personRut + "/" + index;
            printHtml(url, response);
        }catch (Exception e){
            System.out.println("Ha ocurrido un problemon");
        }
    }
    //METHODS EXTRA
    @GetMapping("/web/get/previewPrincipal/{personRut}")
    @ResponseBody
    public void previewPrincipal(@PathVariable("personRut") String personRut,
                                 HttpServletResponse response){
        try{
            String url = "https://localhost:9443/images/web/getPrincipalImageFromPerson/"+ personRut;
            printHtml(url, response);
        }catch (Exception e){
            System.out.println("Ha ocurrido un problemon");
        }
    }


    //HELPER
    private void printHtml(String url, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        pw.println("<img src='" + url + "' width=\"1000\" height=\"1000\">");
        pw.close();
    }
}
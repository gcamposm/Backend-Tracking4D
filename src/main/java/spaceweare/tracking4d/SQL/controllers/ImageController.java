package spaceweare.tracking4d.SQL.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spaceweare.tracking4d.SQL.dao.CustomerDao;
import spaceweare.tracking4d.SQL.models.Image;
import spaceweare.tracking4d.SQL.services.ImageService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;
    private final CustomerDao customerDao;

    public ImageController(ImageService imageService, CustomerDao customerDao) {
        this.imageService = imageService;
        this.customerDao = customerDao;
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

    @GetMapping("/pathsByCustomer/{customerId}")
    @ResponseBody
    public ResponseEntity pathsByCustomer(@PathVariable Integer customerId){
        try{
            if(customerDao.findById(customerId).isPresent())
            {
                return ResponseEntity.ok(imageService.pathsByCustomer(customerDao.findById(customerId).get()));
            }
        else{
            return ResponseEntity.status(500).body("The customer with id: " + customerId + " could not be found");
        }
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //RETURN A LIST OF IMAGE RESPONSE THAT CONTAINS THE PATH
    @GetMapping("/web/allImageFromCustomer/{customerId}")
    @ResponseBody
    public ResponseEntity getAllImageFromCustomer(@PathVariable("customerId") int customerId){
        try{
            return ResponseEntity.status(200).body(imageService.getAllImagesFromCustomer(customerId));
        }catch (Exception e){
            return ResponseEntity.status(500).body("Could not get the images " + e.getMessage());
        }
    }

    //RETURN THE IMAGE IN BYTE FORMAT USING THE PATH IN THE "getAllImageFromCustomer" RESPONSE
    @GetMapping("/web/get/{customerRut}/{index}")
    @ResponseBody
    public ResponseEntity getImageFromCustomerRutAndIndex(@PathVariable("customerRut") String customerRut,
                                                          @PathVariable("index") Integer index){
        try{
            return ResponseEntity.status(200).body(imageService.getImageFromCustomerRutAndIndex(customerRut, index));
        }catch (Exception e){
            return ResponseEntity.status(500).body("Could not get the principal image");
        }
    }

    //RETURN THE IMAGE IN BYTE FORMAT USING THE PATH IN THE "getAllImageFromCustomer" RESPONSE
    @GetMapping("/web/get/labels/{customerName}/{index}")
    @ResponseBody
    public ResponseEntity getImageFromCustomerNameAndIndex(@PathVariable("customerName") String customerName,
                                                          @PathVariable("index") Integer index){
        try{
            return ResponseEntity.status(200).body(imageService.getImageFromCustomerNameAndIndex(customerName, index));
        }catch (Exception e){
            return ResponseEntity.status(500).body("Could not get the principal image");
        }
    }

    //RETURN THE BYTE ARRAY OF THE PRINCIPAL IMAGE
    @GetMapping("/web/getPrincipalImageFromCustomer/{customerRut}")
    @ResponseBody
    public ResponseEntity getPrincipalImageFromCustomer(@PathVariable("customerRut") String customerRut){
        try{
            return ResponseEntity.status(200).body(imageService.getPrincipalImageFromCustomer(customerRut));
        }catch (Exception e){
            return ResponseEntity.status(500).body("Could not get the principal image");
        }
    }

    //SELECT THE PRINCIPAL IMAGE OF A CUSTOMER
    @PostMapping("/selectPrincipalImageFromCustomer")
    @ResponseBody
    public ResponseEntity selectPrincipalImageFromCustomer(@RequestParam("customerRut") String customerRut,
                                                          @RequestParam("imageId") Integer imageId){
        try{
            return ResponseEntity.status(200).body(imageService.selectPrincipalImageFromCustomer(customerRut, imageId));
        }catch (Exception e){
            return ResponseEntity.status(500).body("Could not select the principal image: " + e.getMessage());
        }
    }

    //DELETE A IMAGE USING THE ID
    @DeleteMapping("/delete/{imageId}")
    @ResponseBody
    public ResponseEntity selectPrincipalImageFromCustomer(@PathVariable("imageId") Integer imageId){
        try{
            imageService.deleteImage(imageId);
            return ResponseEntity.status(200).body("The image has been deleted successfully");
        }catch (Exception e){
            return ResponseEntity.status(500).body("Could not select the principal image: " + e.getMessage());
        }
    }

    //UPLOAD MULTIPLE IMAGES USING THE CUSTOMER RUT AND THE ARRAY OF FILES
    //RETURN A LIST OF IMAGES RESPONSE THAT'S CONTAINS THE URL AND OTHER PARAMETERS
    @PostMapping("/uploadImages/{customerRut}")
    @ResponseBody
    public ResponseEntity uploadImages(@PathVariable String customerRut ,
                                       @RequestParam("file") MultipartFile[] files){
        try{
            return ResponseEntity.status(200).body(imageService.uploadMultipleImages(customerRut, files));
        }catch (Exception e){
            return ResponseEntity.status(500).body("Could not upload the images: " + e.getMessage());
        }
    }
    //UPLOAD ONE IMAGE USING THE CUSTOMER RUT AND THE ARRAY OF FILES
    //RETURN A IMAGES RESPONSE THAT'S CONTAINS THE URL AND OTHER PARAMETERS
    @PostMapping("/uploadImage/{customerRut}")
    @ResponseBody
    public ResponseEntity uploadImage(@PathVariable String customerRut ,
                                      @RequestParam("file") MultipartFile file){
        try{
            if(customerDao.findCustomerByRut(customerRut).isPresent()) {
                return ResponseEntity.status(200).body(imageService.uploadImage(customerDao.findCustomerByRut(customerRut).get(), file.getOriginalFilename(), file.getBytes()));
            }else{
                return ResponseEntity.status(500).body("The customer with rut: " + customerRut + " could not be found");
            }
        }catch (Exception e){
            return ResponseEntity.status(500).body("Could not upload the images: " + e.getMessage());
        }
    }

    //METHODS EXTRA
    @GetMapping("/web/get/preview/{customerRut}/{index}")
    @ResponseBody
    public void getImageForPreview(@PathVariable("customerRut") String customerRut,
                                   @PathVariable("index") Integer index,
                                   HttpServletResponse response){
        try{
            String url = "https://localhost:8080/images/web/get/"+ customerRut + "/" + index;
            printHtml(url, response);
        }catch (Exception e){
            System.out.println("Ha ocurrido un problemon");
        }
    }
    //METHODS EXTRA
    @GetMapping("/web/get/previewPrincipal/{customerRut}")
    @ResponseBody
    public void previewPrincipal(@PathVariable("customerRut") String customerRut,
                                 HttpServletResponse response){
        try{
            String url = "https://localhost:9443/images/web/getPrincipalImageFromCustomer/"+ customerRut;
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
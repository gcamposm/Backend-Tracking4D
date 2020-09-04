package spaceweare.tracking4d.SQL.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spaceweare.tracking4d.Exceptions.IdNotFoundException;
import spaceweare.tracking4d.Exceptions.RutNotFoundException;
import spaceweare.tracking4d.FileManagement.service.FileStorageService;
import spaceweare.tracking4d.SQL.dao.PersonDao;
import spaceweare.tracking4d.SQL.dao.ImageDao;
import spaceweare.tracking4d.SQL.dao.MatchDao;
import spaceweare.tracking4d.SQL.models.Person;
import spaceweare.tracking4d.SQL.models.Image;
import spaceweare.tracking4d.SQL.models.Match;
import spaceweare.tracking4d.SQL.services.PersonService;

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
@RequestMapping("/persons")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private final ImageDao imageDao;
    private final PersonDao personDao;
    private final MatchDao matchDao;
    private final FileStorageService fileStorageService;
    private final PersonService personService;

    public PersonController(PersonDao personDao, PersonService personService, ImageDao imageDao, FileStorageService fileStorageService, MatchDao matchDao) {
        this.personService = personService;
        this.personDao = personDao;
        this.imageDao = imageDao;
        this.fileStorageService = fileStorageService;
        this.matchDao = matchDao;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create (@RequestBody Person person){
        try{
            return ResponseEntity.ok(personService.create(person));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<Person>> readAll(){
        try{
            return ResponseEntity.ok(personService.readAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Person> readById (@PathVariable("id") Integer id){
        try{
            return ResponseEntity.ok(personService.readById(id));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<Person> update (@PathVariable("id") Integer id, @RequestBody Person person){
        try{
            return ResponseEntity.ok(personService.update(person, id));
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
            return ResponseEntity.ok(personService.delete(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/byRut/{personRut}")
    @ResponseBody
    public ResponseEntity deleteByRut(@PathVariable String personRut){
        try{
            return ResponseEntity.ok(personService.deleteByRut(personRut));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/byRut/{personRut}")
    @ResponseBody
    public ResponseEntity byRut(@PathVariable String personRut){
        try{
            return ResponseEntity.ok(personService.byRut(personRut));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/setActual/{rut}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity setActual (@PathVariable String rut){
        try{
            return ResponseEntity.ok(personService.setActual(rut));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getActual")
    @ResponseBody
    public ResponseEntity getActual(){
        try{
            return ResponseEntity.ok(personService.getActual());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/contactsBetweenPersons")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity contactsBetweenPersons (@RequestParam("day") @DateTimeFormat(pattern = "yyyy-MM-dd") Date day){
        try{
            return ResponseEntity.ok(personService.contactsBetweenPersons(day));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "{id}/getprincipalimage", method = RequestMethod.GET)
    public byte[] getPrincipalImage(@PathVariable Integer id, HttpServletRequest request) {
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        Person personToUpdate = personDao.findById(id).get();
        if (personToUpdate != null) {
            try {
                Image principalImage = imageDao.findImageById(imageDao.findImageByPrincipalEquals(personToUpdate));
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
        Person personToUpdate = personDao.findById(id).get();
        if (personToUpdate != null) {
            try {
                Image principalImage = imageDao.findImageById(imageDao.findImageByPrincipalEquals(personToUpdate));
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
            value = "/web/image/preview/{personRut}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    @ResponseBody
    public byte[] previewPrincipalImage(@PathVariable String personRut){
        Person person = personDao.findByRut(personRut);
        if(person != null) {
            try {
                return getPrincipalImageForWeb(person.getId());
            }catch (Exception e){
                throw new IdNotFoundException("Could not found the person images", e);
            }
        }else{
            throw new RutNotFoundException("The person with rut: " + personRut + " could not be found");
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "{id}/allImages", method = RequestMethod.GET)
    public List<byte[]> allImageByPerson(@PathVariable Integer id, HttpServletRequest request) {

        Person person = personDao.findById(id).get();
        List<byte[]> images = new ArrayList<>();
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        if (person != null) {
            try {
                for (Image image: person.getImages()
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
    public ResponseEntity deleteImage (@RequestParam("personId") Integer personId , @RequestParam("position") Integer position){
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        Person personToUpdate = personDao.findById(personId).get();
        if (personToUpdate != null) {
            Image principalImage = personToUpdate.getImages().get(position);
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
    @RequestMapping(value = "/write", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> writeFile(@RequestParam("firstDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date firstDay,
                                            @RequestParam("secondDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date lastDay,
                                            @RequestParam("covid") Boolean covid) throws IOException
    {
        Instant firstCurrent = firstDay.toInstant();
        Instant secondCurrent = lastDay.toInstant();
        LocalDateTime firstLocalDate = LocalDateTime.ofInstant(firstCurrent,
                ZoneId.systemDefault()).plusDays(-1);
        LocalDateTime secondLocalDate = LocalDateTime.ofInstant(secondCurrent,
                ZoneId.systemDefault()).plusDays(1);
        /*LocalDateTime secondLocalDate = LocalDateTime.ofInstant(secondCurrent,
                ZoneId.systemDefault()).plusDays(1);*/
        List<Match> matches;
        if(covid)
        {
            matches = matchDao.findAllByHighTemperature(true);
        }
        else{
            matches = matchDao.findMatchByHourBetween(firstLocalDate, secondLocalDate);
        }
        if (matches.size() == 0) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body("\"No se captaron clientes este día");
        }
        //Path path = fileStorageService.getFileStorageLocation();
        Path filePath = fileStorageService.getFileStorageLocation().resolve("output.xlsx").normalize();
        System.out.println("dir: "+filePath.toString());
        personService.writeXlsx(matches, filePath.toString(), lastDay, covid);
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

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/createUnknown")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity createUnknown (@RequestParam("photoUnknown") String photoUnknown,
                                         @RequestParam("descriptors") List<Float> descriptors){
        try{
            return ResponseEntity.ok(personService.createUnknown(photoUnknown, descriptors));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/prueba")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity prueba (@RequestParam("day") String day,
                                  @RequestParam("personId") Integer personId){
        try{
            return ResponseEntity.ok(personService.prueba(day, personId));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getUnknowns")
    @ResponseBody
    public ResponseEntity<List<Person>> getUnknowns(){
        try{
            return ResponseEntity.ok(personService.getUnknowns());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
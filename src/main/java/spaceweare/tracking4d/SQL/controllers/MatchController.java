package spaceweare.tracking4d.SQL.controllers;

import org.apache.commons.math3.stat.descriptive.summary.Product;
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
import spaceweare.tracking4d.FileManagement.service.FileStorageService;
import spaceweare.tracking4d.SQL.dao.MatchDao;
import spaceweare.tracking4d.SQL.models.Match;
import spaceweare.tracking4d.SQL.services.MatchService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/matches")
public class MatchController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private final MatchService matchService;
    private final MatchDao matchDao;
    private final FileStorageService fileStorageService;
    public MatchController(MatchService matchService, FileStorageService fileStorageService, MatchDao matchDao) {
        this.matchService = matchService;
        this.matchDao = matchDao;
        this.fileStorageService = fileStorageService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<Match> create (@RequestBody Match match){
        try{
            return ResponseEntity.ok(matchService.create(match));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<Match>> readAll(){
        try{
            return ResponseEntity.ok(matchService.readAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Match> readById (@PathVariable("id") Integer id){
        try{
            return ResponseEntity.ok(matchService.readById(id));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<Match> update (@PathVariable("id") Integer id, @RequestBody Match match){
        try{
            return ResponseEntity.ok(matchService.update(match, id));
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
            return ResponseEntity.ok(matchService.delete(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getAllByContact/{contactId}")
    @ResponseBody
    public ResponseEntity<List<Match>> getAllByContact(@PathVariable Integer contactId){
        try{
            return ResponseEntity.ok(matchService.getAllByContact(contactId));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/create/withFilteredMatches", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity chargeData(@RequestParam("matches") List<String> matches,
                                     @RequestParam("cameraId") Integer cameraId){
        try{
            return ResponseEntity.ok(matchService.withFilteredMatches(matches, cameraId));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/createByDetection")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<String> createByDetection (@RequestBody String match){
        try{
            return ResponseEntity.ok(matchService.createByDetection(match));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/detection2")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<String> registerDetection (@RequestBody String detection){
        try{
            return ResponseEntity.ok(matchService.registerDetection(detection));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/detection3", method = RequestMethod.POST)
    public void sendMsg(HttpSession session,
                        @RequestParam(value = "detections")String[] detections)
    {
        for (String data : detections) {
            System.out.println("Your Data =>" + data);
        }
    }

    @ResponseBody
    @RequestMapping(path = "/detection", method = RequestMethod.POST)
    public String BookingItemList(@RequestBody Long[] detections)
    {
        for (Long detection:detections
             ) {
            System.out.println(detection);
        }
        return detections.toString();
    }

    @PostMapping("/getMatchesByDate")
    public ResponseEntity getMatchesByDate(@RequestParam("firstDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date firstDate,
                                           @RequestParam("secondDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date secondDate){

        return ResponseEntity.ok(matchService.getMatchesByDate(firstDate, secondDate));
    }

    @PostMapping("/getMatchesByDateWithRandomLocation")
    public ResponseEntity getMatchesByDateWithRandomLocation(@RequestParam("firstDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date firstDate,
                                           @RequestParam("secondDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date secondDate){

        return ResponseEntity.ok(matchService.getMatchesByDateWithRandomLocation(firstDate, secondDate));
    }

    @PostMapping("/getIncomeOutcome")
    public ResponseEntity getIncomeOutcome(@RequestParam("day") @DateTimeFormat(pattern = "yyyy-MM-dd") Date day,
                                          @RequestParam("personId") Integer personId){

        return ResponseEntity.ok(matchService.getIncomeOutcome(day, personId));
    }

    @GetMapping("/alerts")
    public ResponseEntity alerts(){
        return ResponseEntity.ok(matchService.alerts());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/writePlaceReport", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> writePlaceReport(@RequestParam("day") @DateTimeFormat(pattern = "yyyy-MM-dd") Date day) throws IOException
    {
        // Se planifican las fechas, las cuales son la fecha solicitada y un día
        // después, para adquirir toda la información del día actual completo
        Instant firstCurrent = day.toInstant();
        Instant secondCurrent = day.toInstant();
        LocalDateTime firstLocalDate = LocalDateTime.ofInstant(firstCurrent,
                ZoneId.systemDefault());
        LocalDateTime secondLocalDate = LocalDateTime.ofInstant(secondCurrent,
                ZoneId.systemDefault()).plusDays(1);

        //Se verifica si existen matchs, si es así existen datos de interes
        List<Match> matches = matchDao.findMatchByHourBetween(firstLocalDate, secondLocalDate);
        if (matches.size() == 0) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body("No se captaron clientes este día");
        }

        // Si se llega a este punto es porque existen datos de interes, por lo tanto,
        // se procede a escribir el archivo

        //se vuelve a enviar solo el día de entrada, ya que luego se utilizará en el servicio del match
        Path filePath = fileStorageService.getFileStorageLocation().resolve("output.xlsx").normalize();
        matchService.writeXlsx(matches, filePath.toString(), day);
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
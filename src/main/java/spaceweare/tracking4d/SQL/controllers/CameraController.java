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
import spaceweare.tracking4d.FileManagement.service.FileStorageService;
import spaceweare.tracking4d.SQL.dao.MatchDao;
import spaceweare.tracking4d.SQL.models.Camera;
import spaceweare.tracking4d.SQL.models.Match;
import spaceweare.tracking4d.SQL.services.CameraService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/cameras")
public class CameraController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private final CameraService cameraService;
    private final FileStorageService fileStorageService;
    private final MatchDao matchDao;
    public CameraController(CameraService cameraService, FileStorageService fileStorageService, MatchDao matchDao) {
        this.cameraService = cameraService;
        this.fileStorageService = fileStorageService;
        this.matchDao = matchDao;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<Camera> create (@RequestBody Camera camera){
        try{
            return ResponseEntity.ok(cameraService.create(camera));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<Camera>> readAll(){
        try{
            return ResponseEntity.ok(cameraService.readAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Camera> readById (@PathVariable("id") Integer id){
        try{
            return ResponseEntity.ok(cameraService.readById(id));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<Camera> update (@PathVariable("id") Integer id, @RequestBody Camera camera){
        try{
            return ResponseEntity.ok(cameraService.update(camera, id));
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
            return ResponseEntity.ok(cameraService.delete(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
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
        cameraService.writeXlsx(matches, filePath.toString(), day);
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

    @PostMapping("/getDetectionCamWithSplitCam4")
    @ResponseBody
    public ResponseEntity<Camera> getDetectionCamWithSplitCam4(@RequestParam("x") Integer x,
                                                               @RequestParam("y") Integer y,
                                                               @RequestParam("height") Integer height,
                                                               @RequestParam("width") Integer width) {
        try {
            return ResponseEntity.ok(cameraService.getDetectionCamWithSplitCam4(x, y, height, width));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/getDetectionCamWithSplitCam5")
    @ResponseBody
    public ResponseEntity<Camera> getDetectionCamWithSplitCam5(@RequestParam("x") Integer x,
                                                              @RequestParam("y") Integer y,
                                                              @RequestParam("height") Integer height,
                                                              @RequestParam("width") Integer width) {
        try {
                return ResponseEntity.ok(cameraService.getDetectionCamWithSplitCam5(x, y, height, width));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
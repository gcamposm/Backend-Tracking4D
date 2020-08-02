package spaceweare.tracking4d.SQL.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spaceweare.tracking4d.Helpers.DateHelper;
import spaceweare.tracking4d.SQL.dto.models.DetectionDayStat;
import spaceweare.tracking4d.SQL.models.Detection;
import spaceweare.tracking4d.SQL.services.DetectionService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/detections")
public class DetectionController {

    private final DetectionService detectionService;

    public DetectionController(DetectionService detectionService) {
        this.detectionService = detectionService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<Detection> create (@RequestBody Detection detection){
        try{
            return ResponseEntity.ok(detectionService.create(detection));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<Detection>> readAll(){
        try{
            return ResponseEntity.ok(detectionService.readAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Detection> readById (@PathVariable("id") Integer id){
        try{
            return ResponseEntity.ok(detectionService.readById(id));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<Detection> update (@PathVariable("id") Integer id, @RequestBody Detection detection){
        try{
            return ResponseEntity.ok(detectionService.update(detection, id));
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
            return ResponseEntity.ok(detectionService.delete(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/saveUnknown", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity saveUnknown(@RequestParam("unknown") List<String> unknown){
        try{
            return ResponseEntity.ok(detectionService.saveUnknown(unknown));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "getVisitsBetweenDates")
    public ResponseEntity getVisitsBetweenDates(@RequestParam("firstDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date firstDate,
                                                @RequestParam("secondDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date secondDate){
        return ResponseEntity.ok(detectionService.getVisitsBetweenDates(firstDate, secondDate));
    }
}
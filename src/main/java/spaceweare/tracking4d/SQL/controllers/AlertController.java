package spaceweare.tracking4d.SQL.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spaceweare.tracking4d.SQL.models.Alert;
import spaceweare.tracking4d.SQL.services.AlertService;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<Alert> create (@RequestBody Alert alert){
        try{
            return ResponseEntity.ok(alertService.create(alert));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<Alert>> readAll(){
        try{
            return ResponseEntity.ok(alertService.readAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Alert> readById (@PathVariable("id") Integer id){
        try{
            return ResponseEntity.ok(alertService.readById(id));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<Alert> update (@PathVariable("id") Integer id, @RequestBody Alert alert){
        try{
            return ResponseEntity.ok(alertService.update(alert, id));
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
            return ResponseEntity.ok(alertService.delete(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get/active")
    @ResponseBody
    public ResponseEntity<List<Alert>> active(){
        try{
            return ResponseEntity.ok(alertService.active());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/softDelete/{id}")
    @ResponseBody
    public ResponseEntity<Alert> softDelete (@PathVariable Integer id){
        try{
            return ResponseEntity.ok(alertService.softDelete(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
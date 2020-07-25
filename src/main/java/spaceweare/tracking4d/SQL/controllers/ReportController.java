package spaceweare.tracking4d.SQL.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spaceweare.tracking4d.SQL.models.Report;
import spaceweare.tracking4d.SQL.services.ReportService;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<Report> create (@RequestBody Report report){
        try{
            return ResponseEntity.ok(reportService.create(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<Report>> readAll(){
        try{
            return ResponseEntity.ok(reportService.readAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Report> readById (@PathVariable("id") Integer id){
        try{
            return ResponseEntity.ok(reportService.readById(id));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<Report> update (@PathVariable("id") Integer id, @RequestBody Report report){
        try{
            return ResponseEntity.ok(reportService.update(report, id));
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
            return ResponseEntity.ok(reportService.delete(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
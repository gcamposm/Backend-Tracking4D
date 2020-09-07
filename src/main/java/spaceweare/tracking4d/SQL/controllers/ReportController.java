package spaceweare.tracking4d.SQL.controllers;

import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import spaceweare.tracking4d.SQL.models.Report;
import spaceweare.tracking4d.SQL.services.ReportService;
import java.io.ByteArrayInputStream;
import java.util.Date;
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

    @RequestMapping(value = "/personsPdfReport", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> personsPdfReport() {
        ByteArrayInputStream bis = reportService.personsPdfReport();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=personsReport.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @RequestMapping(value = "/makePersonPdfReport", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    /*public ResponseEntity<InputStreamResource> makePersonPdfReport(@RequestParam("firstDay") @DateTimeFormat(pattern = "yyyy-MM-dd") Date firstDay,
                                                                   @RequestParam("lastDay") @DateTimeFormat(pattern = "yyyy-MM-dd") Date lastDay,
                                                                   @RequestParam("covid") Boolean covid) {
        ByteArrayInputStream bis = reportService.makePersonPdfReport(firstDay, lastDay, covid);*/
    public ResponseEntity<InputStreamResource> makePersonPdfReport() {
        ByteArrayInputStream bis = reportService.makePersonPdfReport();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=personsReport.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    /*@RequestMapping(value = "/makePlacesPdfReport", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> makePlacesPdfReport(@RequestParam("day") @DateTimeFormat(pattern = "yyyy-MM-dd") Date day) {*/
    @RequestMapping(value = "/makePlacesPdfReport", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
        public ResponseEntity<InputStreamResource> makePlacesPdfReport() {
        Date day = new Date();
        ByteArrayInputStream bis = reportService.makePlacesPdfReport(day);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=personsReport.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
package spaceweare.tracking4d.SQL.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spaceweare.tracking4d.SQL.models.Match;
import spaceweare.tracking4d.SQL.services.MatchService;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
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

    @RequestMapping(value = "/create/withFilteredMatches", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity chargeData(@RequestParam("matches") List<String> matches){
        try{
            return ResponseEntity.ok(matchService.withFilteredMatches(matches));
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

    @PostMapping("/getIncomeOutcome")
    public ResponseEntity getIncomeOutcome(@RequestParam("day") @DateTimeFormat(pattern = "yyyy-MM-dd") Date day,
                                          @RequestParam("customerId") Integer customerId){

        return ResponseEntity.ok(matchService.getIncomeOutcome(day, customerId));
    }
}
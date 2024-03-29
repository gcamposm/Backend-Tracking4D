package spaceweare.tracking4d.SQL.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spaceweare.tracking4d.SQL.models.Temperature;
import spaceweare.tracking4d.SQL.services.TemperatureService;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/temperatures")
public class TemperatureController {

    private final TemperatureService temperatureService;

    public TemperatureController(TemperatureService temperatureService) {
        this.temperatureService = temperatureService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<Temperature> create (@RequestBody Temperature temperature){
        try{
            return ResponseEntity.ok(temperatureService.create(temperature));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<Temperature>> readAll(){
        try{
            return ResponseEntity.ok(temperatureService.readAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Temperature> readById (@PathVariable("id") Integer id){
        try{
            return ResponseEntity.ok(temperatureService.readById(id));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<Temperature> update (@PathVariable("id") Integer id, @RequestBody Temperature temperature){
        try{
            return ResponseEntity.ok(temperatureService.update(temperature, id));
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
            return ResponseEntity.ok(temperatureService.delete(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @Scheduled(fixedRate = 86400000)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteAll")
    @ResponseBody
    public void deleteAll (){
        try{
            ResponseEntity.ok(temperatureService.deleteAll());
        }
        catch (Exception e){
            ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/getDetectionTemperature")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<String> getDetectionTemperature (@RequestParam Integer x,
                                                           @RequestParam Integer y,
                                                           @RequestParam Integer height,
                                                           @RequestParam Integer width){
        try{
            return ResponseEntity.ok(temperatureService.getDetectionTemperature(x, y, height, width));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
package spaceweare.tracking4d.SQL.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spaceweare.tracking4d.SQL.models.Pixel;
import spaceweare.tracking4d.SQL.services.PixelService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/pixels")
public class PixelController {

    private final PixelService pixelService;

    public PixelController(PixelService pixelService) {
        this.pixelService = pixelService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<Pixel> create (@RequestBody Pixel pixel){
        try{
            return ResponseEntity.ok(pixelService.create(pixel));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<Pixel>> readAll(){
        try{
            return ResponseEntity.ok(pixelService.readAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Pixel> readById (@PathVariable("id") Integer id){
        try{
            return ResponseEntity.ok(pixelService.readById(id));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<Pixel> update (@PathVariable("id") Integer id, @RequestBody Pixel pixel){
        try{
            return ResponseEntity.ok(pixelService.update(pixel, id));
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
            return ResponseEntity.ok(pixelService.delete(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/saveTemperature")
    @ResponseBody
    public ResponseEntity python(@RequestParam("pixels") List<Float> pixels,
                                 @RequestParam("date") String date) {
        try {
            return ResponseEntity.ok(pixelService.saveTemperature(pixels, date));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/aux")
    @ResponseBody
    public ResponseEntity aux() {
        try {
            List<Float> pixels = new ArrayList<>();
            Random r = new Random();
            int count = 0;
            while (count<768){
                pixels.add(37 + r.nextFloat() * (2));
                count++;
            }
            LocalDateTime ldt = LocalDateTime.now();
            DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formatter = formmat1.format(ldt);
            return ResponseEntity.ok(pixelService.saveTemperature(pixels, formatter));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
package spaceweare.tracking4d.SQL.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spaceweare.tracking4d.SQL.models.Descriptor;
import spaceweare.tracking4d.SQL.services.DescriptorService;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/descriptors")
public class DescriptorController {

    private final DescriptorService descriptorService;
    public DescriptorController(DescriptorService descriptorService) {
        this.descriptorService = descriptorService;
    }

    @RequestMapping(value = "/create/withData", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity chargeData(@RequestParam("descriptor") List<Float> descriptorList,
                                     @RequestParam("path") String path,
                                     @RequestParam("user") String userName){
        try{
            return ResponseEntity.ok(descriptorService.chargeData(descriptorList, path, userName));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<Descriptor> create (@RequestBody Descriptor descriptor){
        try{
            return ResponseEntity.ok(descriptorService.create(descriptor));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<Descriptor>> readAll(){
        try{
            return ResponseEntity.ok(descriptorService.readAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Descriptor> readById (@PathVariable("id") Integer id){
        try{
            return ResponseEntity.ok(descriptorService.readById(id));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<Descriptor> update (@PathVariable("id") Integer id, @RequestBody Descriptor descriptor){
        try{
            return ResponseEntity.ok(descriptorService.update(descriptor, id));
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
            return ResponseEntity.ok(descriptorService.delete(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
package spaceweare.tracking4d.SQL.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spaceweare.tracking4d.SQL.models.PersonPosition;
import spaceweare.tracking4d.SQL.services.PersonPositionService;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/personPositions")
public class PersonPositionController {

    private final PersonPositionService personPositionService;

    public PersonPositionController(PersonPositionService personPositionService) {
        this.personPositionService = personPositionService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<PersonPosition> create (@RequestBody PersonPosition personPosition){
        try{
            return ResponseEntity.ok(personPositionService.create(personPosition));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<PersonPosition>> readAll(){
        try{
            return ResponseEntity.ok(personPositionService.readAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<PersonPosition> readById (@PathVariable("id") Integer id){
        try{
            return ResponseEntity.ok(personPositionService.readById(id));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<PersonPosition> update (@PathVariable("id") Integer id, @RequestBody PersonPosition personPosition){
        try{
            return ResponseEntity.ok(personPositionService.update(personPosition, id));
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
            return ResponseEntity.ok(personPositionService.delete(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
package spaceweare.tracking4d.SQL.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spaceweare.tracking4d.SQL.models.Person;
import spaceweare.tracking4d.SQL.models.PersonType;
import spaceweare.tracking4d.SQL.services.PersonTypeService;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/personTypes")
public class PersonTypeController {

    private final PersonTypeService personTypeService;

    public PersonTypeController(PersonTypeService personTypeService) {
        this.personTypeService = personTypeService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<PersonType> create (@RequestBody PersonType personType){
        try{
            return ResponseEntity.ok(personTypeService.create(personType));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<PersonType>> readAll(){
        try{
            return ResponseEntity.ok(personTypeService.readAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<PersonType> readById (@PathVariable("id") Integer id){
        try{
            return ResponseEntity.ok(personTypeService.readById(id));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<PersonType> update (@PathVariable("id") Integer id, @RequestBody PersonType personType){
        try{
            return ResponseEntity.ok(personTypeService.update(personType, id));
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
            return ResponseEntity.ok(personTypeService.delete(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getAll/names")
    @ResponseBody
    public ResponseEntity<Object> getNames(){
        try{
            return ResponseEntity.ok(personTypeService.getNames());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/personWithType")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<Person> personWithType (@RequestParam("personType") String personType,
                                                  @RequestParam("personRut") String personRut){
        try{
            return ResponseEntity.ok(personTypeService.personWithType(personType, personRut));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
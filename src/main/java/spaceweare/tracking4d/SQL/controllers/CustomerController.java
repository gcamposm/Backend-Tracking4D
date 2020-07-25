package spaceweare.tracking4d.SQL.controllers;

import spaceweare.tracking4d.SQL.models.Customer;
import spaceweare.tracking4d.SQL.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<Customer> create (@RequestBody Customer customer){
        try{
            return ResponseEntity.ok(customerService.create(customer));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<Customer>> readAll(){
        try{
            return ResponseEntity.ok(customerService.readAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Customer> readById (@PathVariable("id") Integer id){
        try{
            return ResponseEntity.ok(customerService.readById(id));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<Customer> update (@PathVariable("id") Integer id, @RequestBody Customer customer){
        try{
            return ResponseEntity.ok(customerService.update(customer, id));
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
            return ResponseEntity.ok(customerService.delete(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
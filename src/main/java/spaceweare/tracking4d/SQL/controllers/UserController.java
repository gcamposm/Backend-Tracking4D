package spaceweare.tracking4d.SQL.controllers;

import spaceweare.tracking4d.SQL.dao.UserDao;
import spaceweare.tracking4d.SQL.dto.Payloads.Register.RegisterPayloadDto;
import spaceweare.tracking4d.SQL.dto.models.UserDto;
import spaceweare.tracking4d.SQL.mappers.UserMapper;
import spaceweare.tracking4d.SQL.models.User;
import spaceweare.tracking4d.SQL.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserDao userDao;
    private final UserService userService;
    private final UserMapper userMapper;
    public UserController(UserDao userDao, UserService userService, UserMapper userMapper) {
        this.userDao = userDao;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping()
    public ResponseEntity<UserDto> createGuest(@RequestBody User user){
        try{
            return ResponseEntity.ok(userMapper.mapToDto(userDao.save(user)));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> readAllUsers(){
        try{
            return ResponseEntity.ok(userMapper.mapToDtoList(userDao.findAll()));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> readUser(@PathVariable("id") long id){
        try{
            return ResponseEntity.ok(userMapper.mapToDto(userDao.findById(id)));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@RequestBody User user, @PathVariable long id){
        try{
            User userFound = userDao.findById(id);
            userFound.setUsername(user.getUsername());
            userFound.setPassword(user.getPassword());
            userFound.setActive(user.getActive());
            return ResponseEntity.ok(userMapper.mapToDto(userDao.save(userFound)));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteUser (@PathVariable int id){
        try{
            userDao.delete(userDao.findById(id));
            return ResponseEntity.ok(HttpStatus.OK);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody RegisterPayloadDto registerPayloadDto){
        return userService.registerUser(registerPayloadDto);
    }

    //REGISTER AND PASSWORD
    @PostMapping("/auth/forgotPassword")
    @ResponseBody
    public ResponseEntity forgotPassword(@RequestParam("userName") String userName){
        try {
            userService.forgotPassword(userName);
            return ResponseEntity.ok().body("Se ha enviado el correo de recuperación con éxito");
        }catch (Exception e){
            return ResponseEntity.status(500).body("No se ha podido enviar el correo: " + e);
        }
    }
    @PostMapping("/auth/confirmResetPassword")
    @ResponseBody
    public ResponseEntity confirmResetPassword(@RequestParam("token") String token){
        return userService.confirmResetPassword(token);
    }

    @PostMapping("/auth/resetPassword")
    @ResponseBody
    public ResponseEntity resetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword){
        try {
            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok().body("the password has been restored successfully");
        }catch (Exception e){
            return ResponseEntity.status(500).body("the password could not be restored" + e);
        }
    }
}
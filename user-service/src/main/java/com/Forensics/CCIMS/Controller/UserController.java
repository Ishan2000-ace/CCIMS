package com.Forensics.CCIMS.Controller;

import com.Forensics.CCIMS.DTO.UserRequestDTO;
import com.Forensics.CCIMS.DTO.UserResponseDTO;
import com.Forensics.CCIMS.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;



    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO user){
        UserResponseDTO response = userService.createUser(user);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getuser/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable String id){
        UserResponseDTO response = userService.getUser(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable String id, @RequestBody UserRequestDTO user){
        UserResponseDTO response = userService.updateUser(id, user);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserResponseDTO> deleteUser(@PathVariable String id){
        UserResponseDTO respose = userService.deleteUser(id);

        return new ResponseEntity<>(respose, HttpStatus.OK);
    }

    @GetMapping("/internal/{id}")
    public ResponseEntity<UserResponseDTO> getUserForService(@PathVariable String id) {
        UserResponseDTO response = userService.getUser(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/internal/username/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsernameForService(
            @PathVariable String username) {

        UserResponseDTO response = userService.getUserByUsername(username);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/test-auth")
    public ResponseEntity<String> testAuth() {

        var authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(
                "Username: " + authentication.getName()
                        + " | Authorities: " + authentication.getAuthorities()
        );
    }
}

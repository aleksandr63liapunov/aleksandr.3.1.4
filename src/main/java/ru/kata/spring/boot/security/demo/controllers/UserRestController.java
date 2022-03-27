package ru.kata.spring.boot.security.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot.security.demo.models.Role;
import ru.kata.spring.boot.security.demo.models.User;
import ru.kata.spring.boot.security.demo.services.UserService;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api")
public class UserRestController {
    private final UserService userService;
    public UserRestController(UserService userService) {
        this.userService = userService;
    }
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    @GetMapping("/admin")
    public ResponseEntity<List<User>> showAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers()) ;
    }

    @GetMapping("/user")
    public ResponseEntity<User> showUserForUser() {
        return ResponseEntity.ok((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()) ;
    }

    @GetMapping("/roles")
    public ResponseEntity<Set<Role>> getAllRoles() {
        return  ResponseEntity.ok(userService.getRoles());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id)) ;
    }

    @PostMapping("/admin/create")
    public void createUser(@ModelAttribute User user,Role role) {
        role.getRole();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.addUser(user);
    }

    @PatchMapping("/edit/{id}")
    public void updateUser(@ModelAttribute User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.updateUser(user);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}

package dev.germantovar.springboot.controllers;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import dev.germantovar.springboot.entities.User;
import dev.germantovar.springboot.services.IUserService;
import dev.germantovar.springboot.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService service1;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> save1(@RequestBody User usuario) {
        Map<String, Object> response = new HashMap<>();
        try {
            Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
            String hash = argon2.hash(1, 1024, 1, usuario.getPassword());
            usuario.setPassword(hash);
            service1.save(usuario);
            response.put("success", true);
            response.put("message", "Usuario registrado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al registrar el usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getUserProfile(@RequestHeader(value = "Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        String userId = jwtUtil.getKey(token.replace("Bearer ", ""));
        if (userId != null) {
            User user = service1.findById(Long.parseLong(userId));
            if (user != null) {
                response.put("name", user.getName());
                response.put("email", user.getEmail());
                return ResponseEntity.ok(response);
            }
        }
        response.put("message", "Usuario no encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<Map<String, Object>> updateUserProfile(@RequestHeader(value = "Authorization") String token, @RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        String userId = jwtUtil.getKey(token.replace("Bearer ", ""));
        if (userId != null) {
            User existingUser = service1.findById(Long.parseLong(userId));
            if (existingUser != null) {
                existingUser.setName(user.getName());
                existingUser.setEmail(user.getEmail());
                service1.save(existingUser);
                response.put("message", "Información actualizada exitosamente");
                return ResponseEntity.ok(response);
            }
        }
        response.put("message", "Usuario no encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
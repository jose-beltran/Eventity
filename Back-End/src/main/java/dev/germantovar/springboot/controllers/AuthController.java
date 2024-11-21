package dev.germantovar.springboot.controllers;

import dev.germantovar.springboot.entities.User;
import dev.germantovar.springboot.services.IUserService;
import dev.germantovar.springboot.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    private IUserService service1;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User usuario) {
        Map<String, Object> response = new HashMap<>();
        User usuariologin = service1.credenciales(usuario);

        if (usuariologin != null) {
            String tokenJwt = jwtUtil.create(String.valueOf(usuariologin.getId()), usuariologin.getEmail());
            response.put("success", true);
            response.put("token", tokenJwt);
            response.put("message", "Inicio de sesión correcto");
            return ResponseEntity.ok(response);
        }
        response.put("success", false);
        response.put("message", "Credenciales inválidas");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
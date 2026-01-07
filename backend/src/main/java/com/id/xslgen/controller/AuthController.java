package com.id.xslgen.controller;

import com.id.xslgen.dto.*;
import com.id.xslgen.model.User;
import com.id.xslgen.repository.UserRepository;
import com.id.xslgen.service.JwtService;
import com.id.xslgen.security.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthController(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        if (req.username() == null || req.username().isBlank() || req.password() == null || req.password().length() < 6) {
            return ResponseEntity.badRequest().build();
        }
        if (users.existsByUsername(req.username())) {
            return ResponseEntity.status(409).build();
        }

        User u = new User();
        u.setUsername(req.username());
        u.setPasswordHash(encoder.encode(req.password()));
        u = users.save(u);

        String token = jwt.issueToken(new UserPrincipal(u.getId(), u.getUsername()));
        return ResponseEntity.status(201).body(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        if (req == null || req.username() == null || req.username().isBlank() || req.password() == null) {
            return ResponseEntity.status(400).build();
        }

        var userOpt = users.findByUsername(req.username());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        var u = userOpt.get();
        if (!encoder.matches(req.password(), u.getPasswordHash())) {
            return ResponseEntity.status(401).build();
        }

        String token = jwt.issueToken(new UserPrincipal(u.getId(), u.getUsername()));
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(Authentication auth) {
        UserPrincipal p = (UserPrincipal) auth.getPrincipal();
        return ResponseEntity.ok(new MeResponse(p.id(), p.username()));
    }
}

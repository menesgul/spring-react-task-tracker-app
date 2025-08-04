package com.tracker.tasks.controllers;

import com.tracker.tasks.auth.AuthenticationRequest;
import com.tracker.tasks.auth.AuthenticationResponse;
import com.tracker.tasks.auth.AuthenticationService;
import com.tracker.tasks.auth.RegisterRequest;
import com.tracker.tasks.domain.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}

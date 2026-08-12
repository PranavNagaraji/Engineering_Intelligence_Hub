package com.pranav.engineering_intelligence_hub.service;

import com.pranav.engineering_intelligence_hub.dto.request.AuthRequest;
import com.pranav.engineering_intelligence_hub.dto.request.RegisterRequest;
import com.pranav.engineering_intelligence_hub.dto.response.AuthResponse;
import com.pranav.engineering_intelligence_hub.entity.Role;
import com.pranav.engineering_intelligence_hub.entity.User;
import com.pranav.engineering_intelligence_hub.exceptions.UserWithEmailOrUsernameFoundException;
import com.pranav.engineering_intelligence_hub.repository.UserRepository;
import com.pranav.engineering_intelligence_hub.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String login(AuthRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.username(),
                    request.password()
            )
        );
        return jwtService.generateToken(request.username());
    }

    public String register(RegisterRequest request){
        if(userRepository.existsByUsername(request.username()) || userRepository.existsByEmail(request.email())){
            throw new UserWithEmailOrUsernameFoundException();
        }
        String password=passwordEncoder.encode(request.password());
        User user=User.builder()
                .username(request.username())
                .email(request.email())
                .role(Role.ENGINEER)
                .password(password)
                .build();
        User savedUser=userRepository.save(user);
        return jwtService.generateToken(savedUser.getUsername());
    }
}
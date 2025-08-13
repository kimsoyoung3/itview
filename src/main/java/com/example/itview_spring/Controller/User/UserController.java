package com.example.itview_spring.Controller.User;

import com.example.itview_spring.DTO.RegisterDTO;
import com.example.itview_spring.Service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final RegisterService registerService;

    @PostMapping("/api/user")
    public void registerPost(@RequestBody RegisterDTO registerDTO) {
        registerService.createUser(registerDTO);
    }
}

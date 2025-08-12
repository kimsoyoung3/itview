package com.example.itview_spring.Service;

import com.example.itview_spring.Constant.Role;
import com.example.itview_spring.DTO.RegisterDTO;
import com.example.itview_spring.Entity.UserEntity;
import com.example.itview_spring.Repository.RegisterRepository;
import com.fasterxml.jackson.annotation.OptBoolean;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterService implements UserDetailsService {
    private final RegisterRepository registerRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = registerRepository.findByEmail(email);

        if (userEntity.isPresent()) {
            return User.withUsername(userEntity.get().getEmail())
                    .password(userEntity.get().getPassword())
                    .roles(userEntity.get().getRole().name())
                    .build();
        } else {
            throw new UsernameNotFoundException(email);
        }
    }

    public void createUser(RegisterDTO registerDTO) {
        Optional<UserEntity> userEntity = registerRepository.findByEmail(registerDTO.getEmail());

        if (userEntity.isPresent()) {
            throw new IllegalStateException("이미 가입한 회원입니다.");
        }

        String password = passwordEncoder.encode(registerDTO.getPassword());
        UserEntity user = modelMapper.map(registerDTO, UserEntity.class);
        user.setPassword(password);
        user.setRole(Role.USER);
        user.setNickname(registerDTO.getNickname());

        registerRepository.save(user);
    }
}

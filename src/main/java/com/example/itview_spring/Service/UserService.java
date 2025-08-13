package com.example.itview_spring.Service;

import com.example.itview_spring.Config.CustomUserDetails;
import com.example.itview_spring.Constant.Role;
import com.example.itview_spring.DTO.EmailDTO;
import com.example.itview_spring.DTO.EmailVerificationDTO;
import com.example.itview_spring.DTO.RegisterDTO;
import com.example.itview_spring.Entity.EmailVerificationEntity;
import com.example.itview_spring.Entity.UserEntity;
import com.example.itview_spring.Repository.EmailVerificationRepository;
import com.example.itview_spring.Repository.UserRepository;
import com.example.itview_spring.Util.AuthCodeGenerator;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository registerRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = registerRepository.findByEmail(email);

        if (userEntity.isPresent()) {
            return new CustomUserDetails(
                userEntity.get().getId(),
                userEntity.get().getEmail(),
                userEntity.get().getPassword(),
                List.of(new SimpleGrantedAuthority(userEntity.get().getRole().name()))
            );

        } else {
            throw new UsernameNotFoundException(email);
        }
    }

    // 가입 된 회원인지 확인
    public boolean isUserExists(String email) {
        return registerRepository.findByEmail(email).isPresent();
    }

    // 회원가입
    public void createUser(RegisterDTO registerDTO) {
        // 이미 가입된 회원인지 확인
        if (isUserExists(registerDTO.getEmail())) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }

        String password = passwordEncoder.encode(registerDTO.getPassword());
        UserEntity user = modelMapper.map(registerDTO, UserEntity.class);
        user.setPassword(password);
        user.setRole(Role.USER);
        user.setNickname(registerDTO.getNickname());

        registerRepository.save(user);
    }

    // 이메일 인증 코드 생성 및 전송
    public void createVerifyingCode(EmailDTO emailDTO) {
        // 이메일로 사용자 조회
        Optional<UserEntity> userEntity = registerRepository.findByEmail(emailDTO.getEmail());
        if (userEntity.isEmpty()) {
            throw new IllegalStateException("가입되지 않은 이메일입니다.");
        }
        // 랜덤번호 6자리 생성
        String code = AuthCodeGenerator.generateCode();
        // 이메일 인증 코드 엔티티 생성
        EmailVerificationEntity emailVerificationEntity = new EmailVerificationEntity();

        emailVerificationEntity.setUser(userEntity.get());
        emailVerificationEntity.setCode(code);

        emailVerificationRepository.save(emailVerificationEntity);

        // 이메일 전송
        SimpleMailMessage Message = new SimpleMailMessage();
        Message.setTo(emailDTO.getEmail());
        Message.setSubject("ITView 이메일 인증 코드");
        Message.setText("인증 코드: " + code);
        mailSender.send(Message);
    }
}
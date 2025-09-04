package com.example.itview_spring.Service;

import com.example.itview_spring.Config.CustomUserDetails;
import com.example.itview_spring.Constant.ContentType;
import com.example.itview_spring.Constant.Role;
import com.example.itview_spring.DTO.CommentAndContentDTO;
import com.example.itview_spring.DTO.ContentResponseDTO;
import com.example.itview_spring.DTO.EmailDTO;
import com.example.itview_spring.DTO.EmailVerificationDTO;
import com.example.itview_spring.DTO.NewPasswordDTO;
import com.example.itview_spring.DTO.RatingDTO;
import com.example.itview_spring.DTO.RegisterDTO;
import com.example.itview_spring.DTO.UserContentCountDTO;
import com.example.itview_spring.DTO.UserProfileUpdateDTO;
import com.example.itview_spring.DTO.UserRatingCountDTO;
import com.example.itview_spring.DTO.UserResponseDTO;
import com.example.itview_spring.Entity.EmailVerificationEntity;
import com.example.itview_spring.Entity.UserEntity;
import com.example.itview_spring.Repository.*;
import com.example.itview_spring.Util.AuthCodeGenerator;
import com.example.itview_spring.Util.S3Uploader;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ContentRepository contentRepository;
    private final RatingRepository ratingRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final S3Uploader s3Uploader;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);

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
        return userRepository.findByEmail(email).isPresent();
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

        userRepository.save(user);
    }

    // 이메일 인증 코드 생성 및 전송
    public void createVerifyingCode(EmailDTO emailDTO) {
        // 이메일로 사용자 조회
        Optional<UserEntity> userEntity = userRepository.findByEmail(emailDTO.getEmail());
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

    // 이메일 인증 코드 확인
    public boolean verifyCode(EmailVerificationDTO emailVerificationDTO) {
        // 이메일로 사용자 조회
        Optional<UserEntity> userEntity = userRepository.findByEmail(emailVerificationDTO.getEmail());
        if (userEntity.isEmpty()) {
            throw new IllegalStateException("가입되지 않은 이메일입니다.");
        }

        // 인증 코드 조회
        String code = emailVerificationRepository.findCode(userEntity.get().getId());

        if (code != null && code.equals(emailVerificationDTO.getCode())) {
            // 인증 코드가 유효한 경우
            return true;
        } else {
            // 인증 코드가 유효하지 않은 경우
            return false;
        }
    }

    // 비밀번호 변경
    public void setPassword(NewPasswordDTO newPasswordDTO) {
        // 이메일로 사용자 조회
        Optional<UserEntity> userEntity = userRepository.findByEmail(newPasswordDTO.getEmail());
        if (userEntity.isEmpty()) {
            throw new IllegalStateException("가입되지 않은 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(newPasswordDTO.getNewPassword());
        userEntity.get().setPassword(encodedPassword);

        userRepository.save(userEntity.get());
    }

    // 유저 페이지 정보 조회
    public UserResponseDTO getUserProfile(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        return userRepository.findUserResponseById(id);
    }

    // 유저 프로필 수정
    public void updateUserProfile(UserProfileUpdateDTO userProfileUpdateDTO) {
        if (!userRepository.existsById(userProfileUpdateDTO.getId())) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        try {
            UserEntity user = userRepository.findById(userProfileUpdateDTO.getId()).get();
            user.setNickname(userProfileUpdateDTO.getNickname());
            user.setIntroduction(userProfileUpdateDTO.getIntroduction());
            if (userProfileUpdateDTO.getProfile() != null) {
                s3Uploader.deleteFile(user.getProfile());
                user.setProfile(s3Uploader.uploadFile(userProfileUpdateDTO.getProfile()));
            }
        } catch (UnsupportedFormatException e) {
            throw new IllegalStateException("지원하지 않는 이미지 형식입니다.");
        } catch (IOException e) {
            throw new IllegalStateException("이미지 업로드에 실패했습니다.");
        }
    }

    // 유저가 매긴 별점 개수 조회
    public UserRatingCountDTO getUserRatingCount(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        return ratingRepository.findUserRatingCount(userId);
    }

    // 유저가 매긴 특정 컨텐츠 타입의 별점 개수 및 위시리스트 개수 조회
    public UserContentCountDTO getUserContentCount(Integer userId, ContentType contentType) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        return ratingRepository.findUserContentCount(userId, contentType);
    }

    // 유저가 매긴 특정 컨텐츠 타입의 별점 조회
    public Page<RatingDTO> getUserContentRating(Integer userId, ContentType contentType, Integer page, String order) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }

        Pageable pageable = PageRequest.of(page - 1, 1);
        return ratingRepository.findUserContentRatings(pageable, userId, contentType, order);
    }

    // 유저의 위시리스트 조회
    public Page<ContentResponseDTO> getUserWishlist(Integer userId, ContentType contentType, Integer page, String order) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        Pageable pageable = PageRequest.of(page - 1, 10);
        return wishlistRepository.findWishlistByUserIdAndContentType(userId, contentType, pageable, order);
    }

    // 유저의 코멘트 목록 조회
    public Page<CommentAndContentDTO> getUserComment(Integer loginUserId, Integer userId, ContentType contentType, Integer page, String order) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        Pageable pageable = PageRequest.of(page - 1, 10);
        return commentRepository.findCommentAndContentByUserId(loginUserId, userId, contentType, pageable, order);
    }
}
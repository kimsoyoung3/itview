package com.example.itview_spring.Controller.User;

import com.example.itview_spring.Config.CustomUserDetails;
import com.example.itview_spring.Constant.ContentType;
import com.example.itview_spring.DTO.CollectionResponseDTO;
import com.example.itview_spring.DTO.CommentAndContentDTO;
import com.example.itview_spring.DTO.ContentResponseDTO;
import com.example.itview_spring.DTO.EmailDTO;
import com.example.itview_spring.DTO.EmailVerificationDTO;
import com.example.itview_spring.DTO.LoginDTO;
import com.example.itview_spring.DTO.NewPasswordDTO;
import com.example.itview_spring.DTO.PersonDTO;
import com.example.itview_spring.DTO.RatingDTO;
import com.example.itview_spring.DTO.RegisterDTO;
import com.example.itview_spring.DTO.UserContentCountDTO;
import com.example.itview_spring.DTO.UserInfoDTO;
import com.example.itview_spring.DTO.UserProfileUpdateDTO;
import com.example.itview_spring.DTO.UserRatingCountDTO;
import com.example.itview_spring.DTO.UserResponseDTO;
import com.example.itview_spring.Service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserRestController {
    
    private final UserService registerService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final UserService userService;

    // 회원가입
    @PostMapping
    public ResponseEntity<Void> registerPost(@RequestBody RegisterDTO registerDTO) {
        try {
            registerService.createUser(registerDTO);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Void> loginPost(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        try {
            // loginDTO로 인증 토큰 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginDTO.getEmail(), loginDTO.getPassword());

            // 인증 매니저를 사용하여 인증 시도
            Authentication authentication = authenticationManager.authenticate(authToken);

            // 인증이 성공하면 SecurityContextHolder에 인증 정보 저장
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            // 세션 생성
            securityContextRepository.saveContext(context, request, response);
        } catch (Exception e) {
            return ResponseEntity.status(401).build(); // 인증 실패
        }
        return ResponseEntity.ok().build();
    }

    // 소셜 연동 해제
    @PostMapping("/unlink")
    public ResponseEntity<Void> unlinkSocial(@AuthenticationPrincipal CustomUserDetails user,
                                             @RequestParam("provider") String provider) {
        userService.unlinkSocial(user.getId(), provider);
        return ResponseEntity.ok().build();
    }

    // 현재 로그인된 사용자 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserInfoDTO> me(@AuthenticationPrincipal CustomUserDetails user) {
        if (user == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(userService.getUserInfo(user.getId()));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        // SecurityContext를 비워서 로그아웃 처리
        SecurityContextHolder.clearContext();
        
        // 기존 세션 무효화
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        
        // JSESSIONID 쿠키 삭제
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);
        
        return ResponseEntity.ok().build();
    }

    // 존재하는 이메일인지 확인
    @PostMapping("/email")
    public ResponseEntity<Void> emailCheck(@RequestBody EmailDTO emailDTO) {
        if (registerService.isUserExists(emailDTO.getEmail())) {
            return ResponseEntity.ok().build(); // 이미 가입된 이메일인 경우
        }
        return ResponseEntity.badRequest().build(); // 가입되지 않은 이메일인 경우
    }

    // 이메일 인증번호 생성
    @PostMapping("/createVerification")
    public ResponseEntity<Void> verificationPost(@RequestBody EmailDTO emailDTO) {
        try {
            registerService.createVerifyingCode(emailDTO);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build(); // 가입되지 않은 이메일인 경우
        }
        return ResponseEntity.ok().build();
    }

    // 이메일 인증번호 확인
    @PostMapping("/checkVerification")
    public ResponseEntity<Void> verificationGet(@RequestBody EmailVerificationDTO emailVerificationDTO) {
        try {
            boolean isValid = registerService.verifyCode(emailVerificationDTO);
            if (!isValid) {
                return ResponseEntity.badRequest().build(); // 인증 코드가 유효하지 않은 경우
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build(); // 가입되지 않은 이메일인 경우
        }
        
        return ResponseEntity.ok().build();
    }

    // 비밀번호 변경
    @PostMapping("/setPW")
    public ResponseEntity<Void> setPassword(@RequestBody NewPasswordDTO newPasswordDTO) {
        try {
            registerService.setPassword(newPasswordDTO);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build(); // 비밀번호 변경 실패
        }
        return ResponseEntity.ok().build();
    }

    // 소셜 계정 연결
    @PostMapping("/link")
    public void linkGoogle(HttpServletRequest request,
                           HttpServletResponse response,
                           @AuthenticationPrincipal CustomUserDetails user,
                           @RequestParam("redirectURL") String redirectURL) throws IOException {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (user != null) { // 이미 로그인된 사용자라면 소셜 계정 연동을 위한 정보를 세션에 저장
            request.getSession().setAttribute("ORIGINAL_AUTH", auth);
            request.getSession().setAttribute("LINK_FLOW", Boolean.TRUE);
            request.getSession().setAttribute("USER_ID", user.getId());
        }
        Cookie redirectCookie = new Cookie("REDIRECT_URL", redirectURL);
        redirectCookie.setPath("/");
        redirectCookie.setHttpOnly(false);
        response.addCookie(redirectCookie);
        System.out.println("redirectURL: " + redirectURL);
    }

    // 유저 페이지 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserProfile(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(registerService.getUserProfile(id));
    }

    // 유저 프로필 수정
    @PutMapping
    public ResponseEntity<UserResponseDTO> updateUserProfile(@AuthenticationPrincipal CustomUserDetails user,
                                  @ModelAttribute UserProfileUpdateDTO userProfileUpdateDTO) {
        if (user.getId() != userProfileUpdateDTO.getId()) {
            throw new SecurityException("본인의 프로필만 수정할 수 있습니다.");
        }
        userService.updateUserProfile(userProfileUpdateDTO);
        return ResponseEntity.ok(userService.getUserProfile(userProfileUpdateDTO.getId()));
    }

    // 유저 평점 개수 조회
    @GetMapping("/{id}/rating")
    public ResponseEntity<UserRatingCountDTO> getUserRatingCount(@PathVariable("id") Integer userId) {
        return ResponseEntity.ok(userService.getUserRatingCount(userId));
    }

    // 유저가 매긴 특정 컨텐츠 타입의 별점 개수 및 위시리스트 개수 조회
    @GetMapping("/{id}/content/{contentType}")
    public ResponseEntity<UserContentCountDTO> getUserContentCount(@PathVariable("id") Integer userId,
                                                                   @PathVariable("contentType") String contentTypeStr) {
        return ResponseEntity.ok(userService.getUserContentCount(userId, ContentType.valueOf(contentTypeStr.toUpperCase())));
    }

    // 유저가 매긴 특정 컨텐츠 타입의 평점 목록 조회
    @GetMapping("/{id}/content/{contentType}/rating")
    public ResponseEntity<Page<RatingDTO>> getUserContentRating(@PathVariable("id") Integer userId,
                                                                @PathVariable("contentType") String contentTypeStr,
                                                                @PageableDefault(page=1) Pageable pageable,
                                                                @RequestParam(value = "order", defaultValue = "avg_score_low") String order) {
        // 'my_score_high' : 내가 매긴 평점 높은 순
        // 'my_score_low' : 내가 매긴 평점 낮은 순
        // 'avg_score_high' : 평균 평점 높은 순
        // 'avg_score_low' : 평균 평점 낮은 순
        // 'new' : 최근 등록 순
        // 'old' : 오래된 순
        return ResponseEntity.ok(userService.getUserContentRating(userId, ContentType.valueOf(contentTypeStr.toUpperCase()), pageable.getPageNumber(), order));
    }

    // 유저가 매긴 특정 컨텐츠 타입의 특정 평점 목록 조회
    @GetMapping("/{id}/content/{contentType}/rating/{score}")
    public ResponseEntity<Page<RatingDTO>> getUserContentRatingScore(@PathVariable("id") Integer userId,
                                                                     @PathVariable("contentType") String contentTypeStr,
                                                                     @PageableDefault(page=1) Pageable pageable,
                                                                     @PathVariable("score") Integer score) {
        return ResponseEntity.ok(userService.getUserContentRatingScore(userId, ContentType.valueOf(contentTypeStr.toUpperCase()), pageable.getPageNumber(), score));
    }

    // 유저의 위시리스트 조회
    @GetMapping("/{id}/content/{contentType}/wish")
    public ResponseEntity<Page<ContentResponseDTO>> getUserWishlist(@PathVariable("id") Integer userId,
                                                                    @PathVariable("contentType") String contentTypeStr,
                                                                    @PageableDefault(page=1) Pageable pageable,
                                                                    @RequestParam(value = "order", defaultValue = "new") String order) {

        // 'new' : 담은 순
        // 'old' : 담은 역순
        // 'rating_high' : 평균 별점 높은 순
        // 'rating_low' : 평균 별점 낮은 순
        return ResponseEntity.ok(userService.getUserWishlist(userId, ContentType.valueOf(contentTypeStr.toUpperCase()), pageable.getPageNumber(), order));
    }

    // 유저의 코멘트 조회
    @GetMapping("/{id}/comment/{contentType}")
    public ResponseEntity<Page<CommentAndContentDTO>> getUserComment(@PathVariable("id") Integer userId,
                                                                     @PathVariable("contentType") String contentTypeStr,
                                                                     @PageableDefault(page=1) Pageable pageable,
                                                                     @RequestParam(value = "order", defaultValue = "recent") String order) {
        // 'recent' : 최근 작성 순
        // 'like' : 좋아요 많은 순
        // 'reply' : 댓글 많은 순
        // 'rating' : 해당 유저의 별점 높은 순
        // 'new' : 신작 순
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Integer loginUserId = 0;
        if (auth.getPrincipal() != "anonymousUser") {
            loginUserId = ((CustomUserDetails) auth.getPrincipal()).getId();
        }
        return ResponseEntity.ok(userService.getUserComment(loginUserId, userId, ContentType.valueOf(contentTypeStr.toUpperCase()), pageable.getPageNumber(), order));
    }

    // 유저의 컬렉션 조회
    @GetMapping("/{id}/collection")
    public ResponseEntity<Page<CollectionResponseDTO>> getUserCollection(@PathVariable("id") Integer userId,
                                                                         @PageableDefault(page=1) Pageable pageable) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Integer loginUserId = 0;
        if (auth.getPrincipal() != "anonymousUser") {
            loginUserId = ((CustomUserDetails) auth.getPrincipal()).getId();
        }
        return ResponseEntity.ok(userService.getUserCollections(loginUserId, userId, pageable.getPageNumber()));
    }

    // 유저의 좋아요한 인물 조회
    @GetMapping("/{id}/like/person")
    public ResponseEntity<Page<PersonDTO>> getUserLike(@PathVariable("id") Integer userId,
                                                       @PageableDefault(page=1) Pageable pageable) {
        return ResponseEntity.ok(userService.getPersonUserLike(userId, pageable.getPageNumber()));
    }

    // 유저의 좋아요한 컬렉션 조회
    @GetMapping("/{id}/like/collection")
    public ResponseEntity<Page<CollectionResponseDTO>> getUserLikeCollection(@PathVariable("id") Integer userId,
                                                                             @PageableDefault(page=1) Pageable pageable) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Integer loginUserId = 0;
        if (auth.getPrincipal() != "anonymousUser") {
            loginUserId = ((CustomUserDetails) auth.getPrincipal()).getId();
        }
        return ResponseEntity.ok(userService.getCollectionUserLike(loginUserId, userId, pageable.getPageNumber()));
    }

    // 유저의 좋아요한 코멘트 조회
    @GetMapping("/{id}/like/comment")
    public ResponseEntity<Page<CommentAndContentDTO>> getUserLikeComment(@PathVariable("id") Integer userId,
                                                                         @PageableDefault(page=1) Pageable pageable) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Integer loginUserId = 0;
        if (auth.getPrincipal() != "anonymousUser") {
            loginUserId = ((CustomUserDetails) auth.getPrincipal()).getId();
        }
        return ResponseEntity.ok(userService.getCommentUserLike(loginUserId, userId, pageable.getPageNumber()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException ex) {
        return ResponseEntity.status(403).body(ex.getMessage());
    }
}
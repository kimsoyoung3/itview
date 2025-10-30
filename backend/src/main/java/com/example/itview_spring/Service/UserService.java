package com.example.itview_spring.Service;

import com.example.itview_spring.Config.CustomUserDetails;
import com.example.itview_spring.Constant.ActivityLogType;
import com.example.itview_spring.Constant.ContentType;
import com.example.itview_spring.Constant.NotiType;
import com.example.itview_spring.Constant.Replyable;
import com.example.itview_spring.Constant.Role;
import com.example.itview_spring.DTO.*;
import com.example.itview_spring.Entity.ActivityLogEntity;
import com.example.itview_spring.Entity.CollectionEntity;
import com.example.itview_spring.Entity.CommentEntity;
import com.example.itview_spring.Entity.EmailVerificationEntity;
import com.example.itview_spring.Entity.FollowEntity;
import com.example.itview_spring.Entity.NotificationEntity;
import com.example.itview_spring.Entity.RatingEntity;
import com.example.itview_spring.Entity.ReplyEntity;
import com.example.itview_spring.Entity.UserEntity;
import com.example.itview_spring.Entity.WishlistEntity;
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
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final WishlistRepository wishlistRepository;
    private final ActivityLogRepository activityLogRepository;
    private final FollowRepository followRepository;
    private final SocialRepository socialRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final RatingRepository ratingRepository;
    private final PersonRepository personRepository;
    private final ReplyRepository replyRepository;
    private final CollectionRepository collectionRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final NotificationRepository notificationRepository;
    private final CollectionService collectionService;
    private final CommentService commentService;
    private final ReplyService replyService;
    private final NotificationService notificationService;
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

    // 소셜 연동 해제
    public void unlinkSocial(Integer userId, String provider) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        if (!socialRepository.existsByUser_IdAndProvider(userId, provider)) {
            throw new NoSuchElementException("연동된 소셜 계정이 없습니다.");
        }
        socialRepository.deleteByUser_IdAndProvider(userId, provider);
    }

    // 탈퇴
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        UserEntity user = userRepository.findById(userId).get();
        // 프로필 사진 S3에서 삭제
        if (user.getProfile() != null) {
            s3Uploader.deleteFile(user.getProfile());
        }

        // 컬렉션 삭제
        List<Integer> collectionIds = collectionRepository.findAllIdsByUserId(userId);
        for (var collectionId : collectionIds) {
            collectionService.deleteCollection(userId, collectionId);
        }

        // 코멘트 삭제
        List<Integer> commentIds = commentRepository.findAllIdsByUserId(userId);
        for (var commentId : commentIds) {
            commentService.deleteComment(userId, commentId);
        }

        // 댓글 삭제
        List<Integer> replyIds = replyRepository.findAllByUserId(userId);
        for (var replyId : replyIds) {
            replyService.deleteReply(userId, replyId);
        }

        userRepository.deleteById(userId);
    }

    // 알림 목록 조회
    public Page<NotificationDTO> getNotifications(Integer userId, Integer page) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<NotificationEntity> notifications = notificationRepository.findAllByUser_IdOrderByIdDesc(userId, pageable);
        Page<NotificationDTO> notificationDTOs = notifications.map(notification -> {
            NotificationDTO dto = new NotificationDTO();
            dto.setProfile(notification.getActor().getProfile());
            dto.setActorId(notification.getActor().getId());
            dto.setCreatedAt(notification.getCreatedAt());

            if (notification.getType() == NotiType.FOLLOW) {
                dto.setTitle("**" + notification.getActor().getNickname() + "**님이 나를 팔로우해요");
                dto.setLink("/user/" + notification.getActor().getId());
                return dto;
            }

            String targetName = "**내** ";
            if (notification.getTargetType() == Replyable.COMMENT) {
                CommentEntity comment = commentRepository.findById(notification.getTargetId()).orElse(null);
                if (!comment.getUser().getId().equals(userId)) {
                    targetName = "내가 댓글 남긴 " + "**" + comment.getUser().getNickname() + "**님의 ";
                }
            } else if (notification.getTargetType() == Replyable.COLLECTION) {
                CollectionEntity collection = collectionRepository.findById(notification.getTargetId()).orElse(null);
                if (!collection.getUser().getId().equals(userId)) {
                    targetName = "내가 댓글 남긴 " + "**" + collection.getUser().getNickname() + "**님의 ";
                }
            }

            String targetType = "";
            if (notification.getTargetType() == Replyable.COMMENT) {
                targetType = "코멘트";
                dto.setLink("/comment/" + notification.getTargetId());
            } else if (notification.getTargetType() == Replyable.COLLECTION) {
                targetType = "컬렉션";
                dto.setLink("/collection/" + notification.getTargetId());
            } else if (notification.getTargetType() == Replyable.REPLY) {
                targetType = "댓글";
                ReplyEntity reply = replyRepository.findById(notification.getTargetId()).orElse(null);
                if (reply.getTargetType() == Replyable.COMMENT) {
                    dto.setLink("/comment/" + reply.getTargetId());
                } else if (reply.getTargetType() == Replyable.COLLECTION) {
                    dto.setLink("/collection/" + reply.getTargetId());
                }
            }

            String tail = "";
            if (notification.getType() == NotiType.REPLY) {
                tail = "에 댓글을 남겼어요";
            } else if (notification.getType() == NotiType.LIKE) {
                if (targetType.equals("코멘트")) {
                    tail = "를 좋아해요";
                } else {
                    tail = "을 좋아해요";
                }
            }
            dto.setTitle("**" + notification.getActor().getNickname() + "**님이 " + targetName + targetType + tail);
            return dto;
        });
        return notificationDTOs;
    }

    // 한글 받침 유무 확인
    private boolean hasJong(char ch) {
        if (ch < 0xAC00 || ch > 0xD7A3) return false; // 한글 범위 밖
        int code = ch - 0xAC00;
        int jongseong = code % 28;
        return jongseong != 0;
    }

    // 친구 소식 조회
    public Page<NotificationDTO> getFriendNotifications(Integer userId, Integer page) {


        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<ActivityLogEntity> activities = activityLogRepository.findFriendActivities(userId, pageable);
        Page<NotificationDTO> notificationDTOs = activities.map(activity -> {        
            NotificationDTO dto = new NotificationDTO();
            dto.setProfile(activity.getUser().getProfile());
            dto.setActorId(activity.getUser().getId());
            dto.setCreatedAt(activity.getTimestamp());
            
            if (activity.getType() == ActivityLogType.COMMENT) {
                dto.setLink("/comment/" + activity.getReferenceId());
                CommentEntity comment = commentRepository.findById(activity.getReferenceId()).get();
                dto.setTitle("**" + activity.getUser().getNickname() + "**님이 **" + comment.getContent().getTitle() + "**" + (activity.getIsUpdate() ? "의 코멘트를 수정했어요" : "에 코멘트를 남겼어요"));
            } else if (activity.getType() == ActivityLogType.COLLECTION) {
                dto.setLink("/collection/" + activity.getReferenceId());
                CollectionEntity collection = collectionRepository.findById(activity.getReferenceId()).get();
                dto.setTitle("**" + activity.getUser().getNickname() + "**님이 **" + collection.getTitle() + "** 컬렉션을 " + (activity.getIsUpdate() ? "수정했어요" : "만들었어요"));
            } else if (activity.getType() == ActivityLogType.RATING) {
                RatingEntity rating = ratingRepository.findById(activity.getReferenceId()).get();
                dto.setLink("/content/" + rating.getContent().getId());
                dto.setTitle("**" + activity.getUser().getNickname() + "**님이 **" + rating.getContent().getTitle() + "**" + (activity.getIsUpdate() ? "의 별점을 " + rating.getScore() + "점으로 수정했어요" : "에 별점" + rating.getScore() + "점을 남겼어요"));
            } else if (activity.getType() == ActivityLogType.WISH) {
                WishlistEntity wishlist = wishlistRepository.findById(activity.getReferenceId()).get();
                dto.setLink("/content/" + wishlist.getContent().getId());
                dto.setTitle("**" + activity.getUser().getNickname() + "**님이 **" + wishlist.getContent().getTitle() + (hasJong(wishlist.getContent().getTitle().charAt(wishlist.getContent().getTitle().length() - 1)) ? "**을" : "**를") + " 보고싶어해요");
            }
            return dto;
        });
        return notificationDTOs;
    }

    // 마지막 알림 확인 시각 업데이트
    public void updateLastNotificationCheckAt(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        UserEntity user = userRepository.findById(userId).get();
        user.setLastNotificationCheckAt(LocalDateTime.now());
        userRepository.save(user);
    }

    // userInfo 조회
    public UserInfoDTO getUserInfo(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserId(id);
        userInfoDTO.setGoogle(socialRepository.existsByUser_IdAndProvider(id, "google"));
        userInfoDTO.setKakao(socialRepository.existsByUser_IdAndProvider(id, "kakao"));
        userInfoDTO.setNaver(socialRepository.existsByUser_IdAndProvider(id, "naver"));
        userInfoDTO.setNewNotification(
            notificationRepository.existsByUser_IdAndCreatedAtAfter(id, userRepository.findById(id).get().getLastNotificationCheckAt())
        );
        return userInfoDTO;
    }

    // 유저 페이지 정보 조회
    public UserResponseDTO getUserProfile(Integer id, Integer loginUserId) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        return userRepository.findUserResponseById(id, loginUserId);
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

    // 유저 팔로우
    public void followUser(Integer userId, Integer targetId) {
        if (!userRepository.existsById(targetId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        if (followRepository.findByFollower_IdAndFollowing_Id(userId, targetId) != null) {
            throw new IllegalStateException("이미 팔로우한 유저입니다.");
        }
        FollowEntity follow = new FollowEntity();
        follow.setFollower(userRepository.findById(userId).get());
        follow.setFollowing(userRepository.findById(targetId).get());
        followRepository.save(follow);

        // 알림 전송
        NotificationEntity notification = new NotificationEntity();
        notification.setUser(userRepository.findById(targetId).get());
        notification.setActor(userRepository.findById(userId).get());
        notification.setType(NotiType.FOLLOW);
        notificationRepository.save(notification);
        // 실시간 알림 전송
        notificationService.sendNotification(targetId);
    }

    // 유저 언팔로우
    public void unfollowUser(Integer userId, Integer targetId) {
        if (!userRepository.existsById(targetId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        FollowEntity follow = followRepository.findByFollower_IdAndFollowing_Id(userId, targetId);
        if (follow == null) {
            throw new IllegalStateException("팔로우하지 않는 유저입니다.");
        }
        followRepository.delete(follow);
    }

    // 유저 팔로워 조회
    public Page<UserResponseDTO> getUserFollowers(Integer userId, Integer loginUserId, Integer page) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        Pageable pageable = PageRequest.of(page - 1, 10);
        return userRepository.findUserFollower(userId, loginUserId, pageable);
    }

    // 유저 팔로잉 조회
    public Page<UserResponseDTO> getUserFollowing(Integer userId, Integer loginUserId, Integer page) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        Pageable pageable = PageRequest.of(page - 1, 10);
        return userRepository.findUserFollowing(userId, loginUserId, pageable);
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

        Pageable pageable = PageRequest.of(page - 1, 10);
        return ratingRepository.findUserContentRatings(pageable, userId, contentType, order);
    }

    // 유저가 매긴 특정 컨텐츠 타입의 특정 별점 조회
    public Page<RatingDTO> getUserContentRatingScore(Integer userId, ContentType contentType, Integer page, Integer score) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }

        Pageable pageable = PageRequest.of(page - 1, 10);
        return ratingRepository.findUserContentRatingsByScore(pageable, userId, contentType, score);
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
        Pageable pageable = PageRequest.of(page - 1, 1);
        return commentRepository.findCommentAndContentByUserId(loginUserId, userId, contentType, pageable, order);
    }

    // 유저 컬렉션 목록 조회
    public Page<CollectionResponseDTO> getUserCollections(Integer loginUserId, Integer userId, Integer page) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다");
        }
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<CollectionResponseDTO> res = collectionRepository.findUserCollections(loginUserId, userId, pageable);
        for (CollectionResponseDTO dto : res) {
            List<String> posters = collectionService.getCollectionPosters(dto.getId());
            dto.setPoster(posters);
        }
        return res;
    }

    // 유저가 좋아요한 인물 조회
    public Page<PersonDTO> getPersonUserLike(Integer userId, Integer page) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        Pageable pageable = PageRequest.of(page - 1, 12);
        return personRepository.findPersonUserLike(userId, pageable);
    }

    // 유저가 좋아요한 컬렉션 조회
    public Page<CollectionResponseDTO> getCollectionUserLike(Integer loginUserId, Integer userId, Integer page) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        Pageable pageable = PageRequest.of(page - 1, 1);
        Page<CollectionResponseDTO> res = collectionRepository.findCollectionUserLike(loginUserId, userId, pageable);
        for (CollectionResponseDTO dto : res) {
            List<String> posters = collectionService.getCollectionPosters(dto.getId());
            dto.setPoster(posters);
        }
        return res;
    }

    // 유저가 좋아요한 코멘트 조회
    public Page<CommentAndContentDTO> getCommentUserLike(Integer loginUserId, Integer userId, Integer page) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
        Pageable pageable = PageRequest.of(page - 1, 1);
        return commentRepository.findCommentAndContentUserLike(loginUserId, userId, pageable);
    }

    // 관리자 페이지 - 유저 목록 조회
    public Page<AdminUserDTO> list(Pageable pageable, String keyword) {
        Page<UserEntity> userEntities;

        if (StringUtils.hasText(keyword)) {
            // 새로 추가한 레포지토리 메서드를 호출하여 검색
            userEntities = userRepository.findByKeyword(keyword, pageable);
        } else {
            // 키워드가 없으면 전체 목록 조회
            userEntities = userRepository.findAll(pageable);
        }

        // UserEntity Page를 AdminUserDTO Page로 변환
        return userEntities.map(userEntity -> {
            AdminUserDTO dto = new AdminUserDTO();
            dto.setId(userEntity.getId());
            dto.setNickname(userEntity.getNickname());
            dto.setIntroduction(userEntity.getIntroduction());
            dto.setProfile(userEntity.getProfile());
            dto.setEmail(userEntity.getEmail());
            return dto;
        });
    }

    // 관리자 페이지 - 유저 상세 조회
    public AdminUserDTO read(int userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. ID: " + userId));

        return modelMapper.map(userEntity, AdminUserDTO.class);
    }

    // 관리자 페이지 - 슈퍼어드민이 유저 역할 조회
    public Role getRoleById(int userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. ID: " + userId));
        return userEntity.getRole();
    }

    // 관리자 페이지 - 유저 수정
    public void update(int userId, SuperUserDTO superUserDTO) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();

            // 닉네임, 자기소개 업데이트 (기존 로직 유지)
            userEntity.setNickname(superUserDTO.getNickname());
            if (StringUtils.hasText(superUserDTO.getIntroduction())) {
                userEntity.setIntroduction(superUserDTO.getIntroduction());
            } else {
                userEntity.setIntroduction(null);
            }

            // 프로필이 비어있지 않을 때만 업데이트
            if (StringUtils.hasText(superUserDTO.getProfile())) {
                userEntity.setProfile(superUserDTO.getProfile());
            } else {
                // 프로필이 없는 유저의 프로필을 변경하지 않고 수정 시,
                // DTO의 프로필 필드는 빈 문자열이 되므로 DB에 null로 저장
                userEntity.setProfile(null);
            }

            // ... (기존 역할 업데이트 로직 유지) ...
            if (superUserDTO.getRole() != null) {
                userEntity.setRole(Role.valueOf(superUserDTO.getRole()));
            }

            userRepository.save(userEntity);
        }
    }

    // 관리자 페이지 - 유저 정보 삭제
    public void delete(int id) {
        // 1. 삭제할 유저의 기존 정보를 가져옵니다.
        Optional<UserEntity> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();

            // 2. 프로필 URL이 존재하면 S3에서 파일을 삭제합니다.
            if (userEntity.getProfile() != null && !userEntity.getProfile().isEmpty()) {
                s3Uploader.deleteFile(userEntity.getProfile());
            }

            // 3. 데이터베이스에서 유저 정보를 삭제합니다.
            userRepository.deleteById(id);
        }
    }
}
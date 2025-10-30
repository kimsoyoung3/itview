package com.example.itview_spring.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.itview_spring.Constant.ActivityLogType;
import com.example.itview_spring.Entity.ActivityLogEntity;
import com.example.itview_spring.Entity.FollowEntity;
import com.example.itview_spring.Entity.RatingEntity;
import com.example.itview_spring.Repository.ActivityLogRepository;
import com.example.itview_spring.Repository.ContentRepository;
import com.example.itview_spring.Repository.RatingRepository;
import com.example.itview_spring.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RatingService {

    private final RatingRepository ratingRepository;
    private final NotificationService notificationService;
    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;

    // 별점 등록
    public void rateContent(Integer userId, Integer contentId, Integer score) {

        if (!contentRepository.existsById(contentId)) {
            throw new NoSuchElementException("Invalid contentId: " + contentId);
        }

        // 기존 별점 조회
        Optional<RatingEntity> existingRating = ratingRepository.findByUserIdAndContentId(userId, contentId);

        if (existingRating.isEmpty()) {
            RatingEntity ratingEntity = new RatingEntity();
            ratingEntity.setUser(userRepository.findById(userId).get());
            ratingEntity.setContent(contentRepository.findById(contentId).get());
            ratingEntity.setScore(score);
            ratingRepository.save(ratingEntity);

            ActivityLogEntity activityLog = new ActivityLogEntity();
            activityLog.setUser(ratingEntity.getUser());
            activityLog.setType(ActivityLogType.RATING);
            activityLog.setReferenceId(ratingEntity.getId());
            activityLog.setTimestamp(LocalDateTime.now());
            activityLogRepository.save(activityLog);
        }
        else {
            // 기존 별점이 있는 경우 업데이트
            RatingEntity ratingEntity = existingRating.get();
            ratingEntity.setScore(score);
            ratingRepository.save(ratingEntity);

            ActivityLogEntity activityLog = activityLogRepository.findByReferenceIdAndType(ratingEntity.getId(), ActivityLogType.RATING);
            if (activityLog != null) {
                activityLog.setTimestamp(LocalDateTime.now());
                activityLog.setIsUpdate(true);
                activityLogRepository.save(activityLog);
            } else {
                activityLog = new ActivityLogEntity();
                activityLog.setUser(ratingEntity.getUser());
                activityLog.setType(ActivityLogType.RATING);
                activityLog.setReferenceId(ratingEntity.getId());
                activityLog.setTimestamp(LocalDateTime.now());
                activityLog.setIsUpdate(true);
                activityLogRepository.save(activityLog);
            }
        }

        // 팔로워들에게 알림 전송
        for (FollowEntity follow : userRepository.findById(userId).get().getFollowers()) {
            notificationService.sendNotification(follow.getFollower().getId());
        }
    }

    // 별점 삭제
    public void deleteRating(Integer userId, Integer contentId) {
        if (!contentRepository.existsById(contentId)) {
            throw new NoSuchElementException("Invalid contentId: " + contentId);
        }
        Optional<RatingEntity> ratingEntity = ratingRepository.findByUserIdAndContentId(userId, contentId);
        if (ratingEntity.isEmpty()) {
            return;
        }
        activityLogRepository.deleteByReferenceIdAndType(ratingEntity.get().getId(), ActivityLogType.RATING);
        ratingRepository.delete(ratingEntity.get());
    }
}

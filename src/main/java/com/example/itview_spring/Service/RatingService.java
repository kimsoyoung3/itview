package com.example.itview_spring.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.itview_spring.Entity.RatingEntity;
import com.example.itview_spring.Repository.ContentRepository;
import com.example.itview_spring.Repository.RatingRepository;
import com.example.itview_spring.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RatingService {

    private final RatingRepository ratingRepository;
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
        }
        else {
            // 기존 별점이 있는 경우 업데이트
            RatingEntity ratingEntity = existingRating.get();
            ratingEntity.setScore(score);
            ratingRepository.save(ratingEntity);
        }        
    }

    // 별점 삭제
    public void deleteRating(Integer userId, Integer contentId) {
        if (!contentRepository.existsById(contentId)) {
            throw new NoSuchElementException("Invalid contentId: " + contentId);
        }
        ratingRepository.deleteByUserIdAndContentId(userId, contentId);
    }
}

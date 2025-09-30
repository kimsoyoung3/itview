package com.example.itview_spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.itview_spring.Entity.FollowEntity;

public interface FollowRepository extends JpaRepository<FollowEntity, Integer> {
    FollowEntity findByFollower_IdAndFollowing_Id(Integer followerId, Integer followingId);
}

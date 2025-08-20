package com.example.itview_spring.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.itview_spring.Entity.SocialEntity;


@Repository
public interface SocialRepository extends JpaRepository<SocialEntity, Integer> {
    Optional<SocialEntity> findByProviderAndProviderId(String provider, String providerId);
}

package com.example.itview_spring.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.itview_spring.Entity.SocialEntity;


@Repository
public interface SocialRepository extends JpaRepository<SocialEntity, Integer> {
    @Query("SELECT s FROM SocialEntity s JOIN FETCH s.user u WHERE s.provider = :provider AND s.providerId = :providerId")
    Optional<SocialEntity> findByProviderAndProviderId(@Param("provider") String provider, @Param("providerId") String providerId);
}

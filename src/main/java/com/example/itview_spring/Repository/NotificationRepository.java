package com.example.itview_spring.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.itview_spring.Entity.NotificationEntity;
import com.example.itview_spring.Constant.Replyable;


public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {
    Page<NotificationEntity> findAllByUser_IdOrderByIdDesc(Integer userId, Pageable pageable);
    void deleteByTargetIdAndTargetType(Integer targetId, Replyable targetType);
}

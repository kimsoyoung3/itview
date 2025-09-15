package com.example.itview_spring.DTO;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CollectionResponseDTO {

    private Integer id;
    private String title;
    private Integer contentCount;
    private LocalDateTime updatedAt;
    private Boolean liked;
    private Long likeCount;
    private Long replyCount;
    private String description;
    private List<String> poster;
    private UserProfileDTO user;

    public CollectionResponseDTO(Integer id, String title, Integer contentCount, LocalDateTime updatedAt, Boolean liked, Long likeCount, Long replyCount, String description, UserProfileDTO user) {
        this.id = id;
        this.title = title;
        this.contentCount = contentCount;
        this.updatedAt = updatedAt;
        this.liked = liked;
        this.likeCount = likeCount;
        this.replyCount = replyCount;
        this.description = description;
        this.user = user;
    }
}

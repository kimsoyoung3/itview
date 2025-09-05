package com.example.itview_spring.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private UserProfileDTO userProfile;
    private Long ratingCount;
    private Long commentCount;
    private Long collectionCount;
    private Long personLikeCount;
    private Long collectionLikeCount;
    private Long commentLikeCount;
}

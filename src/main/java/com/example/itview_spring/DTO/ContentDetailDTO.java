package com.example.itview_spring.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContentDetailDTO {
    
    ContentResponseDTO contentInfo;
    Boolean wishlistCheck;
    Integer myRating;

}

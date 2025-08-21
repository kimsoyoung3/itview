package com.example.itview_spring.Controller.Content;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.itview_spring.Config.CustomUserDetails;
import com.example.itview_spring.DTO.ContentDetailDTO;
import com.example.itview_spring.DTO.CreditDTO;
import com.example.itview_spring.DTO.RatingRequestDTO;
import com.example.itview_spring.Service.ContentService;
import com.example.itview_spring.Service.CreditService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ContentRestController {

    private final ContentService contentService;
    private final CreditService creditService;

    @GetMapping("/{id}")
    public ResponseEntity<ContentDetailDTO> getContentDetail(@PathVariable("id") Integer id) {
        ContentDetailDTO contentDetail = contentService.getContentDetail(id);
        return ResponseEntity.ok(contentDetail);
    }

    @GetMapping("/{id}/credit")
    public ResponseEntity<Page<CreditDTO>> getContentCredit(@PageableDefault(page=1) Pageable pageable, @PathVariable("id") Integer id) {
        return ResponseEntity.ok(creditService.getCreditByContentId(pageable, id));
    }

    @PostMapping("/{id}/rating")
    public ResponseEntity<Void> postContentRating(@PathVariable("id") Integer id,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @RequestBody RatingRequestDTO ratingRequest) {
        contentService.rateContent(userDetails.getId(), id, ratingRequest.getScore());
        // System.out.println(ratingRequest);
        // System.out.println(userDetails);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/rating")
    public ResponseEntity<Void> deleteContentRating(@PathVariable("id") Integer id,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (contentService.deleteRating(userDetails.getId(), id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}

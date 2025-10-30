package com.example.itview_spring.Controller.Content;

import java.util.List;
import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.itview_spring.DTO.GalleryDTO;
import com.example.itview_spring.Service.GalleryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GalleryController {

    private final GalleryService galleryService;

    /** 갤러리 등록/수정 폼 (GET) */
    @GetMapping("/content/{contentId}/gallery")
    public String galleryForm(@PathVariable("contentId") Integer contentId, Model model) {
        List<GalleryDTO> galleryList = galleryService.getGallerysByContentId(contentId);
        model.addAttribute("galleryList", galleryList);
        model.addAttribute("contentId", contentId);

        return "content/galleryForm";
    }

    /** 갤러리 단건 업로드 및 DB 저장 */
    @PostMapping("/content/{contentId}/gallery/upload-image")
    @ResponseBody
    public ResponseEntity<GalleryDTO> uploadImage(@PathVariable("contentId") Integer contentId,
                                                  @RequestParam("file") MultipartFile file) {
        try {
            GalleryDTO newGallery = galleryService.addGallery(contentId, file);
            return ResponseEntity.ok(newGallery);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    /** 갤러리 단건 삭제 */
    @DeleteMapping("/content/{contentId}/gallery/{galleryId}")
    @ResponseBody
    public ResponseEntity<String> deleteGallery(@PathVariable("galleryId") Integer galleryId) {
        try {
            galleryService.deleteGallery(galleryId);
            return ResponseEntity.ok("갤러리가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("갤러리 삭제 중 오류가 발생했습니다.");
        }
    }
}
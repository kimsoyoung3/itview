package com.example.itview_spring.Controller.Content;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.itview_spring.DTO.VideoDTO;
import com.example.itview_spring.Entity.VideoEntity;
import com.example.itview_spring.Service.VideoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    /**
     * 📌 영상 등록/수정 폼 (GET)
     * 이 페이지는 모든 비디오 목록을 보여주는 역할만 합니다.
     */
    @GetMapping("/content/{contentId}/video")
    public String videoForm(@PathVariable("contentId") Integer contentId, Model model) {
        List<VideoEntity> videoList = videoService.getVideosByContentId(contentId);
        model.addAttribute("videoList", videoList);
        model.addAttribute("contentId", contentId);

        return "content/videoForm";
    }

    /**
     * 📌 영상 등록 (AJAX 요청 처리)
     * 유튜브 URL을 받아 영상 정보를 저장합니다.
     */
    @PostMapping("/content/{contentId}/video/register")
    @ResponseBody // JSON 응답을 위해 추가
    public ResponseEntity<VideoDTO> registerVideo(@PathVariable("contentId") Integer contentId,
                                                  @RequestBody VideoDTO videoDTO) {
        try {
            // VideoDTO에는 사용자가 입력한 URL만 담겨있습니다.
            // 서비스에서 URL을 분석해 썸네일과 제목 등을 추출합니다.
            VideoDTO createdVideo = videoService.createVideo(contentId, videoDTO);
            return ResponseEntity.ok(createdVideo);
        } catch (Exception e) {
            // 오류 발생 시 HTTP 500 상태 코드와 함께 오류 메시지 반환
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 📌 영상 삭제 (AJAX 요청 처리)
     */
    @DeleteMapping("/content/{contentId}/video/{videoId}")
    @ResponseBody
    public ResponseEntity<String> deleteVideo(@PathVariable("videoId") Integer videoId) {
        try {
            videoService.deleteVideo(videoId);
            return ResponseEntity.ok("영상이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("영상 삭제 중 오류가 발생했습니다.");
        }
    }
}
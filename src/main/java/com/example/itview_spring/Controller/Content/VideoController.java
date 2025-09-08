package com.example.itview_spring.Controller.Content;

import java.util.List;
import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.itview_spring.DTO.VideoDTO;
import com.example.itview_spring.Entity.VideoEntity;
import com.example.itview_spring.Repository.VideoRepository;
import com.example.itview_spring.Service.VideoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;
    private final ModelMapper modelMapper;

    //    1. 기능 흐름
//    기능명	URL	설명
//    콘텐츠 등록 폼 이동	GET /content/register	콘텐츠 등록 화면 이동
//    콘텐츠 등록 처리	POST /content/register	저장 후 장르 선택으로 리디렉트
//    콘텐츠 전체 조회	GET /content/list	페이지네이션 포함
//    콘텐츠 상세 보기	GET /content/detail?id={id}	contentId 기반 상세 정보
//    콘텐츠 수정 폼	GET /content/{id}/update	콘텐츠 수정 화면
//    콘텐츠 수정 처리	POST /content/{id}/update	저장 후 장르 수정으로 이동
//    콘텐츠 삭제	GET /content/delete?id={id}	삭제 후 리스트로 이동
//    장르 선택 폼	GET /content/{id}/genre	장르 선택 화면 표시
//    장르 저장 처리	POST /content/{id}/genre	저장 후 영상 등록으로 이동
//



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////0825 vidio 추가///////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // 📌 영상 목록 페이지
    @GetMapping("/content/{contentId}/video/list")
    public String videoList(@PathVariable Integer contentId,
                            Model model,
                            @ModelAttribute("error") String error,
                            @ModelAttribute("message") String message) {
        List<VideoEntity> videoList = videoService.getVideosByContentId(contentId);
        model.addAttribute("videoList", videoList);
        model.addAttribute("contentId", contentId);
        model.addAttribute("error", error);
        model.addAttribute("message", message);
        return "content/videoList"; // 별도 목록 템플릿
    }

    /**
     * 📌 단일 비디오 상세 보기
     */
    @GetMapping("/content/{contentId}/video/{videoId}")
    public String getVideo(@PathVariable Integer contentId,
                           @PathVariable Integer videoId,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            VideoEntity video = videoService.getVideoById(videoId);
            model.addAttribute("video", video);
            return "content/videoDetail"; // ✅ 상세 페이지
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/content/" + contentId + "/video/list"; // ✅ 목록 페이지로 리다이렉트
        }
    }
    /**
     * 영상 등록/수정 폼(get)
     */
    @GetMapping("/content/{contentId}/video")
    public String videoForm(@PathVariable("contentId") Integer contentId,
                            @RequestParam(value = "id", required = false) Integer videoId,
                            Model model,
                            RedirectAttributes redirectAttributes,
                            @ModelAttribute("error") String error,
                            @ModelAttribute("message") String message) {

        // 콘텐츠 유효성 검사
        // 콘텐츠가 유효한지 검사하는 로직을 제거하고, 대신 `videoservice`를 이용한 콘텐츠 확인
        // 콘텐츠가 존재하지 않으면 IllegalArgumentException을 던지도록 변경
        // if (ContentService.read(contentId) == null) {  // 삭제된 부분
        //     throw new IllegalArgumentException("존재하지 않는 콘텐츠 ID: " + contentId);  // 콘텐츠가 없으면 예외 처리
        // }  // 삭제된 부분

        VideoDTO videoDTO = new VideoDTO();
        if (videoId != null) {
            try {
                VideoEntity entity = videoService.getVideoById(videoId);
                videoDTO = new VideoDTO(entity.getId(), entity.getTitle(), entity.getImage(), entity.getUrl());
            } catch (NoSuchElementException e) {
                error = e.getMessage();
            }
        }

        // 2️⃣ 전체 VideoDTO 리스트 조회 (목록)
        List<VideoEntity> videoList = videoService.getVideosByContentId(contentId);  // `ContentService`에서 `getVideosByContentId` 메서드 호출로 변경

        // 3️⃣ ContentId도 모델에 전달
        model.addAttribute("contentId", contentId); // 콘텐츠 ID를 모델에 전달
        model.addAttribute("videoDTO", videoDTO);
        model.addAttribute("videoList", videoList);  // 모델에 전체 영상 목록 추가
        model.addAttribute("error", error);
        model.addAttribute("message", message);

        return "content/videoForm";  // 영상 등록/수정 폼을 보여줄 템플릿 경로
    }

    /**
     * 영상 등록 또는 수정 처리 (post)
     */
    @PostMapping("/content/{contentId}/video")
    public String createVideo(
            @PathVariable("contentId") Integer contentId,
            @ModelAttribute VideoDTO videoDTO,
            RedirectAttributes redirectAttributes) {

        try {
            if (videoDTO.getId() == null) {
                videoService.createVideo(contentId, videoDTO);
                redirectAttributes.addFlashAttribute("message", "영상이 등록되었습니다.");
            } else {
                videoService.updateVideo(videoDTO.getId(), videoDTO);
                redirectAttributes.addFlashAttribute("message", "영상이 수정되었습니다.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/content/" + contentId + "/video/list";  // 다시 영상 리스트 페이지로 리다이렉트
    }

    /**
     * 영상 삭제 처리
     */
    //삭제 처리 메서드에서는 videoservice.deleteVideo(videoId)를 호출하여
    // 해당 영상을 삭제하고, 삭제 완료 메시지를 리다이렉트 후 전달합니다.
    @PostMapping("/content/{contentId}/video/delete")
    public String deleteVideo(@PathVariable Integer contentId,
                              @RequestParam("videoId") Integer videoId,
                              RedirectAttributes redirectAttributes) {
        System.out.println("🗑️ [Video 삭제] contentId == " + contentId); // 삭제 로그 출력

        try {
            videoService.deleteVideo(videoId);// 영상 삭제 메서드 호출
            redirectAttributes.addFlashAttribute("message", "영상이 삭제되었습니다.");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/content/" + contentId + "/video/list";  // 삭제 후 영상 리스트 페이지로 리다이렉트
    }
}

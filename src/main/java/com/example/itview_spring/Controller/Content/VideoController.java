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
    private final VideoRepository videoRepository;
    private final ModelMapper modelMapper;
    
    /**
     * 영상 등록/수정 폼(get)
     */
    @GetMapping("/content/{contentId}/video")
    public String videoForm(@PathVariable("contentId") Integer contentId,
                            @RequestParam(value = "id", required = false) Integer videoId,
                            Model model) {

        // videoId로 VideoEntity 가져오기 (비즈니스 로직)

        VideoEntity videoEntity = videoRepository.findById(videoId)
                .orElseThrow(() -> new NoSuchElementException("해당 영상이 존재하지 않습니다: " + videoId));


        // 수정 후: Entity -> DTO 매핑 적용
        VideoDTO videoDTO = modelMapper.map(videoEntity, VideoDTO.class);
        model.addAttribute("videoDTO", videoDTO); // 모델에 VideoDTO 추가

        // 수정할 비디오가 없을 경우 기본값으로 초기화
        if (videoId != null && videoDTO == null) {
            throw new NoSuchElementException("해당 ID에 대한 영상이 존재하지 않습니다: " + videoId);  // 영상이 없으면 예외 처리
        }

        // 전체 VideoDTO 리스트 조회 (목록)
        List<VideoDTO> videoList = videoService.getVideosByContentId(contentId);  // `ContentService`에서 `getVideosByContentId` 메서드 호출로 변경
        model.addAttribute("videoList", videoList);  // 모델에 전체 영상 목록 추가

        // ContentId도 모델에 전달
        model.addAttribute("contentId", contentId); // 콘텐츠 ID를 모델에 전달

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

        if (videoDTO.getId() == null) {
            // 신규 등록 처리
            videoService.createVideo(contentId, videoDTO);  // 신규 영상 등록
            redirectAttributes.addFlashAttribute("message", "등록되었습니다."); // 등록 완료 메시지
        } else {
            // 수정 처리
            videoService.updateVideo(videoDTO.getId(), videoDTO);  // 수정된 영상 처리
            redirectAttributes.addFlashAttribute("message", "수정되었습니다."); // 수정 완료 메시지
        }

        redirectAttributes.addAttribute("contentId", contentId);  // contentId를 리다이렉트에 포함
        return "redirect:/content/" + contentId + "/video";  // 다시 영상 리스트 페이지로 리다이렉트
    }

    /**
     * 영상 삭제 처리
     */
//삭제 처리 메서드에서는 contentService.deleteVideo(videoId)를 호출하여
// 해당 영상을 삭제하고, 삭제 완료 메시지를 리다이렉트 후 전달합니다.
    @PostMapping("/content/{contentId}/video/delete")
    public String deleteVideo(@PathVariable Integer contentId,
                              @RequestParam("videoId") Integer videoId,
                              RedirectAttributes redirectAttributes) {
        System.out.println("🗑️ [Video 삭제] contentId == " + contentId); // 삭제 로그 출력
        System.out.println("<UNK> [Video <UNK>] videoId == " + videoId);  // 삭제 로그 출력

        videoService.deleteVideo(videoId);  // 영상 삭제 메서드 호출
        redirectAttributes.addFlashAttribute("message", "삭제되었습니다.");  // 삭제 완료 메시지

        return "redirect:/content/" + contentId + "/video";  // 삭제 후 영상 리스트 페이지로 리다이렉트
    }
}

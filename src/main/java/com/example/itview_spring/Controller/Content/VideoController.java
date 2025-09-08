package com.example.itview_spring.Controller.Content;

import com.example.itview_spring.DTO.GalleryDTO;
import com.example.itview_spring.DTO.VideoDTO;
import com.example.itview_spring.Service.CreditService;
import com.example.itview_spring.Service.GalleryService;
import com.example.itview_spring.Service.VideoService;
import com.example.itview_spring.Util.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/content")
public class VideoController {
    private final VideoService videoService;  //Video 서비스
    private final GalleryService galleryService;  // 갤러리 서비스

    private final CreditService creditService;  // Credit 서비스

    private final PageInfo pageInfo;

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

//    // 콘텐츠 유효성 검사
//        if (videoservice.read(contentId) == null) {
//            throw new IllegalArgumentException("존재하지 않는 콘텐츠 ID: " + contentId);  // 콘텐츠가 없으면 예외 처리
//        }
//
//    // 1️⃣ 단일 VideoDTO 조회 (수정 모드)
//    VideoDTO videoDTO = (videoId != null)
//            ? videoservice.getVideoByI(videoId)  // 수정 모드일 경우 영상 ID로 데이터 조회
//            : new VideoDTO();             // 등록 모드일 경우 새 VideoDTO 생성
//
//    // 수정할 비디오가 없을 경우 기본값으로 초기화
//        if (videoId != null && videoDTO == null) {
//            throw new NoSuchElementException("해당 ID에 대한 영상이 존재하지 않습니다: " + videoId);  // 영상이 없으면 예외 처리
//        }
//
//                model.addAttribute("videoDTO", videoDTO);
//
//    // 2️⃣ 전체 VideoDTO 리스트 조회 (목록)
//    List<VideoDTO> videoList = videoservice.getVideosByContentId(contentId);  // 콘텐츠 ID에 해당하는 영상 리스트 조회
//        model.addAttribute("videoList", videoList); // 모델에 전체 영상 목록 추가
//
//    // 3️⃣ ContentId도 모델에 전달
//        model.addAttribute("contentId", contentId); // 콘텐츠 ID를 모델에 전달
//
//        return "content/videoForm";  // 영상 등록/수정 폼을 보여줄 템플릿 경로
//                }


    /**
     * 영상 등록/수정 폼(get)
     */
    @GetMapping("/content/{contentId}/video")
    public String videoForm(@PathVariable("contentId") Integer contentId,
                            @RequestParam(value = "id", required = false) Integer videoId,
                            Model model) {

        // 콘텐츠 유효성 검사
        // 콘텐츠가 유효한지 검사하는 로직을 제거하고, 대신 `videoservice`를 이용한 콘텐츠 확인
        // 콘텐츠가 존재하지 않으면 IllegalArgumentException을 던지도록 변경
        // if (ContentService.read(contentId) == null) {  // 삭제된 부분
        //     throw new IllegalArgumentException("존재하지 않는 콘텐츠 ID: " + contentId);  // 콘텐츠가 없으면 예외 처리
        // }  // 삭제된 부분

        // 1️⃣ 단일 VideoDTO 조회 (수정 모드)
        VideoDTO videoDTO = (videoId != null)
                ? videoService.getVideoByContentId(videoId)  // videoservice에서 단일 VideoId 조회 메서드로 변경
                : new VideoDTO();                      // 등록 모드에서는 새 VideoDTO 생성
        model.addAttribute("videoDTO", videoDTO); // 모델에 VideoDTO 추가


        // 수정할 비디오가 없을 경우 기본값으로 초기화
        if (videoId != null && videoDTO == null) {
            throw new NoSuchElementException("해당 ID에 대한 영상이 존재하지 않습니다: " + videoId);  // 영상이 없으면 예외 처리
        }

        // 수정할 비디오가 없을 경우 기본값으로 초기화
        // videoId가 있을 때 해당 영상이 존재하지 않으면 예외를 던지는 코드로 변경
        if (videoId != null && videoDTO == null) {
            throw new NoSuchElementException("해당 ID에 대한 영상이 존재하지 않습니다: " + videoId);  // 영상이 없으면 예외 처리
        }

        // 2️⃣ 전체 VideoDTO 리스트 조회 (목록)
        List<VideoDTO> videoList = videoService.getVideosByContentId(contentId);  // `ContentService`에서 `getVideosByContentId` 메서드 호출로 변경
        model.addAttribute("videoList", videoList);  // 모델에 전체 영상 목록 추가

        // 3️⃣ ContentId도 모델에 전달
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
    //삭제 처리 메서드에서는 videoservice.deleteVideo(videoId)를 호출하여
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
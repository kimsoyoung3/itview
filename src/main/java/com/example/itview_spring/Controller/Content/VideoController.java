package com.example.itview_spring.Controller.Content;

import com.example.itview_spring.DTO.VideoDTO;
import com.example.itview_spring.DTO.PageInfoDTO;
import com.example.itview_spring.Service.VideoService;
import com.example.itview_spring.Util.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;
    private final PageInfo pageInfo;

    // ==================== ERROR PAGE HANDLER ====================
    //    500 에러가 나더라도 커스텀 오류 페이지 추가 가능 (선택 사항)-----------------------------------
//    @Controller
//    public class CustomErrorController implements ErrorController {
//        @RequestMapping("/error")
//        public String handleError() {
//            return "error/customError"; // templates/error/customError.html
//        }
//    }
// ==================== CONTENT CRUD ==========================
// 등록 폼 이동
    @GetMapping("/video/register")
    public String newVideo() {

        return "video/register";
    }

    // 등록 처리 후 → 장르 선택 페이지로 이동
    @PostMapping("/video/register")
    public String newVideo(VideoDTO videoDTO) {
        //데이터 저장처리 (Service -> Controller
        VideoDTO savedVideo = videoService.create(videoDTO); // 저장 후

        System.out.println(" 00 savedVideo ==" + savedVideo);
//        return "redirect:/video/" + savedVideo.getId() + "/genre";     // 장르 등록 폼으로 이동
           return "redirect:/video/list";
    }


    //localhost:8080/video/list 확인함(@GetMppping
    //전체강의 조회후 (list.html)로이동

    /**
     * 전체조회
     *
     * @param pageable 조회할 페이지 번호
     * @param model    결좌 전달
     * @return 페이지 이동
     */
    @GetMapping("/video/list")
    // 전체 목록 조회
    public String listVideo(@PageableDefault(page = 1) Pageable pageable, Model model) {
        //모든 데이터를 조회
        //keyword  추가 ****
        Page<VideoDTO> videoDTOS = videoService.getAllVideos(pageable);
        model.addAttribute("videoDTOS", videoDTOS);
        //    System.out.println("videoDTO.            ==", videoDTOS);

        PageInfoDTO pageInfoDTO = pageInfo.getPageInfo(videoDTOS);
        model.addAttribute("pageInfoDTO", pageInfoDTO);
//        model.addAttribute("videoDTOS", videoDTOS);
//        model.addAttribute("pageInfoDTO", pageInfo.getPageInfo(videoDTOS));
        return "video/list";
    }

    // 상세 보기
// @GetMapping("/video/{id:\\d+}")
//    @GetMapping("/video/detail")
//    @GetMapping("/video/{id}/detail")
//    public String detailVideo(@PathVariable("id") Integer id, Model model) {
    @GetMapping("/video/detail")
    public String detailVideo(Integer id, Model model) {
        // URL 경로 변수인 {id}를 받으려면 @PathVariable을 써야 합니다.
        VideoDTO videoDTO = videoService.read(id);
        model.addAttribute("videoDTO", videoDTO);
        System.out.println("deteil id         ===>" + id);
        System.out.println("deteil videoDTO ===>" + videoDTO);
        return "video/detail"; // 경로가 정확한지 확인 필요
    }

    // 수정 폼
    @GetMapping("/video/{id}/update")
    public String updateVideo(@PathVariable("id") Integer id, Model model) {
        VideoDTO videoDTO = videoService.read(id);
        model.addAttribute("videoDTO", videoDTO);
        return "video/update";
    }

    // 수정 처리 (→ 장르 수정 화면으로 리디렉트)
    @PostMapping("/video/{id}/update")
    public String updateVideoProc(@PathVariable("id") Integer id, VideoDTO videoDTO) {
//        videoService.update(id, videoDTO);
//        return "redirect:/video/list";
        VideoDTO savedVideo = videoService.update(id, videoDTO);
        System.out.println(" 22 savedVideo ==" + savedVideo);
        return "redirect:/video/" + savedVideo.getId() + "/genre";     // 장르 수정 폼으로 이동
    }

    //삭제처리
    @GetMapping("/video/delete")
    public String deleteVideo(@RequestParam("id") Integer id) {
        videoService.delete(id);
        return "redirect:/video/list";
    }

}

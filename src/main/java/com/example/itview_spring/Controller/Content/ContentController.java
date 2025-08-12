package com.example.itview_spring.Controller.Content;

import com.example.itview_spring.DTO.ContentDTO;
import com.example.itview_spring.DTO.PageInfoDTO;
import com.example.itview_spring.Service.ContentService;
import com.example.itview_spring.Util.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;
    private final PageInfo pageInfo;


    //강의등록페이지

    @GetMapping("/content/new")
    public String newContent() {

        return "register" ;
    }

    //동록 강의저장후 목록 으로 이동
    @PostMapping("/content/new")
    public String newContent(ContentDTO contentDTO) {
        //데이터 저장처리 (Service -> Controller
        contentService.create(contentDTO);
        return "redirect:/content/list";
    }
    //localhost:8080/content/list 확인함(@GetMppping
    //전체강의 조회후 (list.html)로이동
    /**
     * 전체조회
     * @param pageable 조회할 페이지 번호
     * @param model 결좌 전달
     * @return 페이지 이동
     */
    @GetMapping("/content/list")
    //모든 데이터를 조회

    public String listContent(@PageableDefault(page=1) Pageable pageable, Model model) {
        //모든 데이터를 조회
        //keyword  추가 **** 
        Page<ContentDTO> contentDTOS =contentService.getAllContents(pageable);
        model.addAttribute("contentDTOS",contentDTOS);

        PageInfoDTO pageInfoDTO = pageInfo.getPageInfo(contentDTOS);
        model.addAttribute("pageInfoDTO", pageInfoDTO);
      
        return "list" ;

////////////////////////////////////////////////////////////////////////////////////////////
//   페이지 작업전
//        List<ContentEntity> contents = contentRepository.findAll();
//        List<ContentDTO> contentDTOS = Arrays.asList(modelMapper.map(contents, ContentDTO[].class));
//        return contentDTOS;
////////////////////////////////////////////////////////////////////////////////////////////
    }
    //상세보기
    @GetMapping("/content/view")
    public String detailContent(Integer id ,Model model) {
        ContentDTO contentDTO =contentService.read(id);
        model.addAttribute("contentDTO",contentDTO);
        //     System.out.printf("contentDTO.getContentId==",contentDTO.getContentId());
        //     System.out.printf("contentDTO.            ==",contentDTO);
        return "detail" ;
    }

    //수정페이지 이동
    @GetMapping("/content/edit")
    public String editContent(@RequestParam("id") Integer id ,Model model) {
        ContentDTO contentDTO =contentService.read(id);
        model.addAttribute("contentDTO",contentDTO);
        return  "edit";
    }

    //수정된 강위저장후 목록으로이동
    @PostMapping("/content/edit")
    public String editContentProc(ContentDTO contentDTO) {
        contentService.update(contentDTO);
        return "redirect:/content/list";
    }

    //삭제처리
    @GetMapping("/content/delete")
    public String deleteContent(Integer id) {
        contentService.delete(id);
        return "redirect:/content/list";
    }
}

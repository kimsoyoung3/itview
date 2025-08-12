package com.example.itview_spring.Controller.Content;

import com.example.itview_spring.DTO.ContentDTO;
import com.example.itview_spring.Service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller 
@RequiredArgsConstructor
@RequestMapping("/controller")
public class ContentViewController {
    private final ContentService contentService;

    //목록페이지 이동
    //templates// register경로 생성한다
    //index.html복사해서 list.html, detail.html,form.html 만든다
    //목록 HTML로 이동 (list.html)
    //
    @GetMapping("/list")
    //public String 이름은마음애로(Model model) { //반드시 model은 org.sp
    //List<ProductDTO> productDTOS = productService.안가르켜줘();
    public String list(Model model){//반드시 model은 org.sp
//        //service에서 목록작업하는 함수를 지정
//        List<ContentDTO> ContentDTOS = contentService.list();
//        //model로 html에전달
//        model.addAttribute("ContentDTOS", ContentDTOS);
//        //보고싶은 html파일를지정
        return "register/list";
    }
    //상세페이지 이동
    //상세 HTML로 이동 (detail.html)  //@getmapping("/detail")
    //Service에 있는 입력값()안에 작성
    //public String 상세보기(@PathVariable Integer id, Model model) {
    //ProductDTO productDTO = productService.역시가르켜줘(orderId);
    //public String read(@PathVariable Integer id,  Model model) {   ex)  즉 read , detail 사용예
    @GetMapping("/read/{id}") //@GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        //해당내용을 조회
        ContentDTO ContentDTO = contentService.read(id);//위에입력받은 내용을 서비스에 그대로 작성
        model.addAttribute("ContentDTO", ContentDTO);
        return "register/detail";
    }
    //등록페이지 이동
    //삽입 HTML로 이동 (form.html)
    // public String 등록(Model model) {
    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("ContentDTO", new ContentDTO()); //빈 DTO전달
        return "register/form";
    }
    //수정페이지 이동
    //수정 HTML로 이동 (Form.html)
    //public String 수정(@PathVariable Integer id, Model model) {
    //        ContentDTO ContentDTO = contentService.역시안가켜줘(orderId);
    @GetMapping("/update/{id}")
    public String update(@PathVariable Integer id,  Model model){
        ContentDTO ContentDTO = contentService.read(id);
        model.addAttribute("ContentDTO", ContentDTO);
        return "register/form";
    }
    
    
}

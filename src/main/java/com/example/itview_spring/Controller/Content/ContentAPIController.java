//package com.example.itview_spring.Controller;
//
//import com.example.itview_spring.DTO.ContentDTO;
//import com.example.itview_spring.Service.ContentService;
//import com.example.itview_spring.Util.PageInfo;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/content")     //api추가
//@CrossOrigin(origins = "http://localhost:3001") //react접속 허용
//public class ContentAPIController {
//    private final ContentService service;
//    private final PageInfo pageInfo;
//
//    //사용할 서비스를 추가
//    private final ContentService contentService;
//    // service에서 작성한 순서대로 사용
//    //목록조회(전체조회ㅣ
//    @GetMapping
////    public ResponseEntity<List<ContentDTO>> List() {
////        //    List<ContentDTO> registerDTOS = regsiterService.List();
////        //    return RegsterEntity.ok(regsterDTOS);                   2줄을 1줄로 작성예
////        return ResponseEntity.ok(contentService.List());
//    public ResponseEntity<List<ContentDTO>> List() {
//        //    List<ContentDTO> registerDTOS = regsiterService.List();
//        //    return RegsterEntity.ok(regsterDTOS);                   2줄을 1줄로 작성예
//    //    return ResponseEntity.ok(contentService.List());
//}
//
//    //상세조회(개별조회)
//    //public ResponseEntity<ContentDTO> getRegisterById(@PathVariable Integer id) {
//    //public ResponseEntity<ContentDTO> read(@PathVariable Integer id) {  read 사용 시 ex)
//    @GetMapping("/{id}")
//    public ResponseEntity<ContentDTO> read(@PathVariable Integer id) {
//        // 다른 사용 예
//        // ContentDTO registerDTO = contentService.getRegisterById(id);
//        //  return ResponseEntity.ok(registerDTO);
//        ContentDTO dto = contentService.read(id);
//        return dto != null ? ResponseEntity.ok(dto):ResponseEntity.notFound().build();
//    }
//
//    //삽입처리 , 저장처리
//    @GetMapping("/new")    /// ///////  다시 처리
//    public ResponseEntity<ContentDTO> createAInfo() {
//        return ResponseEntity.ok(new ContentDTO());
//    }
//
//    //신규등록 ,삽입처리
//    //   @PostMapping  Nova PGm 사용한것
//    //   public ResponseEntity<Void> create(@RequestBody ProductDTO productDTO) {
//    //       productService.create(productDTO);
//    //       return ResponseEntity.ok().build();
//    //   }
//    //public ResponseEntity<ContentDTO> create(@RequestBody ContentDTO registerDTO) { //ex)
//    @PostMapping
//    public ResponseEntity<ContentDTO> create(@RequestBody ContentDTO registerDTO) {
//        System.out.println("create===> "+registerDTO);
//
//        return ResponseEntity.ok(contentService.create(registerDTO));
//    }
//
//    //수정
//    @PutMapping("/{id}")
//    public ResponseEntity<ContentDTO> update(@PathVariable Integer id, @RequestBody ContentDTO registerDTO) {
//        ContentDTO updated = contentService.update(id, registerDTO);
//        return updated != null ? ResponseEntity.ok(updated):ResponseEntity.notFound().build();
//    }
//    //삭제
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Integer id) {
//        boolean deleted = contentService.delete(id);
//        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
//    }
//}

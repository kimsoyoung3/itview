package com.example.itview_spring.Controller.Search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.itview_spring.Config.CustomUserDetails;
import com.example.itview_spring.DTO.CollectionResponseDTO;
import com.example.itview_spring.DTO.ContentDTO;
import com.example.itview_spring.DTO.PersonDTO;
import com.example.itview_spring.DTO.SearchContentDTO;
import com.example.itview_spring.DTO.UserResponseDTO;
import com.example.itview_spring.Service.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchRestController {
    
    private final SearchService searchService;

    @GetMapping("/content")
    public ResponseEntity<SearchContentDTO> searchContents(@RequestParam("keyword") String keyword, @PageableDefault(page = 1) Pageable page) {
        return ResponseEntity.ok(searchService.searchContents(keyword, page.getPageNumber()));
    }

    @GetMapping("/content/detail")
    public ResponseEntity<Page<ContentDTO>> searchContents(@RequestParam("type") String type, @RequestParam("keyword") String keyword, @PageableDefault(page = 1) Pageable page) {
        return ResponseEntity.ok(searchService.searchContents(type, keyword, page.getPageNumber()));
    }

    @GetMapping("/person")
    public ResponseEntity<Page<PersonDTO>> searchPersons(@RequestParam("keyword") String keyword, @PageableDefault(page = 1) Pageable page) {
        return ResponseEntity.ok(searchService.searchPersons(keyword, page.getPageNumber()));
    }

    @GetMapping("/collection")
    public ResponseEntity<Page<CollectionResponseDTO>> searchCollections(@RequestParam("keyword") String keyword, @PageableDefault(page = 1) Pageable page) {
        return ResponseEntity.ok(searchService.searchCollections(keyword, page.getPageNumber()));
    }

    @GetMapping("/user")
    public ResponseEntity<Page<UserResponseDTO>> searchUsers(@RequestParam("keyword") String keyword, @PageableDefault(page = 1) Pageable page) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Integer loginUserId = 0;
        if (auth.getPrincipal() != "anonymousUser") {
            loginUserId = ((CustomUserDetails) auth.getPrincipal()).getId();
        }

        return ResponseEntity.ok(searchService.searchUsers(keyword, loginUserId, page.getPageNumber()));
    }
}
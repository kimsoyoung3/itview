package com.example.itview_spring.Controller.Search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.itview_spring.DTO.CollectionResponseDTO;
import com.example.itview_spring.DTO.ContentDTO;
import com.example.itview_spring.DTO.ContentResponseDTO;
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

    @GetMapping("/content/movie")
    public ResponseEntity<Page<ContentDTO>> searchMovieContents(@RequestParam("keyword") String keyword, @PageableDefault(page = 1) Pageable page) {
        return ResponseEntity.ok(searchService.searchMovies(keyword, page.getPageNumber()));
    }

    @GetMapping("/content/series")
    public ResponseEntity<Page<ContentDTO>> searchSeriesContents(@RequestParam("keyword") String keyword, @PageableDefault(page = 1) Pageable page) {
        return ResponseEntity.ok(searchService.searchSeries(keyword, page.getPageNumber()));
    }

    @GetMapping("/content/webtoon")
    public ResponseEntity<Page<ContentDTO>> searchWebtoonContents(@RequestParam("keyword") String keyword, @PageableDefault(page = 1) Pageable page) {
        return ResponseEntity.ok(searchService.searchWebtoons(keyword, page.getPageNumber()));
    }

    @GetMapping("/content/book")
    public ResponseEntity<Page<ContentDTO>> searchBookContents(@RequestParam("keyword") String keyword, @PageableDefault(page = 1) Pageable page) {
        return ResponseEntity.ok(searchService.searchBooks(keyword, page.getPageNumber()));
    }

    @GetMapping("/content/record")
    public ResponseEntity<Page<ContentDTO>> searchRecordContents(@RequestParam("keyword") String keyword, @PageableDefault(page = 1) Pageable page) {
        return ResponseEntity.ok(searchService.searchRecords(keyword, page.getPageNumber()));
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
        return ResponseEntity.ok(searchService.searchUsers(keyword, page.getPageNumber()));
    }
}
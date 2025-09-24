package com.example.itview_spring.Controller.Home;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.itview_spring.Constant.Channel;
import com.example.itview_spring.Constant.ContentType;
import com.example.itview_spring.Constant.Genre;
import com.example.itview_spring.DTO.ContentDTO;
import com.example.itview_spring.Service.HomeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeRestController {

    private final HomeService homeService;

    @GetMapping
    public ResponseEntity<Page<ContentDTO>> getHomeContent(@PageableDefault(page = 1) Pageable pageable, @RequestParam("contentType") String contentType) {
        return ResponseEntity.ok(homeService.getContentsByContentType(ContentType.valueOf(contentType.toUpperCase()), pageable.getPageNumber()));
    }

    @GetMapping("/movie/genre")
    public ResponseEntity<List<Genre>> getMovies() {
        return ResponseEntity.ok(homeService.getMovieGenres());
    }

    @GetMapping("/series/genre")
    public ResponseEntity<List<Genre>> getSeriesGenres() {
        return ResponseEntity.ok(homeService.getSeriesGenres());
    }

    @GetMapping("/series/channel")
    public ResponseEntity<List<Channel>> getSeriesChannels() {
        return ResponseEntity.ok(homeService.getSeriesChannels());
    }

    @GetMapping("/book/genre")
    public ResponseEntity<List<Genre>> getBookGenres() {
        return ResponseEntity.ok(homeService.getBookGenres());
    }

    @GetMapping("/webtoon/genre")
    public ResponseEntity<List<Genre>> getWebtoonGenres() {
        return ResponseEntity.ok(homeService.getWebtoonGenres());
    }

    @GetMapping("/webtoon/channel")
    public ResponseEntity<List<Channel>> getWebtoonChannels() {
        return ResponseEntity.ok(homeService.getWebtoonChannels());
    }
}
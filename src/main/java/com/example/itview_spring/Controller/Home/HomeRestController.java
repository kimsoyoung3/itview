package com.example.itview_spring.Controller.Home;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{contentType}/genre")
    public ResponseEntity<List<Genre>> getGenres(@PathVariable String contentType) {
        return ResponseEntity.ok(homeService.getGenres(ContentType.valueOf(contentType.toUpperCase())));
    }

    @GetMapping("/{contentType}/genre/{genre}")
    public ResponseEntity<Page<ContentDTO>> getContentsByGenre(@PathVariable String contentType, @PathVariable String genre, @PageableDefault(page = 1) Pageable pageable) {
        return ResponseEntity.ok(homeService.getContentsByGenre(ContentType.valueOf(contentType.toUpperCase()), Genre.valueOf(genre.toUpperCase()), pageable.getPageNumber()));
    }

    @GetMapping("/{contentType}/channel")
    public ResponseEntity<List<Channel>> getChannels(@PathVariable String contentType) {
        return ResponseEntity.ok(homeService.getChannels(ContentType.valueOf(contentType.toUpperCase())));
    }

    @GetMapping("/{contentType}/channel/{channel}")
    public ResponseEntity<Page<ContentDTO>> getContentsByChannel(@PathVariable String contentType, @PathVariable String channel, @PageableDefault(page = 1) Pageable pageable) {
        return ResponseEntity.ok(homeService.getContentsByChannel(ContentType.valueOf(contentType.toUpperCase()), Channel.valueOf(channel.toUpperCase()), pageable.getPageNumber()));
    }
}
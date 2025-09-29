package com.example.itview_spring.Controller.Home;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.itview_spring.Constant.Channel;
import com.example.itview_spring.Constant.ContentType;
import com.example.itview_spring.Constant.Genre;
import com.example.itview_spring.DTO.ContentResponseDTO;
import com.example.itview_spring.DTO.HomeContentDTO;
import com.example.itview_spring.Service.HomeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeRestController {

    private final HomeService homeService;

    @GetMapping
    public ResponseEntity<HomeContentDTO> getHomeContent() {
        return ResponseEntity.ok(homeService.getHomeContent());
    }

    @GetMapping("/{contentType}/genre")
    public ResponseEntity<List<Pair<String, String>>> getGenres(@PathVariable String contentType) {
        List<Genre> genres = homeService.getGenres(ContentType.valueOf(contentType.toUpperCase()));
        List<Pair<String, String>> genrePairs = new ArrayList<>();
        for (Genre genre : genres) {
            genrePairs.add(Pair.of(genre.name(), genre.getGenreName()));
        }
        return ResponseEntity.ok(genrePairs);
    }

    @GetMapping("/{contentType}/genre/{genre}")
    public ResponseEntity<Page<ContentResponseDTO>> getContentsByGenre(@PathVariable String contentType, @PathVariable String genre, @PageableDefault(page = 1) Pageable pageable) {
        return ResponseEntity.ok(homeService.getContentsByGenre(ContentType.valueOf(contentType.toUpperCase()), Genre.valueOf(genre.toUpperCase()), pageable.getPageNumber()));
    }

    @GetMapping("/{contentType}/channel")
    public ResponseEntity<List<Pair<String, String>>> getChannels(@PathVariable String contentType) {
        List<Channel> channels = homeService.getChannels(ContentType.valueOf(contentType.toUpperCase()));
        List<Pair<String, String>> channelPairs = new ArrayList<>();
        for (Channel channel : channels) {
            channelPairs.add(Pair.of(channel.name(), channel.getDescription()));
        }
        return ResponseEntity.ok(channelPairs);
    }

    @GetMapping("/{contentType}/channel/{channel}")
    public ResponseEntity<Page<ContentResponseDTO>> getContentsByChannel(@PathVariable String contentType, @PathVariable String channel, @PageableDefault(page = 1) Pageable pageable) {
        return ResponseEntity.ok(homeService.getContentsByChannel(ContentType.valueOf(contentType.toUpperCase()), Channel.valueOf(channel.toUpperCase()), pageable.getPageNumber()));
    }
}
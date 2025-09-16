package com.example.itview_spring.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.itview_spring.Constant.ContentType;
import com.example.itview_spring.DTO.CollectionResponseDTO;
import com.example.itview_spring.DTO.ContentDTO;
import com.example.itview_spring.DTO.PersonDTO;
import com.example.itview_spring.DTO.SearchContentDTO;
import com.example.itview_spring.DTO.UserResponseDTO;
import com.example.itview_spring.Repository.CollectionRepository;
import com.example.itview_spring.Repository.ContentRepository;
import com.example.itview_spring.Repository.PersonRepository;
import com.example.itview_spring.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
    
    private final ContentRepository contentRepository;
    private final PersonRepository personRepository;
    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;

    public SearchContentDTO searchContents(String keyword, int page) {
        Pageable pageable = PageRequest.of(page - 1, 9);
        Page<ContentDTO> movie = contentRepository.searchContents(keyword, ContentType.MOVIE, pageable);
        Page<ContentDTO> series = contentRepository.searchContents(keyword, ContentType.SERIES, pageable);
        Page<ContentDTO> book = contentRepository.searchContents(keyword, ContentType.BOOK, pageable);
        Page<ContentDTO> webtoon = contentRepository.searchContents(keyword, ContentType.WEBTOON, pageable);
        Page<ContentDTO> record = contentRepository.searchContents(keyword, ContentType.RECORD, pageable);
        SearchContentDTO result = new SearchContentDTO();
        result.setMovie(movie.getContent());
        result.setSeries(series.getContent());
        result.setBook(book.getContent());
        result.setWebtoon(webtoon.getContent());
        result.setRecord(record.getContent());
        return result;
    }

    public Page<ContentDTO> searchMovies(String keyword, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<ContentDTO> movie = contentRepository.searchContents(keyword, ContentType.MOVIE, pageable);
        return movie;
    }

    public Page<ContentDTO> searchSeries(String keyword, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<ContentDTO> series = contentRepository.searchContents(keyword, ContentType.SERIES, pageable);
        return series;
    }

    public Page<ContentDTO> searchWebtoons(String keyword, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<ContentDTO> webtoon = contentRepository.searchContents(keyword, ContentType.WEBTOON, pageable);
        return webtoon;
    }

    public Page<ContentDTO> searchBooks(String keyword, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<ContentDTO> book = contentRepository.searchContents(keyword, ContentType.BOOK, pageable);
        return book;
    }

    public Page<ContentDTO> searchRecords(String keyword, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<ContentDTO> record = contentRepository.searchContents(keyword, ContentType.RECORD, pageable);
        return record;
    }
    
    public Page<PersonDTO> searchPersons(String keyword, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<PersonDTO> person = personRepository.searchPersons(keyword, pageable);
        return person;
    }

    public Page<CollectionResponseDTO> searchCollections(String keyword, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        return collectionRepository.searchCollections(keyword, pageable);
    }

    public Page<UserResponseDTO> searchUsers(String keyword, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        return userRepository.searchUsers(keyword, pageable);
    }
}

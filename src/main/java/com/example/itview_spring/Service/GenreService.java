package com.example.itview_spring.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.example.itview_spring.DTO.ContentCreateDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.itview_spring.Constant.Genre;
import com.example.itview_spring.Entity.ContentEntity;
import com.example.itview_spring.Entity.ContentGenreEntity;
import com.example.itview_spring.Repository.ContentGenreRepository;
import com.example.itview_spring.Repository.ContentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GenreService {

    private final ContentGenreRepository contentGenreRepository;
    private final ContentRepository contentRepository;
    private final ModelMapper modelMapper;

    //전체조회
    //목록(전체조회)
    //모두 읽어서 list<방금 작성한 DTO> 전달
    //public 전달할 모양 이름은 마음대로(받을것{
    //
    //     사용한 Repository.작업할 함수.var
    //     return 전달할 값;
    //}
    // public List<ProductDTO> 안알려줌() {
    // public List<ProductDTO> List() {      ex)

    //////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 콘텐츠에 장르 추가
     *///
    //장르조회
    @Transactional
    public List<Genre> getGenresByContentId(Integer contentId) {
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new NoSuchElementException("콘텐츠 ID가 유효하지 않습니다. id: " + contentId));

        List<ContentGenreEntity> genreEntities = contentGenreRepository.findByContent(content);

        return genreEntities.stream()
                .map(ContentGenreEntity::getGenre)
                .collect(Collectors.toList());
    }

    // -----------------------------
    // 콘텐츠 DTO 조회
    // -----------------------------
    public ContentCreateDTO read(Integer contentId) {
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID에 대한 콘텐츠를 찾을 수 없습니다: " + contentId));
        return modelMapper.map(content, ContentCreateDTO.class);
    }

    // -----------------------------
    // 헬퍼: 단일 장르 추가
    // -----------------------------
    public void addGenre(ContentEntity content, Genre genre) {
        ContentGenreEntity genreEntity = new ContentGenreEntity();
        genreEntity.setContent(content);
        genreEntity.setGenre(genre);
        content.getGenres().add(genreEntity);
    }
    // -----------------------------
    // 장르 추가 (기존 장르 유지)
    // -----------------------------
    public void addGenres(Integer contentId, List<Genre> genres) {
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("콘텐츠 ID가 유효하지 않습니다. id: " + contentId));


        List<Genre> existingGenres = new ArrayList<>();
        for (ContentGenreEntity e : content.getGenres()) {
            existingGenres.add(e.getGenre());
        }

        for (Genre genre : genres) {
            if (!existingGenres.contains(genre)) {
                ContentGenreEntity genreEntity = new ContentGenreEntity();
                genreEntity.setContent(content);
                genreEntity.setGenre(genre);
                content.getGenres().add(genreEntity);
            }
        }
        contentRepository.save(content);
    }
    /**
     * 콘텐츠 장르 수정 (기존 장르 모두 삭제 후, 새로 추가)
     */
    @Transactional
    public void updateGenres(Integer contentId, List<Genre> newGenres) {
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("콘텐츠 ID가 유효하지 않습니다. id: " + contentId));

        // 기존 장르 제거 (orphanRemoval 작동)
        content.getGenres().clear();

        for (Genre genre : newGenres) {
            ContentGenreEntity genreEntity = new ContentGenreEntity();
            genreEntity.setContent(content); // 양방향 연관관계 유지
            genreEntity.setGenre(genre);
            content.getGenres().add(genreEntity);
        }

        contentRepository.save(content); // 명시적 저장
    }

}

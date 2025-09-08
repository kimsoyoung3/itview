package com.example.itview_spring.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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

    // @Transactional
    public void addGenres(Integer contentId, List<Genre> genres) {
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("콘텐츠 ID가 유효하지 않습니다. id: " + contentId));
        List<Genre> existingGenres = getGenresByContentId(contentId);
// 0825 주석함
//        for (Genre genre : genres) {
//            if (existingGenres.contains(genre)) {
//                continue; // 이미 있는 장르는 건너뜀
//            }
//            System.out.println(" add contentId :"+contentId);
//            System.out.println(" add content :"+content);
//            System.out.println(" add genre :"+genre);
//            System.out.println(" -----------------");
//
//            ContentGenreEntity contentGenre = new ContentGenreEntity();
//            contentGenre.setContent(content);// ✅ null 아님
//            contentGenre.setGenre(genre);  // ✅ 여기서 content가 null이면 에러 발생
//
//            contentGenreRepository.save(contentGenre);
        //   Set<Genre> existingGenreSet = new HashSet<>(existingGenres);
        for (Genre genre : genres) {
            if (!existingGenres.contains(genre)) {  // Avoid adding existing genre
                ContentGenreEntity contentGenre = new ContentGenreEntity();
                contentGenre.setContent(content);
                contentGenre.setGenre(genre);
                contentGenreRepository.save(contentGenre);
            }
        }
    }

//    @Transactional
//    public void saveContentGenres(Integer contentId, List<String> genreNames) {
//        Content content = contentRepository.findById(contentId)
//                .orElseThrow(() -> new IllegalArgumentException("콘텐츠를 찾을 수 없습니다."));
//
//        // 기존 장르 제거 (전체 삭제 후 재등록 방식)
//        contentGenreRepository.deleteByContent(content);
//
//        // 새 장르 등록
//        if (genreNames != null) {
//            for (String genreName : genreNames) {
//                Genre genre = Genre.valueOf(genreName); // 🔥 여기에 잘못된 값 들어오면 예외 발생!
//                ContentGenreEntity genreEntity = new ContentGenreEntity();
//                genreEntity.setContent(content);
//                genreEntity.setGenre(genre);
//                contentGenreRepository.save(genreEntity);
//            }
//        }
//    }

    /**
     * 콘텐츠 장르 수정 (기존 장르 모두 삭제 후, 새로 추가)
     */
    @Transactional
    public void updateGenres(Integer contentId, List<Genre> newGenres) {
        ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("콘텐츠 ID가 유효하지 않습니다. id: " + contentId));

        // 기존 장르 삭제
        contentGenreRepository.deleteByContent(content);  // 기존 장르 삭제

        // 새로운 장르 추가
        for (Genre genre : newGenres) {
//            System.out.println(" update contentId :"+contentId);
//            System.out.println(" update content :"+content);
//            System.out.println(" update genre :"+genre);
//            System.out.println(" -----------------");

            ContentGenreEntity contentGenre = new ContentGenreEntity();
            contentGenre.setContent(content); // 반드시 content 세팅
            contentGenre.setGenre(genre);
            contentGenreRepository.save(contentGenre); // 새로운 장르 저장
            // 로그로 이전 장르와 수정된 장르 비교 (선택사항)
//            System.out.println("Old Genres: " + oldGenres);
//            System.out.println("New Genres: " + newGenres);

        }
    }
}

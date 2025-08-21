package com.example.itview_spring.Service;//package com.example.itview_spring.Service;
//
//import com.example.itview_spring.Constant.Genre;
//import com.example.itview_spring.DTO.ContentCreateDTO;
//import com.example.itview_spring.Entity.ContentEntity;
//import com.example.itview_spring.Entity.ContentGenreEntity;
//import com.example.itview_spring.Repository.ContentGenreRepository;
//import com.example.itview_spring.Repository.ContentRepository;
//import io.swagger.v3.oas.annotations.media.Content;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class GenreService {
//    //반드시 사용할 Repository (방금 작업한 파일)와 MOdelMapper추가
//    private final ContentRepository contentRepository;
//    private final ContentGenreRepository contentGenreRepository;
//    private final ModelMapper modelMapper;
//
//    /**
//     * 콘텐츠 ID로 장르 목록 조회
//     */
//    /**
//    public List<ContentGenreEntity> getGenresByContentId(Integer id) {
//        ContentEntity content = contentGenreRepository.findById(id).
//                orElseThrow(() -> new IllegalArgumentException("Invalid content ID: " +id));
//        return contentGenreRepository.findByContent(content);
//    }
//    */
//    /**
//     * 콘텐츠에 장르 추가
//     */
//
//
//    public void addGenres(Integer id, List<Genre> genres) {
//        // 콘텐츠 조회
//        Content content =contentGenreRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("콘텐츠 id가 유효하지 않습니다. id: " + id));
//
//        // 장르 추가
//        for (Genre genre : genres) {
//            ContentGenreEntity contentGenre = new ContentGenreEntity();
//            contentGenre.setContent(id);
//            contentGenre.setGenre(genre);
//            contentGenreRepository.save(contentGenre);
//        }
//    }
//
//    public void addGenres(Integer id, List<Genre> genres) {
//        ContentEntity content = contentGenreRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("콘텐츠 id 가 유효하지 않습니다 id : " + id));
//
//        for (Genre genre : genres) {
//            ContentGenreEntity contentGenre = new ContentGenreEntity();
//            contentGenre.setContent(content);
//            contentGenre.setGenre(genre);
//            contentGenreRepository.save(contentGenre);
//        }
//    }
//
//    /**
//     * 기존 장르 전체 삭제 후 새로 저장 (수정용)
//     */
//    @Transactional
//    public void updateGenres(Integer id, List<Genre> genres) {
//        ContentEntity content = contentGenreRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid content ID: " + id));
//
//        // 기존 삭제
//        contentGenreRepository.deleteByContent(content);
//
//        // 새로 저장
//        addGenres(id, genres);
//    }
//
//    /**
//     * 장르 단일 삭제
//     */
//    @Transactional
//    public void deleteGenre(Integer genreId) {
//        contentGenreRepository.deleteById(genreId);
//    }
//  //////////////////////////////////////////////////////////////////////////////////////
//    public Integer saveContentAndGenres(ContentGenreRepositoryDTO dto, List<Genre> genres) {
//        ContentEntity content = contentGenreRepository.save(dto.toEntity());
//
//        // 기존 장르 삭제 (수정 시)
//        contentGenreRepository.deleteByContent(content);
//
//        if (genres != null) {
//            for (Genre genre : genres) {
//                ContentGenreEntity cg = new ContentGenreEntity();
//                cg.setContent(content);
//                cg.setGenre(genre);
//                contentGenreRepository.save(cg);
//            }
//        }
//
//        return content.getId();
//    }
//    /// //////////////////////////////////////////////////////////////////////////////////////
//
//
//
//
//
//}

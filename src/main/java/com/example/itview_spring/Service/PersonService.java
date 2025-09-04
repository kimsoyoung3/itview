package com.example.itview_spring.Service;

import com.example.itview_spring.DTO.PersonResponseDTO;
import com.example.itview_spring.Entity.PersonEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonService {

    // 검색/리스트 조회
    Page<PersonEntity> list(Pageable pageable, String keyword);

    // 단건 조회 (Entity)
    PersonEntity get(Integer personId);

    // 단건 조회 (좋아요 여부 포함)
    PersonResponseDTO getPersonResponseDTO(Integer userId, Integer personId);

    // 등록
    PersonEntity create(PersonEntity req);

    // 수정
    PersonEntity update(Integer personId, PersonEntity req);

    // 삭제
    void delete(Integer personId);

    // 좋아요 등록
    void likePerson(Integer userId, Integer personId);

    // 좋아요 취소
    void unlikePerson(Integer userId, Integer personId);
}

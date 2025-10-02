package com.example.itview_spring.Service;

import com.example.itview_spring.Constant.Replyable;
import com.example.itview_spring.DTO.PersonDTO;
import com.example.itview_spring.DTO.PersonResponseDTO;
import com.example.itview_spring.Entity.PersonEntity;
import com.example.itview_spring.Repository.LikeRepository;
import com.example.itview_spring.Repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final LikeRepository likeRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true) // 읽기 전용으로 설정하는 것이 성능에 좋습니다.
    public Page<PersonDTO> list(String keyword, Pageable pageable) {

        // 검색어가 유효하면 (null이 아니고 비어있지 않으면)
        if (keyword != null && !keyword.trim().isEmpty()) {
            // 1. DTO를 반환하는 @Query 메서드 사용 (정렬 로직 포함)
            return personRepository.searchPersons(keyword, pageable);
        } else {
            // 2. 검색 조건이 없으면 findAll()을 호출하고 DTO로 변환

            // PersonEntity를 조회합니다.
            Page<PersonEntity> personEntities = personRepository.findAll(pageable);

            // 조회된 Entity를 DTO로 매핑합니다.
            return personEntities.map(entity -> modelMapper.map(entity, PersonDTO.class));
        }
    }

    public PersonEntity get(Integer personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person not found: " + personId));
    }

    public PersonResponseDTO getPersonResponseDTO(Integer userId, Integer personId) {

        PersonResponseDTO dto = personRepository.findPersonResponseDTO(userId, personId);
        if (dto == null) {
            throw new IllegalArgumentException("Person not found: " + personId);
        }
        return dto;
    }

    @Transactional
    public PersonEntity create(PersonEntity req) {

        PersonEntity entity = new PersonEntity();
        entity.setName(req.getName());
        entity.setProfile(req.getProfile());
        entity.setJob(req.getJob());

        return personRepository.save(entity);
    }

    @Transactional
    public PersonEntity update(Integer personId, PersonEntity req) {
        PersonEntity entity = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person not found: " + personId));
        entity.setName(req.getName());
        entity.setProfile(req.getProfile());
        entity.setJob(req.getJob());

        return personRepository.save(entity);
    }

    @Transactional
    public void delete(Integer personId) {
        if (!personRepository.existsById(personId)) {
            throw new IllegalArgumentException("Person not found: " + personId);
        }
        personRepository.deleteById(personId);
    }

    @Transactional
    public void likePerson(Integer userId, Integer personId) {

        likeRepository.likeTarget(userId, personId, Replyable.PERSON);
    }

    @Transactional
    public void unlikePerson(Integer userId, Integer personId) {
        likeRepository.unlikeTarget(userId, personId, Replyable.PERSON);
    }
}

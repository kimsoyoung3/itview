package com.example.itview_spring.Service;

import com.example.itview_spring.Constant.Replyable;
import com.example.itview_spring.DTO.PersonResponseDTO;
import com.example.itview_spring.Entity.PersonEntity;
import com.example.itview_spring.Repository.LikeRepository;
import com.example.itview_spring.Repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final LikeRepository likeRepository;

    @Override
    public Page<PersonEntity> list(Pageable pageable, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return personRepository.findAll(pageable);
        }
        // 리포지토리 추가 쿼리 없이 Query-by-Example로 간단 검색(name/profile/job 포함)
        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withMatcher("name", m -> m.contains())
                .withMatcher("profile", m -> m.contains())
                .withMatcher("job", m -> m.contains());
        PersonEntity probe = new PersonEntity();
        probe.setName(keyword);
        probe.setProfile(keyword);
        probe.setJob(keyword);
        return personRepository.findAll(Example.of(probe, matcher), pageable);
    }

    @Override
    public PersonEntity get(Integer personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person not found: " + personId));
    }

    @Override
    public PersonResponseDTO getPersonResponseDTO(Integer userId, Integer personId) {

        PersonResponseDTO dto = personRepository.findPersonResponseDTO(userId, personId);
        if (dto == null) {
            throw new IllegalArgumentException("Person not found: " + personId);
        }
        return dto;
    }

    @Override
    @Transactional
    public PersonEntity create(PersonEntity req) {

        PersonEntity entity = new PersonEntity();
        entity.setName(req.getName());
        entity.setProfile(req.getProfile());
        entity.setJob(req.getJob());

        return personRepository.save(entity);
    }

    @Override
    @Transactional
    public PersonEntity update(Integer personId, PersonEntity req) {
        PersonEntity entity = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person not found: " + personId));
        entity.setName(req.getName());
        entity.setProfile(req.getProfile());
        entity.setJob(req.getJob());

        return personRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(Integer personId) {
        if (!personRepository.existsById(personId)) {
            throw new IllegalArgumentException("Person not found: " + personId);
        }
        personRepository.deleteById(personId);
    }

    @Override
    @Transactional
    public void likePerson(Integer userId, Integer personId) {

        likeRepository.likeTarget(userId, personId, Replyable.PERSON);
    }

    @Override
    @Transactional
    public void unlikePerson(Integer userId, Integer personId) {
        likeRepository.unlikeTarget(userId, personId, Replyable.PERSON);
    }
}

package com.example.itview_spring.Service;

import com.example.itview_spring.Constant.Replyable;
import com.example.itview_spring.DTO.PersonDTO;
import com.example.itview_spring.DTO.PersonResponseDTO;
import com.example.itview_spring.Entity.PersonEntity;
import com.example.itview_spring.Repository.LikeRepository;
import com.example.itview_spring.Repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonService {
    private final PersonRepository personRepository;
    private final LikeRepository likeRepository;
    private final ModelMapper modelMapper ;
    // 전체조회
    public List<PersonDTO>listPersons() {
        List<PersonEntity> personEntities = personRepository.findAll();
        List<PersonDTO> personDTOS = Arrays.asList(modelMapper.map(personEntities, PersonDTO[].class));
        return personDTOS;
    }
    //상세보기
    public PersonDTO readPerson(Integer id){
        Optional<PersonEntity> personEntity = personRepository.findById(id);
        PersonDTO personDTO = modelMapper.map(personEntity, PersonDTO.class);
        return personDTO;
    }
    //삽입
     public void register(PersonDTO personDTO){
        PersonEntity personEntity = modelMapper.map(personDTO,PersonEntity.class);
        personRepository.save(personEntity);
     }
     //수정
     public void update(PersonDTO personDTO){
            PersonEntity save =personRepository.findById(personDTO.getId())
                    .orElse(null);
        if (save !=null){
            PersonEntity personEntity =modelMapper.map(personDTO,PersonEntity.class);
            personRepository.save(personEntity);
        }


         }
         //삭제
    public Void deletePerson(Integer id){
        personRepository.deleteById(id);

        return null;
    }

    // 인물 정보 + 좋아요 여부, 좋아요 수 조회
    public PersonResponseDTO getPersonResponseDTO(Integer userId, Integer personId) {
        if (!personRepository.existsById(personId)) {
            throw new NoSuchElementException("존재하지 않는 인물입니다");
        }
        return personRepository.findPersonResponseDTO(userId, personId);
    }

    // 인물에 좋아요 등록
    public void likePerson(Integer userId, Integer personId) {
        if (!personRepository.existsById(personId)) {
            throw new NoSuchElementException("존재하지 않는 인물입니다");
        }
        likeRepository.likeTarget(userId, personId, Replyable.PERSON);
    }

    // 인물에 좋아요 취소
    public void unlikePerson(Integer userId, Integer personId) {
        if (!personRepository.existsById(personId)) {
            throw new NoSuchElementException("존재하지 않는 인물입니다");
        }
        likeRepository.unlikeTarget(userId, personId, Replyable.PERSON);
    }
}

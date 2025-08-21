package com.example.itview_spring.Controller.Person;

import com.example.itview_spring.DTO.PersonDTO;
import com.example.itview_spring.Service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;
    //전체조회
    @GetMapping("/list")
    public String getList(Model model){
        List<PersonDTO>personDTOS = personService.listPersons();
        model .addAttribute("personDTOS", personDTOS);
        return "/list";
    }
    //상세조회
    @GetMapping("/read")
    public String readPerson(Integer id,Model model){
        PersonDTO personDTO = personService.readPerson(id);
        model.addAttribute("personDTO",personDTO);
        return "detail";
    }
    // 등록(페이지이동)
    @GetMapping("/register")
    public String register(){
        return "/register";
    }
    //등록(저장)
    @PostMapping("/register")
    public String registerPerson(PersonDTO personDTO){
        personService.register(personDTO );
        return "/redirect/list";
    }
    //수정
    @GetMapping("/update")
    public String getupdate(Integer id,Model model){
        PersonDTO personDTO = personService.readPerson(id);
        model.addAttribute("personDTO", personDTO);
        return "/update";
    }
    //수정저장 후 목록이동
    @PostMapping("/update")
    public String postupate(PersonDTO personDTO){
        personService.update(personDTO);
        return "redirect/list";
    }
    //삭제
    @GetMapping("/delete")
    public String deleteperson(Integer id){
        personService.deletePerson(id);
        return "redirect/list";
    }
}

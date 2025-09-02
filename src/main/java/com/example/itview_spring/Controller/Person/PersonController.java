package com.example.itview_spring.Controller.Person;

import com.example.itview_spring.DTO.PersonDTO;
import com.example.itview_spring.Service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor

public class PersonController {
    private final PersonService personService;


    //전체조회
    @GetMapping("/person/list")
    public String listPersons(Model model){
        List<PersonDTO>personList = personService.listPersons();
        model .addAttribute("personList", personList);
        return "Person/list";
    }
    //상세조회
    @GetMapping("/person/{id}")
    public String detail( @PathVariable Integer id, Model model){
        PersonDTO personDTO = personService.readPerson(id);
        model.addAttribute("personDTO",personDTO);
        return "Person/detail";
    }
    // 등록(페이지이동)
    @GetMapping("/person/register")
    public String registerForm(Model model){
        model.addAttribute("personDTO",new PersonDTO());
        return "Person/register";
    }
    //등록(저장)
    @PostMapping("/person/register")
    public String register( @ModelAttribute PersonDTO personDTO){
        personService.register(personDTO);
        return "redirect:/person/list";
    }
    //수정
    @GetMapping("/person/{id}/update")
    public String update(@PathVariable Integer id, Model model){
        PersonDTO personDTO = personService.readPerson(id);
        model.addAttribute("personDTO", personDTO);
        return "Person/update";
    }
    //수정저장 후 목록이동
    @PostMapping("/person/{id}update")
    public String personupdate(@ModelAttribute Integer id,PersonDTO personDTO){
        personDTO.setId(id);
        personService.update(personDTO);
        return "redirect:/person/list";
    }
    //삭제

    @GetMapping("/person/{id}delete")
    public String deleteperson(@PathVariable Integer id){
        personService.deletePerson(id);
        return "redirect:/person/list";
    }
}

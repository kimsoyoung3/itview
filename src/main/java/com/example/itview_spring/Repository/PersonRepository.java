package com.example.itview_spring.Repository;


import com.example.itview_spring.Entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository <PersonEntity, Integer> {


}

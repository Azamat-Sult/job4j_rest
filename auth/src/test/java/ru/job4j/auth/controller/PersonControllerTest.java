package ru.job4j.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.AuthApplication;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.PersonRepository;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = AuthApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PersonControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    public void findAllPersonsTest() throws Exception {

        Person person1 = new Person();
        person1.setLogin("admin");
        person1.setPassword("123456");
        repository.save(person1);

        Person person2 = new Person();
        person2.setLogin("user1");
        person2.setPassword("123456");
        repository.save(person2);

        Person person3 = new Person();
        person3.setLogin("user2");
        person3.setPassword("123456");
        repository.save(person3);

        mockMvc.perform(get("/person/"))
                .andExpect(status().isOk())
                .andExpect(
                        content().json(objectMapper.writeValueAsString(
                                Arrays.asList(person1, person2, person3)))
                );

    }

    @Test
    public void findByIdFoundTest() throws Exception {

        Person person1 = new Person();
        person1.setLogin("admin");
        person1.setPassword("123456");
        Person savedPerson = repository.save(person1);

        mockMvc.perform(get("/person/{id}", savedPerson.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedPerson.getId()))
                .andExpect(jsonPath("$.login").value(savedPerson.getLogin()))
                .andExpect(jsonPath("$.password").value(savedPerson.getPassword()));

    }

    @Test
    public void findByIdNotFoundTest() throws Exception {

        mockMvc.perform(get("/person/{id}", 1))
                .andExpect(status().isNotFound());

    }

    @Test
    public void createPersonTest() throws Exception {

        Person person1 = new Person();
        person1.setLogin("admin");
        person1.setPassword("123456");

        mockMvc.perform(post("/person/")
                                .content(objectMapper.writeValueAsString(person1))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.login").value("admin"))
                .andExpect(jsonPath("$.password").value("123456"));

    }

    @Test
    public void updatePersonTest() throws Exception {

        Person oldPerson = new Person();
        oldPerson.setLogin("admin");
        oldPerson.setPassword("123456");
        Person savedOldPerson = repository.save(oldPerson);

        Person newPerson = new Person();
        newPerson.setId(savedOldPerson.getId());
        newPerson.setLogin("user1");
        newPerson.setPassword("654321");

        mockMvc.perform(put("/person/")
                        .content(objectMapper.writeValueAsString(newPerson))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        Person updatedPerson = repository.findById(savedOldPerson.getId()).get();

        Assertions.assertEquals(updatedPerson.getId(), newPerson.getId());
        Assertions.assertEquals(updatedPerson.getLogin(), newPerson.getLogin());
        Assertions.assertEquals(updatedPerson.getPassword(), newPerson.getPassword());

    }

    @Test
    public void deletePersonTest() throws Exception {

        Person personToDelete = new Person();
        personToDelete.setLogin("user1");
        personToDelete.setPassword("123456");
        Person savedPersonToDelete = repository.save(personToDelete);

        mockMvc.perform(delete("/person/{id}", savedPersonToDelete.getId()))
                .andExpect(status().isOk());

        Assertions.assertTrue(repository.findById(savedPersonToDelete.getId()).isEmpty());

    }

}
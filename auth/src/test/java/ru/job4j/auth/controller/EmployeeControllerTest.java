package ru.job4j.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.AuthApplication;
import ru.job4j.auth.domain.Employee;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.EmployeeRepository;
import ru.job4j.auth.repository.PersonRepository;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(classes = AuthApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EmployeeControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    public void resetDb() {
        employeeRepository.deleteAll();
    }

    @Test
    public void findAllEmployeesTest() throws Exception {

        Person person1 = new Person();
        person1.setLogin("user1");
        person1.setPassword("123456");

        Person person2 = new Person();
        person2.setLogin("user2");
        person2.setPassword("654321");

        Employee employee1 = Employee.of("BIG", "BOSS", 123456789);
        employee1.addAccount(person1);
        employeeRepository.save(employee1);

        Employee employee2 = Employee.of("MEGA", "SPECIALIST", 987654321);
        employee2.addAccount(person2);
        employeeRepository.save(employee2);

        mockMvc.perform(get("/employee/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        content().json(objectMapper.writeValueAsString(
                                Arrays.asList(employee1, employee2)))
                );

    }

}
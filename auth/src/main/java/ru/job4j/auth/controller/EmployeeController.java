package ru.job4j.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.job4j.auth.domain.Employee;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private RestTemplate rest;

    private EmployeeRepository employeeRepository;

    private static final String API_PERSON = "http://localhost:8080/person/";
    private static final String API_PERSON_ID = "http://localhost:8080/person/{id}";

    public EmployeeController(RestTemplate rest, EmployeeRepository employeeRepository) {
        this.rest = rest;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/")
    public List<Employee> findAllEmployee() {
        return StreamSupport.stream(
                this.employeeRepository.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    @GetMapping("/person")
    public List<Person> findAllPerson() {
        return rest.exchange(
                API_PERSON,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Person>>() { }
        ).getBody();
    }

    @GetMapping("/person/{id}")
    public Person findPersonById(@PathVariable int id) {
        return rest.getForObject(API_PERSON_ID, Person.class, id);
    }

    @PostMapping("/person")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        Person rsl = rest.postForObject(API_PERSON, person, Person.class);
        return new ResponseEntity<>(
                rsl,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/person")
    public ResponseEntity<Void> updatePerson(@RequestBody Person person) {
        rest.put(API_PERSON, person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/person/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable int id) {
        rest.delete(API_PERSON_ID, id);
        return ResponseEntity.ok().build();
    }

}
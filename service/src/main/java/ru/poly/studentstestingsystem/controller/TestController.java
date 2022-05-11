package ru.poly.studentstestingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.poly.studentstestingsystem.dto.TestDto;
import ru.poly.studentstestingsystem.service.TestService;

@RestController
@RequestMapping(path = "api/test")
@PreAuthorize("hasRole('TEACHER')")
public class TestController {

    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping
    public TestDto importTest(@RequestParam MultipartFile file, @RequestParam String courseName) {
        return testService.importTest(file, courseName);
    }
}

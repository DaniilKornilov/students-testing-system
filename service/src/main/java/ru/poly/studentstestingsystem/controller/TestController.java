package ru.poly.studentstestingsystem.controller;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @PostMapping
    public TestDto importTest(@RequestParam MultipartFile file, @RequestParam String courseName) {
        return testService.importTest(file, courseName);
    }
}

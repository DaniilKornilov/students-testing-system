package ru.poly.studentstestingsystem.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.poly.studentstestingsystem.dto.CourseDto;
import ru.poly.studentstestingsystem.service.CourseService;

@RestController
@RequestMapping(path = "api/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public List<CourseDto> getCourses() {
        return courseService.getCourses();
    }
}

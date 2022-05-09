package ru.poly.studentstestingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.poly.studentstestingsystem.dto.StudentDto;
import ru.poly.studentstestingsystem.service.StudentService;

import java.util.List;

@RestController
@RequestMapping(path = "api/student")
@PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<StudentDto> getStudents() {
        return studentService.getStudents();
    }

    @GetMapping(path = "id/{id}")
    public StudentDto getStudentById(@PathVariable("id") long id) {
        return studentService.getStudentById(id);
    }

    @GetMapping(path = "group/")
    public List<StudentDto> getStudentByGroupName(@RequestParam String group) {
        return studentService.getStudentsByGroupName(group);
    }

    @PostMapping
    public List<StudentDto> importStudents(@RequestParam MultipartFile file, @RequestParam String teacherUsername) {
        return studentService.importStudents(file, teacherUsername);
    }
}

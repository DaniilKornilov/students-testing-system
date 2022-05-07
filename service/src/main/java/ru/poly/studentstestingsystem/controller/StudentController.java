package ru.poly.studentstestingsystem.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.poly.studentstestingsystem.dto.StudentDto;
import ru.poly.studentstestingsystem.service.StudentService;

@RestController
@RequestMapping(path = "api/student")
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
    public List<StudentDto> importStudents(@RequestParam MultipartFile file) {
        return studentService.importStudents(file);
    }
}

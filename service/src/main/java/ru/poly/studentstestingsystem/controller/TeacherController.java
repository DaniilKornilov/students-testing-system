package ru.poly.studentstestingsystem.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.poly.studentstestingsystem.dto.TeacherDto;
import ru.poly.studentstestingsystem.pojo.request.TeacherSignUpRequest;
import ru.poly.studentstestingsystem.service.TeacherService;

@RestController
@RequestMapping(path = "api/teacher")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@Valid @RequestBody TeacherSignUpRequest teacherSignUpRequest) {
        TeacherDto teacherDto = teacherService.registerTeacher(teacherSignUpRequest);
        return ResponseEntity.ok(teacherDto);
    }
}

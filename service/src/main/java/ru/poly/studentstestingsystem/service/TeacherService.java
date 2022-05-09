package ru.poly.studentstestingsystem.service;

import ru.poly.studentstestingsystem.dto.TeacherDto;
import ru.poly.studentstestingsystem.pojo.request.TeacherSignUpRequest;

public interface TeacherService {

    TeacherDto registerTeacher(TeacherSignUpRequest teacherSignUpRequest);
}

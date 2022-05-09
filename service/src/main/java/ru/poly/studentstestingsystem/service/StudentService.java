package ru.poly.studentstestingsystem.service;

import org.springframework.web.multipart.MultipartFile;
import ru.poly.studentstestingsystem.dto.StudentDto;

import java.util.List;

public interface StudentService {

    StudentDto getStudentById(long id);

    List<StudentDto> getStudents();

    List<StudentDto> importStudents(MultipartFile file, String teacherUsername);

    List<StudentDto> getStudentsByGroupName(String groupName);
}

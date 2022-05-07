package ru.poly.studentstestingsystem.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import ru.poly.studentstestingsystem.dto.StudentDto;

public interface StudentService {

    StudentDto getStudentById(long id);

    List<StudentDto> getStudents();

    List<StudentDto> importStudents(MultipartFile file);

    List<StudentDto> getStudentsByGroupName(String groupName);
}

package ru.poly.studentstestingsystem.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import ru.poly.studentstestingsystem.dto.TestDto;

public interface TestService {

    List<TestDto> getTests();

    List<TestDto> getTestsByCourseName(String courseName);

    TestDto importTest(MultipartFile multipartFile, String courseName);
}

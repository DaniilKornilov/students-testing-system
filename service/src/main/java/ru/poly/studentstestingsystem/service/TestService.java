package ru.poly.studentstestingsystem.service;

import org.springframework.web.multipart.MultipartFile;
import ru.poly.studentstestingsystem.dto.TestDto;

public interface TestService {

    TestDto importTest(MultipartFile multipartFile);
}

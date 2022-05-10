package ru.poly.studentstestingsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.poly.studentstestingsystem.dto.TestDto;
import ru.poly.studentstestingsystem.excelhandler.ExcelTestsReader;
import ru.poly.studentstestingsystem.repository.AnswerRepository;
import ru.poly.studentstestingsystem.repository.ImageRepository;
import ru.poly.studentstestingsystem.repository.TestRepository;
import ru.poly.studentstestingsystem.service.TestService;

@Service
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;

    private final AnswerRepository answerRepository;

    private final ImageRepository imageRepository;

    private final ExcelTestsReader excelTestsReader;

    @Autowired
    public TestServiceImpl(TestRepository testRepository, ExcelTestsReader excelTestsReader,
            AnswerRepository answerRepository, ImageRepository imageRepository) {
        this.testRepository = testRepository;
        this.excelTestsReader = excelTestsReader;
        this.answerRepository = answerRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public TestDto importTest(MultipartFile multipartFile) {
        TestDto testDto = excelTestsReader.readExcel(multipartFile);
        return null;
    }
}

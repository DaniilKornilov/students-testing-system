package ru.poly.studentstestingsystem.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.poly.studentstestingsystem.dto.AnswerDto;
import ru.poly.studentstestingsystem.dto.ImageDto;
import ru.poly.studentstestingsystem.dto.TaskDto;
import ru.poly.studentstestingsystem.dto.TestDto;
import ru.poly.studentstestingsystem.entity.Answer;
import ru.poly.studentstestingsystem.entity.Course;
import ru.poly.studentstestingsystem.entity.Image;
import ru.poly.studentstestingsystem.entity.Task;
import ru.poly.studentstestingsystem.entity.Test;
import ru.poly.studentstestingsystem.excelhandler.ExcelTestsReader;
import ru.poly.studentstestingsystem.exception.TestConstraintException;
import ru.poly.studentstestingsystem.mapper.AnswerMapper;
import ru.poly.studentstestingsystem.mapper.ImageMapper;
import ru.poly.studentstestingsystem.mapper.TaskMapper;
import ru.poly.studentstestingsystem.mapper.TestMapper;
import ru.poly.studentstestingsystem.repository.AnswerRepository;
import ru.poly.studentstestingsystem.repository.CourseRepository;
import ru.poly.studentstestingsystem.repository.ImageRepository;
import ru.poly.studentstestingsystem.repository.TaskRepository;
import ru.poly.studentstestingsystem.repository.TestRepository;
import ru.poly.studentstestingsystem.service.TestService;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private static final String NO_SUCH_COURSE_MESSAGE = "Курса с именем: %s не существует";

    private static final String NO_SUCH_TASK_MESSAGE = "Задания с именем: %s не существует";

    private final TestRepository testRepository;

    private final CourseRepository courseRepository;

    private final AnswerRepository answerRepository;

    private final ImageRepository imageRepository;

    private final TaskRepository taskRepository;
    private final ExcelTestsReader excelTestsReader;

    private final TestMapper testMapper;

    private final TaskMapper taskMapper;

    private final AnswerMapper answerMapper;

    private final ImageMapper imageMapper;

    @Override
    public List<TestDto> getTests() {
        return testRepository.findAll().stream()
                .map(testMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<TestDto> getTestsByCourseName(String courseName) {
        return testRepository.findTestsByCourse_Name(courseName).stream()
                .map(testMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TestDto importTest(MultipartFile multipartFile, String courseName) {
        TestDto testDto = excelTestsReader.readExcel(multipartFile);
        List<TaskDto> taskDtos = excelTestsReader.getTaskDtos();
        List<ImageDto> imageDtos = excelTestsReader.getExcelImages();
        List<AnswerDto> answerDtos = excelTestsReader.getAnswerDtos();

        Test test = getTestToSave(testDto, courseName);
        testRepository.saveAndFlush(test);

        List<Task> tasks = getTasksToSave(taskDtos, test);
        taskRepository.saveAllAndFlush(tasks);

        List<Image> images = getImagesToSave(imageDtos);
        imageRepository.saveAllAndFlush(images);

        List<Answer> answers = getAnswersToSave(answerDtos);
        answerRepository.saveAllAndFlush(answers);

        return testMapper.map(test);
    }

    private Test getTestToSave(TestDto testDto, String courseName) {
        Test test = testMapper.map(testDto);
        test.setCreatedTimestamp(LocalDateTime.now());
        test.setUpdatedTimestamp(LocalDateTime.now());
        Course course = courseRepository.findByName(courseName)
                .orElseThrow(() -> new TestConstraintException(String.format(NO_SUCH_COURSE_MESSAGE, courseName)));
        test.setCourse(course);
        return test;
    }

    private List<Task> getTasksToSave(List<TaskDto> taskDtos, Test test) {
        List<Task> tasks = taskDtos.stream()
                .map(taskMapper::map).toList();
        tasks.forEach((t) -> t.setTest(test));
        return tasks;
    }

    private List<Image> getImagesToSave(List<ImageDto> imageDtos) {
        List<Image> images = imageDtos.stream()
                .filter((i) -> i.getImageBytes() != null)
                .map(imageMapper::map).toList();
        images.forEach((i) -> {
            String taskName = i.getTask().getName();
            i.setTask(taskRepository.findTaskByName(taskName)
                    .orElseThrow(() -> new TestConstraintException(
                            String.format(NO_SUCH_TASK_MESSAGE, taskName))));
        });
        return images;
    }

    private List<Answer> getAnswersToSave(List<AnswerDto> answerDtos) {
        List<Answer> answers = answerDtos.stream()
                .map(answerMapper::map).toList();
        answers.forEach((a) -> {
            String taskName = a.getTask().getName();
            a.setTask(taskRepository.findTaskByName(taskName)
                    .orElseThrow(() -> new TestConstraintException(
                            String.format(NO_SUCH_TASK_MESSAGE, taskName))));
        });
        return answers;
    }
}

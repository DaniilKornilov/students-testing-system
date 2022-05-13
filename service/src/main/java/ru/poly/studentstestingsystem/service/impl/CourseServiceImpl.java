package ru.poly.studentstestingsystem.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.poly.studentstestingsystem.dto.CourseDto;
import ru.poly.studentstestingsystem.mapper.CourseMapper;
import ru.poly.studentstestingsystem.repository.CourseRepository;
import ru.poly.studentstestingsystem.service.CourseService;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    @Override
    public List<CourseDto> getCourses() {
        return courseRepository.findAll().stream()
                .map(courseMapper::map)
                .collect(Collectors.toList());
    }
}

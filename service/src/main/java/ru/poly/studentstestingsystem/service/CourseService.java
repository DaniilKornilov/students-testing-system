package ru.poly.studentstestingsystem.service;

import java.util.List;
import ru.poly.studentstestingsystem.dto.CourseDto;

public interface CourseService {

    List<CourseDto> getCourses();
}

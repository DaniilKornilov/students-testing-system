package ru.poly.studentstestingsystem.dto;

import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CourseDto {

    private long id;

    private String name;

    private TeacherDto teacherDto;

    private Set<GroupDto> groupDtos;
}

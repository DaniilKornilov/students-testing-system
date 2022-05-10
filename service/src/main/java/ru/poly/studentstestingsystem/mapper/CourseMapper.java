package ru.poly.studentstestingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.poly.studentstestingsystem.dto.CourseDto;
import ru.poly.studentstestingsystem.entity.Course;

@Mapper(uses = {TeacherMapper.class, GroupMapper.class},
        componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface CourseMapper {

    @Mapping(source = "teacherDto", target = "teacher")
    @Mapping(source = "groupDtos", target = "groups")
    Course map(CourseDto courseDto);

    @Mapping(source = "teacher", target = "teacherDto")
    @Mapping(source = "groups", target = "groupDtos")
    CourseDto map(Course course);
}

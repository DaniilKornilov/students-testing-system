package ru.poly.studentstestingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.poly.studentstestingsystem.dto.TeacherDto;
import ru.poly.studentstestingsystem.entity.Teacher;

@Mapper(uses = UserMapper.class,
        componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface TeacherMapper {

    @Mapping(source = "userDto", target = "user")
    Teacher map(TeacherDto teacherDto);

    @Mapping(source = "user", target = "userDto")
    TeacherDto map(Teacher teacher);
}

package ru.poly.studentstestingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.poly.studentstestingsystem.dto.StudentDto;
import ru.poly.studentstestingsystem.entity.Student;

@Mapper(uses = {GroupMapper.class, UserMapper.class},
        componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface StudentMapper {

    @Mapping(source = "groupDto", target = "group")
    @Mapping(source = "userDto", target = "user")
    Student map(StudentDto studentDto);

    @Mapping(source = "group", target = "groupDto")
    @Mapping(source = "user", target = "userDto")
    StudentDto map(Student student);
}

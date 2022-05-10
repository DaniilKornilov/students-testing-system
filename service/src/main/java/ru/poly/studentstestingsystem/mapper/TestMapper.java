package ru.poly.studentstestingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.poly.studentstestingsystem.dto.TestDto;
import ru.poly.studentstestingsystem.entity.Test;

@Mapper(uses = CourseMapper.class,
        componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface TestMapper {

    @Mapping(source = "courseDto", target = "course")
    Test map(TestDto testDto);

    @Mapping(source = "course", target = "courseDto")
    TestDto map(Test test);
}

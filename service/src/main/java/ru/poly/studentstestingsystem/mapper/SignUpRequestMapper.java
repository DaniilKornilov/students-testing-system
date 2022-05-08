package ru.poly.studentstestingsystem.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.poly.studentstestingsystem.pojo.SignUpRequest;
import ru.poly.studentstestingsystem.pojo.TeacherSignUpRequest;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface SignUpRequestMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"firstName", "lastName"})
    SignUpRequest map(TeacherSignUpRequest teacherSignUpRequest);

    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    TeacherSignUpRequest map(SignUpRequest signUpRequest);
}

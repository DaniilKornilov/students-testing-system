package ru.poly.studentstestingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.poly.studentstestingsystem.dto.AnswerDto;
import ru.poly.studentstestingsystem.entity.Answer;

@Mapper(uses = TaskMapper.class,
        componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface AnswerMapper {

    @Mapping(source = "taskDto", target = "task")
    Answer map(AnswerDto answerDto);

    @Mapping(source = "task", target = "taskDto")
    AnswerDto map(Answer answer);
}

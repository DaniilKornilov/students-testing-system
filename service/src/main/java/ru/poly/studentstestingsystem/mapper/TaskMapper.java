package ru.poly.studentstestingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.poly.studentstestingsystem.dto.TaskDto;
import ru.poly.studentstestingsystem.entity.Task;

@Mapper(uses = TestMapper.class,
        componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface TaskMapper {

    @Mapping(source = "testDto", target = "test")
    Task map(TaskDto taskDto);

    @Mapping(source = "test", target = "testDto")
    TaskDto map(Task task);
}

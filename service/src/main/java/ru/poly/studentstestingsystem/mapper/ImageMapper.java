package ru.poly.studentstestingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.poly.studentstestingsystem.dto.ImageDto;
import ru.poly.studentstestingsystem.entity.Image;

@Mapper(uses = TaskMapper.class,
        componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface ImageMapper {

    @Mapping(source = "taskDto", target = "task")
    Image map(ImageDto imageDto);

    @Mapping(source = "task", target = "taskDto")
    ImageDto map(Image image);
}

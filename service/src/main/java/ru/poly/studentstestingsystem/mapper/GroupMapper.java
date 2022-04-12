package ru.poly.studentstestingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.poly.studentstestingsystem.dto.GroupDto;
import ru.poly.studentstestingsystem.entity.Group;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface GroupMapper {
    Group map(GroupDto groupDto);

    GroupDto map(Group group);
}

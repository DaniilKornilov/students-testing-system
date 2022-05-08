package ru.poly.studentstestingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.poly.studentstestingsystem.dto.UserDto;
import ru.poly.studentstestingsystem.entity.User;

@Mapper(uses = RoleMapper.class, componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    User map(UserDto userDto);

    UserDto map(User user);
}

package ru.poly.studentstestingsystem.mapper;

import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.poly.studentstestingsystem.dto.RoleDto;
import ru.poly.studentstestingsystem.entity.Role;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface RoleMapper {

    @Mapping(source = "roleName", target = "roleEnum")
    Role map(RoleDto roleDto);

    @Mapping(source = "roleEnum", target = "roleName")
    RoleDto map(Role role);

    Set<Role> mapToDto(Set<RoleDto> roleDtos);

    Set<RoleDto> mapToEntity(Set<Role> roles);
}

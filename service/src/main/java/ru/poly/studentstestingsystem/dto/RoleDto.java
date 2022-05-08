package ru.poly.studentstestingsystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.poly.studentstestingsystem.entity.enumeration.RoleEnum;

@Getter
@Setter
@NoArgsConstructor
public class RoleDto {

    private long id;

    private RoleEnum roleName;
}

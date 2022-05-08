package ru.poly.studentstestingsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private long id;

    private String email;

    private String username;

    @JsonIgnore
    private String password;

    private Set<RoleDto> roles;
}

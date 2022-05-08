package ru.poly.studentstestingsystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeacherDto {

    private long id;

    private String firstName;

    private String lastName;

    private UserDto userDto;
}

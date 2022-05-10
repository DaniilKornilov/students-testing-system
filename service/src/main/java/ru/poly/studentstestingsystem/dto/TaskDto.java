package ru.poly.studentstestingsystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskDto {

    private long id;

    private String name;

    private String description;

    private TestDto testDto;
}

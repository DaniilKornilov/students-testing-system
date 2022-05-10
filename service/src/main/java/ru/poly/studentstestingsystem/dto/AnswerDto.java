package ru.poly.studentstestingsystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerDto {

    private long id;

    private TaskDto taskDto;

    private double value;

    private String name;
}

package ru.poly.studentstestingsystem.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TestDto {

    private long id;

    private String name;

    private String description;

    private LocalDateTime createdTimestamp;

    private LocalDateTime updatedTimestamp;

    private LocalTime timeLimit;

    private LocalDateTime availableFrom;

    private LocalDateTime availableTo;

    private CourseDto courseDto;
}

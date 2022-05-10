package ru.poly.studentstestingsystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageDto {

    private long id;

    private TaskDto taskDto;

    private String path;
}

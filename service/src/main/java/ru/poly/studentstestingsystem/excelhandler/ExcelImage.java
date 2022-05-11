package ru.poly.studentstestingsystem.excelhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.poly.studentstestingsystem.dto.TaskDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExcelImage {

    private String fileName;

    private byte[] bytes;

    private TaskDto taskDto;
}

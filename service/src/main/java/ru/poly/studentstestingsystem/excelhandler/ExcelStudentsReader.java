package ru.poly.studentstestingsystem.excelhandler;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.poly.studentstestingsystem.dto.GroupDto;
import ru.poly.studentstestingsystem.dto.StudentDto;
import ru.poly.studentstestingsystem.excelhandler.constants.ExcelStudentsConstants;
import ru.poly.studentstestingsystem.exception.ExcelReadingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ExcelStudentsReader {
    private static final String EXCEL_READING_ERROR = "Excel file is invalid!";

    private static final String STUDENTS_HEADERS_ERROR = "Student excel file headers are invalid!";

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public List<StudentDto> readExcel(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            throw new ExcelReadingException(EXCEL_READING_ERROR);
        }
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(ExcelStudentsConstants.SHEET_INDEX);
            Row row = sheet.getRow(ExcelStudentsConstants.HEADERS_ROW_INDEX);
            validateHeaders(row);
            return readStudentsFromSheet(sheet);
        } catch (IOException exception) {
            throw new ExcelReadingException(EXCEL_READING_ERROR);
        }
    }

    private void validateHeaders(Row row) {
        List<String> headers = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();

        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null) {
                headers.add(dataFormatter.formatCellValue(cell));
            }
        }

        if (headers.size() != ExcelStudentsConstants.STUDENTS_HEADERS.size()) {
            throw new ExcelReadingException(STUDENTS_HEADERS_ERROR);
        }

        for (int i = 0; i < headers.size(); i++) {
            if (!Objects.equals(headers.get(i), ExcelStudentsConstants.STUDENTS_HEADERS.get(i))) {
                throw new ExcelReadingException(STUDENTS_HEADERS_ERROR);
            }
        }
    }

    private List<StudentDto> readStudentsFromSheet(Sheet sheet) {
        List<StudentDto> studentDtos = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            StudentDto studentDto = new StudentDto();
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                if (cell != null) {
                    String cellValue = dataFormatter.formatCellValue(cell);
                    setStudentDtoValues(studentDto, j, cellValue);
                }
            }
            if (studentDto.getEmail() != null && studentDto.getName() != null) {
                studentDtos.add(studentDto);
            }
        }

        return studentDtos;
    }

    private void setStudentDtoValues(StudentDto studentDto, int index, String value) {
        switch (index) {
            case ExcelStudentsConstants.NAME_COLUMN_INDEX -> studentDto.setName(value);
            case ExcelStudentsConstants.EMAIL_COLUMN_INDEX -> studentDto.setEmail(value);
            case ExcelStudentsConstants.GROUP_COLUMN_INDEX -> {
                GroupDto groupDto = new GroupDto();
                groupDto.setName(value);
                studentDto.setGroupDto(groupDto);
            }
        }
    }
}

package ru.poly.studentstestingsystem.excelhandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.poly.studentstestingsystem.dto.StudentDto;
import ru.poly.studentstestingsystem.dto.UserDto;
import ru.poly.studentstestingsystem.excelhandler.constants.ExcelConstants;
import ru.poly.studentstestingsystem.excelhandler.constants.ExcelStudentsConstants;
import ru.poly.studentstestingsystem.exception.ExcelReadingException;

@Component
public class ExcelStudentsReader {

    private static final String EXCEL_READING_ERROR_MESSAGE = "Excel файл не валиден!";

    private static final String STUDENTS_HEADERS_ERROR_MESSAGE =
            "Excel файл имеет неверные колонки! Номер колонки: %d" +
                    "Получено: %s" + "Ожидалось: %s";

    private static final String STUDENTS_SHEET_ERROR_MESSAGE = "В Excel файле нет листов!";

    private static final String INVALID_VALUE_MESSAGE = "Неверное значение %s в Excel файле! Строка: %d";

    private final DataFormatter dataFormatter = new DataFormatter();

    public List<StudentDto> readExcel(MultipartFile file) {
        if (!ExcelConstants.TYPE.equals(file.getContentType())) {
            throw new ExcelReadingException(EXCEL_READING_ERROR_MESSAGE);
        }
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = getSheet(workbook);
            return readStudentsFromSheet(sheet);
        } catch (IOException exception) {
            throw new ExcelReadingException(EXCEL_READING_ERROR_MESSAGE);
        }
    }

    private Sheet getSheet(Workbook workbook) {
        try {
            Sheet studentsSheet = workbook.getSheetAt(ExcelStudentsConstants.SHEET_INDEX);
            validateHeaders(studentsSheet.getRow(ExcelStudentsConstants.HEADERS_ROW_INDEX));
            return studentsSheet;
        } catch (IllegalArgumentException exception) {
            throw new ExcelReadingException(STUDENTS_SHEET_ERROR_MESSAGE);
        }
    }

    private void validateHeaders(Row row) {
        List<String> validationHeaders = ExcelStudentsConstants.STUDENTS_HEADERS;
        for (int i = 0; i < validationHeaders.size(); i++) {
            Cell cell = row.getCell(i);
            String cellValue = dataFormatter.formatCellValue(cell);
            if (!cellValue.equals(validationHeaders.get(i))) {
                throw new ExcelReadingException(
                        String.format(STUDENTS_HEADERS_ERROR_MESSAGE, i, cellValue, validationHeaders.get(i)));
            }
        }
    }

    private List<StudentDto> readStudentsFromSheet(Sheet sheet) {
        List<StudentDto> studentDtos = new ArrayList<>();

        for (int i = ExcelStudentsConstants.HEADERS_ROW_INDEX + 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (isNotEmptyRow(row)) {
                StudentDto studentDto = new StudentDto();
                UserDto userDto = new UserDto();
                studentDto.setUserDto(userDto);

                setUsername(row, studentDto);
                setFirstName(row, studentDto);
                setLastName(row, studentDto);
                setEmail(row, studentDto);
                setPassword(row, studentDto);

                studentDtos.add(studentDto);
            }
        }

        return studentDtos;
    }

    private void setUsername(Row row, StudentDto studentDto) {
        Cell cell = row.getCell(ExcelStudentsConstants.USERNAME_COLUMN_INDEX);
        String cellValue = dataFormatter.formatCellValue(cell);
        if (cellValue.isEmpty()) {
            throw new ExcelReadingException(String.format(INVALID_VALUE_MESSAGE,
                    ExcelStudentsConstants.STUDENTS_HEADERS.get(ExcelStudentsConstants.USERNAME_COLUMN_INDEX),
                    row.getRowNum()));
        }
        studentDto.getUserDto().setUsername(cellValue);
    }

    private void setFirstName(Row row, StudentDto studentDto) {
        Cell cell = row.getCell(ExcelStudentsConstants.FIRST_NAME_COLUMN_INDEX);
        String cellValue = dataFormatter.formatCellValue(cell);
        if (cellValue.isEmpty()) {
            throw new ExcelReadingException(String.format(INVALID_VALUE_MESSAGE,
                    ExcelStudentsConstants.STUDENTS_HEADERS.get(ExcelStudentsConstants.FIRST_NAME_COLUMN_INDEX),
                    row.getRowNum()));
        }
        studentDto.setFirstName(cellValue);
    }

    private void setLastName(Row row, StudentDto studentDto) {
        Cell cell = row.getCell(ExcelStudentsConstants.LAST_NAME_COLUMN_INDEX);
        String cellValue = dataFormatter.formatCellValue(cell);
        if (cellValue.isEmpty()) {
            throw new ExcelReadingException(String.format(INVALID_VALUE_MESSAGE,
                    ExcelStudentsConstants.STUDENTS_HEADERS.get(ExcelStudentsConstants.LAST_NAME_COLUMN_INDEX),
                    row.getRowNum()));
        }
        studentDto.setLastName(cellValue);
    }

    private void setEmail(Row row, StudentDto studentDto) {
        Cell cell = row.getCell(ExcelStudentsConstants.EMAIL_COLUMN_INDEX);
        String cellValue = dataFormatter.formatCellValue(cell);
        if (cellValue.isEmpty()) {
            throw new ExcelReadingException(String.format(INVALID_VALUE_MESSAGE,
                    ExcelStudentsConstants.STUDENTS_HEADERS.get(ExcelStudentsConstants.EMAIL_COLUMN_INDEX),
                    row.getRowNum()));
        }
        studentDto.getUserDto().setEmail(cellValue);
    }

    private void setPassword(Row row, StudentDto studentDto) {
        Cell cell = row.getCell(ExcelStudentsConstants.PASSWORD_COLUMN_INDEX);
        String cellValue = dataFormatter.formatCellValue(cell);
        if (cellValue.isEmpty()) {
            throw new ExcelReadingException(String.format(INVALID_VALUE_MESSAGE,
                    ExcelStudentsConstants.STUDENTS_HEADERS.get(ExcelStudentsConstants.PASSWORD_COLUMN_INDEX),
                    row.getRowNum()));
        }
        studentDto.getUserDto().setPassword(cellValue);
    }

    private boolean isNotEmptyRow(Row row) {
        if (row == null) {
            return false;
        }
        if (row.getLastCellNum() <= 0) {
            return false;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i, MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null) {
                return true;
            }
        }
        return false;
    }
}

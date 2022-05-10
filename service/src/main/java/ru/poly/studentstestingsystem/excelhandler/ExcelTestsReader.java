package ru.poly.studentstestingsystem.excelhandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.poly.studentstestingsystem.dto.AnswerDto;
import ru.poly.studentstestingsystem.dto.ImageDto;
import ru.poly.studentstestingsystem.dto.TaskDto;
import ru.poly.studentstestingsystem.dto.TestDto;
import ru.poly.studentstestingsystem.excelhandler.constants.ExcelConstants;
import ru.poly.studentstestingsystem.excelhandler.constants.ExcelTestsConstants;
import ru.poly.studentstestingsystem.exception.ExcelReadingException;

@Component
public class ExcelTestsReader {

    private static final String TEST_ROWS_ERROR_MESSAGE =
            "Excel файл имеет неверные строки! Номер строки: %d" +
                    "Получено: %s" + "Ожидалось: %s";

    private static final String CELL_VALUE_ERROR_MESSAGE = "Введите непустое значение в Excel файле! Номер строки: %d" +
            "Номер столбца: %d";

    private final DataFormatter dataFormatter = new DataFormatter();

    private TestDto testDto;

    private List<TaskDto> taskDtos;

    private List<AnswerDto> answerDtos;

    private List<ImageDto> imageDtos;

    private int currentRowIndex;

    public TestDto readExcel(MultipartFile file) {
        clearPreviousData();
        if (!ExcelConstants.TYPE.equals(file.getContentType())) {
            throw new ExcelReadingException(ExcelConstants.EXCEL_FORMAT_ERROR_MESSAGE);
        }
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(ExcelTestsConstants.SHEET_INDEX);
            return readDataFromSheet(sheet);
        } catch (IOException exception) {
            throw new ExcelReadingException(ExcelConstants.EXCEL_READING_ERROR_MESSAGE);
        }
    }

    private void clearPreviousData() {
        testDto = new TestDto();
        taskDtos = new ArrayList<>();
        answerDtos = new ArrayList<>();
        imageDtos = new ArrayList<>();
        currentRowIndex = 0;
    }

    private TestDto readDataFromSheet(Sheet sheet) {
        testDto = readTestDataFromSheet(sheet);
        taskDtos = readTasksDataFromSheet(sheet);
        return testDto;
    }

    private TestDto readTestDataFromSheet(Sheet sheet) {
        for (int i = 0; i < ExcelTestsConstants.TEST_ROWS.size(); i++) {
            Row row = sheet.getRow(i);
            validateTestDataKey(row, i);
            setTestValues(row, i);
        }

        return testDto;
    }

    private void validateTestDataKey(Row row, int index) {
        String validationValue = ExcelTestsConstants.TEST_ROWS.get(index);
        Cell cell = row.getCell(ExcelTestsConstants.VALIDATION_COLUMN_INDEX);
        String cellValue = dataFormatter.formatCellValue(cell);
        if (cellValue.isEmpty() || !cellValue.equals(validationValue)) {
            throw new ExcelReadingException(String.format(TEST_ROWS_ERROR_MESSAGE, index + 1, cellValue,
                    validationValue));
        }
    }

    private String validateAndGetTestDataValue(Row row, int index) {
        int colNum = ExcelTestsConstants.VALIDATION_COLUMN_INDEX + 1;
        Cell cell = row.getCell(colNum);
        String cellValue = dataFormatter.formatCellValue(cell);
        if (cellValue.isEmpty()) {
            throw new ExcelReadingException(String.format(CELL_VALUE_ERROR_MESSAGE, index + 1, colNum + 1));
        }

        return cellValue;
    }

    private void setTestValues(Row row, int index) {
        String cellValue = validateAndGetTestDataValue(row, index);
        switch (index) {
            case ExcelTestsConstants.TEST_NAME_INDEX -> testDto.setName(cellValue);
            case ExcelTestsConstants.TEST_DESCRIPTION_INDEX -> testDto.setDescription(cellValue);
            case ExcelTestsConstants.TEST_TIME_LIMIT_INDEX -> {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                testDto.setTimeLimit(LocalTime.parse(cellValue));
            }
            case ExcelTestsConstants.TEST_AVAILABLE_FROM_INDEX ->
                    testDto.setAvailableFrom(LocalDateTime.parse(cellValue));
            case ExcelTestsConstants.TEST_AVAILABLE_TO_INDEX -> testDto.setAvailableTo(LocalDateTime.parse(cellValue));
        }
    }

    private List<TaskDto> readTasksDataFromSheet(Sheet sheet) {
        return null;
    }

    public TestDto getTestDto() {
        return testDto;
    }

    public List<TaskDto> getTaskDtos() {
        return taskDtos;
    }

    public List<AnswerDto> getAnswerDtos() {
        return answerDtos;
    }

    public List<ImageDto> getImageDtos() {
        return imageDtos;
    }
}

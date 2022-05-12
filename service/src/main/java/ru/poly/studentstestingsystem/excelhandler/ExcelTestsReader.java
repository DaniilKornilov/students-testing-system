package ru.poly.studentstestingsystem.excelhandler;

import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.poly.studentstestingsystem.dto.AnswerDto;
import ru.poly.studentstestingsystem.dto.ImageDto;
import ru.poly.studentstestingsystem.dto.TaskDto;
import ru.poly.studentstestingsystem.dto.TestDto;
import ru.poly.studentstestingsystem.excelhandler.constants.ExcelConstants;
import ru.poly.studentstestingsystem.excelhandler.constants.ExcelTestsConstants;
import ru.poly.studentstestingsystem.excelhandler.excelutils.ExcelUtils;
import ru.poly.studentstestingsystem.exception.ExcelReadingException;
import ru.poly.studentstestingsystem.parser.TestAnswerParser;
import ru.poly.studentstestingsystem.vo.TestAnswerValues;

@Component
public class ExcelTestsReader {

    private static final String TEST_ROWS_ERROR_MESSAGE =
            "Excel файл имеет неверные строки! Номер строки: %d Получено: %s Ожидалось: %s";

    private static final String CELL_VALUE_ERROR_MESSAGE =
            "Введите непустое значение в Excel файле! Номер строки: %d Номер столбца: 2";

    private static final String TIME_LIMIT_ERROR_MESSAGE =
            "Введите время в формате Ч:мм! Номер строки: %d Номер столбца: 2";

    private static final String DATE_TIME_ERROR_MESSAGE =
            "Введите дату и время в формате дд.ММ.гггг ЧЧ:мм! Номер строки: %d Номер столбца: 2";

    private static final String DATE_TIME_SEQUENCE_ERROR_MESSAGE =
            "Время закрытия теста не должно быть раньше времени начала теста! Номер строки: %d Номер столбца: 2";

    private final DataFormatter dataFormatter = new DataFormatter();

    private TestAnswerParser testAnswerParser;

    private Sheet sheet;
    private List<XSSFShape> shapes;

    private TestDto testDto;

    private List<TaskDto> taskDtos;

    private List<AnswerDto> answerDtos;

    private List<ImageDto> excelImages;

    private int currentTaskRowIndex;

    private int currentFileRowIndex;

    private TaskDto currentTaskDto;

    public TestDto readExcel(MultipartFile file) {
        clearPreviousData();
        if (!ExcelConstants.TYPE.equals(file.getContentType())) {
            throw new ExcelReadingException(ExcelConstants.EXCEL_FORMAT_ERROR_MESSAGE);
        }
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            sheet = workbook.getSheetAt(ExcelTestsConstants.SHEET_INDEX);
            XSSFDrawing patriarch = (XSSFDrawing) sheet.createDrawingPatriarch();
            shapes = patriarch.getShapes();
            readDataFromSheet();
            return testDto;
        } catch (IOException exception) {
            throw new ExcelReadingException(ExcelConstants.EXCEL_READING_ERROR_MESSAGE);
        }
    }

    private void clearPreviousData() {
        testDto = new TestDto();
        taskDtos = new ArrayList<>();
        answerDtos = new ArrayList<>();
        excelImages = new ArrayList<>();
        currentTaskRowIndex = 0;
        currentFileRowIndex = 0;
        currentTaskDto = new TaskDto();
    }

    private void readDataFromSheet() {
        readTestDataFromSheet();
        readTasksDataFromSheet();
    }

    private void readTestDataFromSheet() {
        for (currentFileRowIndex = 0; currentFileRowIndex < ExcelTestsConstants.TEST_ROWS.size();
                currentFileRowIndex++) {
            Row row = sheet.getRow(currentFileRowIndex);
            validateRowKey(row, ExcelTestsConstants.TEST_ROWS, currentFileRowIndex);
            setTestValues(row);
        }
    }

    private void validateRowKey(Row row, List<String> keyValues, int index) {
        String validationValue = keyValues.get(index);
        Cell cell = row.getCell(ExcelTestsConstants.VALIDATION_COLUMN_INDEX);
        String cellValue = dataFormatter.formatCellValue(cell);
        if (cellValue.isEmpty() || !cellValue.equals(validationValue)) {
            throw new ExcelReadingException(String.format(TEST_ROWS_ERROR_MESSAGE, index + 1, cellValue,
                    validationValue));
        }
    }

    private String validateAndGetTestDataValue(Row row) {
        int colNum = ExcelTestsConstants.VALIDATION_COLUMN_INDEX + 1;
        Cell cell = row.getCell(colNum);
        String cellValue = dataFormatter.formatCellValue(cell);
        if (currentFileRowIndex == ExcelTestsConstants.TEST_TIME_LIMIT_INDEX) {
            return cellValue;
        }
        if (cellValue.isEmpty()) {
            throw new ExcelReadingException(
                    String.format(CELL_VALUE_ERROR_MESSAGE, currentFileRowIndex + 1));
        }
        return cellValue;
    }

    private void setTestValues(Row row) {
        String cellValue = validateAndGetTestDataValue(row);
        switch (currentFileRowIndex) {
            case ExcelTestsConstants.TEST_NAME_INDEX -> testDto.setName(cellValue);
            case ExcelTestsConstants.TEST_DESCRIPTION_INDEX -> testDto.setDescription(cellValue);
            case ExcelTestsConstants.TEST_TIME_LIMIT_INDEX -> testDto.setTimeLimit(parseLocalTime(cellValue));
            case ExcelTestsConstants.TEST_AVAILABLE_FROM_INDEX ->
                    testDto.setAvailableFrom(parseLocalDateTime(cellValue));
            case ExcelTestsConstants.TEST_AVAILABLE_TO_INDEX -> {
                testDto.setAvailableTo(parseLocalDateTime(cellValue));
                validateTestDataAvailableTime();
            }
        }
    }

    private void validateTestDataAvailableTime() {
        if (testDto.getAvailableFrom().isAfter(testDto.getAvailableTo())) {
            throw new ExcelReadingException(
                    String.format(DATE_TIME_SEQUENCE_ERROR_MESSAGE, currentFileRowIndex + 1));
        }
    }

    private LocalDateTime parseLocalDateTime(String cellValue) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ExcelTestsConstants.DATE_TIME_FORMAT);
            return LocalDateTime.parse(cellValue, formatter);
        } catch (DateTimeParseException exception) {
            throw new ExcelReadingException(String.format(DATE_TIME_ERROR_MESSAGE, currentFileRowIndex + 1));
        }
    }

    private LocalTime parseLocalTime(String cellValue) {
        if (cellValue.equals(StringUtils.EMPTY)) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ExcelTestsConstants.TIME_FORMAT);
            return LocalTime.parse(cellValue, formatter);
        } catch (DateTimeParseException exception) {
            throw new ExcelReadingException(String.format(TIME_LIMIT_ERROR_MESSAGE, currentFileRowIndex + 1));
        }
    }

    private void readTasksDataFromSheet() {
        for (currentFileRowIndex = ExcelTestsConstants.TEST_ROWS.size(); currentFileRowIndex <= sheet.getLastRowNum();
                currentFileRowIndex++) {
            Row row = sheet.getRow(currentFileRowIndex);
            if (ExcelUtils.isNotEmptyRow(row)) {
                validateRowKey(row, ExcelTestsConstants.TASK_ROWS, currentTaskRowIndex);
                setTaskValues(row);
                incrementCurrentTaskRowPosition();
            }
        }
    }

    private void incrementCurrentTaskRowPosition() {
        currentTaskRowIndex++;
        if (currentTaskRowIndex == ExcelTestsConstants.TASK_ROWS.size()) {
            currentTaskRowIndex = 0;
            taskDtos.add(currentTaskDto);
            currentTaskDto = new TaskDto();
        }
    }

    private void setTaskValues(Row row) {
        switch (currentTaskRowIndex) {
            case ExcelTestsConstants.TASK_NAME_INDEX -> setTaskNameValue(row);
            case ExcelTestsConstants.TASK_DESCRIPTION_INDEX -> setTaskDescriptionValue(row);
            case ExcelTestsConstants.TASK_IMAGES_INDEX -> setTaskImagesValues();
            case ExcelTestsConstants.TASK_ANSWERS_INDEX -> setTaskAnswersValues(row);
        }
    }

    private void setTaskNameValue(Row row) {
        String cellValue = validateAndGetTestDataValue(row);
        currentTaskDto.setName(cellValue);
    }

    private void setTaskDescriptionValue(Row row) {
        String cellValue = validateAndGetTestDataValue(row);
        currentTaskDto.setDescription(cellValue);
    }

    private void setTaskImagesValues() {
        Map<Integer, byte[]> imageByLocations = shapes.stream()
                .filter(Picture.class::isInstance)
                .map(s -> (Picture) s)
                .map(this::toMapEntry)
                .collect(toMap(Pair::getKey, Pair::getValue));

        List<ImageDto> images = StreamSupport.stream(sheet.spliterator(), false)
                .filter(this::isCurrentRow)
                .map(r -> new ImageDto(
                        0L,
                        currentTaskDto.toBuilder().build(),
                        imageByLocations.get(r.getRowNum())))
                .toList();
        excelImages.addAll(images);
    }

    private void setTaskAnswersValues(Row row) {
        for (int i = ExcelTestsConstants.VALIDATION_COLUMN_INDEX + 1; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            String cellValue = dataFormatter.formatCellValue(cell);
            if (!cellValue.isEmpty()) {
                TestAnswerValues testAnswerValues = testAnswerParser.parse(cellValue);
                AnswerDto answerDto = new AnswerDto();
                answerDto.setName(testAnswerValues.getAnswerName());
                answerDto.setValue(testAnswerValues.getAnswerValue());
                answerDto.setTaskDto(currentTaskDto);
                answerDtos.add(answerDto);
            }
        }
    }

    private boolean isCurrentRow(Row row) {
        return row.getRowNum() == currentFileRowIndex;
    }

    private Pair<Integer, byte[]> toMapEntry(Picture picture) {
        byte[] data = picture.getPictureData().getData();
        ClientAnchor anchor = picture.getClientAnchor();
        return Pair.of(anchor.getRow1(), data);
    }

    public List<TaskDto> getTaskDtos() {
        return taskDtos;
    }

    public List<AnswerDto> getAnswerDtos() {
        return answerDtos;
    }

    public List<ImageDto> getExcelImages() {
        return excelImages;
    }

    @Autowired
    public void setTestAnswerParser(TestAnswerParser testAnswerParser) {
        this.testAnswerParser = testAnswerParser;
    }
}

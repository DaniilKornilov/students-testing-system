package ru.poly.studentstestingsystem.excelhandler.constants;

import java.util.List;

public class ExcelTestsConstants {

    public static final int SHEET_INDEX = 0;

    public static final int VALIDATION_COLUMN_INDEX = 0;

    public static final List<String> TEST_ROWS = List.of("Тест:", "Описание:", "Время на выполнение:", "Доступен с:",
            "Доступен по:");

    public static final int TEST_NAME_INDEX = 0;

    public static final int TEST_DESCRIPTION_INDEX = 1;

    public static final int TEST_TIME_LIMIT_INDEX = 2;

    public static final int TEST_AVAILABLE_FROM_INDEX = 3;

    public static final int TEST_AVAILABLE_TO_INDEX = 4;

    public static final String DATE_TIME_FORMAT = "M/d/yy H:mm";

    public static final String TIME_FORMAT = "H:mm";

    public static final List<String> TASK_ROWS = List.of("Задание:", "Описание:", "Изображение:", "Ответы:");

    public static final int TASK_NAME_INDEX = 0;

    public static final int TASK_DESCRIPTION_INDEX = 1;

    public static final int TASK_IMAGES_INDEX = 2;

    public static final int TASK_ANSWERS_INDEX = 3;
}

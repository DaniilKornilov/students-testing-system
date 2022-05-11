package ru.poly.studentstestingsystem.parser;

import org.springframework.stereotype.Component;
import ru.poly.studentstestingsystem.exception.TestConstraintException;
import ru.poly.studentstestingsystem.vo.TestAnswerValues;

@Component
public class TestAnswerParser {

    private static final char ANSWER_DELIMITER = ':';

    private static final String INVALID_TEST_ANSWER_MESSAGE =
            "Неверный ввод ответа на тест! " + "Введите данные в формате Имя ответа:ответ" +
                    "Было получено: %s";

    private static final String NOT_UNIQUE_SPECIAL_SYMBOL_MESSAGE = "Символ: \"%s\" должен быть уникальным!";

    private static final String NOT_NUMBER_ANSWER_MESSAGE = "В ответе на задание должно быть число! Было получено: %s";

    public TestAnswerValues parse(String answerString) {
        validateAnswerValue(answerString);
        TestAnswerValues testAnswerValues = new TestAnswerValues();
        parseAndSetAnswerName(testAnswerValues, answerString);
        parseAndSetAnswerValue(testAnswerValues, answerString);
        return testAnswerValues;
    }

    private void validateAnswerValue(String answerString) {
        int symbolIndex = answerString.indexOf(ANSWER_DELIMITER);
        if (symbolIndex == -1 || answerString.substring(symbolIndex + 1).indexOf(ANSWER_DELIMITER) != -1) {
            throw new TestConstraintException(
                    String.format(INVALID_TEST_ANSWER_MESSAGE, answerString)
                            + String.format(NOT_UNIQUE_SPECIAL_SYMBOL_MESSAGE, ANSWER_DELIMITER));
        }
    }

    private void parseAndSetAnswerName(TestAnswerValues testAnswerValues, String answerValue) {
        int answerDelimiterIndex = answerValue.indexOf(ANSWER_DELIMITER);
        String answerName = answerValue.substring(0, answerDelimiterIndex);
        testAnswerValues.setAnswerName(answerName);
    }

    private void parseAndSetAnswerValue(TestAnswerValues testAnswerValues, String answerString) {
        int answerDelimiterIndex = answerString.indexOf(ANSWER_DELIMITER);
        String answerValue = answerString.substring(answerDelimiterIndex + 1);
        double value;
        try {
            value = Double.parseDouble(answerValue);
        } catch (NumberFormatException exception) {
            throw new TestConstraintException(String.format(NOT_NUMBER_ANSWER_MESSAGE, answerValue));
        }
        testAnswerValues.setAnswerValue(value);
    }
}

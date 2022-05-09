package ru.poly.studentstestingsystem.parser;

import org.springframework.stereotype.Component;
import ru.poly.studentstestingsystem.exception.StudentConstraintException;
import ru.poly.studentstestingsystem.vo.StudentUsernameValues;

@Component
public class StudentUsernameParser {

    private static final char COURSE_NAME_DELIMITER = '_';

    private static final char TEACHER_USERNAME_DELIMITER = '.';

    private static final char GROUP_NAME_DELIMITER = '/';

    private static final String INVALID_STUDENT_USERNAME_MESSAGE =
            "Неверный идентификатор студента! " + "Введите данные в формате курс_преподаватель.группа/студент " +
                    "Было получено: %s";

    private static final String NOT_UNIQUE_SPECIAL_SYMBOL_MESSAGE = "Символ: \"%s\" должен быть уникальным!";

    public StudentUsernameValues parse(String studentUsername) {
        validateDelimiters(studentUsername);

        StudentUsernameValues studentUsernameValues = new StudentUsernameValues();
        studentUsernameValues.setStudentUsername(studentUsername);

        parseAndSetCourseName(studentUsernameValues, studentUsername);
        parseAndSetGroupName(studentUsernameValues, studentUsername);
        parseAndSetTeacherUsername(studentUsernameValues, studentUsername);

        return studentUsernameValues;
    }

    private void validateDelimiters(String studentUsername) {
        checkIfSpecialSymbolIsUnique(studentUsername, COURSE_NAME_DELIMITER);
        checkIfSpecialSymbolIsUnique(studentUsername, GROUP_NAME_DELIMITER);
        checkIfSpecialSymbolIsUnique(studentUsername, TEACHER_USERNAME_DELIMITER);

        int courseDelimiterIndex = studentUsername.indexOf(COURSE_NAME_DELIMITER);
        int teacherDelimiterIndex = studentUsername.indexOf(TEACHER_USERNAME_DELIMITER);
        int groupDelimiterIndex = studentUsername.indexOf(GROUP_NAME_DELIMITER);
        if (courseDelimiterIndex > teacherDelimiterIndex || courseDelimiterIndex > groupDelimiterIndex) {
            throw new StudentConstraintException(String.format(INVALID_STUDENT_USERNAME_MESSAGE, studentUsername));
        }
        if (teacherDelimiterIndex > groupDelimiterIndex) {
            throw new StudentConstraintException(String.format(INVALID_STUDENT_USERNAME_MESSAGE, studentUsername));
        }
    }

    private void checkIfSpecialSymbolIsUnique(String studentUsername, char symbol) {
        int symbolIndex = studentUsername.indexOf(symbol);
        if (symbolIndex == -1 || studentUsername.substring(symbolIndex + 1).indexOf(symbol) != -1) {
            throw new StudentConstraintException(
                    String.format(INVALID_STUDENT_USERNAME_MESSAGE, studentUsername)
                            + String.format(NOT_UNIQUE_SPECIAL_SYMBOL_MESSAGE, symbol));
        }
    }

    private void parseAndSetCourseName(StudentUsernameValues studentUsernameValues, String studentUsername) {
        int courseDelimiterIndex = studentUsername.indexOf(COURSE_NAME_DELIMITER);
        String courseName = studentUsername.substring(0, courseDelimiterIndex);
        studentUsernameValues.setCourseName(courseName);
    }

    private void parseAndSetGroupName(StudentUsernameValues studentUsernameValues, String studentUsername) {
        int groupDelimiterIndex = studentUsername.indexOf(GROUP_NAME_DELIMITER);
        int teacherDelimiterIndex = studentUsername.indexOf(TEACHER_USERNAME_DELIMITER);
        String groupName = studentUsername.substring(teacherDelimiterIndex + 1, groupDelimiterIndex);
        studentUsernameValues.setGroupName(groupName);
    }

    private void parseAndSetTeacherUsername(StudentUsernameValues studentUsernameValues, String studentUsername) {
        int courseDelimiterIndex = studentUsername.indexOf(COURSE_NAME_DELIMITER);
        int teacherDelimiterIndex = studentUsername.indexOf(TEACHER_USERNAME_DELIMITER);
        String teacherUsername = studentUsername.substring(courseDelimiterIndex + 1, teacherDelimiterIndex);
        studentUsernameValues.setTeacherUsername(teacherUsername);
    }
}

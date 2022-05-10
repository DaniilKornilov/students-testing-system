package ru.poly.studentstestingsystem.excelhandler.excelutils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

public class ExcelUtils {

    public static boolean isNotEmptyRow(Row row) {
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

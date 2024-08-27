package com.itheima;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class TestExcel {

    public void readSpecificSheet(MultipartFile file, String sheetName) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet != null) {
                readSheet(sheet);
            }
        }
    }

    private void readSheet(Sheet sheet) {
        for (Row row : sheet) {
            for (Cell cell : row) {
                // 根据你的需求处理单元格内容
                String cellValue = getCellValueAsString(cell);
                System.out.print(cellValue + "\t");
            }
            System.out.println();
        }
    }

    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return String.valueOf(cell.getCellFormula());
            default:
                return "";
        }
    }
}
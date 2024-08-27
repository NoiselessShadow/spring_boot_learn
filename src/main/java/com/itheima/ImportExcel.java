package com.itheima;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ImportExcel {

    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream("C:\\Users\\Administrator\\Desktop\\test (2).xlsx");
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<Row> splitRows = new ArrayList<>();


        for (Row row : sheet) {
            System.out.print(row.getRowNum());
            if(0== row.getRowNum()){
                continue;
            }
            System.out.print("====");
            for (Cell cell : row) {
                System.out.print(cell.getColumnIndex());
                System.out.print("=");

                System.out.println(cell.getNumericCellValue());

            }
        }







        workbook.close();
        inputStream.close();
    }




}

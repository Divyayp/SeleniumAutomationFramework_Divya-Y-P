package com.automation.framework.utils;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;

public class ExcelUtils {

    private XSSFWorkbook workbook;

    public ExcelUtils(String filePath) {

        try {

            System.out.println("Excel Path: " + filePath);

            FileInputStream file = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(file);

            System.out.println("Sheets in workbook:");

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                System.out.println("Sheet " + i + " = [" + workbook.getSheetName(i) + "]");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public XSSFSheet getSheet(String sheetName) {

        return workbook.getSheet(sheetName);

    }

    public int getRowCount(String sheetName) {

        XSSFSheet sheet = workbook.getSheet(sheetName);

        int lastRow = sheet.getLastRowNum();

        for (int i = lastRow; i >= 1; i--) {

            if (sheet.getRow(i) != null) {

                boolean hasData = false;

                for (int j = 0; j < sheet.getRow(i).getLastCellNum(); j++) {

                    if (sheet.getRow(i).getCell(j) != null &&
                        !sheet.getRow(i).getCell(j).toString().trim().isEmpty()) {

                        hasData = true;
                        break;
                    }
                }

                if (hasData) {
                    return i + 1;
                }
            }
        }

        return 1;
    }

    public String getCellData(String sheetName, int rowNum, int colNum) {

        DataFormatter formatter = new DataFormatter();

        return formatter.formatCellValue(
                workbook.getSheet(sheetName)
                        .getRow(rowNum)
                        .getCell(colNum));

    }

    }
    

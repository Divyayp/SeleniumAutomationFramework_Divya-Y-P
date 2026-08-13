package com.automation.framework.tests;

import org.testng.annotations.DataProvider;

import com.automation.framework.utils.ExcelUtils;

public class TestDataProvider {

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {

        ExcelUtils excel = new ExcelUtils("src/test/resources/TestData.xlsx");

        int rows = excel.getRowCount("Login");

        Object[][] data = new Object[rows - 1][3];

        for (int i = 1; i < rows; i++) {

            data[i - 1][0] = excel.getCellData("LoginData", i, 0); // TestCase
            data[i - 1][1] = excel.getCellData("LoginData", i, 1); // Username
            data[i - 1][2] = excel.getCellData("LoginData", i, 2); // Password
        }

        return data;
    }
}
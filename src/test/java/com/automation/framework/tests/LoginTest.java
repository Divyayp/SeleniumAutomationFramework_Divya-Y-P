package com.automation.framework.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.automation.framework.base.BaseTest;
import com.automation.framework.pages.LoginPage;
import com.automation.framework.utils.ExcelUtils;

import com.automation.framework.reports.TestNGDescription;

public class LoginTest extends BaseTest {

    @Test(dataProvider = "loginData")
    public void loginTest(
            String testCase,
            String description,
            String username,
            String password) {

        // Make description available to the TestNG listener
        TestNGDescription.setDescription(description);

        System.out.println("Executing Test Case : " + testCase);
        System.out.println("Description : " + description);
        System.out.println("Username : " + username);

        LoginPage loginPage = new LoginPage(driver);

        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
    }


    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {

        ExcelUtils excel = new ExcelUtils(
                "src/test/resources/TestData.xlsx"
        );

        int rows = excel.getRowCount("LoginData");

        Object[][] data = new Object[rows - 1][4];

        for (int i = 1; i < rows; i++) {

            data[i - 1][0] =
                    excel.getCellData("LoginData", i, 0);

            data[i - 1][1] =
                    excel.getCellData("LoginData", i, 1);

            data[i - 1][2] =
                    excel.getCellData("LoginData", i, 2);

            data[i - 1][3] =
                    excel.getCellData("LoginData", i, 3);
        }

        return data;
    }
}
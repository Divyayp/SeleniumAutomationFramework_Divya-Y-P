package com.automation.framework.utils;

import org.testng.annotations.DataProvider;

public class TestDataProvider {

	@DataProvider(name="loginData")
	public Object[][] getLoginData(){

	    ExcelUtils excel = new ExcelUtils("src/test/resources/TestData.xlsx");

	    int rows = excel.getRowCount("LoginData");

	    Object[][] data = new Object[rows-1][3];

	    for(int i=1;i<rows;i++){

	        data[i-1][0] = excel.getCellData("LoginData", i, 0);
	        data[i-1][1] = excel.getCellData("LoginData", i, 1);
	        data[i-1][2] = excel.getCellData("LoginData", i, 2);

	    }

	    return data;
	}
}
package com.automation.framework.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;

import com.automation.framework.driver.DriverFactory;
import com.automation.framework.listeners.ExtentTestListener;
import org.testng.annotations.Optional;


@Listeners(ExtentTestListener.class)

public class BaseTest {


    protected WebDriver driver;


    @BeforeMethod
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {

        System.out.println("Launching Browser : " + browser);


        DriverFactory.initDriver(browser);


        driver = DriverFactory.getDriver();


        driver.get(
          "https://practicetestautomation.com/practice-test-login/"
        );


    }



    @AfterMethod
    public void tearDown() {


        DriverFactory.quitDriver();


    }

}
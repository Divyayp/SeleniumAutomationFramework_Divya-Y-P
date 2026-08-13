package com.automation.framework.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class DriverFactory {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initDriver(String browser) {

        System.out.println("Starting browser : " + browser);

        try {

            if(browser.equalsIgnoreCase("chrome")) {

                System.out.println("Setting up ChromeDriver");

                driver.set(new ChromeDriver());

                System.out.println("Chrome launched successfully");

            }

            else if(browser.equalsIgnoreCase("edge")) {

                System.out.println("Setting up EdgeDriver");

                System.setProperty(
                    "webdriver.edge.driver",
                    "C:\\Users\\Divya\\Downloads\\edgedriver_win64 (2)\\msedgedriver.exe"
                );

                driver.set(new EdgeDriver());

                System.out.println("Edge launched successfully");

            }

            else {

                throw new RuntimeException(
                        "Invalid browser : " + browser
                );

            }

            getDriver()
            .manage()
            .window()
            .maximize();

        }
        catch(Exception e) {

            System.out.println(
                    "Browser launch failed : " + browser
            );

            e.printStackTrace();

            throw e;

        }

    }

    public static WebDriver getDriver() {

        return driver.get();

    }

    public static void quitDriver() {

        if(driver.get()!=null) {

            driver.get().quit();

            driver.remove();

            System.out.println(
                    "Browser closed successfully"
            );

        }

    }

}
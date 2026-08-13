package com.automation.framework.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtil {

    public static String captureScreenshot(WebDriver driver, String testName) {

        String screenshotFolder =
                System.getProperty("user.dir")
                + "/test-output/screenshots";

        File folder = new File(screenshotFolder);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fileName =
                testName + "_"
                + Thread.currentThread().getId()
                + "_"
                + System.currentTimeMillis()
                + ".png";

        File src =
                ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE);

        File dest =
                new File(
                        screenshotFolder
                        + "/"
                        + fileName
                );

        try {

            FileUtils.copyFile(src, dest);

        }
        catch (IOException e) {

            e.printStackTrace();

        }

        // Relative path from custom-report folder
        return "../screenshots/" + fileName;

    }

}
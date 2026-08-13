package com.automation.framework.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.automation.framework.driver.DriverFactory;
import com.automation.framework.reports.ExtentReportManager;
import com.automation.framework.reports.ReportData;
import com.automation.framework.reports.TestNGDescription;
import com.automation.framework.utils.ScreenshotUtil;
import com.aventstack.extentreports.ExtentTest;

public class ExtentTestListener implements ITestListener {

    private static ThreadLocal<ExtentTest> extentTest =
            new ThreadLocal<>();


    @Override
    public void onTestStart(ITestResult result) {

        String browser =
                result.getTestContext()
                        .getCurrentXmlTest()
                        .getParameter("browser");


        ExtentTest test =
                ExtentReportManager
                        .getExtentReports()
                        .createTest(
                                result.getMethod().getMethodName()
                        );


        ReportData.total.incrementAndGet();


        ReportData.startTime.set(
                System.currentTimeMillis()
        );


        if (!ReportData.browsers.contains(browser)) {

            ReportData.browsers.add(browser);
        }


        test.info("Browser : " + browser);


        extentTest.set(test);
    }


    public static ExtentTest getTest() {

        return extentTest.get();
    }


    @Override
    public void onTestSuccess(ITestResult result) {

        getTest()
                .pass("Test Passed Successfully");


        ReportData.passed.incrementAndGet();


        long duration =
                System.currentTimeMillis()
                        - ReportData.startTime.get();


        String executionTime =
                String.format(
                        "%.2fs",
                        duration / 1000.0
                );


        String screenshotPath =
                ScreenshotUtil.captureScreenshot(
                        DriverFactory.getDriver(),
                        result.getMethod().getMethodName()
                );


        String browser =
                result.getTestContext()
                        .getCurrentXmlTest()
                        .getParameter("browser");


        String description =
                TestNGDescription.getDescription();


        ReportData.results.add(
                new String[] {

                        "TC00"
                                + ReportData.testCounter
                                .getAndIncrement(),

                        result.getMethod()
                                .getMethodName(),

                        description,

                        browser,

                        "PASS",

                        executionTime,

                        screenshotPath
                }
        );


        try {

            getTest()
                    .addScreenCaptureFromPath(
                            screenshotPath
                    );

        } catch (Exception e) {

            e.printStackTrace();
        }


        TestNGDescription.clear();
    }


    @Override
    public void onTestFailure(ITestResult result) {

        getTest()
                .fail(result.getThrowable());


        ReportData.failed.incrementAndGet();


        String screenshotPath =
                ScreenshotUtil.captureScreenshot(
                        DriverFactory.getDriver(),
                        result.getMethod().getMethodName()
                );


        long duration =
                System.currentTimeMillis()
                        - ReportData.startTime.get();


        String executionTime =
                String.format(
                        "%.2fs",
                        duration / 1000.0
                );


        String browser =
                result.getTestContext()
                        .getCurrentXmlTest()
                        .getParameter("browser");


        String description =
                TestNGDescription.getDescription();


        ReportData.results.add(
                new String[] {

                        "TC00"
                                + ReportData.testCounter
                                .getAndIncrement(),

                        result.getMethod()
                                .getMethodName(),

                        description,

                        browser,

                        "FAIL",

                        executionTime,

                        screenshotPath
                }
        );


        try {

            getTest()
                    .addScreenCaptureFromPath(
                            screenshotPath
                    );

        } catch (Exception e) {

            e.printStackTrace();
        }


        TestNGDescription.clear();
    }


    @Override
    public void onTestSkipped(ITestResult result) {

        ReportData.skipped.incrementAndGet();


        String browser =
                result.getTestContext()
                        .getCurrentXmlTest()
                        .getParameter("browser");


        String description =
                TestNGDescription.getDescription();


        ReportData.results.add(
                new String[] {

                        "TC00"
                                + ReportData.testCounter
                                .getAndIncrement(),

                        result.getMethod()
                                .getMethodName(),

                        description,

                        browser,

                        "SKIPPED",

                        "NA",

                        "—"
                }
        );


        if (getTest() != null) {

            getTest()
                    .skip("Test Skipped");
        }


        TestNGDescription.clear();
    }


    @Override
    public void onFinish(ITestContext context) {

        // Report generation is handled by SuiteReportListener
    }
}
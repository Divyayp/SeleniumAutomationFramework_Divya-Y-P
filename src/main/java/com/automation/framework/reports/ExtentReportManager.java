package com.automation.framework.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager {

    private static ExtentReports extent;


    public static ExtentReports getExtentReports() {


        if (extent == null) {


            String reportPath =
                    System.getProperty("user.dir")
                    + "/test-output/AutomationReport.html";


            ExtentSparkReporter spark =
                    new ExtentSparkReporter(reportPath);


            spark.config().setTheme(Theme.STANDARD);

            spark.config()
                 .setDocumentTitle("Automation Test Report");


            spark.config()
                 .setReportName("Automation Execution Report");


            spark.config()
                 .setTimeStampFormat("dd-MM-yyyy HH:mm:ss");



            extent = new ExtentReports();


            extent.attachReporter(spark);



            extent.setSystemInfo(
                    "Framework",
                    "Selenium Automation Framework"
            );


            extent.setSystemInfo(
                    "Environment",
                    "QA"
            );


            extent.setSystemInfo(
                    "QA Engineer",
                    "Divya Y P"
            );


            extent.setSystemInfo(
                    "OS",
                    System.getProperty("os.name")
            );


            extent.setSystemInfo(
                    "Java Version",
                    System.getProperty("java.version")
            );



            String browser =
                    System.getProperty("browser");


            extent.setSystemInfo(
                    "Browser",
                    browser != null
                    ? browser.toUpperCase()
                    : "Not Defined"
            );

        }


        return extent;

    }

}
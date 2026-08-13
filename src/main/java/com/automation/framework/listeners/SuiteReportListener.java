package com.automation.framework.listeners;

import org.testng.ISuite;
import org.testng.ISuiteListener;

import com.automation.framework.reports.CustomReportGenerator;
import com.automation.framework.reports.ExtentReportManager;
import com.automation.framework.reports.PDFReportGenerator;
import com.automation.framework.utils.EmailUtil;

public class SuiteReportListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {

        // Suite started
    }

    @Override
    public void onFinish(ISuite suite) {

        // Flush Extent Report once after the complete suite
        if (ExtentReportManager.getExtentReports() != null) {

            ExtentReportManager
                    .getExtentReports()
                    .flush();
        }


        // Generate custom HTML report once
        try {

            CustomReportGenerator.generateReport();

        } catch (Exception e) {

            e.printStackTrace();
        }


        // Generate PDF report once
        try {

            PDFReportGenerator.generatePDF();

        } catch (Exception e) {

            e.printStackTrace();
        }


        // Send execution summary email once
        try {

            EmailUtil.sendEmail(suite);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
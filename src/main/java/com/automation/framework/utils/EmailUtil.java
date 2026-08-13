package com.automation.framework.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import org.testng.ISuite;
import org.testng.ITestResult;

import com.automation.framework.config.ConfigReader;

public class EmailUtil {

    public static void sendEmail(ISuite suite) {

        String senderEmail = ConfigReader.getProperty("email.username");
        String appPassword = ConfigReader.getProperty("email.password");
        String receiverEmail = ConfigReader.getProperty("email.to");

        String host = ConfigReader.getProperty("email.host");
        String port = ConfigReader.getProperty("email.port");

        Properties properties = new Properties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(
                properties,
                new Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {

                        return new PasswordAuthentication(
                                senderEmail,
                                appPassword
                        );
                    }
                }
        );

        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(senderEmail));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(receiverEmail)
            );

            message.setSubject("Automation Test Execution Report");

            String emailBody = buildEmailBody(suite);

            // Create multipart message for email body + attachment
            Multipart multipart = new MimeMultipart();

            // Add email body
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(
                    emailBody,
                    "text/html; charset=UTF-8"
            );
            multipart.addBodyPart(bodyPart);

            // Add PDF attachment
            String pdfPath = "test-output/custom-report/Automation-Test-Execution-Report.pdf";
            File pdfFile = new File(pdfPath);

            if (pdfFile.exists()) {

                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(pdfFile);
                multipart.addBodyPart(attachmentPart);

                System.out.println("PDF attachment added to email");

            } else {

                System.out.println("PDF file not found at: " + pdfPath);
            }

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Email sent successfully with attachment.");

        } catch (Exception e) {

            System.out.println("Failed to send email.");
            e.printStackTrace();
        }
    }


    private static String buildEmailBody(ISuite suite) {

        int total = 0;
        int passed = 0;
        int failed = 0;
        int skipped = 0;
        
        long suiteStartTime = Long.MAX_VALUE;
        long suiteEndTime = 0;

        StringBuilder testDetails = new StringBuilder();

        /*
         * Collect test results from the complete suite.
         */
        for (Map.Entry<String, org.testng.ISuiteResult> entry
                : suite.getResults().entrySet()) {

            org.testng.ISuiteResult suiteResult = entry.getValue();

            /*
             * Passed tests
             */
            for (ITestResult result
                    : suiteResult.getTestContext()
                    .getPassedTests()
                    .getAllResults()) {

                total++;
                passed++;

                // Track suite start and end time
                if (result.getStartMillis() < suiteStartTime) {
                    suiteStartTime = result.getStartMillis();
                }
                if (result.getEndMillis() > suiteEndTime) {
                    suiteEndTime = result.getEndMillis();
                }

                appendTestResult(testDetails, result, "PASS");
            }

            /*
             * Failed tests
             */
            for (ITestResult result
                    : suiteResult.getTestContext()
                    .getFailedTests()
                    .getAllResults()) {

                total++;
                failed++;

                // Track suite start and end time
                if (result.getStartMillis() < suiteStartTime) {
                    suiteStartTime = result.getStartMillis();
                }
                if (result.getEndMillis() > suiteEndTime) {
                    suiteEndTime = result.getEndMillis();
                }

                appendTestResult(testDetails, result, "FAIL");
            }

            /*
             * Skipped tests
             */
            for (ITestResult result
                    : suiteResult.getTestContext()
                    .getSkippedTests()
                    .getAllResults()) {

                total++;
                skipped++;

                // Track suite start and end time
                if (result.getStartMillis() < suiteStartTime) {
                    suiteStartTime = result.getStartMillis();
                }
                if (result.getEndMillis() > suiteEndTime) {
                    suiteEndTime = result.getEndMillis();
                }

                appendTestResult(testDetails, result, "SKIPPED");
            }
        }

        String executionDate =
                new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss")
                        .format(new Date());

        String suiteName = suite.getName();

        // Calculate total execution time
        long totalExecutionTime = 0;
        if (suiteStartTime != Long.MAX_VALUE && suiteEndTime > 0) {
            totalExecutionTime = suiteEndTime - suiteStartTime;
        }
        
        String totalExecutionTimeText = formatDuration(totalExecutionTime);

        StringBuilder html = new StringBuilder();

        html.append("<html>");
        html.append("<body style='font-family:Arial,Helvetica,sans-serif;"
                + "background-color:#f4f6f9;"
                + "padding:20px;'>");

        /*
         * Header
         */
        html.append(
                "<div style='background-color:#1f2937;"
                + "color:white;"
                + "padding:20px;"
                + "border-radius:6px;'>"
        );

        html.append(
                "<h2 style='margin:0;'>"
                + "Automation Test Execution Report"
                + "</h2>"
        );

        html.append(
                "<p style='margin:8px 0 0 0;'>"
                + "Suite: <b>" + suiteName + "</b>"
                + "</p>"
        );

        html.append("</div>");

        /*
         * Execution information
         */
        html.append(
                "<h3 style='margin-top:25px;'>Execution Details</h3>"
        );

        html.append(
                "<table style='border-collapse:collapse;"
                + "width:100%;"
                + "background:white;'>"
        );

        html.append(
                createSummaryRow(
                        "Execution Date",
                        executionDate
                )
        );

        html.append(
                createSummaryRow(
                        "Suite Name",
                        suiteName
                )
        );

        html.append(
                createSummaryRow(
                        "Total Execution Time",
                        totalExecutionTimeText
                )
        );

        html.append(
                createSummaryRow(
                        "Total Tests",
                        String.valueOf(total)
                )
        );

        html.append(
                createSummaryRow(
                        "Passed",
                        String.valueOf(passed)
                )
        );

        html.append(
                createSummaryRow(
                        "Failed",
                        String.valueOf(failed)
                )
        );

        html.append(
                createSummaryRow(
                        "Skipped",
                        String.valueOf(skipped)
                )
        );

        html.append("</table>");

        /*
         * Test details
         */
        html.append(
                "<h3 style='margin-top:25px;'>Test Execution Details</h3>"
        );

        html.append(
                "<table style='border-collapse:collapse;"
                + "width:100%;"
                + "background:white;'>"
        );

        html.append(
                "<tr style='background-color:#374151;color:white;'>"
        );

        html.append("<th style='padding:10px;border:1px solid #ddd;'>");
        html.append("TC ID");
        html.append("</th>");

        html.append("<th style='padding:10px;border:1px solid #ddd;'>");
        html.append("Description");
        html.append("</th>");

        html.append("<th style='padding:10px;border:1px solid #ddd;'>");
        html.append("Browser");
        html.append("</th>");

        html.append("<th style='padding:10px;border:1px solid #ddd;'>");
        html.append("Status");
        html.append("</th>");

        html.append("<th style='padding:10px;border:1px solid #ddd;'>");
        html.append("Duration");
        html.append("</th>");

        html.append("</tr>");

        html.append(testDetails);

        html.append("</table>");

        /*
         * Footer
         */
        html.append(
                "<p style='margin-top:25px;color:#666;'>"
                + "This is an automated email generated by the "
                + "QA Automation Framework."
                + "</p>"
        );

        html.append(
                "<p style='color:#666;'>"
                + "Regards,<br>"
                + "<b>QA Automation Team</b>"
                + "</p>"
        );

        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }


    private static void appendTestResult(
            StringBuilder html,
            ITestResult result,
            String status) {

        String testCaseId =
                result.getMethod().getMethodName();

        String description =
                result.getMethod().getDescription();

        if (description == null || description.trim().isEmpty()) {
            description = "-";
        }

        /*
         * Get browser from TestNG parameter.
         * Try multiple ways to get the browser parameter
         */
        String browser = "-";

        try {

            browser = result
                    .getTestContext()
                    .getCurrentXmlTest()
                    .getParameter("browser");

            if (browser == null || browser.trim().isEmpty()) {
                browser = "-";
            }

        } catch (Exception e) {

            // If above fails, try getting from suite parameters
            try {
                
                browser = result
                        .getTestContext()
                        .getSuite()
                        .getParameter("browser");

                if (browser == null || browser.trim().isEmpty()) {
                    browser = "-";
                }

            } catch (Exception ex) {
                browser = "-";
            }
        }

        /*
         * Calculate duration.
         */
        long duration =
                result.getEndMillis()
                        - result.getStartMillis();

        String durationText =
                formatDuration(duration);

        String statusStyle;

        if ("PASS".equals(status)) {

            statusStyle =
                    "color:#15803d;font-weight:bold;";

        } else if ("FAIL".equals(status)) {

            statusStyle =
                    "color:#dc2626;font-weight:bold;";

        } else {

            statusStyle =
                    "color:#d97706;font-weight:bold;";
        }

        html.append("<tr>");

        html.append(
                "<td style='padding:9px;border:1px solid #ddd;'>"
                        + escapeHtml(testCaseId)
                        + "</td>"
        );

        html.append(
                "<td style='padding:9px;border:1px solid #ddd;'>"
                        + escapeHtml(description)
                        + "</td>"
        );

        html.append(
                "<td style='padding:9px;border:1px solid #ddd;'>"
                        + escapeHtml(browser)
                        + "</td>"
        );

        html.append(
                "<td style='padding:9px;border:1px solid #ddd;"
                        + statusStyle
                        + "'>"
                        + status
                        + "</td>"
        );

        html.append(
                "<td style='padding:9px;border:1px solid #ddd;'>"
                        + durationText
                        + "</td>"
        );

        html.append("</tr>");
    }


    private static String createSummaryRow(
            String label,
            String value) {

        return
                "<tr>"
                + "<td style='padding:10px;"
                + "border:1px solid #ddd;"
                + "font-weight:bold;"
                + "width:30%;'>"
                + escapeHtml(label)
                + "</td>"
                + "<td style='padding:10px;"
                + "border:1px solid #ddd;'>"
                + escapeHtml(value)
                + "</td>"
                + "</tr>";
    }


    private static String formatDuration(long duration) {

        long seconds = duration / 1000;

        long minutes = seconds / 60;

        seconds = seconds % 60;

        return String.format(
                "%02d min %02d sec",
                minutes,
                seconds
        );
    }


    private static String escapeHtml(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}

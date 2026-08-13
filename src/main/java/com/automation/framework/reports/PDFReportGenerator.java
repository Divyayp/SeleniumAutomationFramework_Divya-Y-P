package com.automation.framework.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

public class PDFReportGenerator {

    private static final String PDF_PATH =
            "test-output/custom-report/Automation-Test-Execution-Report.pdf";

    private PDFReportGenerator() {
        // Utility class
    }

    public static void generatePDF() {

        try {

            Path pdfPath =
                    Paths.get(PDF_PATH);

            Files.createDirectories(
                    pdfPath.getParent()
            );

            Files.deleteIfExists(
                    pdfPath
            );

            String html =
                    buildHtml();

            try (
                    FileOutputStream outputStream =
                            new FileOutputStream(
                                    pdfPath.toFile()
                            )
            ) {

                PdfRendererBuilder builder =
                        new PdfRendererBuilder();

                builder.useFastMode();

                /*
                 * Project root is used as the base URI
                 * so screenshot files can be loaded.
                 */
                builder.withHtmlContent(
                        html,
                        new File(".")
                                .getAbsoluteFile()
                                .toURI()
                                .toString()
                );

                builder.toStream(
                        outputStream
                );

                builder.run();
            }

            System.out.println();
            System.out.println(
                    "=============================================="
            );
            System.out.println(
                    "PDF REPORT GENERATED SUCCESSFULLY"
            );
            System.out.println(
                    "Location:"
            );
            System.out.println(
                    pdfPath.toAbsolutePath()
            );
            System.out.println(
                    "=============================================="
            );
            System.out.println();

        }
        catch (Exception e) {

            System.err.println();
            System.err.println(
                    "=============================================="
            );
            System.err.println(
                    "PDF REPORT GENERATION FAILED"
            );
            System.err.println(
                    "=============================================="
            );

            e.printStackTrace();
        }
    }


    private static String buildHtml() {

        String executionDate =
                LocalDateTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "dd-MM-yyyy HH:mm:ss"
                                )
                        );


        String browsers =
                ReportData.browsers
                        .stream()
                        .distinct()
                        .collect(
                                Collectors.joining(", ")
                        );


        if (browsers.isEmpty()) {
            browsers = "N/A";
        }


        /*
         * IMPORTANT:
         *
         * We use placeholders and replace()
         * instead of String.formatted().
         *
         * This prevents CSS % characters from
         * being interpreted as format specifiers.
         */

        String html = """
                <!DOCTYPE html>

                <html xmlns="http://www.w3.org/1999/xhtml">

                <head>

                    <meta charset="UTF-8"/>

                    <style>

                        @page {
                            size: A4 landscape;
                            margin: 20px;
                        }

                        body {
                            font-family: Arial, Helvetica, sans-serif;
                            font-size: 10px;
                            color: #222222;
                            margin: 0;
                            padding: 0;
                        }

                        .header {
                            background-color: #1f2937;
                            color: white;
                            padding: 15px;
                            margin-bottom: 15px;
                        }

                        .header h1 {
                            margin: 0 0 8px 0;
                            font-size: 20px;
                        }

                        .header p {
                            margin: 3px 0;
                            font-size: 10px;
                        }

                        .summary {
                            width: 100%;
                            border-collapse: collapse;
                            margin-bottom: 18px;
                        }

                        .summary td {
                            width: 20%;
                            border: 1px solid #cccccc;
                            text-align: center;
                            padding: 10px;
                            background-color: #f8f9fa;
                        }

                        .summary-number {
                            font-size: 18px;
                            font-weight: bold;
                            color: #1f2937;
                        }

                        .summary-label {
                            font-size: 9px;
                            margin-top: 4px;
                            color: #555555;
                        }

                        .section-title {
                            font-size: 14px;
                            font-weight: bold;
                            margin-bottom: 8px;
                        }

                        .results {
                            width: 100%;
                            border-collapse: collapse;
                            table-layout: fixed;
                        }

                        .results th {
                            background-color: #1f2937;
                            color: white;
                            border: 1px solid #999999;
                            padding: 7px;
                            font-size: 9px;
                            text-align: left;
                        }

                        .results td {
                            border: 1px solid #cccccc;
                            padding: 7px;
                            font-size: 9px;
                            vertical-align: top;
                            word-wrap: break-word;
                        }

                        .results tr:nth-child(even) {
                            background-color: #f8f9fa;
                        }

                        .tcid {
                            width: 7%;
                        }

                        .testcase {
                            width: 17%;
                        }

                        .description {
                            width: 30%;
                        }

                        .browser {
                            width: 10%;
                        }

                        .status {
                            width: 10%;
                            text-align: center;
                        }

                        .duration {
                            width: 11%;
                            text-align: center;
                        }

                        .screenshot {
                            width: 15%;
                            text-align: center;
                        }

                        .pass {
                            color: #198754;
                            font-weight: bold;
                        }

                        .fail {
                            color: #dc3545;
                            font-weight: bold;
                        }

                        .skip {
                            color: #fd7e14;
                            font-weight: bold;
                        }

                        .na {
                            color: #777777;
                        }

                        .screenshot-image {
                            max-width: 140px;
                            max-height: 90px;
                        }

                        .footer {
                            margin-top: 15px;
                            text-align: center;
                            font-size: 8px;
                            color: #777777;
                        }

                    </style>

                </head>

                <body>

                    <div class="header">

                        <h1>
                            Automation Test Execution Report
                        </h1>

                        <p>
                            <b>Execution Date:</b>
                            {{EXECUTION_DATE}}
                        </p>

                        <p>
                            <b>Environment:</b>
                            {{ENVIRONMENT}}
                        </p>

                        <p>
                            <b>Tester:</b>
                            {{TESTER}}
                        </p>

                        <p>
                            <b>Browser(s):</b>
                            {{BROWSERS}}
                        </p>

                    </div>


                    <table class="summary">

                        <tr>

                            <td>
                                <div class="summary-number">
                                    {{TOTAL_TESTS}}
                                </div>

                                <div class="summary-label">
                                    TOTAL TESTS
                                </div>
                            </td>


                            <td>
                                <div class="summary-number">
                                    {{PASSED_TESTS}}
                                </div>

                                <div class="summary-label">
                                    PASSED
                                </div>
                            </td>


                            <td>
                                <div class="summary-number">
                                    {{FAILED_TESTS}}
                                </div>

                                <div class="summary-label">
                                    FAILED
                                </div>
                            </td>


                            <td>
                                <div class="summary-number">
                                    {{SKIPPED_TESTS}}
                                </div>

                                <div class="summary-label">
                                    SKIPPED
                                </div>
                            </td>


                            <td>
                                <div class="summary-number">
                                    {{BROWSERS}}
                                </div>

                                <div class="summary-label">
                                    BROWSERS
                                </div>
                            </td>

                        </tr>

                    </table>


                    <div class="section-title">
                        Test Execution Details
                    </div>


                    <table class="results">

                        <thead>

                            <tr>

                                <th class="tcid">
                                    TC ID
                                </th>

                                <th class="testcase">
                                    Test Case
                                </th>

                                <th class="description">
                                    Description
                                </th>

                                <th class="browser">
                                    Browser
                                </th>

                                <th class="status">
                                    Status
                                </th>

                                <th class="duration">
                                    Duration
                                </th>

                                <th class="screenshot">
                                    Screenshot
                                </th>

                            </tr>

                        </thead>


                        <tbody>

                            {{TEST_RESULTS}}

                        </tbody>

                    </table>


                    <div class="footer">

                        Automation Framework Report

                        <br/>

                        Generated automatically after test execution

                    </div>

                </body>

                </html>
                """;


        // =========================================================
        // REPLACE DYNAMIC VALUES
        // =========================================================

        html =
                html.replace(
                        "{{EXECUTION_DATE}}",
                        escape(executionDate)
                );


        html =
                html.replace(
                        "{{ENVIRONMENT}}",
                        escape(ReportData.environment)
                );


        html =
                html.replace(
                        "{{TESTER}}",
                        escape(ReportData.tester)
                );


        html =
                html.replace(
                        "{{BROWSERS}}",
                        escape(browsers)
                );


        html =
                html.replace(
                        "{{TOTAL_TESTS}}",
                        String.valueOf(
                                ReportData.total.get()
                        )
                );


        html =
                html.replace(
                        "{{PASSED_TESTS}}",
                        String.valueOf(
                                ReportData.passed.get()
                        )
                );


        html =
                html.replace(
                        "{{FAILED_TESTS}}",
                        String.valueOf(
                                ReportData.failed.get()
                        )
                );


        html =
                html.replace(
                        "{{SKIPPED_TESTS}}",
                        String.valueOf(
                                ReportData.skipped.get()
                        )
                );


        html =
                html.replace(
                        "{{TEST_RESULTS}}",
                        buildResultRows()
                );


        return html;
    }


    private static String buildResultRows() {

        StringBuilder rows =
                new StringBuilder();


        synchronized (ReportData.results) {

            List<String[]> results =
                    ReportData.results;


            for (String[] result : results) {

                if (result == null) {
                    continue;
                }


                String tcId =
                        getValue(result, 0);

                String testCase =
                        getValue(result, 1);

                String description =
                        getValue(result, 2);

                String browser =
                        getValue(result, 3);

                String status =
                        getValue(result, 4);

                String duration =
                        getValue(result, 5);

                String screenshot =
                        getValue(result, 6);


                rows.append("<tr>");


                rows.append("<td>")
                        .append(escape(tcId))
                        .append("</td>");


                rows.append("<td>")
                        .append(escape(testCase))
                        .append("</td>");


                rows.append("<td>")
                        .append(escape(description))
                        .append("</td>");


                rows.append("<td>")
                        .append(escape(browser))
                        .append("</td>");


                rows.append("<td class=\"")
                        .append(getStatusClass(status))
                        .append("\">")
                        .append(escape(status))
                        .append("</td>");


                rows.append("<td>")
                        .append(escape(duration))
                        .append("</td>");


                rows.append("<td>");


                appendScreenshot(
                        rows,
                        screenshot
                );


                rows.append("</td>");


                rows.append("</tr>");
            }
        }


        if (rows.length() == 0) {

            rows.append("""
                    <tr>
                        <td colspan="7"
                            style="text-align:center;">
                            No test execution data available
                        </td>
                    </tr>
                    """);
        }


        return rows.toString();
    }


    private static void appendScreenshot(
            StringBuilder rows,
            String screenshot) {


        if (screenshot == null ||
                screenshot.trim().isEmpty() ||
                "—".equals(screenshot)) {

            rows.append(
                    "<span class=\"na\">N/A</span>"
            );

            return;
        }


        /*
         * ScreenshotUtil returns:
         *
         * ../screenshots/file.png
         *
         * relative to:
         *
         * test-output/custom-report/
         *
         *
         * Therefore resolve it from the custom-report folder.
         */

        File reportFolder =
                new File(
                        "test-output/custom-report"
                );


        File screenshotFile =
                new File(
                        reportFolder,
                        screenshot
                );


        if (!screenshotFile.exists()) {

            /*
             * Fallback in case an absolute path
             * was supplied.
             */

            screenshotFile =
                    new File(screenshot);


            if (!screenshotFile.exists()) {

                rows.append(
                        "<span class=\"na\">"
                        + "Screenshot not found"
                        + "</span>"
                );

                return;
            }
        }


        try {

            String imageUri =
                    screenshotFile
                            .getCanonicalFile()
                            .toURI()
                            .toString();


            rows.append(
                    "<img class=\"screenshot-image\" "
                    + "src=\""
            );


            rows.append(
                    escapeAttribute(imageUri)
            );


            rows.append(
                    "\" alt=\"Screenshot\" />"
            );


        }
        catch (Exception e) {

            rows.append(
                    "<span class=\"na\">"
                    + "Screenshot unavailable"
                    + "</span>"
            );
        }
    }


    private static String getValue(
            String[] result,
            int index) {

        if (result.length <= index) {
            return "";
        }

        if (result[index] == null) {
            return "";
        }

        return result[index];
    }


    private static String getStatusClass(
            String status) {

        if (status == null) {
            return "";
        }


        String value =
                status.trim()
                        .toLowerCase();


        if (value.contains("pass")) {
            return "pass";
        }


        if (value.contains("fail")) {
            return "fail";
        }


        if (value.contains("skip")) {
            return "skip";
        }


        return "";
    }


    private static String escape(
            String value) {

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


    private static String escapeAttribute(
            String value) {

        return escape(value);
    }
}
package com.automation.framework.reports;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomReportGenerator {

    private CustomReportGenerator() {
        // Utility class
    }

    public static void generateReport() {

        try {

            String projectPath =
                    System.getProperty("user.dir");

            // =========================================================
            // REPORT FOLDER
            // =========================================================

            File reportFolder =
                    new File(
                            projectPath
                            + "/test-output/custom-report"
                    );

            if (!reportFolder.exists()) {
                reportFolder.mkdirs();
            }


            // =========================================================
            // READ HTML TEMPLATE
            // =========================================================

            File templateFile =
                    new File(
                            projectPath
                            + "/src/main/resources/report-template/report.html"
                    );

            if (!templateFile.exists()) {

                System.out.println(
                        "ERROR: report.html not found at: "
                        + templateFile.getAbsolutePath()
                );

                return;
            }

            String html =
                    Files.readString(
                            templateFile.toPath()
                    );


            // =========================================================
            // EXECUTION DATE
            // =========================================================

            String executionDate =
                    LocalDateTime.now()
                            .format(
                                    DateTimeFormatter.ofPattern(
                                            "dd-MM-yyyy HH:mm:ss"
                                    )
                            );


            // =========================================================
            // SUMMARY DATA
            // =========================================================

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


            // =========================================================
            // ENVIRONMENT
            // =========================================================

            html =
                    html.replace(
                            "{{ENVIRONMENT}}",
                            escapeHtml(
                                    ReportData.environment
                            )
                    );


            // =========================================================
            // TESTER
            // =========================================================

            html =
                    html.replace(
                            "{{TESTER}}",
                            escapeHtml(
                                    ReportData.tester
                            )
                    );


            // =========================================================
            // EXECUTION DATE
            // =========================================================

            html =
                    html.replace(
                            "{{EXECUTION_DATE}}",
                            escapeHtml(
                                    executionDate
                            )
                    );


            // =========================================================
            // BROWSERS
            // =========================================================

            String browsers =
                    String.join(
                            " , ",
                            ReportData.browsers
                    );

            if (browsers.isBlank()) {
                browsers = "N/A";
            }

            html =
                    html.replace(
                            "{{BROWSERS}}",
                            escapeHtml(browsers)
                    );


            // =========================================================
            // TEST RESULT TABLE
            // =========================================================

            StringBuilder table =
                    new StringBuilder();


            synchronized (ReportData.results) {

                for (String[] row :
                        ReportData.results) {

                    if (row == null) {
                        continue;
                    }

                    table.append("<tr>");


                    for (int i = 0;
                         i < row.length;
                         i++) {

                        String value =
                                row[i] == null
                                ? ""
                                : row[i];


                        table.append("<td>");


                        // =================================================
                        // STATUS COLUMN
                        // =================================================

                        if (i == 4) {

                            if ("PASS".equalsIgnoreCase(value)) {

                                table.append(
                                        "<span class=\"status-pass\">"
                                        + "PASS"
                                        + "</span>"
                                );

                            }

                            else if ("FAIL".equalsIgnoreCase(value)) {

                                table.append(
                                        "<span class=\"status-fail\">"
                                        + "FAIL"
                                        + "</span>"
                                );

                            }

                            else if ("SKIPPED".equalsIgnoreCase(value)) {

                                table.append(
                                        "<span class=\"status-skip\">"
                                        + "SKIPPED"
                                        + "</span>"
                                );

                            }

                            else {

                                table.append(
                                        escapeHtml(value)
                                );
                            }
                        }


                        // =================================================
                        // SCREENSHOT COLUMN
                        // =================================================

                        else if (
                                i == 6
                                && !value.isBlank()
                                && !"—".equals(value)
                        ) {

                            /*
                             * ScreenshotUtil returns:
                             *
                             * ../screenshots/file.png
                             *
                             * We convert Windows backslashes to
                             * URL-friendly forward slashes.
                             */

                            String screenshotPath =
                                    value.replace(
                                            "\\",
                                            "/"
                                    );


                            /*
                             * Escape single quotes because
                             * the path is passed to JavaScript.
                             */

                            screenshotPath =
                                    screenshotPath.replace(
                                            "'",
                                            "\\'"
                                    );


                            table.append(
                                    "<a href=\"#\" "
                                    + "class=\"screenshot-link\" "
                                    + "onclick=\"openScreenshot('"
                                    + screenshotPath
                                    + "'); return false;\">"
                                    + "View Screenshot"
                                    + "</a>"
                            );

                        }


                        // =================================================
                        // NORMAL COLUMN
                        // =================================================

                        else {

                            table.append(
                                    escapeHtml(value)
                            );
                        }


                        table.append("</td>");
                    }


                    table.append("</tr>");
                }
            }


            // =========================================================
            // NO RESULTS
            // =========================================================

            if (table.length() == 0) {

                table.append(
                        "<tr>"
                        + "<td colspan=\"7\" "
                        + "style=\"text-align:center;\">"
                        + "No test execution data available"
                        + "</td>"
                        + "</tr>"
                );
            }


            // =========================================================
            // INSERT TEST RESULTS
            // =========================================================

            html =
                    html.replace(
                            "{{TEST_RESULTS}}",
                            table.toString()
                    );


            // =========================================================
            // WRITE HTML REPORT
            // =========================================================

            File htmlFile =
                    new File(
                            reportFolder,
                            "AutomationReport.html"
                    );


            Files.writeString(
                    htmlFile.toPath(),
                    html
            );


            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "HTML REPORT GENERATED SUCCESSFULLY"
            );

            System.out.println(
                    "HTML Location: "
                    + htmlFile.getAbsolutePath()
            );

            System.out.println(
                    "=============================================="
            );


            // =========================================================
            // COPY CSS
            // =========================================================

            File cssSource =
                    new File(
                            projectPath
                            + "/src/main/resources/report-template/style.css"
                    );


            File cssTarget =
                    new File(
                            reportFolder,
                            "style.css"
                    );


            if (cssSource.exists()) {

                Files.copy(
                        cssSource.toPath(),
                        cssTarget.toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                );


                System.out.println(
                        "CSS copied successfully"
                );

            }

            else {

                System.out.println(
                        "WARNING: style.css not found at: "
                        + cssSource.getAbsolutePath()
                );
            }


            // =========================================================
            // COPY JAVASCRIPT
            // =========================================================

            File javascriptSource =
                    new File(
                            projectPath
                            + "/src/main/resources/report-template/script.js"
                    );


            File javascriptTarget =
                    new File(
                            reportFolder,
                            "script.js"
                    );


            if (javascriptSource.exists()) {

                Files.copy(
                        javascriptSource.toPath(),
                        javascriptTarget.toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                );


                System.out.println(
                        "JavaScript copied successfully"
                );

            }

            else {

                System.out.println(
                        "WARNING: script.js not found at: "
                        + javascriptSource.getAbsolutePath()
                );
            }


        }
        catch (Exception e) {

            System.out.println(
                    "ERROR WHILE GENERATING CUSTOM HTML REPORT"
            );

            e.printStackTrace();
        }
    }


    // =============================================================
    // HTML ESCAPING
    // =============================================================

    private static String escapeHtml(
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
}
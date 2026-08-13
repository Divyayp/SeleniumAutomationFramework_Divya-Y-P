package com.automation.framework.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Central data holder for the automation execution report.
 *
 * This class only stores reporting information.
 * It does NOT control test execution, browsers, WebDriver or TestNG execution.
 */
public final class ReportData {

    private ReportData() {
        // Utility class
    }

    // =========================================================
    // EXECUTION SUMMARY
    // =========================================================

    public static final AtomicInteger total =
            new AtomicInteger(0);

    public static final AtomicInteger passed =
            new AtomicInteger(0);

    public static final AtomicInteger failed =
            new AtomicInteger(0);

    public static final AtomicInteger skipped =
            new AtomicInteger(0);

    // =========================================================
    // TEST CASE COUNTER
    // =========================================================

    public static final AtomicInteger testCounter =
            new AtomicInteger(1);

    // =========================================================
    // BROWSERS
    // =========================================================

    public static final List<String> browsers =
            Collections.synchronizedList(
                    new ArrayList<>()
            );

    // =========================================================
    // TEST RESULTS
    //
    // Structure:
    //
    // [0] TC ID
    // [1] Test Case
    // [2] Description
    // [3] Browser
    // [4] Status
    // [5] Duration
    // [6] Screenshot
    // =========================================================

    public static final List<String[]> results =
            Collections.synchronizedList(
                    new ArrayList<>()
            );

    // =========================================================
    // TEST START TIMES
    // =========================================================

    public static final ThreadLocal<Long> startTime =
            new ThreadLocal<>();

    // =========================================================
    // EXECUTION INFORMATION
    // =========================================================

    public static String application =
            "Automation Application";

    public static String environment =
            "QA";

    public static String tester =
            "Divya Y P";

    // =========================================================
    // EXECUTION DATE
    //
    // This will be set once when the suite starts.
    // HTML and PDF can then use the SAME execution date.
    // =========================================================

    public static volatile String executionDate = "";

    public static volatile String executionStartTime = "";

    public static volatile String executionEndTime = "";

    // =========================================================
    // RESET METHOD
    //
    // Useful if the same JVM executes another suite.
    // =========================================================

    public static synchronized void reset() {

        total.set(0);
        passed.set(0);
        failed.set(0);
        skipped.set(0);

        testCounter.set(1);

        browsers.clear();
        results.clear();

        startTime.remove();

        executionDate = "";
        executionStartTime = "";
        executionEndTime = "";
    }

    // =========================================================
    // ADD BROWSER SAFELY
    // =========================================================

    public static void addBrowser(String browser) {

        if (browser == null ||
                browser.trim().isEmpty()) {

            return;
        }

        synchronized (browsers) {

            if (!browsers.contains(browser)) {
                browsers.add(browser);
            }
        }
    }
}
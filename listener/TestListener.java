package com.appdirect.listener;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

public class TestListener implements ITestListener {
    public void onFinish(ITestContext result) {
        Reporter.log("\n[Info] All tests Completed ");
    }

    public void onStart(ITestContext result) {
        Reporter.log("\n[Info] Tests to be started ");
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    public void onTestFailure(ITestResult result) {
        Reporter.log("\n[Failure] at "+result.getName());
    }

    public void onTestSkipped(ITestResult result) {
        Reporter.log("\n[Skipped] "+result.getName()+" Test Case Skipped");
    }

    public void onTestStart(ITestResult result) {
        Reporter.log("\n[Info] "+result.getName()+" Test Case Started");
    }

    public void onTestSuccess(ITestResult result) {
        Reporter.log("\n[Info] "+result.getName()+" Test Case Success");
    }


}

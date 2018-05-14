package com.appdirect.listener;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.Reporter;


    public class SuiteListener implements ISuiteListener {


    public void onStart(ISuite suite) {
        Reporter.log("\n[Info] Test Suite "+suite.getName()+" Started ");
    }

    public void onFinish(ISuite suite) {
        Reporter.log("\n[Info] Test Suite "+suite.getName()+" Completed");

    }
}

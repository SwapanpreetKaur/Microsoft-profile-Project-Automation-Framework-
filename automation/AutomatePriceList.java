package com.appdirect.automation;
import java.io.IOException;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.appdirect.googleAccess.Editions;
import com.appdirect.googleAccess.PricesFromGoogleSheet;
import com.appdirect.utils.DriverUtils;
import com.google.gdata.util.ServiceException;

import reporter.CsvReporter;
import reporter.PdfReporter;

public class AutomatePriceList {
    private WebDriver driverEdition, driverAddon;
    private HashMap<String, Editions> editions, addons;
    private String baseUrl;
    DriverUtils utilEdition, utilAddon;
    
    @BeforeSuite
    public void sheetAccess() throws IOException, ServiceException {
        PricesFromGoogleSheet pricesFromGoogleSheet = new PricesFromGoogleSheet();
        pricesFromGoogleSheet.getusdPricefromsheet()
        					 .getotherPricefromsheet()
        					 .displayapps();
        
        addons = pricesFromGoogleSheet.getAddonList();
        editions = pricesFromGoogleSheet.getEditionsList();
    }
    
    @BeforeTest
    public void loadBrowser() {
        utilEdition = new DriverUtils();
        driverEdition = utilEdition.getDriver();
        baseUrl = utilEdition.getUrl();
        utilAddon = new DriverUtils();
        driverAddon = utilAddon.getDriver();
        baseUrl = utilAddon.getUrl();
    }
    
    @Test
    public void testMarketplacePriceEditions() {
        utilEdition.openUrl(baseUrl)
                    .gotoLogin()
                    .gotoHome()
                    .gotoEditionProfile(editions);
    }
  
    @Test
   public void testMarketplacePriceAddons() {
        utilAddon.openUrl(baseUrl)
                 .gotoLogin()
                 .gotoHome()
                .gotoAddOnProfile(addons);
    }
    
    @AfterTest
    public void testOutput() throws IOException, Exception {
        new CsvReporter().csvDifferentPrice()
                         .csvSamePrice();
        
        new PdfReporter().pdfReport();
        driverEdition.close();
        driverAddon.close();
    }
}
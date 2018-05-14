package com.appdirect.pages;

import com.appdirect.utils.DriverUtils;
import java.util.Properties;
import reporter.Reporterlog;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {
    private WebDriver driver;
    private Reporterlog log;
    private Properties properties;
    private DriverUtils util;
    private WebElement loginButton, enterEmail, enterPass;
    
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.log = new Reporterlog();
        util = new DriverUtils(driver);
        properties = util.getProperty("resources/res-pages.properties");
    }
    
    public HomePage gotoHome() {
        enterEmail = util.getElement(properties.getProperty("enterEmail"));
        if (DriverUtils.platform.equals("orchard"))
            enterEmail.sendKeys(properties.getProperty("orchardloginEmail"));
        else
            enterEmail.sendKeys(properties.getProperty("loginEmail"));
        enterPass = util.getElement(properties.getProperty("enterPass"));
        if (DriverUtils.platform.equals("orchard"))
            enterPass.sendKeys(properties.getProperty("orchardloginPass"));
        else
            enterPass.sendKeys(properties.getProperty("loginPass"));
        loginButton = util.getElement(properties.getProperty("login"));
        this.loginButton.click();
        log.info("Clicked Log In button on Login Page");
        return new HomePage(this.driver);
    }
}

package com.appdirect.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.appdirect.pages.HomePage;

import reporter.Reporterlog;

public class DriverUtils {
	private WebDriver driver;
	private String baseUrl;
	private Reporterlog log;
	public static String platform;

	public DriverUtils() {
		platform = getPlatform();
		initDriver();
	}

	public DriverUtils(WebDriver driver) {
		this.driver = driver;
	}

	public void initDriver() {
		Properties properties = this.getProperty("resources/res-utils.properties");

		if (platform.equals("orchard"))
			baseUrl = properties.getProperty("orchardbaseUrl");
		else
			baseUrl = properties.getProperty("baseUrl");

		String browser = properties.getProperty("browser");
		try {
			if (browser == null)
				browser = properties.getProperty("browser");
			if (browser.equalsIgnoreCase("Firefox")) {
				System.setProperty(properties.getProperty("firefoxDriverType"),
				        properties.getProperty("firefoxDriverPath"));
				driver = new FirefoxDriver();
			} else if (browser.equalsIgnoreCase("chrome")) {
				System.setProperty(properties.getProperty("chromeDriverType"),
				        properties.getProperty("chromeDriverPath"));
				driver = new ChromeDriver();
			}
		} catch (Exception e) {
			log.exceptionlog("Driver Not Initialized");
		}
	}

	public WebDriver getDriver() {
		return this.driver;
	}

	public Properties getProperty(String resource) {
		Properties properties = null;
		try {
			File file = new File(resource);
			FileInputStream fileInput = new FileInputStream(file);
			properties = new Properties();
			properties.load(fileInput);
		} catch (Exception e) {
			log.exceptionlog("Property file load exception");
		}
		return properties;
	}

	public HomePage openUrl(String baseUrl) {
		driver.get(baseUrl);
		return new HomePage(driver);
	}

	public String getUrl() {
		return this.baseUrl;
	}

	public void waitFor(String locator, int time) {
		WebDriverWait wait = new WebDriverWait(driver, time);
		String locate[] = locator.split(";");
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locate[0])));
		} catch (Exception e) {
			log.exceptionlog("Element Not Found At: " + locate[0]);
		}
	}

	public List<WebElement> getElementsList(String locator) {
		String locate[] = locator.split(";");
		WebDriverWait wait = new WebDriverWait(driver, 20);
		List<WebElement> webelement = new ArrayList<WebElement>();
		switch (locate[1]) {
		case "XPATH":
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locate[0])));
			webelement = driver.findElements(By.xpath(locate[0]));
			break;
		case "id":
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locate[0])));
			webelement = driver.findElements(By.id(locate[0]));
			break;
		case "CSS":
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locate[0])));
			webelement = driver.findElements(By.cssSelector(locate[0]));
			break;
		case "name":
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locate[0])));
			webelement = driver.findElements(By.name(locate[0]));
			break;
		case "linktext":
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(locate[0])));
			webelement = driver.findElements(By.linkText(locate[0]));
			break;
		case "tagname":
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(locate[0])));
			webelement = driver.findElements(By.tagName(locate[0]));
			break;
		case "class":
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(locate[0])));
			webelement = driver.findElements(By.className(locate[0]));
			break;
		default:
			webelement = null;
		}
		return webelement;
	}

	public WebElement getElement(String... locator) {
		if (locator.length == 2) {
			locator[0] = locator[0].replace("%p", locator[1]);
		}
		return getElementsList(locator[0]).get(0);
	}

	public List<WebElement> getElements(String locator) {
		return getElementsList(locator);
	}

	public String getProductId() {
		return (this.driver.getCurrentUrl().split("/"))[5];
	}

	public boolean waitForJSandJQueryToLoad() {
		WebDriverWait wait = new WebDriverWait(driver, 25);
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				try {
					return ((Long) ((JavascriptExecutor) getDriver()).executeScript("return jQuery.active") == 0);
				} catch (Exception e) {
					return true;
				}
			}
		};
		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) getDriver()).executeScript("return document.readyState").toString()
				        .equals("complete");
			}
		};
		return wait.until(jQueryLoad) && wait.until(jsLoad);
	}

	public String getPlatform() {
		Properties properties = getProperty("resources/res-utils.properties");
		return properties.getProperty("platform");
	}
}

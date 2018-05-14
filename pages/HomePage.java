package com.appdirect.pages;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import com.appdirect.googleAccess.Editions;
import com.appdirect.googleAccess.PricesFromGoogleSheet;
import com.appdirect.utils.DriverUtils;

import reporter.Reporterlog;

public class HomePage {
	private WebDriver driver;
	private Reporterlog log;
	private Properties properties;
	private WebElement loginButton;
	DriverUtils util;

	public HomePage(WebDriver driver) {
		this.driver = driver;
		this.log = new Reporterlog();
		util = new DriverUtils(driver);
		properties = util.getProperty("resources/res-pages.properties");
	}

	public LoginPage gotoLogin() {
		loginButton = util.getElement(properties.getProperty("loginButton"));
		this.loginButton.click();
		log.info("Clicked log in button on Home Page");
		return new LoginPage(this.driver);
	}

	public HomePage gotoAddOnProfile(HashMap<String, Editions> addons) {
		int editionCount = 0, editionAppCount = 0;
		try {
			for (Map.Entry<String, Editions> edition : addons.entrySet()) {

				editionCount++;
				if (!(edition.getValue().getappId().equals("NA") || edition.getValue().getappId().equals("Conflict"))) {
					editionAppCount++;
					log.info("\n\n" + edition.getValue().getappId());
					log.info("\n" + editionAppCount + "\tOpening: " + edition.getValue().getEditionName()
					        + "\nOffer id:" + edition.getValue().getOfferId());
					new ProfilePages(this.driver, edition.getValue()).openLink(edition.getValue().getappId())
					        .AddOnPricing();
				}
			}
		} catch (Exception e) {
			log.exceptionlog("Skipped: " + e.getLocalizedMessage());
		}
		log.info("\nTotal SKU= " + editionCount + " Edition with App Id=" + editionAppCount + " Editions Found= "
		        + ProfilePages.editionFound);
		return new HomePage(driver);
	}

	public void gotoEditionProfile(HashMap<String, Editions> editions) {
		int editionCount = 0, editionAppCount = 0;
		try {
			for (Map.Entry<String, Editions> edition : PricesFromGoogleSheet.skus.entrySet()) {
				editionCount++;

				if (!(edition.getValue().getappId().equals("NA") || edition.getValue().getappId().equals("Conflict"))) {
					editionAppCount++;
					Reporter.log("\n\n" + edition.getValue().getappId(), true);
					log.info("\n" + editionAppCount + "\tOpening: " + edition.getValue().getEditionName()
					        + "\nOffer id:" + edition.getValue().getOfferId());
					new ProfilePages(this.driver, edition.getValue()).openLink(edition.getValue().getappId())
					        .EditionPricing();
				}
			}
		} catch (Exception e) {
			log.exceptionlog("Skipped: ");
		}
		log.info("\nTotal SKU= " + editionCount + " Edition with App Id=" + editionAppCount + " Editions Found= "
		        + ProfilePages.editionFound);
	}
}
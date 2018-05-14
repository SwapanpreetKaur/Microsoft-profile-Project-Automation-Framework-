package com.appdirect.pages;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import com.appdirect.googleAccess.EditionPriceDomain;
import com.appdirect.googleAccess.Editions;
import com.appdirect.utils.DriverUtils;

import reporter.Reporterlog;

public class ProfilePages {
    private WebDriver driver;
    private Reporterlog log;
    private Editions edition;
    private Properties properties;
    public static int editionFound = 0;
    public static int addonFound = 0;
    private List<WebElement> dropDownEditions;
    public static List<EditionPriceDomain> differentPricesLists = new ArrayList<EditionPriceDomain>();
    public static List<EditionPriceDomain> samePricesLists = new ArrayList<EditionPriceDomain>();
    DriverUtils util;

    public ProfilePages(WebDriver driver, Editions edition) {
        this.driver = driver;
        this.log = new Reporterlog();
        this.edition = edition;
        util = new DriverUtils(driver);
        properties = util.getProperty("resources/res-pages.properties");
    }
    public void AddOnPricing() {
        try {
            util.getElement(properties.getProperty("clickpricingforaddon1")).click();
            String id = util.getElement(properties.getProperty("editionCode")).getAttribute("value");
            if (id.equals(edition.getOfferId())) {
                addonFound++;
                String usdPrice = getPrice("USD");
                String audPrice = getPrice("AUD");
                String eurPrice = getPrice("EUR");
                String gbpPrice = getPrice("GBP");
                this.showPrice(usdPrice, eurPrice, gbpPrice, audPrice);
            } else {
                log.warning("Id not matching");
            }
        } catch (Exception e) {
            log.warning("Pricing not clicked :: " + e.getLocalizedMessage());
        }
    }

    public void addToList(List<EditionPriceDomain> requiredList, String... price) {
        EditionPriceDomain pricingForFoundEditions = new EditionPriceDomain();
        pricingForFoundEditions.setEditionName(edition.getEditionName());
        pricingForFoundEditions.setOfferid(edition.getOfferId());
        pricingForFoundEditions.setSkutype(edition.getSkuType());
        this.setPrice(pricingForFoundEditions, price[0], price[1], price[2], price[3]);
        requiredList.add(pricingForFoundEditions);
    }

    public void EditionPricing() {
        int count = 0;
        try {
            util.getElement(properties.getProperty("clickEditions")).click();
            dropDownEditions = util.getElements(properties.getProperty("dropDownEditions"));
            for (WebElement dropDownEdition : dropDownEditions) {
                if (dropDownEdition.getText().equals(edition.getEditionName())) {
                    log.info(dropDownEdition.getText());
                    dropDownEdition.click();
                    String id = util.getElement(properties.getProperty("editionCode")).getAttribute("value");
                    if (id.equals(edition.getOfferId())) {
                        Reporter.log("Id matching", true);
                        editionFound++;
                        count++;
                        String usdPrice = getPrice("USD");
                        String audPrice = getPrice("AUD");
                        String eurPrice = getPrice("EUR");
                        String gbpPrice = getPrice("GBP");
                        this.showPrice(usdPrice, eurPrice, gbpPrice, audPrice);
                    } else {
                        log.warning("Id not matching");
                    }
                    break;
                }
            }
        } catch (Exception e) {
            log.exceptionlog("Skipped: Page Load Failure" + e.getLocalizedMessage());
        }
        if (count == 0) {
            log.warning("Edition Not Found");
        }
    }

    public String getPrice(String currency) {
        return util.getElement(properties.getProperty("price"), currency).getAttribute("value");
    }

    public void updatePrice(String newPrice, String currency) {
        util.getElement(properties.getProperty("price"), currency).clear();
        util.getElement(properties.getProperty("price"), currency).sendKeys(newPrice);
    }

    public void showPrice(String... price) {
        boolean flag = true;
        String report = "Prices are changed for ";
        log.info("USD\t[OLD VALUE] : " + price[0] + "  [NEW VALUE] :" + edition.getusdPrice());
        log.info("EUR\t[OLD VALUE] : " + price[1] + "  [NEW VALUE] :" + edition.geteurPrice());
        log.info("GBP\t[OLD VALUE] : " + price[2] + "  [NEW VALUE] :" + edition.getgbpPrice());
        log.info("AUD\t[OLD VALUE] : " + price[3] + "  [NEW VALUE] :" + edition.getaudPrice());
        if (Double.parseDouble(price[0]) - Double.parseDouble(edition.getusdPrice()) != 0) {
            report = report + "USD,";
            updatePrice(edition.getusdPrice(), "USD");
            flag = false;
        }
        if (Double.parseDouble(price[1]) - Double.parseDouble(edition.geteurPrice()) != 0) {
            report = report + "EUR,";
            updatePrice(edition.geteurPrice(), "EUR");
            flag = false;
        }
        if (Double.parseDouble(price[2]) - Double.parseDouble(edition.getgbpPrice()) != 0) {
            report = report + "GBP,";
            updatePrice(edition.getgbpPrice(), "GBP");
            flag = false;
        }
        if (Double.parseDouble(price[3]) - Double.parseDouble(edition.getaudPrice()) != 0) {
            report = report + "AUD";
            updatePrice(edition.getaudPrice(), "AUD");
            flag = false;
        }
        if (flag) {
            log.info("Prices are not changed");
            this.addToList(samePricesLists, price[0], price[1], price[2], price[3]);
        } else {
            log.info(report);
            this.addToList(differentPricesLists, price[0], price[1], price[2], price[3]);
            util.getElement(properties.getProperty("clickSaveButton")).click();
            util.waitFor(properties.getProperty("clickSaveButton"), 10);
        }
    }

    public ProfilePages setPrice(EditionPriceDomain priceForFoundEditions, String... price) {
        priceForFoundEditions.setOldaudPrice(price[3]);
        priceForFoundEditions.setOldeurPrice(price[1]);
        priceForFoundEditions.setOldusdPrice(price[0]);
        priceForFoundEditions.setOldgbpPrice(price[2]);
        priceForFoundEditions.setNewaudPrice(edition.getaudPrice());
        priceForFoundEditions.setNeweurPrice(edition.geteurPrice());
        priceForFoundEditions.setNewusdPrice(edition.getusdPrice());
        priceForFoundEditions.setNewgbpPrice(edition.getgbpPrice());
        return new ProfilePages(driver, edition);
    }

    public ProfilePages openLink(String getappId) {
        if (DriverUtils.platform.equals("orchard"))
            driver.navigate().to(properties.getProperty("orchardmarketplaceEditUrl") + getappId + "?");
        else
            driver.navigate().to(properties.getProperty("marketplaceEditUrl") + getappId + "?");
        
        util.waitForJSandJQueryToLoad();
        return new ProfilePages(this.driver, edition);
    }

    public ProfilePages editionDisplay(List<EditionPriceDomain> list) {
        Reporter.log("Total rows=" + list.size() + "\n", true);
        int count = 0;
        for (EditionPriceDomain ed : list) {
            log.info(count + "\t" + ed.getEditionName() + "\t" + ed.getOfferid() + "\t" + ed.getOldaudPrice() + "\t"
                    + ed.getNewaudPrice() + "\t" + ed.getOldusdPrice() + "\t" + ed.getNewusdPrice() + "\t"
                    + ed.getOldgbpPrice() + "\t" + ed.getNewgbpPrice() + "\t" + ed.getOldeurPrice() + "\t"
                    + ed.getNeweurPrice());
            count++;
        }
        return new ProfilePages(driver, edition);
    }
}
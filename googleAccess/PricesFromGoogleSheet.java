package com.appdirect.googleAccess;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.testng.Reporter;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.util.ServiceException;

import reporter.CsvReporter;
import reporter.Reporterlog;

public class PricesFromGoogleSheet {
	public static HashMap<String, Editions> skus = new HashMap<String, Editions>();
	private HashMap<String, Editions> editions = new HashMap<String, Editions>();
	private HashMap<String, Editions> addons = new HashMap<String, Editions>();
	java.util.Properties properties;
	CsvReporter csvReporter;
	Reporterlog log;

	public PricesFromGoogleSheet() {
		csvReporter = new CsvReporter();
		log = new Reporterlog();
		properties = csvReporter.getProperty("resources/res-utils.properties");
	}

	public PricesFromGoogleSheet getusdPricefromsheet() throws IOException, ServiceException {
		SpreadsheetService service = new SpreadsheetService("sdaa");
		URL url = new URL(getsheetUrl(properties.getProperty("sheeturl"), "1"));
		ListFeed lf = null;
		lf = service.getFeed(url, ListFeed.class);
		for (ListEntry le : lf.getEntries()) {
			CustomElementCollection cec = le.getCustomElements();
			Editions edition = new Editions();
			edition.setEditionName(cec.getValue("OfferDisplayName"));
			edition.setOfferId(cec.getValue("OfferID"));
			edition.setSkuType(cec.getValue("SecondaryLicenseType"));
			edition.setusdPrice(cec.getValue("ERPPrice"));
			edition.setappId(cec.getValue("OrchidAppId"));
			skus.put(cec.getValue("OfferID"), edition);
		}
		return new PricesFromGoogleSheet();
	}

	public String getsheetUrl(String... urls) {
		if (urls.length == 2)
			urls[0] = urls[0].replace("%p", urls[1]);

		return urls[0];
	}

	public PricesFromGoogleSheet getotherPricefromsheet() throws IOException, ServiceException {
		SpreadsheetService service = new SpreadsheetService("sdaa");
		for (int i = 1; i <= 4; i++) {
			URL url = new URL(getsheetUrl(properties.getProperty("sheeturl"), String.valueOf(i)));
			ListFeed lf = null;
			lf = service.getFeed(url, ListFeed.class);
			for (ListEntry le : lf.getEntries()) {
				CustomElementCollection cec = le.getCustomElements();
				Editions edition = skus.get(cec.getValue("OfferID"));
				if (i == 2)
					edition.setaudPrice(cec.getValue("ERPPrice"));
				else if (i == 3)
					edition.seteurPrice(cec.getValue("ERPPrice"));
				else
					edition.setgbpPrice(cec.getValue("ERPPrice"));
			}
		}
		return new PricesFromGoogleSheet();
	}

	public PricesFromGoogleSheet displayapps() throws IOException, ServiceException {
		int count = 1;
		Iterator<Entry<String, Editions>> iterator = skus.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Editions> pair = iterator.next();
			Editions ed = pair.getValue();
			if (!(ed.getappId().equals("NA") || ed.getappId().equals("Conflict"))) {
				Reporter.log(count + "\t" + ed.getEditionName() + "\t" + ed.getOfferId() + "\t" + ed.getusdPrice()
				        + "\t" + ed.geteurPrice() + "\t" + ed.getaudPrice() + "\t" + ed.getgbpPrice() + "\t"
				        + ed.getSkuType() + "\t" + ed.getappId(), true);
				count++;
			}
		}
		return new PricesFromGoogleSheet();
	}

	public HashMap<String, Editions> getAddonList() {
		Iterator<Entry<String, Editions>> iterator = skus.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Editions> pair = iterator.next();
			Editions ed = pair.getValue();
			if (ed.getSkuType().equals("ADDON")) {
				addons.put(ed.getOfferId(), ed);
			} else
				editions.put(ed.getOfferId(), ed);
		}
		return this.addons;
	}

	public HashMap<String, Editions> getEditionsList() {
		return this.editions;
	}

}
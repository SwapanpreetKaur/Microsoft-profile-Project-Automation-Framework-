package com.appdirect.googleAccess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.testng.Reporter;

public class GoogleDriveTest {

	private static final String CLIENT_ID = "743428857413-3afj3le01ai4lbbigg43qt50d0cqmtet.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "sppSalXj0N2sJFwhQn8D4VIx";
	private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
	private static GoogleDriveBasic gd;

	public void insertToDrive() throws IOException{
		gd = new GoogleDriveBasic(CLIENT_ID, CLIENT_SECRET, REDIRECT_URI);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = br.readLine();
		gd.setCode(code);
		String fileID1 = gd.uploadPdfFile("EditionWithDifferentPrices.pdf", "EditionStatusForDifferentPricing.pdf");
		String fileID2 = gd.uploadPdfFile("EditionWithSamePrices.pdf", "EditionStatusForSamePricing.pdf");
		
		Reporter.log("Success"+fileID1+"To Drive",true);

		Reporter.log("Success"+fileID2+"To Drive",true);
	}
}

package com.appdirect.googleAccess;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.testng.Reporter;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.itextpdf.text.DocumentException;

import reporter.PdfReporter;

public class GoogleDriveAccess {

	private static final String APPLICATION_NAME = "Google Sheets API Quickstart";
	
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials/14/drive-java-quickstart.json");
	
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private static HttpTransport HTTP_TRANSPORT;

	private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE);
	
    String finalPDFFile;

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Exception e) {
			Reporter.log("Unable to access drive"+e.getMessage());

		}
	}

	public static Credential authorize() throws IOException {

		InputStream in = GoogleDriveAccess.class.getResourceAsStream("client_secert.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
		        clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		return credential;
	}
	

	public GoogleDriveAccess(String finalPDFFile) {
		this.finalPDFFile=finalPDFFile;
	}
	
	public static Drive getDriveService() throws IOException {
		Credential credential = authorize();
		return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	}
	public PdfReporter insertFile() throws IOException, DocumentException {
		
		Drive service = getDriveService();
		String folderId = "0B4TEMo6MLUPjTXJrZFBwZDBlOFU";
		File fileMetadata = new File();

		fileMetadata.setName("EditionStatus_"+finalPDFFile+".pdf");
		fileMetadata.setMimeType("application/pdf");
		fileMetadata.setParents(Collections.singletonList(folderId));
		java.io.File filePath = new java.io.File(finalPDFFile);
		FileContent mediaContent = new FileContent("application/pdf", filePath);
		File file = service.files().create(fileMetadata, mediaContent)
		        		   .setFields("id, parents")
		        		   .execute();
		Reporter.log("File ID: " + file.getId(),true);
		return new PdfReporter();
	} 
}

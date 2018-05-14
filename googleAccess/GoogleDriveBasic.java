package com.appdirect.googleAccess;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

public class GoogleDriveBasic {
	private String redirectURI;
	HttpTransport httpTransport;
	JsonFactory jsonFactory;
	GoogleAuthorizationCodeFlow flow;
	Drive service;

	public GoogleDriveBasic(String CLIENT_ID, String CLIENT_SECRET, String REDIRECT_URI) {
		this.redirectURI = REDIRECT_URI;
		httpTransport = new NetHttpTransport();
		jsonFactory = new JacksonFactory();
		flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET,
		Arrays.asList(DriveScopes.DRIVE)).setAccessType("online").setApprovalPrompt("auto").build();
	}

	public String getURL() {
		String url = flow.newAuthorizationUrl().setRedirectUri(redirectURI).build();
		return url;
	}

	public void setCode(String code) throws IOException {
		GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
		GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);
		service = new Drive.Builder(httpTransport, jsonFactory, credential).build();
	}

	public String uploadPdfFile(String filePath, String title) throws IOException {
		File body = new File();
		String folderId = "0B4TEMo6MLUPjTXJrZFBwZDBlOFU";
		body.setName(title);
		body.setDescription("Price Update");
		body.setMimeType("application/pdf");
		body.setParents(Collections.singletonList(folderId));
		java.io.File fileContent = new java.io.File(filePath);
		FileContent mediaContent = new FileContent("application/pdf", fileContent);
		File file = service.files().create(body, mediaContent).setFields("id, parents").execute();
		//File file = service.files().create(body, mediaContent).execute();
		return file.getId();
	}

}
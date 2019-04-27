package com.scriptNinja.gmailReader.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.StringUtils;
import com.google.api.client.util.Value;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.Collections;
import java.util.List;

@Component
public class GmailReaderService {

    @Value("${searchQueries.irea}")
    private String ireaQuery;
    @Value("${searchQueries.exel}")
    private String exelQuery;

    @Autowired
    RestTemplate restTemplate;

    private Gmail service;
    private static final String APPLICATION_NAME = "gmailReaderApplication";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.MAIL_GOOGLE_COM);
    private static final String CREDENTIALS_FILE_PATH = "/home/kalyan/ideaProjects/gmailReader/src/main/resources/credentials.json";


    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        File file = new File(CREDENTIALS_FILE_PATH);
        InputStream in = new FileInputStream(file);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private void authenticateUser() throws Exception{
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public String searchEmails(String query) throws Exception{
        String searchQuery = "from:uber";
        /*if ("IREA".equalsIgnoreCase(query)) {
            searchQuery = ireaQuery;
        }else{
            searchQuery = exelQuery;
        }*/
        authenticateUser();
        ListMessagesResponse response = service.users().messages().list("me").setQ(searchQuery).execute();
        StringBuilder sb = new StringBuilder();
        if(response.getMessages() != null){
            for(Message m : response.getMessages()){
                Message message = service.users().messages().get("me", m.getId()).execute();
                String received = message.getPayload().getHeaders().get(1).getValue();
                String[] smtpIdAndTime = received.split(";");
                String dateTime = smtpIdAndTime[1].trim();
                sb.append(dateTime);
                sb.append(" ");
                sb.append(message.getSnippet());
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    public List<Label> readLabels() throws Exception{

        authenticateUser();
        // Print the labels in the user's account.
        String user = "me";
        ListLabelsResponse listResponse = service.users().labels().list(user).execute();
        List<Label> labels = listResponse.getLabels();
        if (labels.isEmpty()) {
            System.out.println("No labels found.");
        } else {
            System.out.println("Labels:");
            for (Label label : labels) {
                System.out.printf("- %s\n", label.getName());
            }
        }

        return labels;

    }


}

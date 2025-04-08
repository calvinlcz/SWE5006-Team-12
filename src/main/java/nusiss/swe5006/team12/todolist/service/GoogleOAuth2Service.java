package nusiss.swe5006.team12.todolist.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import nusiss.swe5006.team12.todolist.domain.Task;

public class GoogleOAuth2Service implements IOAuth2StrategyService {

    private final String TOKENS_DIRECTORY_PATH = "tokens"; // Directory to store user tokens
    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final String APPLICATION_NAME = "Google Calendar API Java Quickstart";

    {
        Path tokensDir = Path.of(TOKENS_DIRECTORY_PATH);
        if (Files.notExists(tokensDir)) {
            try {
                Files.createDirectories(tokensDir); // Creates the directory if it doesn't exist
            } catch (IOException e) {
                // Handle the exception as needed, maybe log it or rethrow
                e.printStackTrace();
            }
        }
    }

    // Build Google OAuth client flow
    private GoogleAuthorizationCodeFlow getGoogleAuthorizationCodeFlow() throws IOException {
        InputStream inputStream = GoogleOAuth2Service.class.getResourceAsStream("/google_client_secret.json");

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream));

        try {
            return new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientSecrets,
                Collections.singleton(CalendarScopes.CALENDAR)
            )
                .setDataStoreFactory(new FileDataStoreFactory(Path.of(TOKENS_DIRECTORY_PATH).toFile()))
                .setAccessType("offline")
                .build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    // Authorize and obtain a Credential object
    private Credential authorize() throws Exception {
        GoogleAuthorizationCodeFlow flow = getGoogleAuthorizationCodeFlow();
        String authorizationUrl = flow.newAuthorizationUrl().setRedirectUri("http://localhost:60554/Callback").build();
        Desktop.getDesktop().browse(new URI(authorizationUrl));
        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
            .setPort(60554) // Match this with your Google OAuth redirect URI
            .setCallbackPath("/Callback") // Ensure this matches the redirect URI in Google Cloud settings
            .build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    // Create a new Google Calendar event
    @Override
    public void addEvent(Task task) throws Exception {
        Credential credential = authorize();
        Calendar service = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
            .setApplicationName(APPLICATION_NAME)
            .build();

        Event event = new Event()
            .setSummary(task.getName())
            .setDescription(task.getDescription())
            .setStart(
                new EventDateTime()
                    .setDateTime(new DateTime(task.getDateCreated().atStartOfDay().toInstant(ZoneOffset.of("+08:00")).toString()))
            )
            .setEnd(
                new EventDateTime()
                    .setDateTime(new DateTime(task.getDueDate().atStartOfDay().toInstant(ZoneOffset.of("+08:00")).toString()))
            );

        service.events().insert("primary", event).execute();
        System.out.println("Event added to Google Calendar: " + task.getName());
    }
}

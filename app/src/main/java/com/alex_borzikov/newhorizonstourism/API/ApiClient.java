package com.alex_borzikov.newhorizonstourism.API;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.alex_borzikov.newhorizonstourism.API.ConnectionModes.GET_QUESTS_LIST;

public class ApiClient {

    private static final String TAG = "Borlehandro";

    public static final Map<String, Integer> MODES;

    static {
        Map<String, Integer> proxyMap = new HashMap<>();
        proxyMap.put("REGISTER", 1);
        proxyMap.put("LOGIN", 2);
        proxyMap.put("GET_QUESTS_LIST", 3);
        proxyMap.put("GET_QUEST_INFO", 4);
        proxyMap.put("GET_POINT_INFO", 5);
        proxyMap.put("CHECK_POINT_CODE", 6);
        proxyMap.put("GET_TASKS_INFO", 7);
        proxyMap.put("CHECK_ANSWER", 8);
        MODES = Collections.unmodifiableMap(proxyMap);
    }

    private static final String url = "https://new-horizons-tourism.herokuapp.com/";

    public static String getGuestList() throws IOException {

        HttpURLConnection connection = connect();

        OutputStream data = connection.getOutputStream();

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(data, StandardCharsets.UTF_8));

        writer.write("mode="+MODES.get("GET_GUESTS_LIST"));
        writer.flush();
        writer.close();

        InputStream content = connection.getInputStream();


        // Todo REFACTOR! IT'S COPY-PASTE CODE!
        String line;
        //connection.
        BufferedReader in = new BufferedReader(new InputStreamReader(content));
        StringBuilder outputStringBuilder = new StringBuilder();

        while ((line = in.readLine()) != null) {
            outputStringBuilder.append(line);
        }

        Log.d(TAG, "Login Client must return: " + outputStringBuilder.toString());
        return outputStringBuilder.toString();
    }

    public static String login(String username, String password) throws IOException {
        HttpURLConnection connection = connect();

        OutputStream data = connection.getOutputStream();

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(data, StandardCharsets.UTF_8));

        writer.write("mode="+MODES.get("LOGIN"));
        writer.write("&username="+username);
        writer.write("&password="+password);
        writer.flush();
        writer.close();

        InputStream content = connection.getInputStream();

        // Todo REFACTOR! IT'S COPY-PASTE CODE!
        String line;
        //connection.
        BufferedReader in = new BufferedReader(new InputStreamReader(content));
        StringBuilder outputStringBuilder = new StringBuilder();

        while ((line = in.readLine()) != null) {
            outputStringBuilder.append(line);
        }

        Log.d(TAG, "Login Client must return: " + outputStringBuilder.toString());
        return outputStringBuilder.toString();
    }

    public static String register(String username, String password, String language) throws IOException {

        HttpURLConnection connection = connect();

        OutputStream data = connection.getOutputStream();

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(data, StandardCharsets.UTF_8));

        writer.write("mode="+MODES.get("REGISTER"));
        writer.write("&username="+username);
        writer.write("&password="+password);
        writer.write("&language="+language);
        writer.flush();
        writer.close();

        InputStream content = connection.getInputStream();

        // Todo REFACTOR! IT'S COPY-PASTE CODE!
        String line;
        //connection.
        BufferedReader in = new BufferedReader(new InputStreamReader(content));
        StringBuilder outputStringBuilder = new StringBuilder();

        while ((line = in.readLine()) != null) {
            outputStringBuilder.append(line);
        }

        Log.d(TAG, "Login Client must return: " + outputStringBuilder.toString());
        return outputStringBuilder.toString();
    }

    private static HttpURLConnection connect() throws IOException {
        Log.d(TAG, "Execute client");
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        return connection;
    }
}
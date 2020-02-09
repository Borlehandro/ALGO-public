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
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.alex_borzikov.newhorizonstourism.API.ConnectionModes.GET_QUESTS_LIST;

public class ApiClient {

    private static final String url = "https://new-horizons-tourism.herokuapp.com/";

    public static String getGuestList() throws IOException {

        Log.d("TAG", "Execute client");
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");

        OutputStream data = connection.getOutputStream();

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(data, StandardCharsets.UTF_8));

        writer.write("mode="+GET_QUESTS_LIST.getValue());
        writer.flush();
        writer.close();

        InputStream content = connection.getInputStream();

        String line;
        //connection.
        BufferedReader in = new BufferedReader(new InputStreamReader(content));
        StringBuilder outputStringBuilder = new StringBuilder();

        while ((line = in.readLine()) != null) {
            outputStringBuilder.append(line);
        }

        Log.d("TAG", "Login Client must return: " + outputStringBuilder.toString());
        return outputStringBuilder.toString();
    }
}
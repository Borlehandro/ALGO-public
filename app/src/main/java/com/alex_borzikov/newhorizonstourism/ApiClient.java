package com.alex_borzikov.newhorizonstourism;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiClient {

    private URL url = null;

    private static final ApiClient instance = new ApiClient();

    public static ApiClient getInstance(){
        return instance;
    }

    private ApiClient() {
        try {
            this.url = new URL("https://new-horizons-tourism.herokuapp.com/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String connect() throws IOException {

        Log.d("TAG", "Execute client");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

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
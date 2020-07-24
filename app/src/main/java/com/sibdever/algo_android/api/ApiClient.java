package com.sibdever.algo_android.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.Map;

public class ApiClient {

    private static final String TAG = "Sibdever";

    // Release
    private static final String SERVER_URL = "http://Algo-env.eba-iaghw6qa.eu-west-2.elasticbeanstalk.com";

    // Debug
    // private static final String SERVER_URL = "http://algo-data.herokuapp.com/";

    public static String send(Command command) {

        try {
            HttpURLConnection connection = connectPost(command.getPath());

            connection.setDoOutput(true);
            connection.setDoInput(true);

            Log.d(TAG, "send: " + connection.getURL());

            OutputStream data = connection.getOutputStream();

            // BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(data, StandardCharsets.UTF_8));

            for(Map.Entry<String, String> item : command.getArguments().entrySet()) {
                writer.write(item.getKey() + "=" + item.getValue());
                System.err.print(item.getKey() + "=" + item.getValue());
                writer.write("&");
                System.err.print("&");
            }
            System.err.println();

            writer.flush();
            writer.close();
            // errorReader.lines().forEach(line -> Log.e(TAG, line));

            InputStream content = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            StringBuilder builder;
            builder = reader.lines().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);
            reader.close();

            connection.disconnect();
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getImage(String dir) throws IOException {

        dir = "https://algo-project.herokuapp.com/pic/map.png";

        HttpURLConnection connection = connectGet(dir);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        InputStream content = connection.getInputStream();

        Bitmap inputBitmap = BitmapFactory.decodeStream(content);

        Log.d(TAG, "Login Client must return: " + inputBitmap);
        Log.d("Borlehandro", String.valueOf(inputBitmap==null));
        return inputBitmap;
    }

    private static HttpURLConnection connectPost(String path) throws IOException {
        Log.d(TAG, "Execute POST");

        HttpURLConnection connection = (HttpURLConnection) new URL(SERVER_URL + path).openConnection();
        connection.setRequestMethod("POST");
        return connection;
    }

    private static HttpURLConnection connectGet(String dir) throws IOException {
        Log.d(TAG, "Execute GET");
        // System.out.println(SERVER_URL + dir);
        System.out.println(dir);
        HttpURLConnection connection = (HttpURLConnection) new URL(dir).openConnection();
        connection.setRequestMethod("GET");
        System.out.println(connection);
        return connection;
    }
}
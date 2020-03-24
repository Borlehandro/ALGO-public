package com.alex_borzikov.newhorizonstourism.api;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
        proxyMap.put("GET_POINTS_QUEUE", 9);

        MODES = Collections.unmodifiableMap(proxyMap);
    }

    private static final String SERVER_URL = "https://algo-project.herokuapp.com/";

    public static String getGuestList(String language) throws IOException {

        HttpURLConnection connection = connectPost();

        connection.setDoOutput(true);

        OutputStream data = connection.getOutputStream();

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(data, StandardCharsets.UTF_8));

        writer.write("mode=" + MODES.get("GET_QUESTS_LIST"));
        writer.write("&language=" + language);

        Log.d(TAG, "Send lang " + language);
        System.out.println( "Send lang " + language);
        System.out.println("Send mode " + MODES.get("GET_QUESTS_LIST"));
        Log.d(TAG, "Send mode " + MODES.get("GET_QUESTS_LIST"));

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
        System.out.println("Login Client must return: " + outputStringBuilder.toString());
        return outputStringBuilder.toString();
    }

    public static String login(String username, String password) throws IOException {
        HttpURLConnection connection = connectPost();

        OutputStream data = connection.getOutputStream();

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(data, StandardCharsets.UTF_8));

        writer.write("mode=" + MODES.get("LOGIN"));
        writer.write("&username=" + username);
        writer.write("&password=" + password);
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

        HttpURLConnection connection = connectPost();

        OutputStream data = connection.getOutputStream();

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(data, StandardCharsets.UTF_8));

        writer.write("mode=" + MODES.get("REGISTER"));
        writer.write("&username=" + username);
        writer.write("&password=" + password);
        writer.write("&language=" + language);
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

    public static String getQuestInfo(String questId, String language) throws IOException {

        HttpURLConnection connection = connectPost();
        connection.setDoOutput(true);

        OutputStream data = connection.getOutputStream();

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(data, StandardCharsets.UTF_8));

        writer.write("mode=" + MODES.get("GET_QUEST_INFO"));
        writer.write("&language=" + language);
        writer.write("&quest_id=" + questId);
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

    public static String getPointsQueue(String questId, String language) throws IOException {

        HttpURLConnection connection = connectPost();

        connection.setDoOutput(true);

        OutputStream data = connection.getOutputStream();

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(data, StandardCharsets.UTF_8));

        writer.write("mode=" + MODES.get("GET_POINTS_QUEUE"));
        writer.write("&language=" + language);
        writer.write("&quest_id=" + questId);

        Log.d(TAG, "Send lang " + language);
        System.out.println( "Send lang " + language);

        Log.d(TAG, "Send questId " + questId);
        System.out.println( "Send questId " + questId);

        System.out.println("Send mode " + MODES.get("GET_QUESTS_LIST"));
        Log.d(TAG, "Send mode " + MODES.get("GET_QUESTS_LIST"));

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
        System.out.println("Login Client must return: " + outputStringBuilder.toString());
        return outputStringBuilder.toString();
    }


    public static String getPointInfo(String pointId, String language) throws IOException {

        HttpURLConnection connection = connectPost();
        connection.setDoOutput(true);

        OutputStream data = connection.getOutputStream();

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(data, StandardCharsets.UTF_8));

        writer.write("mode=" + MODES.get("GET_POINT_INFO"));
        writer.write("&language=" + language);
        writer.write("&point_id=" + pointId);

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

    public static String getTaskInfo(String taskId, String language, String userTicket) throws IOException {

        HttpURLConnection connection = connectPost();
        connection.setDoOutput(true);

        OutputStream data = connection.getOutputStream();

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(data, StandardCharsets.UTF_8));

        writer.write("mode=" + MODES.get("GET_TASKS_INFO"));
        writer.write("&language=" + language);
        writer.write("&task=" + taskId);
        writer.write("&userTicket=" + userTicket);

        Log.d(TAG, (MODES.get("GET_TASKS_INFO") + " " + language + " " + taskId
                + " " + userTicket ));

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

    public static String checkPointCode(String code) throws IOException {

        HttpURLConnection connection = connectPost();
        connection.setDoOutput(true);

        OutputStream data = connection.getOutputStream();

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(data, StandardCharsets.UTF_8));

        writer.write("mode=" + MODES.get("CHECK_POINT_CODE"));
        writer.write("&code=" + code);

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

    public static String checkTaskAnswer(String answerIndex, String taskId, String userTicket) throws IOException {
        HttpURLConnection connection = connectPost();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        OutputStream data = connection.getOutputStream();

        Log.d(TAG, "checkTaskAnswer: " + MODES.get("CHECK_ANSWER")  + " " +answerIndex + " "
                + taskId + " " + userTicket);

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(data, StandardCharsets.UTF_8));

        writer.write("mode=" + MODES.get("CHECK_ANSWER"));
        writer.write("&answer=" + answerIndex);
        writer.write("&task=" + taskId);
        writer.write("&userTicket=" + userTicket);

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

        // Todo change it!
        return outputStringBuilder.toString();
    }

    public static Bitmap getImage(String dir) throws IOException {

        HttpURLConnection connection = connectGet(dir);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        InputStream content = connection.getInputStream();

        Bitmap inputBitmap = BitmapFactory.decodeStream(content);

        Log.d(TAG, "Login Client must return: " + inputBitmap);
        Log.d("Borlehandro", String.valueOf(inputBitmap==null));
        return inputBitmap;
    }

    public static StringBuffer getDescription(String dir) throws IOException {

        HttpURLConnection connection = connectGet(dir);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        Log.d(TAG, "getDescription: Connection " + connection);

        BufferedReader content = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuffer sb = new StringBuffer();
        String str;

        while((str = content.readLine())!= null){
            sb.append(str);
        }

        Log.d(TAG, "Login Client must return: " + sb.toString());

        return sb;
    }

    private static HttpURLConnection connectPost() throws IOException {
        Log.d(TAG, "Execute POST");

        HttpURLConnection connection = (HttpURLConnection) new URL(SERVER_URL).openConnection();
        connection.setRequestMethod("POST");
        return connection;
    }

    private static HttpURLConnection connectGet(String dir) throws IOException {
        Log.d(TAG, "Execute GET");
        System.out.println(SERVER_URL + dir);
        HttpURLConnection connection = (HttpURLConnection) new URL(SERVER_URL + dir).openConnection();
        connection.setRequestMethod("GET");
        System.out.println(connection);
        return connection;
    }
}
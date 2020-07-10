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

//    public static String getGuestList(String language, String userTicket) throws IOException {
//
//        HttpURLConnection connection = connectPost();
//
//        connection.setDoOutput(true);
//
//        OutputStream data = connection.getOutputStream();
//
//        BufferedWriter writer = new BufferedWriter(
//                new OutputStreamWriter(data, StandardCharsets.UTF_8));
//
//        writer.write("mode=" + MODES.get("GET_QUESTS_LIST"));
//        writer.write("&language=" + language);
//        writer.write("&userTicket=" + userTicket);
//
//        Log.d(TAG, "Send lang " + language);
//        Log.d(TAG, "Send mode " + MODES.get("GET_QUESTS_LIST"));
//
//        writer.flush();
//        writer.close();
//
//        InputStream content = connection.getInputStream();
//
//        // Todo REFACTOR! IT'S COPY-PASTE CODE!
//        String line;
//        //connection.
//        BufferedReader in = new BufferedReader(new InputStreamReader(content));
//        StringBuilder outputStringBuilder = new StringBuilder();
//
//        while ((line = in.readLine()) != null) {
//            outputStringBuilder.append(line);
//        }
//
//        Log.d(TAG, "Login Client must return: " + outputStringBuilder.toString());
//        return outputStringBuilder.toString();
//    }
//
//    public static String login(String username, String password) throws IOException {
//        HttpURLConnection connection = connectPost();
//
//        OutputStream data = connection.getOutputStream();
//
//        BufferedWriter writer = new BufferedWriter(
//                new OutputStreamWriter(data, StandardCharsets.UTF_8));
//
//        writer.write("mode=" + MODES.get("LOGIN"));
//        writer.write("&username=" + username);
//        writer.write("&password=" + password);
//        writer.flush();
//        writer.close();
//
//        InputStream content = connection.getInputStream();
//
//        // Todo REFACTOR! IT'S COPY-PASTE CODE!
//        String line;
//        //connection.
//        BufferedReader in = new BufferedReader(new InputStreamReader(content));
//        StringBuilder outputStringBuilder = new StringBuilder();
//
//        while ((line = in.readLine()) != null) {
//            outputStringBuilder.append(line);
//        }
//
//        Log.d(TAG, "Login Client must return: " + outputStringBuilder.toString());
//        return outputStringBuilder.toString();
//    }
//
//    public static String register(String username, String password, String language) throws IOException {
//
//        HttpURLConnection connection = connectPost();
//
//        OutputStream data = connection.getOutputStream();
//
//        BufferedWriter writer = new BufferedWriter(
//                new OutputStreamWriter(data, StandardCharsets.UTF_8));
//
//        writer.write("mode=" + MODES.get("REGISTER"));
//        writer.write("&username=" + username);
//        writer.write("&password=" + password);
//        writer.write("&language=" + language);
//        writer.flush();
//        writer.close();
//
//        InputStream content = connection.getInputStream();
//
//        // Todo REFACTOR! IT'S COPY-PASTE CODE!
//        String line;
//        //connection.
//        BufferedReader in = new BufferedReader(new InputStreamReader(content));
//        StringBuilder outputStringBuilder = new StringBuilder();
//
//        while ((line = in.readLine()) != null) {
//            outputStringBuilder.append(line);
//        }
//
//        Log.d(TAG, "Login Client must return: " + outputStringBuilder.toString());
//        return outputStringBuilder.toString();
//    }
//
//    public static String getQuestInfo(String questId, String language) throws IOException {
//
//        HttpURLConnection connection = connectPost();
//        connection.setDoOutput(true);
//
//        OutputStream data = connection.getOutputStream();
//
//        BufferedWriter writer = new BufferedWriter(
//                new OutputStreamWriter(data, StandardCharsets.UTF_8));
//
//        writer.write("mode=" + MODES.get("GET_QUEST_INFO"));
//        writer.write("&language=" + language);
//        writer.write("&quest_id=" + questId);
//        writer.flush();
//        writer.close();
//
//        InputStream content = connection.getInputStream();
//
//        // Todo REFACTOR! IT'S COPY-PASTE CODE!
//        String line;
//        //connection.
//        BufferedReader in = new BufferedReader(new InputStreamReader(content));
//        StringBuilder outputStringBuilder = new StringBuilder();
//
//        while ((line = in.readLine()) != null) {
//            outputStringBuilder.append(line);
//        }
//
//        Log.d(TAG, "Login Client must return: " + outputStringBuilder.toString());
//        return outputStringBuilder.toString();
//
//    }
//
//    public static String getPointsQueue(String questId, String language) throws IOException {
//
//        HttpURLConnection connection = connectPost();
//
//        connection.setDoOutput(true);
//
//        OutputStream data = connection.getOutputStream();
//
//        BufferedWriter writer = new BufferedWriter(
//                new OutputStreamWriter(data, StandardCharsets.UTF_8));
//
//        writer.write("mode=" + MODES.get("GET_POINTS_QUEUE"));
//        writer.write("&language=" + language);
//        writer.write("&quest_id=" + questId);
//
//        Log.d(TAG, "Send lang " + language);
//        System.out.println( "Send lang " + language);
//
//        Log.d(TAG, "Send questId " + questId);
//        System.out.println( "Send questId " + questId);
//
//        System.out.println("Send mode " + MODES.get("GET_QUESTS_LIST"));
//        Log.d(TAG, "Send mode " + MODES.get("GET_QUESTS_LIST"));
//
//        writer.flush();
//        writer.close();
//
//        InputStream content = connection.getInputStream();
//
//        // Todo REFACTOR! IT'S COPY-PASTE CODE!
//        String line;
//        //connection.
//        BufferedReader in = new BufferedReader(new InputStreamReader(content));
//        StringBuilder outputStringBuilder = new StringBuilder();
//
//        while ((line = in.readLine()) != null) {
//            outputStringBuilder.append(line);
//        }
//
//        Log.d(TAG, "Login Client must return: " + outputStringBuilder.toString());
//        System.out.println("Login Client must return: " + outputStringBuilder.toString());
//        return outputStringBuilder.toString();
//    }
//
//
//    public static String getPointInfo(String pointId, String language) throws IOException {
//
//        HttpURLConnection connection = connectPost();
//        connection.setDoOutput(true);
//
//        OutputStream data = connection.getOutputStream();
//
//        BufferedWriter writer = new BufferedWriter(
//                new OutputStreamWriter(data, StandardCharsets.UTF_8));
//
//        writer.write("mode=" + MODES.get("GET_POINT_INFO"));
//        writer.write("&language=" + language);
//        writer.write("&point_id=" + pointId);
//
//        writer.flush();
//        writer.close();
//
//        InputStream content = connection.getInputStream();
//
//        // Todo REFACTOR! IT'S COPY-PASTE CODE!
//        String line;
//        //connection.
//        BufferedReader in = new BufferedReader(new InputStreamReader(content));
//        StringBuilder outputStringBuilder = new StringBuilder();
//
//        while ((line = in.readLine()) != null) {
//            outputStringBuilder.append(line);
//        }
//
//        Log.d(TAG, "Login Client must return: " + outputStringBuilder.toString());
//        return outputStringBuilder.toString();
//
//    }
//
//    public static String getTaskInfo(String taskId, String language, String userTicket) throws IOException {
//
//        HttpURLConnection connection = connectPost();
//        connection.setDoOutput(true);
//
//        OutputStream data = connection.getOutputStream();
//
//        BufferedWriter writer = new BufferedWriter(
//                new OutputStreamWriter(data, StandardCharsets.UTF_8));
//
//        writer.write("mode=" + MODES.get("GET_TASKS_INFO"));
//        writer.write("&language=" + language);
//        writer.write("&task=" + taskId);
//        writer.write("&userTicket=" + userTicket);
//
//        Log.d(TAG, (MODES.get("GET_TASKS_INFO") + " " + language + " " + taskId
//                + " " + userTicket ));
//
//        writer.flush();
//        writer.close();
//
//        InputStream content = connection.getInputStream();
//
//        // Todo REFACTOR! IT'S COPY-PASTE CODE!
//        String line;
//        //connection.
//        BufferedReader in = new BufferedReader(new InputStreamReader(content));
//        StringBuilder outputStringBuilder = new StringBuilder();
//
//        while ((line = in.readLine()) != null) {
//            outputStringBuilder.append(line);
//        }
//
//        Log.d(TAG, "Login Client must return: " + outputStringBuilder.toString());
//        return outputStringBuilder.toString();
//
//    }
//
//    public static String checkPointCode(String code) throws IOException {
//
//        HttpURLConnection connection = connectPost();
//        connection.setDoOutput(true);
//
//        OutputStream data = connection.getOutputStream();
//
//        BufferedWriter writer = new BufferedWriter(
//                new OutputStreamWriter(data, StandardCharsets.UTF_8));
//
//        writer.write("mode=" + MODES.get("CHECK_POINT_CODE"));
//        writer.write("&code=" + code);
//
//        writer.flush();
//        writer.close();
//
//        InputStream content = connection.getInputStream();
//
//        // Todo REFACTOR! IT'S COPY-PASTE CODE!
//        String line;
//        //connection.
//        BufferedReader in = new BufferedReader(new InputStreamReader(content));
//        StringBuilder outputStringBuilder = new StringBuilder();
//
//        while ((line = in.readLine()) != null) {
//            outputStringBuilder.append(line);
//        }
//
//        Log.d(TAG, "Login Client must return: " + outputStringBuilder.toString());
//
//        return outputStringBuilder.toString();
//    }
//
//    public static String checkTaskAnswer(String answerIndex, String taskId, String userTicket) throws IOException {
//        HttpURLConnection connection = connectPost();
//        connection.setDoOutput(true);
//        connection.setDoInput(true);
//        OutputStream data = connection.getOutputStream();
//
//        Log.d(TAG, "checkTaskAnswer: " + MODES.get("CHECK_ANSWER")  + " " +answerIndex + " "
//                + taskId + " " + userTicket);
//
//        BufferedWriter writer = new BufferedWriter(
//                new OutputStreamWriter(data, StandardCharsets.UTF_8));
//
//        writer.write("mode=" + MODES.get("CHECK_ANSWER"));
//        writer.write("&answer=" + answerIndex);
//        writer.write("&task=" + taskId);
//        writer.write("&userTicket=" + userTicket);
//
//        writer.flush();
//        writer.close();
//
//        InputStream content = connection.getInputStream();
//
//        // Todo REFACTOR! IT'S COPY-PASTE CODE!
//        String line;
//        //connection.
//        BufferedReader in = new BufferedReader(new InputStreamReader(content));
//        StringBuilder outputStringBuilder = new StringBuilder();
//
//        while ((line = in.readLine()) != null) {
//            outputStringBuilder.append(line);
//        }
//
//        Log.d(TAG, "Login Client must return: " + outputStringBuilder.toString());
//
//        // Todo change it!
//        return outputStringBuilder.toString();
//    }
//
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
//
//    public static StringBuffer getDescription(String dir) throws IOException {
//
//        HttpURLConnection connection = connectGet(dir);
//        connection.setDoInput(true);
//        connection.setDoOutput(true);
//        Log.d(TAG, "getDescription: Connection " + connection);
//
//        BufferedReader content = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//
//        StringBuffer sb = new StringBuffer();
//        String str;
//
//        while((str = content.readLine())!= null){
//            sb.append(str);
//        }
//
//        Log.d(TAG, "Login Client must return: " + sb.toString());
//
//        return sb;
//    }
//
//    public static String getUserInfo(String userTicket) throws IOException {
//
//        HttpURLConnection connection = connectPost();
//        connection.setDoOutput(true);
//
//        OutputStream data = connection.getOutputStream();
//
//        BufferedWriter writer = new BufferedWriter(
//                new OutputStreamWriter(data, StandardCharsets.UTF_8));
//
//        writer.write("mode=" + MODES.get("GET_USER_INFO"));
//        writer.write("&userTicket=" + userTicket);
//
//        writer.flush();
//        writer.close();
//
//        InputStream content = connection.getInputStream();
//
//        // Todo REFACTOR! IT'S COPY-PASTE CODE!
//        String line;
//        //connection.
//        BufferedReader in = new BufferedReader(new InputStreamReader(content));
//        StringBuilder outputStringBuilder = new StringBuilder();
//
//        while ((line = in.readLine()) != null) {
//            outputStringBuilder.append(line);
//        }
//
//        Log.d(TAG, "Login Client must return: " + outputStringBuilder.toString());
//
//        return outputStringBuilder.toString();
//    }
//
//    public static String checkNotCompleted(String userTicket, String questId) throws IOException {
//
//        HttpURLConnection connection = connectPost();
//        connection.setDoOutput(true);
//
//        OutputStream data = connection.getOutputStream();
//
//        BufferedWriter writer = new BufferedWriter(
//                new OutputStreamWriter(data, StandardCharsets.UTF_8));
//
//        writer.write("mode=" + MODES.get("CHECK_NOT_COMPLETED"));
//        writer.write("&userTicket=" + userTicket);
//        writer.write("&quest_id=" + questId);
//
//        writer.flush();
//        writer.close();
//
//        InputStream content = connection.getInputStream();
//
//        // Todo REFACTOR! IT'S COPY-PASTE CODE!
//        String line;
//        //connection.
//        BufferedReader in = new BufferedReader(new InputStreamReader(content));
//        StringBuilder outputStringBuilder = new StringBuilder();
//
//        while ((line = in.readLine()) != null) {
//            outputStringBuilder.append(line);
//        }
//
//        Log.d(TAG, "Login Client must return: " + outputStringBuilder.toString());
//
//        return outputStringBuilder.toString();
//
//    }
//
//    public static String setCompleted(String userTicket, String questId) throws IOException {
//
//        HttpURLConnection connection = connectPost();
//        connection.setDoOutput(true);
//
//        OutputStream data = connection.getOutputStream();
//
//        BufferedWriter writer = new BufferedWriter(
//                new OutputStreamWriter(data, StandardCharsets.UTF_8));
//
//        writer.write("mode=" + MODES.get("SET_COMPLETED"));
//        writer.write("&userTicket=" + userTicket);
//        writer.write("&quest_id=" + questId);
//
//        writer.flush();
//        writer.close();
//
//        InputStream content = connection.getInputStream();
//
//        // Todo REFACTOR! IT'S COPY-PASTE CODE!
//        String line;
//        //connection.
//        BufferedReader in = new BufferedReader(new InputStreamReader(content));
//        StringBuilder outputStringBuilder = new StringBuilder();
//
//        while ((line = in.readLine()) != null) {
//            outputStringBuilder.append(line);
//        }
//
//        Log.d(TAG, "Login Client must return: " + outputStringBuilder.toString());
//
//        return outputStringBuilder.toString();
//    }
//
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
package com.sibdever.algo_android.api.commands;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;

public abstract class PictureCommand extends Command {

    public PictureCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public Bitmap execute() {
        try {
            HttpURLConnection connection = send("POST");
//        Log.d(TAG, "Method: " + connection.getRequestMethod());
//        Log.d(TAG, "Response: " + connection.getResponseCode());

            InputStream content = connection.getInputStream();

            Bitmap inputBitmap = BitmapFactory.decodeStream(content);

            // Log.d(TAG, "Login Client must return: " + inputBitmap);
            Log.d("Borlehandro", String.valueOf(inputBitmap == null));
            return inputBitmap;
        }catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }
}

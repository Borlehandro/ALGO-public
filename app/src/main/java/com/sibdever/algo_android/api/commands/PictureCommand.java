package com.sibdever.algo_android.api.commands;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class PictureCommand extends Command {

    public static PictureBuilder builder(CommandType type) {
        return new PictureBuilder(type);
    }

    public static class PictureBuilder extends CommandBuilder<PictureBuilder> {

        protected PictureBuilder(CommandType type) {
            super(type);
        }

        @Override
        public PictureCommand build() {
            PictureCommand command = new PictureCommand(type);
            command.setArguments(arguments);
            return command;
        }

        @Override
        protected PictureBuilder getThis() {
            return this;
        }
    }

    protected PictureCommand(CommandType type) {
        super(type);
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

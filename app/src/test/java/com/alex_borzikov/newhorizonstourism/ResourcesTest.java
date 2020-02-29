package com.alex_borzikov.newhorizonstourism;

import android.graphics.Bitmap;

import com.alex_borzikov.newhorizonstourism.api.ApiClient;

import org.junit.Test;

import java.io.IOException;

public class ResourcesTest {

    @Test
    public void testPicture() throws IOException {

        Bitmap bitmap = ApiClient.getImage("photo-1500530855697-b586d89ba3ee?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80");
        System.out.println(bitmap.getHeight());

    }

}

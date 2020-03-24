package com.alex_borzikov.newhorizonstourism.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.alex_borzikov.newhorizonstourism.R;

public class PreLoginActivity extends AppCompatActivity {

    public static String userTicket;
    public static String language;

    static final int REQUEST_ALL_RESULT = 1;

    private static final String TAG = "Borlehandro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);
        TextView text = findViewById(R.id.algoText);
        Typeface typeface = Typeface.createFromAsset(getAssets(),
                "font/config_rounded_black.otf");
        text.setTypeface(typeface);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_ALL_RESULT) {
            // Todo make it dynamic
            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    || (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                Log.d(TAG, "onRequestPermissionsResult: User allow fine");
                startMain();
            } else {
                Log.d(TAG, "onRequestPermissionsResult: User not allow fine");
                finish();
            }
        }
    }

    private void startMain() {

        Intent intent = new Intent(PreLoginActivity.this, MainActivity.class);

        intent.putExtra("uid", "login");

        intent.putExtra("userTicket", userTicket);

        startActivity(intent);
    }

}

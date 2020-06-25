package com.sibdever.algo_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.sibdever.algo_android.R;
import com.sibdever.algo_android.dialogs.PermissionDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PreLoginActivity extends AppCompatActivity {

    static final int REQUEST_ALL_RESULT = 1;

    private SharedPreferences preferences;
    private Locale myLocale;


    private static final String TAG = "Borlehandro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);

        preferences = getSharedPreferences("User", MODE_PRIVATE);

        if(preferences.contains("ticket") && preferences.contains("language")) {
            Log.d(TAG, "onCreate: OK GETTING LANG");
            setLocale(preferences.getString("language", null));
            checkPermissions();
        }

        TextView text = findViewById(R.id.algoText);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/config_rounded_black.otf");
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


    public void checkPermissions() {

        List<String> permissions = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);

            Log.d(TAG, "onCreate: FINE DENIED!");

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            permissions.add(Manifest.permission.CAMERA);

            Log.d(TAG, "onCreate: CAMERA denied");
        }

        if (permissions.isEmpty()) {
            Log.d(TAG, "onCreate: FINE OK");
            startMain();
        } else {
            PermissionDialog dialog = new PermissionDialog(REQUEST_ALL_RESULT, permissions);
            dialog.show(getSupportFragmentManager(), "permissions");
        }
    }

    private void startMain() {
        Intent intent = new Intent(PreLoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
    }

    public void setLocale(String localeName) {

        myLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
//            Intent refresh = new Intent(getContext(), getActivity().getClass());
//            refresh.putExtra("lang", localeName);
//            startActivity(refresh);
    }

}

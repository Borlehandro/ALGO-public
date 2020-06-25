package com.sibdever.algo_android.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CodeScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final String TAG = "Borlehandro";

    ZXingScannerView scannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);

        setContentView(scannerView);

    }

    @Override
    public void handleResult(Result result) {
        MainActivity.pointCode = result.getText().substring(result.getText().indexOf("=")+1);
        Log.d(TAG, "handleResult: " + MainActivity.pointCode);
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}

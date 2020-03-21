package com.alex_borzikov.newhorizonstourism.dialogs;

import android.Manifest;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import java.util.List;

public class PermissionDialog extends DialogFragment {

    private int PERMISSION_NUMBER;
    private String[] permissions;

    public PermissionDialog(int tag, List<String> permissions) {
        PERMISSION_NUMBER =  tag;
        Log.d("Borlehandro", "PermissionDialog: " + permissions);
        Log.d("Borlehandro", "PermissionDialog: " + permissions.toArray(new String[0]));
        this.permissions = permissions.toArray(new String[0]);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Чтобы приложение ALGO работало правильно, необходимо " +
                "разрешить ему использовать камеру и иметь дотуп к вашему местоположению.")

                .setPositiveButton("Хорошо", (dialog, id) -> {

                    ActivityCompat.requestPermissions(getActivity(),
                            permissions,
                            PERMISSION_NUMBER);
                })

                .setNegativeButton("Отменить", (dialog, id) -> {
                    getActivity().finish();
                });

        return builder.create();
    }

}
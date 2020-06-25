package com.sibdever.algo_android.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.sibdever.algo_android.R;

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

        builder.setMessage(getString(R.string.permissionMessage))

                .setPositiveButton(getString(R.string.permissionAllow), (dialog, id) -> {

                    ActivityCompat.requestPermissions(getActivity(),
                            permissions,
                            PERMISSION_NUMBER);
                })

                .setNegativeButton(getString(R.string.permissionDeny), (dialog, id) -> {
                    getActivity().finish();
                });

        return builder.create();
    }

}

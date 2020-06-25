package com.sibdever.algo_android.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.sibdever.algo_android.R;

public class LocationDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(getString(R.string.locationTurningOnText));
        builder.setNegativeButton(getString(R.string.permissionDeny), (dialog, id) -> getActivity().finish());
        builder.setPositiveButton(getString(R.string.settingsText), (dialog, id)
                -> startActivity( new Intent(Settings. ACTION_LOCATION_SOURCE_SETTINGS )));
        return builder.create();
    }

}
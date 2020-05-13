package com.alex_borzikov.newhorizonstourism.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.alex_borzikov.newhorizonstourism.R;

public class AboutDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(getString(R.string.license))
                .setNeutralButton(getString(R.string.permissionAllow), (dialog, id) -> {
                    dismiss();
                });

        return builder.create();
    }

}

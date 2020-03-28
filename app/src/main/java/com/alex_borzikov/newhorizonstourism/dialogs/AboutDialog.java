package com.alex_borzikov.newhorizonstourism.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AboutDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Todo Fix text
        builder.setMessage("О приложении...")
                .setNeutralButton("Хорошо", (dialog, id) -> {
                    dismiss();
                });

        return builder.create();
    }

}

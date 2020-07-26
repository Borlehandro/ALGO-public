package com.sibdever.algo_android.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.sibdever.algo_android.R;
import com.sibdever.algo_android.data.QuestFinishMessage;

public class FinishDialog extends DialogFragment {

    private final int bonuses;
    private final String message;

    public FinishDialog(QuestFinishMessage message) {
        this.bonuses = message.getBonuses();
        this.message = message.getFinishMessage();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(message + "\n" + getString(R.string.bonusesCollectedTitle) + " " + bonuses)
                .setNeutralButton(getString(R.string.permissionAllow), (dialog, id) -> dismiss());

        return builder.create();
    }
}

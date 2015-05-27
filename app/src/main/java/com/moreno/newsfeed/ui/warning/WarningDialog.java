package com.moreno.newsfeed.ui.warning;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.moreno.newsfeed.R;

/**
 * Created by Moreno on 27.05.2015.
 */
public class WarningDialog extends DialogFragment {
    public static final String TITLE_KEY = "title";
    public static final String MESSAGE_KEY = "message";
    public static final String TAG = "warning dialog";

    public static WarningDialog createDialog(int titleId, int messageId) {
        WarningDialog dialog = new WarningDialog();
        Bundle args = new Bundle();
        args.putInt(TITLE_KEY, titleId);
        args.putInt(MESSAGE_KEY, messageId);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        AlertDialog dialog = new AlertDialog
                .Builder(getActivity())
                .setTitle(args.getInt(TITLE_KEY))
                .setMessage(args.getInt(MESSAGE_KEY))
                .setPositiveButton(R.string.warning_dialog_ok_button_text
                        , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                }).create();
        return dialog;
    }
}

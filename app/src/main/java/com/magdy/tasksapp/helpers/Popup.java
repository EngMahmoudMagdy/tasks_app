package com.magdy.tasksapp.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.magdy.tasksapp.R;

public class Popup {

    private Dialog dialog;

    public void dismiss() {
        if (dialog != null)
            dialog.dismiss();
    }

    private TextView create;
    private EditText editText;

    public Popup(Activity activity) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup);
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        editText = dialog.findViewById(R.id.editText);
        create = dialog.findViewById(R.id.create);
    }

    public void setButtoClick(View.OnClickListener onClickListener) {
        create.setOnClickListener(onClickListener);

    }

    public void showDialog() {
        dialog.show();
    }

    public EditText getEditText() {
        return editText;
    }

    public Dialog getDialog() {
        return dialog;
    }
}

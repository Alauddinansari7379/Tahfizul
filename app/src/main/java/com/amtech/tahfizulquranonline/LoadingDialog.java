
package com.amtech.tahfizulquranonline;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;


public class LoadingDialog extends Dialog {
    public LoadingDialog(@NonNull Context context) {
        super(context);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View view = LayoutInflater.from(context).inflate(R.layout.loading_dialog, null);
        setContentView(view);
    }
}

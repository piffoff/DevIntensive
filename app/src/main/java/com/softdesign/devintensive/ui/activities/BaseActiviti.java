package com.softdesign.devintensive.ui.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantMenedger;

/**
 * Created by Admin on 27.06.2016.
 */
public class BaseActiviti extends AppCompatActivity {
    static final String TAG = ConstantMenedger.TAG_PREFIX + "BaseActiviti";
    protected ProgressDialog mProgressDialog;

    public void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this, R.style.custom_dialog);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.show();
            setContentView(R.layout.progress_splash);
        } else {
            mProgressDialog.show();
            setContentView(R.layout.progress_splash);
        }

    }

    public void hideProgress() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    public void showError(String message, Exception error) {
        showToas(message);
        Log.e(TAG, String.valueOf(error));
    }

    public void showToas(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

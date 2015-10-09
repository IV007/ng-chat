package com.sidelance.ngchat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import com.parse.ParseException;

/**
 * Utility class
 */
public class Tools {

    public static final String TAG = Tools.class.getSimpleName();

    public static void displayErrorDialog(String message, String title, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(android.R.string.ok, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void startActivityIntent(Context mContext, Class mNewActivity){

        // TODO - logic to make this work
        Intent intent = new Intent(mContext, mNewActivity.getClass());
        mContext.startActivity(intent);
    }


    public static void setFlagsAndStartMainActivity(Context mCurrentContext, Class mClass, int flagA, int flagB){

        Intent intent = new Intent(mCurrentContext, mClass.getClass());
        intent.addFlags(flagA);
        intent.addFlags(flagB);
        mCurrentContext.startActivity(intent);
    }
}

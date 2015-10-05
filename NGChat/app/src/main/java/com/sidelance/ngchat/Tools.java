package com.sidelance.ngchat;

import android.app.AlertDialog;
import android.content.Context;

import com.parse.ParseException;

/**
 * Created by Ivan on 10/5/2015.
 */
public class Tools {

    public static void displayEditFriendsErrorDialog(ParseException e, String title, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(e.getMessage())
                .setTitle(title)
                .setPositiveButton(android.R.string.ok, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

package com.sidelance.ngchat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ForgotPassword extends AppCompatActivity {

    protected EditText mEmailEditText;
    protected Button mResetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEmailEditText = (EditText) findViewById(R.id.forgotEmailEditText);
        mResetPasswordButton = (Button) findViewById(R.id.resetPasswordButton);

        mResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString();

                if (TextUtils.isEmpty(email)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                    builder.setTitle(R.string.SIGN_UP_ERROR_TITLE);
                    builder.setMessage(R.string.FORGOT_PASSWORD_EMPTY_EMAIL);
                    builder.setPositiveButton(android.R.string.ok, null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {

                    setProgressBarIndeterminateVisibility(true);
                    ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){
                                setProgressBarIndeterminateVisibility(false);

                                AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                                builder.setTitle(R.string.FORGOT_PASSWORD_RESET_SUCCESS_TITLE);
                                builder.setMessage(R.string.FORGOT_PASSWORD_SUCCESS_MESSAGE);
                                builder.setPositiveButton(R.string.BUTTON_LOGIN, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();


                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                                builder.setMessage(e.getMessage() + " " + R.string.FORGOT_PASSWORD_EMPTY_EMAIL)
                                        .setTitle(R.string.FORGOT_PASSWORD_ERROR_TITLE)
                                        .setPositiveButton(android.R.string.ok, null);

                                AlertDialog dialog = builder.create();
                                dialog.show();



                            }
                        }
                    });

                }
            }
        });
    }

}

package com.sidelance.ngchat;

import android.app.AlertDialog;
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
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    protected EditText mUsername;
    protected EditText mPassword;
    protected EditText mEmail;
    protected Button mSigUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
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

        mUsername = (EditText) findViewById(R.id.signUpUsernameEditText);
        mPassword = (EditText) findViewById(R.id.signUpPasswordEditText);
        mEmail = (EditText) findViewById(R.id.signUpEmailEditText);
        mSigUpButton = (Button) findViewById(R.id.signUpButton);

        mSigUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                String email = mEmail.getText().toString();

                username = username.trim();
                password = password.trim();
                email = email.trim();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {

                    Tools.displayErrorDialog(getString(R.string.SIGN_UP_ERROR_MESSAGE), getString(R.string.SIGN_UP_ERROR_TITLE), SignUpActivity.this);

                } else {

                    signUpNewUser(username, password, email);

                }
            }
        });
    }

    private void signUpNewUser(String username, String password, String email) {
        setProgressBarIndeterminateVisibility(true);

        ParseUser newUser = new ParseUser();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                setProgressBarIndeterminateVisibility(false);

                if (e == null) {

                    //Test this method.
                    Tools.setFlagsAndStartMainActivity(SignUpActivity.this,
                            MainActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK,
                            Intent.FLAG_ACTIVITY_CLEAR_TASK);

//
//                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);

                } else {

                    String errorTitle = getString(R.string.SIGN_UP_ERROR_TITLE);
                    Tools.displayErrorDialog(e.getMessage(), errorTitle, SignUpActivity.this);
                    
                }
            }
        });
    }

}

package com.sidelance.ngchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    protected TextView mSignUpTextView;

    protected EditText mUsername;
    protected EditText mPassword;
    protected Button mLoginButton;
    protected TextView mForgotPassword;

    private Tools tools = new Tools();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "Create");
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
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

        mForgotPassword = (TextView) findViewById(R.id.forgotPasswordTextView);
        mUsername = (EditText) findViewById(R.id.userNameEditText);
        mPassword = (EditText) findViewById(R.id.passwordEditText);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mSignUpTextView = (TextView) findViewById(R.id.signUpTextView);

        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Test this method
                Tools.startActivityIntent(LoginActivity.this, SignUpActivity.class);


//                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
//                startActivity(signUpIntent);
            }
        });

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Test this method.
                Tools.startActivityIntent(LoginActivity.this, ForgotPassword.class);


//                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
//                startActivity(intent);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                username = username.trim();
                password = password.trim();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {

                    Tools.displayErrorDialog(getString(R.string.SIGN_UP_ERROR_MESSAGE), getString(R.string.SIGN_UP_ERROR_TITLE), LoginActivity.this);

                } else {

                    setProgressBarIndeterminateVisibility(true);
                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            setProgressBarIndeterminateVisibility(false);

                            if (e == null){

                                //Test this method.
//                                tools.setFlagsAndStartActivity(LoginActivity.this,
//                                        MainActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK,
//                                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            } else{

                                Tools.displayErrorDialog(e.getMessage(), getString(R.string.LOGIN_ERROR_TITLE), LoginActivity.this);

                            }
                        }
                    });

                }
            }
        });


    }

}

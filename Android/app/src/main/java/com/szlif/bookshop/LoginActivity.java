package com.szlif.bookshop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.szlif.bookshop.models.*;
import com.szlif.bookshop.models.Error;
import com.szlif.bookshop.network.BookshopRequestListener;
import com.szlif.bookshop.network.LoginRequest;


public class LoginActivity extends BaseActivity {

    private EditText loginText;
    private EditText passwordText;
    private CheckBox rememberCheckbox;
    private View progressView;
    private View loginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginText = (EditText) findViewById(R.id.login_field);
        passwordText = (EditText) findViewById(R.id.password_field);
        rememberCheckbox = (CheckBox) findViewById(R.id.remember_box);
        loginFormView = findViewById(R.id.register_form);
        progressView = findViewById(R.id.login_progress);

        tryLoginWithSavedCredentails();
    }


    private void tryLoginWithSavedCredentails() {

        Context context = this.getApplicationContext();
        String login = AppData.getLogin(context);
        String password = AppData.getPassword(context);

        if(login != null && password != null) {
            attemptLoginWithCredentials(login, password);
        }
    }

    public void attemptLogin(View view) {
        // Reset errors.
        loginText.setError(null);
        passwordText.setError(null);

        // Store values at the time of the login attempt.
        String email = loginText.getText().toString();
        String password = passwordText.getText().toString();

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordText.setError(getString(R.string.error_invalid_password));
            passwordText.requestFocus();
            return;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            loginText.setError(getString(R.string.error_field_required));
            loginText.requestFocus();
            return;
        }

        attemptLoginWithCredentials(email, password);

    }

    private void attemptLoginWithCredentials(String login, String password) {

        showProgress(true);
        LoginRequest request = new LoginRequest(login, password);
        spiceManager.execute(request, new LoginRequestListener());

    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public void openRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void proceedToWelcomeActivity(Session session) {

        AppData.token = session.token;
        AppData.user = session.user;

        if(rememberCheckbox.isChecked()) {
            String email = loginText.getText().toString();
            String password = passwordText.getText().toString();
            AppData.rememberCredentials(email, password, LoginActivity.this.getApplicationContext());
        }

        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(intent);
        showProgress(false);
        finish();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    public void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        loginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(
                new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(
                new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

    }

    class LoginRequestListener extends BookshopRequestListener<Session>{
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            showProgress(false);
            Toast toast = Toast.makeText(LoginActivity.this, "Unable to connect!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 30);
            toast.show();
        }

        @Override
        public void onRequestCompleted(Session session) {

            LoginActivity.this.proceedToWelcomeActivity(session);

        }

        @Override
        public void onRequestError(Error error) {
            showProgress(false);
            passwordText.setError(error.error);
            passwordText.requestFocus();
        }
    }

}




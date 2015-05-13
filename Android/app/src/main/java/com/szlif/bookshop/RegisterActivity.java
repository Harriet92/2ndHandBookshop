package com.szlif.bookshop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.szlif.bookshop.models.*;
import com.szlif.bookshop.network.BookshopRequestListener;
import com.szlif.bookshop.network.CreateUserRequest;
import com.szlif.bookshop.network.LoginRequest;


public class RegisterActivity extends BaseActivity {

    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText passwordConfirmField;
    private View registerFormView;
    private View progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameField = (EditText) findViewById(R.id.name_field);
        emailField = (EditText) findViewById(R.id.email_field);
        passwordField = (EditText) findViewById(R.id.password_field);
        passwordConfirmField = (EditText) findViewById(R.id.password_confirm_field);

        progressView = findViewById(R.id.login_progress);
        registerFormView = findViewById(R.id.register_form);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void attemptRegister(View view){

        resetErrors();

        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String confirmPassword = passwordConfirmField.getText().toString();

        if (!isNameValid(name)) {
            nameField.setError(getString(R.string.error_invalid_name));
            nameField.requestFocus();
            return;
        }

        if (!isValidEmail(email)) {
            emailField.setError(getString(R.string.error_invalid_email));
            emailField.requestFocus();
            return;
        }

        if (!isPasswordValid(password)) {
            passwordField.setError(getString(R.string.error_invalid_password));
            passwordField.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            passwordField.setError(getString(R.string.error_different_password));
            passwordField.requestFocus();
            return;
        }


        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true);
        CreateUserRequest request = new CreateUserRequest(name, email, password);

        spiceManager.execute(request, new RegisterRequestListener());
    }

    private boolean isNameValid(String name) {
        return name.length() > 5;
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.length() > 5;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void resetErrors(){

        nameField.setError(null);
        emailField.setError(null);
        passwordField.setError(null);
        passwordConfirmField.setError(null);

    }


    public void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        registerFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        registerFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        registerFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    private class RegisterRequestListener extends BookshopRequestListener<User> {

        @Override
        public void onRequestCompleted(User user) {
            showProgress(false);
            Toast toast = Toast.makeText(RegisterActivity.this, "Account has been created", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 30);
            toast.show();
        }

        @Override
        public void onRequestError(com.szlif.bookshop.models.Error error) {
            showProgress(false);
            Toast toast = Toast.makeText(RegisterActivity.this, error.error, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 30);
            toast.show();
        }

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            showProgress(false);
            Toast toast = Toast.makeText(RegisterActivity.this, "Unable to connect", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 30);
            toast.show();
        }
    }
}

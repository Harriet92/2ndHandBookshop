package com.szlif.bookshop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.szlif.bookshop.models.*;
import com.szlif.bookshop.models.Error;
import com.szlif.bookshop.network.BookshopRequestListener;
import com.szlif.bookshop.network.CreateUserRequest;
import com.szlif.bookshop.network.LoginRequest;

import org.json.JSONObject;

import java.util.Arrays;


public class LoginActivity extends BaseActivity {

    private EditText loginText;
    private EditText passwordText;
    private View progressView;
    private View loginFormView;
    private LoginButton facebookLoginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker tracker;

    @Override
    public void onDestroy() {
        super.onDestroy();
        tracker.stopTracking();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AccessToken token = AccessToken.getCurrentAccessToken();
        callbackManager = CallbackManager.Factory.create();
        if(token != null) {
            getFacebookDataAndLogin(token);
        }
        tracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                if(currentAccessToken != null) {
                    getFacebookDataAndLogin(currentAccessToken);
                }
            }
        };
        setContentView(R.layout.activity_login);

        loginText = (EditText) findViewById(R.id.login_field);
        passwordText = (EditText) findViewById(R.id.password_field);
        loginFormView = findViewById(R.id.register_form);
        progressView = findViewById(R.id.login_progress);
        facebookLoginButton = (LoginButton) findViewById(R.id.login_button);
        facebookLoginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));

    }

    private void getFacebookDataAndLogin(AccessToken accessToken) {
        GraphRequest.newMeRequest(
            accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject me, GraphResponse response) {
                if(response.getError() == null) {
                    String id = me.optString("id");
                    String email = me.optString("email");
                    String name = me.optString("first_name");
                    loginAndRegisterIfNeeded(name, email, id);
                }
            }
        }).executeAsync();
    }

    private void loginAndRegisterIfNeeded(String name, String email, String password) {
        showProgress(true);
        LoginRequest request = new LoginRequest(email, password);
        spiceManager.execute(request, new FacebookLoginRequestListener(name, email, password));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
            Toast.makeText(LoginActivity.this, "Unable to connect!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestCompleted(Session session) {

            LoginActivity.this.proceedToWelcomeActivity(session);

        }

        @Override
        public void onRequestError(Error error) {
            showProgress(false);
            Toast.makeText(LoginActivity.this, "Error: " + error.error, Toast.LENGTH_SHORT).show();
        }
    }

    private class FacebookLoginRequestListener extends BookshopRequestListener<Session> {
        private String name;
        private String email;
        private String password;

        public FacebookLoginRequestListener(String name, String email, String password) {

            this.name = name;
            this.email = email;
            this.password = password;
        }

        @Override
        public void onRequestCompleted(Session session) {
            LoginActivity.this.proceedToWelcomeActivity(session);
        }

        @Override
        public void onRequestError(Error error) {
            showProgress(true);
            CreateUserRequest request = new CreateUserRequest(name, email, password);
            spiceManager.execute(request, new BookshopRequestListener<User>(){

                @Override
                public void onRequestCompleted(User user) {
                    LoginRequest request = new LoginRequest(email, password);
                    spiceManager.execute(request, FacebookLoginRequestListener.this);
                }

                @Override
                public void onRequestError(Error error) {
                    showProgress(false);
                    Toast.makeText(LoginActivity.this, "Unable to integrate with facebook!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    showProgress(false);
                    Toast.makeText(LoginActivity.this, "Unable to connect!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            showProgress(false);
            Toast.makeText(LoginActivity.this, "Unable to connect!", Toast.LENGTH_SHORT).show();
        }
    }
}




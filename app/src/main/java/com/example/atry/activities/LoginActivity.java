package com.example.atry.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.atry.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.chromium.net.CronetEngine;
import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient = null;
    private String ClientId = "309201034208-fqm0b6m195uirrerh1dsqvnf7lkhbjm3.apps.googleusercontent.com";
    private String ClientWebId = "309201034208-fqm0b6m195uirrerh1dsqvnf7lkhbjm3.apps.googleusercontent.com";
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.buttonLogin);
        Button register = findViewById(R.id.buttonRegister);
        EditText password = findViewById(R.id.editTextPassword);
        EditText email = findViewById(R.id.editTextEmail);
        CronetEngine.Builder myBuilder = new CronetEngine.Builder(this);
        CronetEngine cronetEngine = myBuilder.build();
        Executor executor = Executors.newSingleThreadExecutor();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUrlRequestCallback myUrlRequestCallback = new MyUrlRequestCallback();
                UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                        "http://192.168.1.206:5000/login?" +
                                "email=" +  email.getText() +
                                "&password=" + password.getText(),
                        myUrlRequestCallback, executor);

                UrlRequest request = requestBuilder.build();
                request.start();
            }
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });



//        updateUI(account);


        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                }

            }
        });

    }

    private void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        Intent home = new Intent(this, HomePageActivity.class);
        startActivityForResult(signInIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.

            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();

            // write all the data entered by the user in SharedPreference and apply
            myEdit.putString("email", account.getEmail());
            myEdit.apply();

            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code = " + e.getStatusCode());
//            updateUI(null);
        }
    }

    public class MyUrlRequestCallback extends UrlRequest.Callback {
        private static final String TAG = "MyUrlRequestCallback";
        @Override
        public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) {
            Log.i(TAG, "onRedirectReceived method called.");
            // You should call the request.followRedirect() method to continue
            // processing the request.
            request.followRedirect();
        }

        @Override
        public void onResponseStarted(UrlRequest request, UrlResponseInfo info) {
            Log.i(TAG, "onResponseStarted method called.");
            // You should call the request.read() method before the request can be
            // further processed. The following instruction provides a ByteBuffer object
            // with a capacity of 102400 bytes for the read() method. The same buffer
            // with data is passed to the onReadCompleted() method.
            request.read(ByteBuffer.allocateDirect(102400));
        }

        @Override
        public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) {
            Log.i(TAG, "onReadCompleted method called.");
            // You should keep reading the request until there's no more data.
            byteBuffer.clear();
            request.read(byteBuffer);
            //Log.i(TAG, StandardCharsets.UTF_8.decode(byteBuffer).toString());
            if (StandardCharsets.UTF_8.decode(byteBuffer).toString().contains("ok")){
                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                EditText email = findViewById(R.id.editTextEmail);

                // write all the data entered by the user in SharedPreference and apply
                myEdit.putString("email", email.getText().toString());
                myEdit.apply();

                startActivity(intent);
            }
        }

        @Override
        public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
            Log.i(TAG, "onSucceeded method called.");
        }

        @Override
        public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
            // The request has failed. If possible, handle the error.
            Log.e(TAG, "The request failed.", error);
        }

    }

}
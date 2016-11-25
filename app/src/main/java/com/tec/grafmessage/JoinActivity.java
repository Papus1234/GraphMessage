package com.tec.grafmessage;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class JoinActivity extends AppCompatActivity {

    LoginButton loginButton;
    EditText ip_adress;
    CallbackManager callbackManager;
    Intent intent;
    public static String ip="";
    public boolean infacebook=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_join);
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        callbackManager = CallbackManager.Factory.create();
        ip_adress = (EditText) findViewById(R.id.ip_editText);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                infacebook=true;
                gotoMainActivity();
            }

            @Override
            public void onCancel() {
                infacebook=false;
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }
    public void Ingresar(View view){
        ip= ip_adress.getText().toString();
        if(infacebook)
        {
            Toast toast = Toast.makeText(getApplicationContext(), ip, Toast.LENGTH_LONG);
            toast.show();
            startActivity(intent);
        }
    }
    private void gotoMainActivity() {
        intent=new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}


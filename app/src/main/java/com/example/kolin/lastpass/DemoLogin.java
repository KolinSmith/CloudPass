package com.example.kolin.lastpass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by laluvjohn on 11/27/2015.
 */
public class DemoLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_demo);
    }

    public void createNewAccount(View view) {
        Intent to_create_user = new Intent(this, CreateUser.class);
        startActivity(to_create_user);
        
    }

    public void login(View view) {
        Intent to_login = new Intent(this, Login.class);
        startActivity(to_login);

    }
}

package com.example.kolin.lastpass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by laluvjohn on 11/25/2015.
 */
public class AddAccount extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add);

        Intent activity_from_body = getIntent();
    }

    //A new AccountData object is made, user values are stored into that object, and sent back to the parent activity.
    public void addNewAccount(View view) {
        Boolean content_okay = false;
        String stenm_str = "";
        String usrnm_str = "";
        String pswd_str = "";
        do {
            EditText  stenm = (EditText) findViewById(R.id.site_input);
            EditText usrnm = (EditText) findViewById(R.id.username_input);
            EditText pswd = (EditText) findViewById(R.id.password_input);

            stenm_str = String.valueOf(stenm.getText());
            usrnm_str = String.valueOf(usrnm.getText());
            pswd_str = String.valueOf(pswd.getText());

            if(stenm_str.trim().length() == 0){
                Toast.makeText(this, "Site Name must not be empty",Toast.LENGTH_SHORT).show();
                return;
            } else if(usrnm_str.trim().length() == 0){
                Toast.makeText(this, "Username must not be empty",Toast.LENGTH_SHORT).show();
                return;
            }else if(pswd_str.trim().length() == 0){
                Toast.makeText(this, "Password must not be empty",Toast.LENGTH_SHORT).show();
                return;
            }else{
                content_okay = true;
            }
        }while(content_okay = false);

        AccountData newone = new AccountData(stenm_str,usrnm_str,pswd_str);

        Intent intentToBody = new Intent(this, BodyActivity.class);
        intentToBody.putExtra("account", newone);
        setResult(Activity.RESULT_OK, intentToBody);
        Toast.makeText(this,"Account Successfully Added", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void goBacktoParent(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}

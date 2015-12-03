package com.example.kolin.lastpass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by laluvjohn on 11/29/2015.
 */
public class ChangePassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
    }


    public void changePassword(View view) throws Exception {

        EditText oldpswdTxt = (EditText) findViewById(R.id.oldPassword_text);
        EditText newpswd1Txt = (EditText) findViewById(R.id.newPassword1_text);
        EditText newpswd2Txt = (EditText) findViewById(R.id.newPassword2_text);

        String old_pswd = String.valueOf(oldpswdTxt.getText());
        String newpswd1 = String.valueOf(newpswd1Txt.getText());
        String newpswd2 = String.valueOf(newpswd2Txt.getText());

        if(old_pswd.trim().length() == 0 || newpswd1.trim().length() == 0 || newpswd2.trim().length() == 0){
            Toast.makeText(this, "Please fill in all fields before proceeding", Toast.LENGTH_LONG).show();
            return;
        }else if(!newpswd1.equals(newpswd2)){
            Toast.makeText(this,"Re-entered Password doesnot match", Toast.LENGTH_LONG).show();
            return;
        }else if(!Encryption.checkPasswordValidity(old_pswd)){
            Toast.makeText(this, "Existing password doesnot match old password entered", Toast.LENGTH_LONG).show();
            return;
        }else if(newpswd1.length() < 7 || newpswd1.length() > 13){
            Toast.makeText(this, "Please make sure your password is between 7 and 13 characters", Toast.LENGTH_LONG).show();
            return;
        }else{
            String hashed_new_pass = Encryption.HashSHA(newpswd1);
            Encryption.setHashedMasterpass(hashed_new_pass);
            String fluffed_pass = Encryption.fluffkey(hashed_new_pass, "change");
            Encryption.getAESKey(fluffed_pass);
            Intent back_to_body = new Intent(this, BodyActivity.class);
            back_to_body.putExtra("status","changed");
            setResult(Activity.RESULT_OK, back_to_body);
            finish();

        }
    }

    public void backtoBody(View view) {
        Intent back_to_body = new Intent(this, BodyActivity.class);
        back_to_body.putExtra("status", "cancelled");
        setResult(Activity.RESULT_OK,back_to_body);
        finish();
    }
}

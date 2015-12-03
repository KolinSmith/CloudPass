package com.example.kolin.lastpass;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Key;
import java.util.prefs.PreferenceChangeEvent;


/**
 * Created by laluvjohn on 11/27/2015.
 */
public class CreateUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);
    }

    public void manageSecurity(View view) throws Exception {

        EditText pswd_text = (EditText) findViewById(R.id.newPassword);
        Button generateButton = (Button) findViewById(R.id.create_user_next_btn);
        Button continueButton = (Button) findViewById(R.id.create_user_continue_btn);
        String user_input =  String.valueOf(pswd_text.getText());
        if(user_input.trim().length() < 7 || user_input.trim().length() > 13){
            Toast.makeText(this, "Password Length must be between 7 to 13 characters",Toast.LENGTH_LONG).show();
            return;
        }
        String unique_serial = Encryption.newSerial();
        TextView showSerial = (TextView) findViewById(R.id.displaySerial);
        //Copy to clipboard////
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData passclip = ClipData.newPlainText("serial_key", unique_serial);
        clipboard.setPrimaryClip(passclip);
        ////////////////////////////
        showSerial.setText("\tYour unique Serial Key is: " + unique_serial + ". \n\t Key has been copied to clipboard. \n\t Kindly store this serial in a secure location as it is unique to your profile.");
        showSerial.setVisibility(View.VISIBLE);
        generateButton.setEnabled(false);


        String hashed_masterpass = "";
        try {
            hashed_masterpass = Encryption.HashSHA(user_input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Encryption.setHashedMasterpass(hashed_masterpass);

        String fluffedkey = Encryption.fluffkey(user_input, unique_serial);
        //Get AES to be used for encryption/decryption
        Encryption.getAESKey(fluffedkey);

        continueButton.setVisibility(View.VISIBLE);

        //Hide Soft keyboard so that user can see continue button
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        String edited_serial = unique_serial + ";new";
        editor.putString(user_input, edited_serial);
        editor.apply();
    }

    public void forwardToBody(View view) {
        Intent forward_to_body = new Intent(this, BodyActivity.class);
        forward_to_body.putExtra("parent","new_user");
        finish();
        startActivity(forward_to_body);


    }
}

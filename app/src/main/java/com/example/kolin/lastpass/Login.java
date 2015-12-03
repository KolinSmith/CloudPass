package com.example.kolin.lastpass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by laluvjohn on 11/27/2015.
 */
public class Login extends AppCompatActivity {
    public static ArrayList<AccountData> data_list = new ArrayList<AccountData>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_layout);
    }
/*
    public void continueToBody(View view) {
        EditText passwrd_text = (EditText) findViewById(R.id.login_password);
        String paswd_input = String.valueOf(passwrd_text.getText());

        if(paswd_input.trim().length() == 0){
            Toast.makeText(this,"Invalid Password, Please try again", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.contains(paswd_input)){
            String[] prefContent = preferences.getString(paswd_input,"").split(";");
            if(prefContent[1].equals("new")){

            }
        }


    }*/

    public void loginToBody(View view) throws Exception {
        EditText passwrd_text = (EditText) findViewById(R.id.login_password);
        EditText serial_text = (EditText) findViewById(R.id.login_serial);
        CheckBox serial_save = (CheckBox) findViewById(R.id.save_serial_check);
        String paswd_input = String.valueOf(passwrd_text.getText()).trim();
        String serial_input = String.valueOf(serial_text.getText()).trim();

        if(paswd_input.trim().length() == 0 || serial_input.trim().length() == 0){
            if(paswd_input.trim().length() != 0 && serial_input.trim().length() == 0){
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                serial_input = preferences.getString(Encryption.HashSHA(paswd_input), "");
                if(serial_input.trim().length() == 0){
                    Toast.makeText(this, "Please enter your unique serial key", Toast.LENGTH_SHORT).show();
                    return;
                }
            }else {
                Toast.makeText(this, "Empty Password or Serial Entered", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        String hashed_password = "";
        try {
            hashed_password = Encryption.HashSHA(paswd_input);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Encryption.setHashedMasterpass(hashed_password);
        String fluffed = Encryption.fluffkey(paswd_input, serial_input);
        Encryption.getAESKey(fluffed); //sets AES key

        String return_from_read = "";
        try {
            return_from_read = ReadXML.readXML(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(return_from_read.equals("Password matches")){
            if(serial_save.isChecked()){
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                String edited_serial = serial_input;
                editor.putString(Encryption.getHashedMasterpass(), edited_serial);
                editor.apply();
            }
            Intent to_body = new Intent(this, BodyActivity.class);
            to_body.putExtra("parent","registered_user");
            startActivity(to_body);
        }else{
            Toast.makeText(this,"Incorrect Password or Serial. Please Try Again", Toast.LENGTH_LONG);
            return;
        }

    }

    public static void setData_list(ArrayList<AccountData> in_data){
        if(in_data.size() > 0) {
            data_list.addAll(in_data);
        }else{
            data_list = new ArrayList<AccountData>();
        }
    }

}

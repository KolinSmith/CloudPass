package com.example.kolin.lastpass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by laluvjohn on 11/25/2015.
 */
public class ExpandedAccount extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private int position;
    private String siteName;
    private String userName;
    private String password;

    EditText editSite;
    EditText editUsrName;
    EditText editPassword;

    ToggleButton passChanger;
    Button edit_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.expanded_account);

        Intent activity_from_body = getIntent();
        int defaul_value = -1;
        position = activity_from_body.getIntExtra("position", defaul_value);
        AccountData givenAccount = (AccountData) activity_from_body.getParcelableExtra("selected_account");
        siteName = givenAccount.getSiteName();
        userName = givenAccount.getUserName();
        password = givenAccount.getPassword();

        editSite = (EditText) findViewById(R.id.expanded_site_input);
        editSite.setText(siteName);
        editSite.setFocusableInTouchMode(false);

        editUsrName = (EditText) findViewById(R.id.expanded_username_input);
        editUsrName.setText(userName);
        editUsrName.setFocusableInTouchMode(false);

        editPassword = (EditText) findViewById(R.id.expanded_password_input);
        editPassword.setText("************");
        editPassword.setFocusableInTouchMode(false);

        passChanger = (ToggleButton) findViewById(R.id.toggleButton);
        passChanger.setOnCheckedChangeListener(this);

        edit_btn = (Button) findViewById(R.id.expanded_edit_btn);

    }

    public void editAccount(View view) {
        Button local_edit_btn = (Button) view;
        String btn_text = local_edit_btn.getText().toString();
        if(btn_text.equals("EDIT")){
            edit_btn.setText("SAVE");
            editSite.setFocusableInTouchMode(true);
            editUsrName.setFocusableInTouchMode(true);
            if(editPassword.getText().toString().equals("************")){
                editPassword.setText(password);
            }
            editPassword.setFocusableInTouchMode(true);
            return;
        }else if(btn_text.equals("SAVE")){
            String tmp_stnm = String.valueOf(editSite.getText());
            String tmp_usrnm = String.valueOf(editUsrName.getText());
            String tmp_pswd = String.valueOf(editPassword.getText());
            if(tmp_stnm.trim().length() == 0 || tmp_usrnm.trim().length() == 0 || tmp_pswd.trim().length() == 0){
                Toast.makeText(this, "Please fill in all fields before saving", Toast.LENGTH_LONG).show();
                return;
            }

            siteName = tmp_stnm;
            userName = tmp_usrnm;
            password = tmp_pswd;
            Intent back_to_body = new Intent(ExpandedAccount.this, BodyActivity.class);
            back_to_body.putExtra("action_taken","saved");
            back_to_body.putExtra("position", position);
            back_to_body.putExtra("siteName", siteName);
            back_to_body.putExtra("userName", userName);
            back_to_body.putExtra("password", password);
            setResult(Activity.RESULT_OK, back_to_body);
            edit_btn.setText("EDIT");
            finish();
        }

    }

    public void deleteAccount(View view) {
        new AlertDialog.Builder(ExpandedAccount.this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this account?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent back_to_body = new Intent(ExpandedAccount.this, BodyActivity.class);
                        back_to_body.putExtra("action_taken", "deleted");
                        back_to_body.putExtra("position", position);
                        setResult(Activity.RESULT_OK, back_to_body);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert).show();

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            editPassword.setText(password);
        }else{
            editPassword.setText("************");
        }

    }

    public void copyToClipboard(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData passclip = ClipData.newPlainText("password_label", password);
        clipboard.setPrimaryClip(passclip);
        Toast.makeText(ExpandedAccount.this, "Password Copied to Clipboard", Toast.LENGTH_SHORT).show();
    }
}

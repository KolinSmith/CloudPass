package com.example.kolin.lastpass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.TokenPair;

/**
 * Created by laluvjohn on 11/28/2015.
 */
public class ConnectToDropBox extends AppCompatActivity {

    public static DropboxAPI<AndroidAuthSession> dropboxAPI;
    private boolean loginStatus;

    private final static String DROPBOX_DIR = "/PasswordStorage/";
    private final static String DROPBOX_NAME = "dropbox_prefs";
    private final static String APP_KEY = "6nfp6boprxnt9lp";
    private final static String APP_SECRET = "y1faspo722n5w3r";
    private final static Session.AccessType ACCESS_TYPE = AccessType.DROPBOX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.connect_dropbox);

        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session;
        loginStatus = false;
        SharedPreferences preferences = getSharedPreferences(DROPBOX_NAME, 0);
        String key = preferences.getString(APP_KEY, null);
        String secret = preferences.getString(APP_SECRET,null);

        if(key != null && secret != null){
            AccessTokenPair token = new AccessTokenPair(key, secret);
            session = new AndroidAuthSession(appKeys, ACCESS_TYPE, token);
        }else{
            session = new AndroidAuthSession(appKeys, ACCESS_TYPE);
        }

        dropboxAPI = new DropboxAPI<AndroidAuthSession>(session);
    }

    @Override
    protected void onResume() {
        super.onResume();

        AndroidAuthSession session = (AndroidAuthSession) dropboxAPI.getSession();
        if(session.authenticationSuccessful()){
            try{
                session.finishAuthentication();
                TokenPair tokens = session.getAccessTokenPair();
                SharedPreferences preferences =  getSharedPreferences(DROPBOX_NAME, 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(APP_KEY,tokens.key);
                editor.putString(APP_SECRET, tokens.secret);
                editor.commit();
                loginStatus = true;
                Toast.makeText(this, "Connected to DropBox!", Toast.LENGTH_SHORT).show();

            }catch (IllegalStateException ex){
                Toast.makeText(this, "DropBox authorization Error", Toast.LENGTH_LONG).show();
            }

          //  try {
              //  DropboxAPI.Entry existingEntry = dropboxAPI.metadata(DROPBOX_DIR,1000,null,false,null);
            //    if(!existingEntry.isDir || existingEntry.contents == null){
                    //if the directory does not exist, or does not contain password file, create new account.
            //        Intent intent_to_new_acc = new Intent(this, CreateUser.class);
            //        startActivity(intent_to_new_acc);
            //    }else{
                    DownloadFile downloadFile = new DownloadFile(this, dropboxAPI, DROPBOX_DIR);
                    downloadFile.execute();
            //        Intent intent_to_login = new Intent(this, Login.class);
            //        startActivity(intent_to_login);
           //     }
           // } catch (DropboxException e) {
         //       e.printStackTrace();
         //   }



        }
    }

    public void loginToDB(View view) {
        if(loginStatus) {
            dropboxAPI.getSession().unlink();
            loginStatus = false;
        }
        ((AndroidAuthSession) dropboxAPI.getSession()).startAuthentication(ConnectToDropBox.this);
    }

    public void logpOutDB(View view) {
        if(loginStatus){
            dropboxAPI.getSession().unlink();
            loginStatus = false;
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        }
    }
}

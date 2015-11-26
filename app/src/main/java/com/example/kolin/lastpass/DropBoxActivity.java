package com.example.kolin.lastpass;

import android.support.v7.app.AppCompatActivity;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

/**
 * Created by Kolin on 11/26/2015.
 */
public class DropBoxActivity extends AppCompatActivity {
    final static private String APP_KEY = "o2f704gfvrxix8d";
    final static private String APP_SECRET = "hki96q7hpsn1d6u";

    private DropboxAPI<AndroidAuthSession> mDBApi;

    private void dropboxInit(String key, String secret){
        // And later in some initialization function:
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
    }

}

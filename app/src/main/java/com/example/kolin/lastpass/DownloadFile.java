package com.example.kolin.lastpass;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by laluvjohn on 11/28/2015.
 */
public class DownloadFile extends AsyncTask<Void, Void, Boolean> {

    private static DropboxAPI dropboxAPI;
    private static String path;
    private static Context ctx;


    public  DownloadFile(Context context, DropboxAPI dropboxAPI, String path){
        super();
        this.dropboxAPI = dropboxAPI;
        this.path = path;
        this.ctx = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            DropboxAPI.Entry existingEntry = dropboxAPI.metadata("/PasswordStorage",1000,null,true,null);
            if(!existingEntry.isDir || existingEntry.contents == null){
                Log.v("Inside file chk","Dir/file not found");
                return false;
            }
            Log.v("Passed file check", "about to download file");
            FileOutputStream _stream= ctx.getApplicationContext().openFileOutput("data.xml", Context.MODE_PRIVATE);
            dropboxAPI.getFile(path+"data.xml",null,_stream, null);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DropboxException e) {
            e.printStackTrace();
        }
        return false;
    }

    //If file is discovered, Login activity is started. If not, New Account creation is started.
    protected void onPostExecute(Boolean result){
        if(result){
            Intent intent_to_login = new Intent(ctx, Login.class);
            ctx.startActivity(intent_to_login);
            Log.v("Download Status: ", "File Succesfully Downloaded");
        }else{
            Intent intent_to_new_acc = new Intent(ctx, CreateUser.class);
            ctx.startActivity(intent_to_new_acc);
            Log.v("Download Status: ", "File Not Downloaded");
        }
    }
}

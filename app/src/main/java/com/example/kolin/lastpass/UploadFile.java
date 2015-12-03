package com.example.kolin.lastpass;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by laluvjohn on 11/28/2015.
 */
public class UploadFile extends AsyncTask<Void, Void, Boolean> {

    private DropboxAPI dropboxAPI = ConnectToDropBox.dropboxAPI;
    private String path;
    private Context ctx;

    public UploadFile(String path, Context context){
        super();
        this.path = path;
        this.ctx = context;
    }
    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            String myfile = ctx.getFilesDir() + "/" + "data.xml";
            File upfile = new File(myfile);
            FileInputStream inputStream = ctx.getApplicationContext().openFileInput("data.xml");
            dropboxAPI.putFileOverwrite(path+"data.xml", inputStream, upfile.length(),null);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DropboxException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if(aBoolean){
            Log.v("UploadStatus: ", "File Succesfully Uploaded!");
        }else{
            Log.v("UploadStatus: ", "File Not Uploaded!");
        }
    }
}

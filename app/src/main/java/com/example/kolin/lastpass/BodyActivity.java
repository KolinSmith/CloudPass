package com.example.kolin.lastpass;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BodyActivity extends AppCompatActivity {

    static ArrayList<AccountData> global_data;
    static ListView myList;
    static TextView help_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        //For making a simple text file:
        String filename = "myfile.txt";
        String string = "Hello world!";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /////////////////////////////////

        //Read file:
        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                    openFileInput("myfile.txt")));
            String inputString;
            StringBuffer stringBuffer = new StringBuffer();
            while ((inputString = inputReader.readLine()) != null) {
                stringBuffer.append(inputString + "\n");
            }
            Toast.makeText(this, stringBuffer.toString(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /////////////////////////////////////////
*/
        Intent from_login = getIntent();
        if(from_login.getStringExtra("parent").toString().equals("new_user")){
            ArrayList<AccountData> new_data = new ArrayList<AccountData>();
            global_data = new_data;
        }else if(from_login.getStringExtra("parent").toString().equals("registered_user")){
            if(Login.data_list.size() > 0) {
                ArrayList<AccountData> new_data = new ArrayList<AccountData>();
                new_data.addAll(Login.data_list);
                global_data = new_data;
            }else{
                global_data = new ArrayList<AccountData>();
            }
        }
        help_text = (TextView) findViewById(R.id.help_text);
        if(global_data.size() > 0){
            help_text.setVisibility(View.GONE);
        }

     //   ListAdapter myAdapter = new ArrayAdapter<String>(this, R.layout.row_layout,R.id.textView1,favoriteTVShows);

        myList = (ListView) findViewById(R.id.listView);

        myList.setAdapter(new MyBaseAdapter(this, global_data));


        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String tvShowPicked = "You selected " + String.valueOf(parent.getItemAtPosition(position));
                Object o = parent.getItemAtPosition(position);
                AccountData object = (AccountData) o;
                Intent from_body_to_edit = new Intent(BodyActivity.this, ExpandedAccount.class);
                from_body_to_edit.putExtra("position",position);
                from_body_to_edit.putExtra("selected_account", object);
                int requestCode = 2;
                startActivityForResult(from_body_to_edit, requestCode);
                Toast.makeText(BodyActivity.this, "You picked: " + " " + object.getSiteName(),Toast.LENGTH_SHORT).show();
              //  Toast.makeText(BodyActivity.this, tvShowPicked,Toast.LENGTH_SHORT ).show();
            }
        });
     //   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
     //   fab.setOnClickListener(new View.OnClickListener() {
     //       @Override
     //       public void onClick(View view) {
     //           Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
    //                    .setAction("Action", null).show();
    //        }
   //     });
    }

    private ArrayList<AccountData> getMockData(){
        ArrayList<AccountData> outgoing = new ArrayList<AccountData>();

        AccountData data1 = new AccountData("Gmail", "laluvjohn", "lololol");
        outgoing.add(data1);

        data1 = new AccountData("IGN", "lalu", "lololol");
        outgoing.add(data1);

        data1 = new AccountData("Dacebook", "Tom", "lololol");
        outgoing.add(data1);

        return outgoing;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_body, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
    //    if (id == R.id.action_settings) {
    //        return true;
    //    }

        return super.onOptionsItemSelected(item);
    }

    public void addAccount(MenuItem item) {
        Intent add_new_account = new Intent(this, AddAccount.class);
       // Bundle bundle_to_add = new Bundle();
       // bundle_to_add.putParcelableArray("accounts", global_data);
        final int result_id = 1;

        //make startActivityForResult(int, result_id)
        startActivityForResult(add_new_account, result_id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){//response from AddAccount Activity
        if(resultCode == -1  && data.getExtras().containsKey("account")) {
            AccountData newone = (AccountData) data.getParcelableExtra("account");
            global_data.add(newone);
            help_text.setVisibility(View.GONE);
        }
        }else if(requestCode == 2){//response from ExpandAccount Activity
            if(resultCode == -1 && data.getStringExtra("action_taken").toString().equals("saved")){
                int position = data.getIntExtra("position",-1);
                String stenm = data.getStringExtra("siteName");
                String usrnm = data.getStringExtra("userName");
                String psswd = data.getStringExtra("password");
                global_data.get(position).setSiteName(stenm);
                global_data.get(position).setUserName(usrnm);
                global_data.get(position).setPassword(psswd);
                myList.setAdapter(new MyBaseAdapter(this, global_data));
                Toast.makeText(this, "Changes Saved", Toast.LENGTH_LONG).show();

            }
            else if(resultCode == -1 && data.getStringExtra("action_taken").toString().equals("deleted")){
                int position = data.getIntExtra("position", -1);
                global_data.remove(position);
                myList.setAdapter(new MyBaseAdapter(this, global_data));
                Toast.makeText(this, "Account Removed", Toast.LENGTH_LONG).show();
            }
        }else if(requestCode == 3){//Response from ChangePassword Activity
            if(resultCode == -1 && data.getStringExtra("status").toString().equals("cancelled")){

            }else if(resultCode == -1 && data.getStringExtra("status").toString().equals("changed")) {
                Toast.makeText(this, "Password Succesfully Changed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            BuildXML.buildXML(global_data,this);
            UploadFile uploadFile = new UploadFile("/PasswordStorage/", this);
            uploadFile.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Change password to a stronger one
    public void changePassword(MenuItem item) {
        Intent change_password = new Intent(this, ChangePassword.class);
        final int result_id = 3;
        startActivityForResult(change_password, result_id);
    }

    //Go back to Connect to Dropbox activity.
    public void goHome(MenuItem item) {
        Intent back_to_home = new Intent(this, ConnectToDropBox.class);
        finish();
        startActivity(back_to_home);
    }
}

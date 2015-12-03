package com.example.kolin.lastpass;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.*;
import java.util.*;


/**
 * Created by laluvjohn on 11/27/2015. Almost all code here is made by James.
 */
public class BuildXML {

    public static void buildXML(ArrayList<AccountData> data, Context ctx) throws Exception{

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.newDocument();
        Element masterpassword, url, username, password;

        Element root = doc.createElement("PassBox");
        doc.appendChild(root);

        Element account = doc.createElement("Account");

        masterpassword = doc.createElement("MasterPass");
        masterpassword.appendChild(doc.createTextNode(Encryption.encrypt(Encryption.getHashedMasterpass())));
        root.appendChild(masterpassword);


        //Adds the data in order from URL, Username, and Password
        for(int i = 0; i < data.size(); i++){
            root.appendChild(makeAccount(doc, Encryption.encrypt(data.get(i).getSiteName()), Encryption.encrypt(data.get(i).getUserName()), Encryption.encrypt(data.get(i).getPassword())));
        }


        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(doc);

        //Creates data.xml file with doc as the source.
        FileOutputStream _stream= ctx.getApplicationContext().openFileOutput("data.xml", Context.MODE_PRIVATE);
        StreamResult sr = new StreamResult(_stream);
        transformer.transform(source, sr);



        //To test the file out:
        /*
        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                    ctx.getApplicationContext().openFileInput("data.xml")));
            String inputString;
            StringBuffer stringBuffer = new StringBuffer();
            while ((inputString = inputReader.readLine()) != null) {
                stringBuffer.append(inputString + "\n");
            }
            Log.v("Text Input: ", stringBuffer.toString());
           // Toast.makeText(ctx, stringBuffer.toString(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }




    private static Node makeAccount(Document doc, String url, String username, String password){
        Element account = doc.createElement("Account");
        account.appendChild(makeAccountElements(doc, account, "URL", url));
        account.appendChild(makeAccountElements(doc, account, "Username", username));
        account.appendChild(makeAccountElements(doc, account, "Password", password));
        return account;

    }

    private static Node makeAccountElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
}

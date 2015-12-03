package com.example.kolin.lastpass;

/**
 * Created by laluvjohn on 11/28/2015. Almost all code here is written by James.
 */

import android.content.Context;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;



import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ReadXML {
    public static String readXML(Context ctx) throws Exception {

        String no_match = "Password does not match";
        String match = "Password matches";

        String hashed_masterpass = "";

        // File xmlFile = new File("data.xml");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(ctx.getApplicationContext().openFileInput("data.xml"));

        //Get MasterPassword
        NodeList list = doc.getElementsByTagName("PassBox");
        Node node = list.item(0);

        if (node.getNodeType() == Node.ELEMENT_NODE) {

            Element element = (Element) node;

            hashed_masterpass = Encryption.decrypt(element.getElementsByTagName("MasterPass").item(0).getTextContent());
        }
        String user_input_hashed_pass = Encryption.getHashedMasterpass();
        if (!user_input_hashed_pass.equals(hashed_masterpass)) {
            return no_match;
        } else {

            ArrayList<AccountData> data_list = new ArrayList<AccountData>();

            //Get Data
            list = doc.getElementsByTagName("Account");

            for (int x = 0; x < list.getLength(); x++) {
                node = list.item(x);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    AccountData newOne = new AccountData();
                    newOne.setSiteName(Encryption.decrypt(element.getElementsByTagName("URL").item(0).getTextContent()));
                    newOne.setUserName(Encryption.decrypt(element.getElementsByTagName("Username").item(0).getTextContent()));
                    newOne.setPassword(Encryption.decrypt(element.getElementsByTagName("Password").item(0).getTextContent()));
                    data_list.add(newOne);
                }

            }
            Login.setData_list(data_list);
            return match;

        }
    }
}


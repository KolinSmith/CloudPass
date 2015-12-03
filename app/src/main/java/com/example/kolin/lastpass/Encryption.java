package com.example.kolin.lastpass;

/**
 * Created by laluvjohn on 11/27/2015. Almost all functions here are written by James.
 */
//Hashing
import java.security.MessageDigest;

//AES
        import java.security.Key;
        import javax.crypto.Cipher;
        import javax.crypto.spec.SecretKeySpec;
        import android.util.Base64;

//Serial
        import java.util.*;
        import java.io.*;


public class Encryption{

    public static String algo = "AES";
    public static Key key;
    private static String masterpass_plain ="";
    private static String masterpass_hashed = "";
    private static String global_serial = "";

    public static void setHashedMasterpass(String pass){
        masterpass_hashed = pass;
    }

    public static String getHashedMasterpass(){
        return masterpass_hashed;
    }

/*
    public static void main(String args[]) throws Exception{

        //USER INPUT
        String masterpass = "passy24";

        //For users making a new passbox. Assigns a new serial.
        String serial = newSerial();

        //New and old user setting up the app for the first time. Saves their serial number
        saveSerial(serial);

        //Fluffs the password to merge the masterpass and serial
        String passKey = fluffkey(masterpass, serial);

        //Makes the AES Key key
        getAESKey(passKey);


        String msg = "Hello world!";

        //How to encrypt the message
        String encodedMsg = encrypt(msg);
        System.out.println("Encrypted: " + encodedMsg);

        //How to decrypt the message
        String decodedMsg = decrypt(encodedMsg);
        System.out.println("Decrypted: " + decodedMsg);

        if(decodedMsg.equals(msg))
            System.out.println("EQUAL!!");
        else
            System.out.println("NOPE");



/*   //HASH TESTING
		String hashed1 = HashSHA("Password");
		System.out.println("Hashed1 = " + hashed1);

		String hashed2 = HashSHA("Password");
		System.out.println("Hashed2 = " + hashed2);

		if(hashed1.equals(hashed2))
			System.out.println("EQUAL!!");
		else
			System.out.println("NOPE");
*/



	/*  //AES-128 TESTING

		//Make a key using the password
 		getAESKey("oJiB@h82(o$bWq2*");


        String message = "Username: User_42\nPassword: GeNerIcPASSword24";
        System.out.println("Phrase: " + message);


		//How to encrypt the message
        String encodedMsg = encrypt(message);
        System.out.println("Encrypted: " + encodedMsg);

		//How to decrypt the message
        String decodedMsg = decrypt(encodedMsg);
        System.out.println("Decrypted: " + decodedMsg);

	*/


  //  }




    //-----------------------------------------------------
// For new members only. This creates a new unique serial number
//	Old members should have recorded their serial because it is unique to each file
//
    public static String newSerial(){

        Random rnd = new Random();

        Long n;
        do{
            n = (rnd.nextLong() % 900000000) + 100000000;
        }
        while((n < 0) || (n < 99999999));

        return Long.toString(n);
    }




    //-----------------------------------------------------
// For setting up the app, this will save the serial number as a file
// Only needed once per device usage.
//
    public static void saveSerial(String serial) throws Exception{

        global_serial = serial;

        BufferedWriter output = null;
        try {
            File file = new File("serial.txt");
            output = new BufferedWriter(new FileWriter(file));
            output.write(serial);
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
        finally {
            if (output != null)
                output.close();
        }
    }



    //------------------------------------------
//	SHA-256 Hashing
//
    public static String HashSHA(String stringToEncrypt) throws Exception{
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(stringToEncrypt.getBytes());
        String encryptedString = new String(messageDigest.digest());

        return encryptedString;

    }


    //-----------------------------------------------------
//
//  For after the user gives a masterpassword.
//  This will fluff it with the serial after every log-in attempt
//
    public static String fluffkey(String password, String serial){

        int y = 0;
        String local_serial = serial;
        if(local_serial.equals("change")){
            local_serial = global_serial;
        }

        for(int x = password.length(); x < 16; x++)
        {
            password += local_serial.charAt(y);
            y++;
        }

        return password;

    }




    //-------------------------------------------
//	AES-128 key is needed for encrypt and decrypt
//  Key should be a global variable for encrypt and decrypt functions.
//
    public static void getAESKey(String userGivenKey){
        byte[] keyValue = userGivenKey.getBytes();
        key = new SecretKeySpec(keyValue, algo);
    }


    public static String encrypt(String msg) throws Exception {
        Cipher c = Cipher.getInstance(algo);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(msg.getBytes());
       // String encryptedValue = new BASE64Encoder().encode(encVal);
        String encryptedValue = Base64.encodeToString(encVal,Base64.DEFAULT);
        return encryptedValue;
    }

    public static String decrypt(String msg) throws Exception{
        Cipher c = Cipher.getInstance(algo);
        c.init(Cipher.DECRYPT_MODE, key);
        //byte[] decordedValue = new BASE64Decoder().decodeBuffer(msg);
        byte[] decordedValue = Base64.decode(msg, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);

        return decryptedValue;


    }

    //Checks if original hashed password matches the old password entered by the user when changing password.
    public static boolean checkPasswordValidity(String oldPassword) throws Exception {
        String temp_new = oldPassword;
        String hashed_temp_new = "";
        try {
            hashed_temp_new = HashSHA(temp_new);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(masterpass_hashed.equals(hashed_temp_new)){
            return true;
        }
        return false;
    }









}
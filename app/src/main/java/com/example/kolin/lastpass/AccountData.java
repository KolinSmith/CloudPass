package com.example.kolin.lastpass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by laluvjohn on 11/24/2015.
 */
public class AccountData implements Parcelable{
    private String siteName ="";
    private String userName ="";
    private String password ="";

    public AccountData(){}

    public AccountData(Parcel in){
        siteName = in.readString();
        userName = in.readString();
        password = in.readString();
    }
    public AccountData(String siteName, String userName, String password){
        this.siteName = siteName;
        this.userName = userName;
        this.password = password;
    }

    public void setSiteName(String siteName){
        this.siteName = siteName;
    }

    public String getSiteName(){
        return siteName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return userName;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(siteName);
        dest.writeString(userName);
        dest.writeString(password);
    }

    public static final Parcelable.Creator<AccountData> CREATOR
            = new Parcelable.Creator<AccountData>(){

            public AccountData createFromParcel(Parcel in){
                return new AccountData(in);
            }

            public AccountData[] newArray(int size){
                return new AccountData[size];
            }
    };
}

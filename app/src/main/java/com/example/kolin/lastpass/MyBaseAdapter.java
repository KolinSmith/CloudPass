package com.example.kolin.lastpass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by laluvjohn on 11/24/2015.
 */
public class MyBaseAdapter extends BaseAdapter {
    private static ArrayList<AccountData> accountDataArrayList;

    private LayoutInflater myInflater;

    public MyBaseAdapter(Context context, ArrayList<AccountData> incoming){
        accountDataArrayList = incoming;
        myInflater = LayoutInflater.from(context);
    }

    public int getCount(){
        return accountDataArrayList.size();
    }

    public long getItemId(int position){
        return position;
    }

    public Object getItem(int position){
        return accountDataArrayList.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if(convertView == null){
            convertView = myInflater.inflate(R.layout.row_layout, null);
            holder = new ViewHolder();
            holder.siteName = (TextView) convertView.findViewById(R.id.textView1);
            holder.userName = (TextView) convertView.findViewById(R.id.textView2);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.siteName.setText(accountDataArrayList.get(position).getSiteName());
        holder.userName.setText(accountDataArrayList.get(position).getUserName());

        return convertView;
    }

    static class ViewHolder {
        TextView siteName;
        TextView userName;
    }

}

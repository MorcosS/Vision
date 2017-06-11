package com.google.android.gms.samples.vision.ocrreader;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.samples.vision.ocrreader.Models.Customers;

import java.util.ArrayList;

/**
 * Created by MorcosS on 2/27/17.
 */

public class CustomerListAdapter extends BaseAdapter {
    ArrayList<Customers> list;
    LayoutInflater inflater;
    Activity activity;


    public CustomerListAdapter(ArrayList<Customers> list, Activity activity) {
        inflater = activity.getLayoutInflater();
        this.list = list;
        this.activity = activity;

    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int i1 = i;
        view = inflater.inflate(R.layout.customer_list_layout,null);
        TextView header = (TextView) view.findViewById(R.id.header);
        TextView footer = (TextView) view.findViewById(R.id.footer);
        header.setText(list.get(i).getCst_ParCode());
        footer.setText(list.get(i).getCst_Name());
        return view;
    }
}

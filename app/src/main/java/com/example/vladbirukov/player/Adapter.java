package com.example.vladbirukov.player;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class Adapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Category> objects;

    Adapter(Context context, ArrayList<Category> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        Category p = getProduct(position);


        ((TextView) view.findViewById(R.id.name)).setText(p.name);
        ((ImageView) view.findViewById(R.id.image)).setImageResource(p.image);


        return view;
    }

    Category getProduct(int position) {
        return ((Category) getItem(position));
    }


}
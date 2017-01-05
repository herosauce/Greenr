package com.thehobbygardener.greenr;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.Locale;


public class PlantRowAdapter extends BaseAdapter {

    ArrayList<PlantRow> list;
    Context c;

    private ArrayList<PlantRow> privateArray;

    public PlantRowAdapter(Context c, ArrayList<PlantRow> list) {
        this.list = list;
        this.c = c;
        privateArray = new ArrayList<>();
        privateArray.addAll(list);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row =  inflater.inflate(R.layout.plant_item,null,true);

        TextView name   = (TextView) row.findViewById(R.id.plant_name);
        TextView description = (TextView) row.findViewById(R.id.plant_desc_short);
        ImageView image = (ImageView) row.findViewById(R.id.plant_icon);

        PlantRow plantRow = list.get(position);

        name.setText(plantRow.name);
        description.setText(plantRow.description_short);
        image.setImageResource(plantRow.imageId);

        return row;
    }


    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length()==0){
            list.addAll(privateArray);
        } else {
            for (PlantRow s : privateArray) {
                if (s.name.toLowerCase(Locale.getDefault()).contains(charText)){
                    list.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }

}

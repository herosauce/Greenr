package com.thehobbygardener.greenr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class SeedRowAdapter extends BaseAdapter{

    ArrayList<SeedTemplate> list;
    Context c;

    private ArrayList<SeedTemplate> privateArray;

    public SeedRowAdapter(Context c, ArrayList<SeedTemplate> list) {
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
        View row = inflater.inflate(R.layout.seed_item, null, true);

        TextView name = (TextView) row.findViewById(R.id.tv_seed_name);
        ImageButton seedFront = (ImageButton) row.findViewById(R.id.ib_seed_front);
        ImageButton seedBack = (ImageButton) row.findViewById(R.id.ib_seed_back);

        SeedTemplate seedTemplate = list.get(position);

        name.setText(seedTemplate.name);

        //Set image views
        if (seedTemplate.frontPhotoPath != null){
            setImage(seedFront, seedTemplate.frontPhotoPath);
        }
        if (seedTemplate.backPhotoPath != null){ setImage(seedBack, seedTemplate.backPhotoPath);}

        return row;
    }

    private void setImage(ImageButton button, String photoPath) {
        //Get dimensions of the view
        int targetW = 64;
        int targetH = 64;

        //Get dimensions of the bitmap
        BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
        bmpOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmpOptions);
        int photoW = bmpOptions.outWidth;
        int photoH = bmpOptions.outHeight;

        //Determine how much you want to scale the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        //Decode the image file into a bitmap sized to fill the image view
        bmpOptions.inJustDecodeBounds = false;
        bmpOptions.inSampleSize = scaleFactor;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmpOptions);
        button.setImageBitmap(bitmap);
    }
}

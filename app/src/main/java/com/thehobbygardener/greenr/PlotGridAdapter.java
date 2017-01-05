package com.thehobbygardener.greenr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class PlotGridAdapter extends BaseAdapter {

    ArrayList<GridSquare> list;
    Context context;
    private ArrayList<GridSquare> privateArray;

    //Constructor
    public PlotGridAdapter(Context c, ArrayList<GridSquare> list) {
        this.context = c;
        this.list = list;
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

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView = inflater.inflate(R.layout.plot_grid_item, null, false);
        ImageView imageView = (ImageView) gridView.findViewById(R.id.iv_grid_square);

        GridSquare gridSquare = list.get(position);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(gridSquare.index);

        return gridView;
    }
}
/*
Below was an attempt that didn't work - saving code just in case.
public class PlotGridAdapter extends BaseAdapter {

    private Context context;
    private PlotTemplate plotTemplate;
    private ArrayList<Integer> index;

    //Constructor
    public PlotGridAdapter(Context c, PlotTemplate plotTemplate, ArrayList<Integer> index) {
        this.context = c;
        this.plotTemplate = plotTemplate;
        this.index = index;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView gridImage;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridSquare = inflater.inflate(R.layout.plot_grid_item, null, false);

        if (convertView == null) {
            gridImage = (ImageView) gridSquare.findViewById(R.id.iv_grid_square);
            gridImage.setPadding(2, 2, 2, 2);
        }
        else
        {
            gridImage = (ImageView) convertView;
        }

        //HashMap for determining proper icon for each square
        HashMap<String, Integer> iconMap = new HashMap<>();
        iconMap.put("empty", R.drawable.question_64);
        iconMap.put("vegetable", R.drawable.raddish_64);
        iconMap.put("herb", R.drawable.basil_64);
        iconMap.put("fruit", R.drawable.strawberry_64);

        int gridIndex = index.get(position);

        //Use the current grid position to set the image, as defined in the map in the plot template
        gridImage.setImageResource(iconMap.get(plotTemplate.gridMap.get(gridIndex)));

        return gridSquare;
    }
}

 */
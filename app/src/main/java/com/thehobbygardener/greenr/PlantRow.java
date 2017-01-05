package com.thehobbygardener.greenr;


import android.widget.ImageView;

public class PlantRow {
    Integer imageId;
    String name;
    String description_short;
    String description_full;

    public PlantRow(Integer integer, String name, String description_short, String descrition_full) {
        this.imageId = integer;
        this.name = name;
        this.description_short = description_short;
        this.description_full = descrition_full;
    }
}

package com.thehobbygardener.greenr;

import java.util.HashMap;

/**
 * Created by herosauce on 9/22/2016.
 */
public class PlotTemplate {

    String name;
    int length;
    int width;
    HashMap<Integer, String> gridMap;

    public PlotTemplate(String name, int length, int width, HashMap<Integer, String> gridMap) {
        this.name = name;
        this.length = length;
        this.width = width;
        this.gridMap = gridMap;
    }
}

package com.thehobbygardener.greenr;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;


public class FragPlotDimens extends DialogFragment {

    public static final String MY_PLOTS = "MyPlotsFile";
    SharedPreferences sp;

    Button bSave, bCancel;

    private EditText plotName;
    private EditText plotWidth;
    private EditText plotLength;

    private PlotTemplate plot;
    private Boolean existingPlot;
    private String existingPlotName;

    public FragPlotDimens() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_plot_dimens, container);

        plotName = (EditText) view.findViewById(R.id.et_plot_name);
        plotWidth = (EditText) view.findViewById(R.id.et_plot_width);
        plotLength = (EditText) view.findViewById(R.id.et_plot_length);

        //Check bundle in case this plot already exists
        existingPlot = false;
        String nullName = "NULL";
        sp = this.getActivity().getSharedPreferences(MY_PLOTS, Context.MODE_PRIVATE);

        if (!nullName.equals(this.getArguments().getString("Name"))){
            //First, set existing token
            existingPlot = true;

            //Retrieve existing plot object
            Gson retrieveGson = new Gson();
            existingPlotName = sp.getString(this.getArguments().getString("Name"), "");
            plot = retrieveGson.fromJson(existingPlotName, PlotTemplate.class);

            //update TextViews based on existing plot information
            plotName.setText(plot.name);
            plotWidth.setText(""+plot.width);
            plotLength.setText(""+plot.length);

        } else {
            //Instantiate plot object
            HashMap<Integer, String> plotMap = new HashMap<>();
            plot = new PlotTemplate(null, 0, 0, plotMap);
        }

        bSave = (Button) view.findViewById(R.id.plot_save);
        bCancel = (Button) view.findViewById(R.id.plot_cancel);


        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (plotName.getText().toString().equals("")){
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter a plot name before saving.", Toast.LENGTH_SHORT).show();
                } else {
                    plot.name = plotName.getText().toString();
                }

                //Verify that width and length parameters have been set
                String strWidth = plotWidth.getText().toString();
                if (strWidth.equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter a plot width before saving.", Toast.LENGTH_SHORT).show();
                } else {
                    plot.width = Integer.parseInt(strWidth);
                }

                String strLength = plotLength.getText().toString();
                if (strLength.equals("")){
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter a plot length before saving.", Toast.LENGTH_SHORT).show();
                } else {
                    plot.length = Integer.parseInt(strLength);
                }

                //TODO: don't allow anything to save unless the above reqs are met - create valid check method, return bool
                SharedPreferences.Editor editor = sp.edit();
                //Before saving, check if there was an existing plot and delete it to avoid duplication
                //TODO: this also appears broken
                if (existingPlot){
                    sp.edit().remove(existingPlotName).apply();
                }

                //Last thing before saving: create hashmap of empty, unplanted squares
                int plotSize = plot.length*plot.width;
                for (int i=0; i<plotSize; i++){
                    plot.gridMap.put(i, "empty");
                }

                Gson gson = new Gson();
                String json = gson.toJson(plot);

                editor.putString(plot.name, json);
                editor.apply();
                getDialog().dismiss();
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onDismiss(final DialogInterface dialog){
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener){
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

}

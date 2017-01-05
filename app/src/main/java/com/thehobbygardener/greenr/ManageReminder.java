package com.thehobbygardener.greenr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class ManageReminder extends AppCompatActivity {

    public static final String MY_REMINDERS = "MyReminderFile";
    SharedPreferences sp;

    //Plots SP file, to get plants currently in garden
    public static final String MY_PLOTS = "MyPlotsFile";

    private TextView reminderType, gardenPlant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_reminder);

        //Set a toolbar
        Toolbar main_toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(main_toolbar);

        main_toolbar.setTitle(R.string.tb_title);
        main_toolbar.setSubtitle(R.string.tb_subtitle);
        main_toolbar.setNavigationIcon(R.drawable.house_icon_xsmall);

        main_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        //Create reminder object to start setting attributes
        final ReminderRow reminderRow = new ReminderRow("", "", null, false, 0, false);

        //Set up selection for type of reminder, then set the object property
        Spinner spinReminderType = (Spinner) findViewById(R.id.spinRemindType);
        final ArrayList<String> reminderTypeList = new ArrayList<>();
        String[] rTypes = {
                "Watering",
                "Planting",
                "Weeding",
                "Pruning",
                "Aerating",
                "Fertilizing"
        };
        Collections.addAll(reminderTypeList, rTypes);
        ArrayAdapter<String> rtAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, reminderTypeList);
        spinReminderType.setAdapter(rtAdapter);
        spinReminderType.setSelection(rtAdapter.getPosition(rTypes[1]));

        reminderType = (TextView) findViewById(R.id.tv_remind_type);
        spinReminderType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reminderRow.taskName = reminderTypeList.get(position);
                reminderType.setText(reminderTypeList.get(position));
                //Want to make sure we reset the text color, in case someone selects nothing, as below
                reminderType.setTextColor(Color.parseColor("#009989"));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                reminderType.setText("Please choose a reminder type");
                reminderType.setTextColor(Color.RED);
            }
        });

        //Setting up plant spinner
        Spinner spinPlant = (Spinner) findViewById(R.id.spinPlant);
        final ArrayList<String> plantList = getCurrentGardenPlants();
        //ArrayList<String> plantList = new ArrayList<>();
        String[] plantTypes = {
                "Herb",
                "Fruit",
                "Vegetable",
                "Other"
        };
        //Check size of plantList. If it only has one thing ("other"), then use the list above instead
        if (plantList.size()<2){
            plantList.clear();
            Collections.addAll(plantList, plantTypes);
        }
        ArrayAdapter<String> plantAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, plantList);
        spinPlant.setAdapter(plantAdapter);

        gardenPlant = (TextView) findViewById(R.id.tv_garden_plants);
        spinPlant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reminderRow.plantName = plantList.get(position);
                gardenPlant.setText(plantList.get(position));
                gardenPlant.setTextColor(Color.parseColor("#009989"));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gardenPlant.setText("2. Select a plant from your garden (optional)");
                //Make the color gray since it doesn't really matter that much
                gardenPlant.setTextColor(Color.GRAY);
            }
        });

        final EditText actionFreq = (EditText) findViewById(R.id.et_freq);
        //TODO: at end of script, set reminderRow attribute for freq
        //TODO: add code to handle a 0 freq as a one-time reminder (or to display a warning for repeating reminders every 0 days)
        actionFreq.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String sFreq = actionFreq.getText().toString();
                Integer frequency = Integer.parseInt(sFreq);

                TextView day_or_days = (TextView) findViewById(R.id.tv_days);

                if (frequency.equals(0)){
                    day_or_days.setText("days (non-repeating reminder)");
                } else if (frequency.equals(1)) {
                    day_or_days.setText("day");
                } else {
                    day_or_days.setText("days");
                }
            }
        });

    }

    public ArrayList<String> getCurrentGardenPlants(){
        ArrayList<String> currentPlants = new ArrayList<>();
        //Iterate through each plot in the garden first
        final SharedPreferences plotSP = getSharedPreferences(MY_PLOTS, MODE_PRIVATE);
        Map<String, ?> allPlots = plotSP.getAll();

        for (final Map.Entry<String, ?> entry: allPlots.entrySet()) {
            //Start by parsing each plot as its own object
            Gson retrieveGson = new Gson();
            String retrieveJson = plotSP.getString(entry.getKey(), "");
            final PlotTemplate currentPlot = retrieveGson.fromJson(retrieveJson, PlotTemplate.class);

            //Now to iterate through the current plot's grid to start storing current plants
            for (int i=0; i<currentPlot.width*currentPlot.length; i++){
                String currentPlant = currentPlot.gridMap.get(i);
                //Check that this plant is unique and not an "empty" placeholder
                if (!currentPlant.equals("empty")){
                    if (!currentPlants.contains(currentPlant)){
                        currentPlants.add(currentPlant);
                    }
                }
            }
        }

        //Add an "other" option at the end, just in case
        currentPlants.add("other");
        return currentPlants;
    }
}

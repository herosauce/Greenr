package com.thehobbygardener.greenr;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Date;

public class PlantDetails extends AppCompatActivity {

    Button bRemove, bHarvest, bReminder, bLog;
    TextView tvSunReqs, tvPH, tvSoilType, tvCompanionPlants, tvAllyPlants, tvEnemyPlants;
    private Cursor pDetails;
    private PlantDatabase db;

    public static final String MY_PLOTS = "MyPlotsFile";
    SharedPreferences plotSP;

    public static final String MY_DAILY_LOG = "MyDailyLogFile";
    SharedPreferences logSP;

    public static final String EVENT_DAYS = "MyEventDays";
    SharedPreferences eventSP;

    public static final String MY_REMINDERS = "MyRemindersFile";
    SharedPreferences reminderSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);

        //Get intent information
        Intent intent = getIntent();
        final String plotName = intent.getStringExtra("Name");
        final String plantName = intent.getStringExtra("Plant");
        final Integer plotGridPosition = intent.getIntExtra("Position", 0);
        final Boolean dictLookup = intent.getBooleanExtra("Lookup", false);

        //Get plant info from database
        //TODO: finish this section


        //Remove buttons from the top of the screen if this is a lookup //TODO test this
        LinearLayout buttonHolder = (LinearLayout) findViewById(R.id.ll_button_holder);
        if (dictLookup){
            buttonHolder.removeAllViews();
        } else {
            //Get plot for sake of editing
            plotSP = getSharedPreferences(MY_PLOTS, MODE_PRIVATE);
            Gson retrieveGson = new Gson();
            String retrieveJson = plotSP.getString(plotName, "");
            final PlotTemplate plotTemplate = retrieveGson.fromJson(retrieveJson, PlotTemplate.class);

            //Button handler to remove the selected plant from the garden
            bRemove = (Button) findViewById(R.id.button_remove);
            bRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Reset grid map to "empty"
                    plotTemplate.gridMap.put(plotGridPosition, "empty");
                    //Save change
                    Gson gson = new Gson();
                    String json = gson.toJson(plotTemplate);
                    plotSP.edit().putString(plotName, json).apply();
                    //Go back to Plot Manager
                    Intent startPlotManager = new Intent(getApplicationContext(), PlotManager.class);
                    startPlotManager.putExtra("Name", plotName);
                    startActivity(startPlotManager);
                }
            });

            //Button handler to launch Daily Log fragment
            bLog = (Button) findViewById(R.id.button_log);
            bLog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Launch a fragment here for creating a new day log
                    FragmentManager manager = getFragmentManager();
                    Fragment frag = manager.findFragmentByTag("fragment_edit_id");
                    if (frag != null) {
                        manager.beginTransaction().remove(frag).commit();
                    }
                    //Grab today's date
                    Date date = new Date();
                    FragDailyLog newLog = new FragDailyLog();
                    //Pass date into dialog
                    Bundle dateBundle = new Bundle();
                    dateBundle.putLong("date", date.getTime());
                    dateBundle.putString("Plant", plantName);
                    //TODO: update Daily Log Frag to list plants in garden, unless something is selected here, in which case default in that plant
                    newLog.setArguments(dateBundle);
                    newLog.show(manager, "fragment_edit_id");
                }
            });

            //Button and handler for reminders
            bReminder = (Button) findViewById(R.id.button_reminders);
            bReminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent addReminder = new Intent(getApplicationContext(), ManageReminder.class);
                    addReminder.putExtra("Plant", plantName);
                    startActivity(addReminder);
                }
            });

            //Button and handler for harvesting
            //TODO: build dialog to document yield and store harvest info
            bHarvest = (Button) findViewById(R.id.button_harvest);
            bHarvest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Reset grid map to "empty"
                    plotTemplate.gridMap.put(plotGridPosition, "empty");
                    //Save change
                    Gson gson = new Gson();
                    String json = gson.toJson(plotTemplate);
                    plotSP.edit().putString(plotName, json).apply();
                    //Go back to Plot Manager TODO: should launch a harvest dialog first
                    Intent startPlotManager = new Intent(getApplicationContext(), PlotManager.class);
                    startPlotManager.putExtra("Name", plotName);
                    startActivity(startPlotManager);
                }
            });
        }

    }
}

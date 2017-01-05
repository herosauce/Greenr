package com.thehobbygardener.greenr;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Reminders extends AppCompatActivity {

    public static final String MY_REMINDERS = "MyRemindersFile";
    SharedPreferences sp;

    ArrayList<ReminderRow> todayArray, upcomingArray, completedArray;
    ListView todayList, upcomingList, completeList;

    //Date and strings for reminder comparison
    Date today;
    String strToday, strThisMonth, strThisYear;

    private PlantRowAdapter adapter;

    String[] reminder = {
            "Water",
            "Weed",
            "Prune",
            "Fertilize",
            "Aerate",
            "Transplant",
            "Harvest"
    };

    TabHost tabHost;

    Integer imageId[] = {
            R.drawable.pruning_shears_64,
            R.drawable.watering_can_64
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        todayList = (ListView) findViewById(R.id.todayReminderList);
        upcomingList = (ListView) findViewById(R.id.upcomingReminderList);
        completeList = (ListView) findViewById(R.id.completedReminderList);

        todayArray = new ArrayList<>();
        upcomingArray = new ArrayList<>();
        completedArray = new ArrayList<>();

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec("Today");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Today");
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec("Upcoming");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Upcoming");
        tabHost.addTab(spec);

        //Tab 3
        spec = tabHost.newTabSpec("Complete");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Complete");
        tabHost.addTab(spec);

        //Set up strings for date comparison
        today = new Date();
        strToday = (String) android.text.format.DateFormat.format("dd", today);
        strThisMonth = (String) android.text.format.DateFormat.format("MMMM", today);
        strThisYear = (String) android.text.format.DateFormat.format("YYY", today);

        //Iterate through MyReminderFile to get all reminders, then add to appropriate array
        sp = getSharedPreferences(MY_REMINDERS, MODE_PRIVATE);
        Map<String, ?> allReminders = sp.getAll();
        for (final Map.Entry<String, ?> entry : allReminders.entrySet()){
            //Start by getting the reminder object
            Gson retrieveGson = new Gson();
            String retrieveJson = sp.getString(entry.getKey(), "");
            ReminderRow currentReminder = retrieveGson.fromJson(retrieveJson, ReminderRow.class);

            //Parse reminder start date
            Date reminderDate = new Date();
            reminderDate.setTime(currentReminder.startDate);
            String remindDay = (String) android.text.format.DateFormat.format("dd", reminderDate);
            String remindMonth = (String) android.text.format.DateFormat.format("MMMM", reminderDate);
            String remindYear = (String) android.text.format.DateFormat.format("YYY", reminderDate);
            //First check: is this reminder already complete?
            if (currentReminder.taskComplete){
                completedArray.add(currentReminder);
            } else if (strToday.equals(remindDay) && strThisMonth.equals(remindMonth) && strThisYear.equals(remindYear)){
                //Second check: is this reminder for today? If so:
                todayArray.add(currentReminder);
            } else {
                //Not today? Must be upcoming.
                upcomingArray.add(currentReminder);
            }
        }

        ReminderRowAdapter todayAdapter = new ReminderRowAdapter(this, todayArray);
        todayList.setAdapter(todayAdapter);
        //TODO set item click listener (for this and the two below

        ReminderRowAdapter upcomingAdapter = new ReminderRowAdapter(this, upcomingArray);
        upcomingList.setAdapter(upcomingAdapter);

        ReminderRowAdapter completedAdapter = new ReminderRowAdapter(this, completedArray);
        completeList.setAdapter(completedAdapter);
    }
}

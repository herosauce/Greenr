package com.thehobbygardener.greenr;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

public class Calendar extends AppCompatActivity implements DialogInterface.OnDismissListener{

    //TODO: Include option for OCD folks to auto-event days on checking app
    public static final String EVENT_DAYS = "MyEventDays";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

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

        updateCalendar(); //Sets icons for days that have events

        MyCalendar cv = ((MyCalendar)findViewById(R.id.home_calendar));
        // assign event handler
        cv.setEventHandler(new MyCalendar.EventHandler() {
            @Override
            public void onDayLongPress(Date date) {
                //Launch a fragment here for creating a new day log
                FragmentManager manager = getFragmentManager();
                Fragment frag = manager.findFragmentByTag("fragment_edit_id");
                if (frag != null) {
                    manager.beginTransaction().remove(frag).commit();
                }

                FragDailyLog newLog = new FragDailyLog();
                //Pass date into dialog
                Bundle dateBundle = new Bundle();
                dateBundle.putLong("date", date.getTime());
                newLog.setArguments(dateBundle);
                newLog.show(manager, "fragment_edit_id");
            }
        });

        Button addReminder = (Button) findViewById(R.id.add_reminder);
        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startNewReminder = new Intent(getApplicationContext(), ManageReminder.class);
                startActivity(startNewReminder);
            }
        });

        Button manageReminders = (Button) findViewById(R.id.manage_reminders);
        manageReminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startOldReminder = new Intent(getApplicationContext(), Reminders.class);
                startActivity(startOldReminder);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.menu_1:
                Intent startCalendar = new Intent(getApplicationContext(), Calendar.class);
                startActivity(startCalendar);
                break;

            case R.id.menu_2:
                Intent startSettings = new Intent(getApplicationContext(), Settings.class);
                startActivity(startSettings);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateCalendar(){
        HashSet<Date> events = new HashSet<>();

        final SharedPreferences eventSP = getSharedPreferences(EVENT_DAYS, MODE_PRIVATE);
        Map<String, ?> allEvents = eventSP.getAll();
        for (final Map.Entry<String, ?> day : allEvents.entrySet()){
            //For each date that has saved information, load it into the event set
            Date newEventDay = new Date();
            newEventDay.setTime(eventSP.getLong(day.getKey(), -1));
            events.add(newEventDay);
        }

        MyCalendar cv = ((MyCalendar)findViewById(R.id.home_calendar));
        cv.updateCalendar(events);
    }

    //Works together with override method in dialog fragment to trigger onDismiss event
    @Override
    public void onDismiss(final DialogInterface dialog){updateCalendar();}
}

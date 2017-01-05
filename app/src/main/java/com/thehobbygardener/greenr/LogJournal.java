package com.thehobbygardener.greenr;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LogJournal extends AppCompatActivity {

    //Daily Log file stores log template objects, which include all the info we need to display in this activity
    public static final String MY_DAILY_LOG = "MyDailyLogFile";
    SharedPreferences logSP;

    //Adapter items
    ArrayList<DailyLogTemplate> logArray;
    ListView listView;
    JournalRowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_journal);

        //Set up toolbar
        Toolbar main_toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(main_toolbar);

        main_toolbar.setTitle("Log Journal");
        main_toolbar.setSubtitle("Lists all days with logged events");
        main_toolbar.setNavigationIcon(R.drawable.house_icon_xsmall);

        //First, create and update list of logged days
        logArray = new ArrayList<>();
        //Iterate through MyDailyLogFile to get all logged days
        logSP = getSharedPreferences(MY_DAILY_LOG, MODE_PRIVATE);
        Map<String, ?> allLogs = logSP.getAll();
        for (final Map.Entry<String, ?> entry: allLogs.entrySet()){
            Gson retrieveGson = new Gson();
            DailyLogTemplate logTemplate = retrieveGson.fromJson(entry.getValue().toString(), DailyLogTemplate.class);
            logArray.add(logTemplate);
        }
        //TODO: after populating array, sort based on date
        //Initialize listview and set adapter
        listView = (ListView) findViewById(R.id.journal_list);
        adapter = new JournalRowAdapter(this, logArray);
        listView.setAdapter(adapter);
        //Set listener to launch fragment to update/review logged info
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DailyLogTemplate selectedLog = logArray.get(position);
                //Launch a fragment here for creating a new day log
                FragmentManager manager = getFragmentManager();
                Fragment frag = manager.findFragmentByTag("fragment_edit_id");
                if (frag != null) {
                    manager.beginTransaction().remove(frag).commit();
                }

                FragDailyLog newLog = new FragDailyLog();
                //Pass date into dialog
                Bundle dateBundle = new Bundle();
                dateBundle.putLong("date", selectedLog.date);
                newLog.setArguments(dateBundle);
                newLog.show(manager, "fragment_edit_id");
            }
        });
    }
}

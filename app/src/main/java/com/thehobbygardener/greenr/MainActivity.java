package com.thehobbygardener.greenr;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mNavigationActivities = {
            "Badges",
            "My Garden",
            "Log Journal",
            "Calendar",
            "Encyclopedia",
            "Cost Planner",
            "Seed Bank",
            "Garden Gallery",
            "Graphs",
            "My Avatar",
            "App Settings"
    };

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        Toolbar main_toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(main_toolbar);

        main_toolbar.setTitle(R.string.tb_title);
        main_toolbar.setSubtitle(R.string.tb_subtitle);
        main_toolbar.setNavigationIcon(R.drawable.house_icon_xsmall);

        Button bCalendar = (Button) findViewById(R.id.button_calendar);
        bCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startCalendar = new Intent(getApplicationContext(), Calendar.class);
                startActivity(startCalendar);
            }
        });

        Button bGarden = (Button) findViewById(R.id.button_garden);
        bGarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startGarden = new Intent(getApplicationContext(), MyGarden.class);
                startActivity(startGarden);
            }
        });

        //Setting up code to handle drawer navigation
        //TODO: make the drawer actually look good
        //TODO: add the drawer to all other activities
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.navigation_list_item, mNavigationActivities));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String activity = mNavigationActivities[position];
                switch (activity){
                    case "Badges":
                        startActivity(new Intent(getApplicationContext(), Achievements.class));
                        break;
                    case "My Garden":
                        startActivity(new Intent(getApplicationContext(), MyGarden.class));
                        break;
                    case "Calendar":
                        startActivity(new Intent(getApplicationContext(), MyCalendar.class));
                        break;
                    case "App Settings":
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        break;
                    case "Log Journal":
                        startActivity(new Intent(getApplicationContext(), LogJournal.class));
                        break;
                    case "Encyclopedia":
                        Intent intent = new Intent(getApplicationContext(), PlantTypeSelection.class);
                        intent.putExtra("Lookup", true);
                        startActivity(intent);
                        break;
                    case "My Avatar":
                        startActivity(new Intent(getApplicationContext(), AvatarDesigner.class));
                        break;
                    case "Cost Planner":
                        startActivity(new Intent(getApplicationContext(), CostPlanner.class));
                        break;
                    case "Seed Bank":
                        startActivity(new Intent(getApplicationContext(), SeedBank.class));
                        break;
                    case "Garden Gallery":
                        startActivity(new Intent(getApplicationContext(), GardenGallery.class));
                        break;
                    case "Graphs":
                        startActivity(new Intent(getApplicationContext(), AnalyticsDashboard.class));
                        break;
                }
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
}

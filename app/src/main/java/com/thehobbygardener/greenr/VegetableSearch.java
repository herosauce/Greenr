package com.thehobbygardener.greenr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

public class VegetableSearch extends AppCompatActivity {

    ArrayList<PlantRow> rowInfo;
    ListView listView;
    EditText inputSearch;
    private PlantRowAdapter adapter;

    public static final String MY_PLOTS = "MyPlotsFile";

    String vegetable[] = {
            "Amaranth",
            "Artichoke",
            "Arugula",
            "Asian greens",
            "Asparagus",
            "Bean",
            "Beet",
            "Bell pepper",
            "Broccoli",
            "Broccoli Arab",
            "Brussels sprouts",
            "Cabbage",
            "Carrot",
            "Cauliflower",
            "Celeriac",
            "Celery",
            "Chard",
            "Chinese cabbage",
            "Collards",
            "Corn",
            "Cowpea",
            "Cucumber",
            "Eggplant",
            "Endive",
            "Fennel",
            "Garlic",
            "Gourd",
            "Hot pepper",
            "Jerusalem artichoke",
            "Kale",
            "Kohlrabi",
            "Leek",
            "Lettuce",
            "Lima bean",
            "Malabar spinach",
            "Melon",
            "New Zealand spinach",
            "Okra",
            "Onion",
            "Parsnip",
            "Pea",
            "Peanut",
            "Potato",
            "Pumpkin",
            "Radicchio",
            "Radish",
            "Rhubarb",
            "Rutabaga",
            "Spinach",
            "Summer squash",
            "Sweet potato",
            "Tomatillo",
            "Tomato",
            "Turnip",
            "Watermelon",
            "Winter squash"
    };

    Integer[] imageId = {
            R.drawable.raddish_64
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegetable_search);

        //Get current plot name and grid position from intent
        Intent intent = getIntent();
        final String plotName = intent.getStringExtra("Name");
        final Integer gridPos = intent.getIntExtra("Position", 0);

        listView = (ListView) findViewById(R.id.list);

        rowInfo = new ArrayList<>();

        String name;
        String short_desc;
        Integer iId;

        for (Integer x=0; x < vegetable.length; x++) {
            name = vegetable[x];
            //TODO: update below to pull description and image
            short_desc = vegetable[x];
            iId = 0;
            PlantRow plant = new PlantRow(imageId[iId], name, short_desc, "NULL");
            rowInfo.add(plant);
        }

        adapter = new PlantRowAdapter(this, rowInfo);

        inputSearch = (EditText) findViewById(R.id.plant_search);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
                //Log.i("Filter", s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }
        });

        final SharedPreferences plotSP = getSharedPreferences(MY_PLOTS, MODE_PRIVATE);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String plantName = ((TextView) view.findViewById(R.id.plant_name)).getText().toString();
                //TODO: Store up to 4 recent plants in a special recent plants SP file
                //TODO: add a "Recent plants" listview/adapter to display those recent things, if available

                //Get plot object and update map before re-opening PlotManager
                Gson retrieveGson = new Gson();
                String retrieveJson = plotSP.getString(plotName, "");
                final PlotTemplate currentPlot = retrieveGson.fromJson(retrieveJson, PlotTemplate.class);

                //Update map
                currentPlot.gridMap.put(gridPos, plantName);
                //Save updates
                Gson gson = new Gson();
                String json = gson.toJson(currentPlot);
                plotSP.edit().putString(plotName, json).apply();

                //Finally, go back to Plot Manager
                Intent startPM = new Intent(getApplicationContext(), PlotManager.class);
                startPM.putExtra("Name", plotName);
                startActivity(startPM);
            }
        });
    }

}

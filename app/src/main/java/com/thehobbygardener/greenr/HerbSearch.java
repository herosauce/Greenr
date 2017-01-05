package com.thehobbygardener.greenr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class HerbSearch extends Activity {

    ArrayList<PlantRow> rowInfo;
    ListView listView;
    EditText inputSearch;
    private PlantRowAdapter adapter;

    public static final String MY_PLOTS = "MyPlotsFile";

    String herb[] = {
            "Aloe Vera",
            "Angelica",
            "Basil",
            "Bayberry",
            "Borage",
            "Calamint",
            "Caraway",
            "Catnip",
            "Chamomile",
            "Chervil",
            "Chives",
            "Cilantro",
            "Coriander",
            "Comfrey",
            "Cuban Oregano",
            "Dill",
            "Epazote",
            "Fennel",
            "Garlic Chives",
            "Ginger",
            "Horehound",
            "Horseradish",
            "Hyssop",
            "Lavendar",
            "Lemon Balm",
            "Lemon Verbena",
            "Lemongrass",
            "Licorice root",
            "Lovage",
            "Marjoram",
            "Milk thistle",
            "Mint",
            "Mullein",
            "Myrtle",
            "Oregano",
            "Parsley",
            "Patchouli",
            "Pennyroyal",
            "Rosemary",
            "Rue",
            "Saffron",
            "Sagen",
            "Salvia",
            "Savory",
            "Scented geranium",
            "Shiso",
            "Sorrel",
            "Stevia",
            "Sweet cicely",
            "Sweet woodruff",
            "Sweetgrass",
            "Tarragon",
            "Thyme",
            "Valerian",
            "Vietnamese coriander",
            "Yarrow"
    };

    Integer[] imageId = {
            R.drawable.basil_64
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_herb_search);

        //Get current plot name and grid position from intent
        Intent intent = getIntent();
        final String plotName = intent.getStringExtra("Name");
        final Integer gridPos = intent.getIntExtra("Position", 0);

        listView = (ListView) findViewById(R.id.list);

        rowInfo = new ArrayList<>();

        String name;
        String short_desc;
        Integer iId;

        for (Integer x=0; x < herb.length; x++) {
            name = herb[x];
            //TODO: update below to pull description and image
            short_desc = herb[x];
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
                Log.i("Filter", s.toString());

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

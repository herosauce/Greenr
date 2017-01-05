package com.thehobbygardener.greenr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PlotManager extends AppCompatActivity {

    public static final String MY_PLOTS = "MyPlotsFile";

    SharedPreferences sp;
    ArrayList<Integer> index;

    //trying something new - which may require some cleanup after the fact
    ArrayList<GridSquare> gridSquareArrayList;

    //HashMap for determining proper icon for each square
    HashMap<String, Integer> iconMap;

    //Adding list of fruit for icon matching
    private String fruit[] = {
            "Apple",
            "Blackberry",
            "Blueberry",
            "Cherry",
            "Fig",
            "Grape",
            "Lemon",
            "Lime",
            "Loquat",
            "Nectarine",
            "Orange",
            "Pawpaw",
            "Peach",
            "Pear",
            "Plum",
            "Raspberry",
            "Strawberry"
    };

    //Ditto - herbs
    private String herb[] = {
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

    //Aaaaand ditto for veggies
    private String vegetable[] = {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_manager);

        //Retrieve plot name (key) and pull plot dimens object
        Intent intent = getIntent();
        final String plotKey = intent.getStringExtra("Name");
        sp = getSharedPreferences(MY_PLOTS, Context.MODE_PRIVATE);
        Gson retrieveGson = new Gson();
        String retrieveJson = sp.getString(plotKey, "");
        final PlotTemplate plotTemplate = retrieveGson.fromJson(retrieveJson, PlotTemplate.class);

        //Use plot length/width to define gridview layout
        GridView gridView = (GridView) findViewById(R.id.plot_grid);
        gridView.setNumColumns(plotTemplate.width);

        //Update Textviews
        TextView tvPlotName = (TextView) findViewById(R.id.tv_plot_name);
        tvPlotName.setText(plotKey);

        TextView tvPlotDimens = (TextView) findViewById(R.id.tv_plot_dimens);
        String dimens = plotTemplate.length +" by "+ plotTemplate.width;
        tvPlotDimens.setText(dimens);

        //Setting up try #2
        gridSquareArrayList = new ArrayList<>();
        iconMap = new HashMap<>();
        iconMap.put("empty", R.drawable.question_64);
        iconMap.put("vegetable", R.drawable.raddish_64);
        iconMap.put("herb", R.drawable.basil_64);
        iconMap.put("fruit", R.drawable.strawberry_64);

        String name;
        String type;
        Integer image;

        for (int i=0; i<plotTemplate.width * plotTemplate.length; i++){
            //TODO: Update object to store plant name
            name = plotTemplate.gridMap.get(i);
            type = plotTemplate.gridMap.get(i);

            //Start by making every square look empty
            image = iconMap.get("empty");

            //Next, check to see if there's actually something there, and match it to a plant type icon
            String imageKey = plotTemplate.gridMap.get(i);
            if (Arrays.asList(fruit).contains(imageKey)){
                image = iconMap.get("fruit");
            } else if (Arrays.asList(herb).contains(imageKey)){
                image = iconMap.get("herb");
            } else if (Arrays.asList(vegetable).contains(imageKey)){
                image = iconMap.get("vegetable");
            }
            GridSquare gs = new GridSquare(name, type, image);
            gridSquareArrayList.add(gs);
        }

        //Set up adapter to populate grid
        PlotGridAdapter gridAdapter = new PlotGridAdapter(this, gridSquareArrayList);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String hasPlant = plotTemplate.gridMap.get(position);
                if (hasPlant.equals("empty")){
                    Intent plantSelect = new Intent(getApplicationContext(), PlantTypeSelection.class);
                    plantSelect.putExtra("Name", plotKey);
                    plantSelect.putExtra("Position", position);
                    startActivity(plantSelect);
                } else {
                    Intent plantInfo = new Intent(getApplicationContext(), PlantDetails.class);
                    //Not really necessary to include the plant itself, but it makes retrieval easier and makes the saving process cleaner for harvesting/removal in Plant Details
                    plantInfo.putExtra("Plant", hasPlant);
                    plantInfo.putExtra("Name", plotKey);
                    plantInfo.putExtra("Position", position);
                    startActivity(plantInfo);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MyGarden.class));
        finish();
    }
}

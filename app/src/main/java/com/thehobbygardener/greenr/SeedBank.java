package com.thehobbygardener.greenr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class SeedBank extends AppCompatActivity {

    public static final String MY_SEED_BANK = "MySeedsFile";
    SharedPreferences seedSP;

    ArrayList<SeedTemplate> seedArray;
    ListView listView;
    private SeedRowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seed_bank);

        listView = (ListView) findViewById(R.id.seed_list);
        seedArray = new ArrayList<>();

        seedSP = getSharedPreferences(MY_SEED_BANK, MODE_PRIVATE);
        Map<String, ?> allSeeds = seedSP.getAll();
        for (Map.Entry<String, ?> entry : allSeeds.entrySet()){
            //Extract seed objects and add them to the array
            Gson retrieveGson = new Gson();
            String retrieveJson = seedSP.getString(entry.getKey(), "");
            final SeedTemplate seed = retrieveGson.fromJson(retrieveJson, SeedTemplate.class);

            seedArray.add(seed);
        }

        adapter = new SeedRowAdapter(this, seedArray);
        listView.setAdapter(adapter);
        //TODO: do I need an on item click listener? How's this gonna work?
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}

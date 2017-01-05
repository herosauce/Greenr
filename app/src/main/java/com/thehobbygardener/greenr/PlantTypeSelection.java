package com.thehobbygardener.greenr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlantTypeSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_type_selection);

        //Retrieve information passed from Plot Manager
        Intent intent = getIntent();
        final String plotName = intent.getStringExtra("Name");
        final Integer gridPos = intent.getIntExtra("Position", 0);
        final Boolean lookup = intent.getBooleanExtra("Lookup", false);
        //Establish buttons and click handlers
        Button searchFruit = (Button) findViewById(R.id.add_fruit);
        searchFruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fruit = new Intent(getApplicationContext(), FruitSearch.class);
                if (lookup) { //if initiated from encyclopedia lookup
                    fruit.putExtra("Lookup", true);
                    startActivity(fruit);
                } else { //if initiated from Plot Manager
                    fruit.putExtra("Name", plotName);
                    fruit.putExtra("Position", gridPos);
                    fruit.putExtra("Type", "fruit");
                    startActivity(fruit);
                }
            }
        });

        Button searchHerbs = (Button) findViewById(R.id.add_herb);
        searchHerbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent herb = new Intent(getApplicationContext(), HerbSearch.class);
                if (lookup) {
                    herb.putExtra("Lookup", true);
                    startActivity(herb);
                } else {
                    herb.putExtra("Name", plotName);
                    herb.putExtra("Position", gridPos);
                    herb.putExtra("Type", "herb");
                    startActivity(herb);
                }
            }
        });

        Button searchVeg = (Button) findViewById(R.id.add_veg);
        searchVeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vegetable = new Intent(getApplicationContext(), VegetableSearch.class);
                if (lookup) {
                    vegetable.putExtra("Lookup", true);
                    startActivity(vegetable);
                } else {
                    vegetable.putExtra("Name", plotName);
                    vegetable.putExtra("Position", gridPos);
                    vegetable.putExtra("Type", "herb");
                    startActivity(vegetable);
                }
            }
        });
    }
}

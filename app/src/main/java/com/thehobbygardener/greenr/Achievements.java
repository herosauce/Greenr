package com.thehobbygardener.greenr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Achievements extends AppCompatActivity {

    //TODO: create a badges SP file
    //TODO: first time app runs, set all badges to false (should happen on main dashboard)
    //TODO: create list of badges
    //TODO: add code for each badge that sets its bool to "true" when criteria are met

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        //TODO: create grid adapter (as with Plot Manager)
    }
}

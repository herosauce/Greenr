package com.thehobbygardener.greenr;

import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Settings extends AppCompatActivity {

    //TODO: feature - add custom tasks and choose an icon to go with it (up to three)
    //TODO: add option to disable "achievement earned" notifications
    private static final String MY_SETTINGS = "MySettingsFile";
    SharedPreferences sp;
    RadioGroup unitsRG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sp = getSharedPreferences(MY_SETTINGS, MODE_PRIVATE);
        RadioGroup unitGroup = (RadioGroup) findViewById(R.id.units_group);
        RadioButton rbFeet = (RadioButton) findViewById(R.id.rb_feet);
        //TODO: figure out how to make radio buttons work

        unitGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_feet:
                        //save feet as units in SP
                        sp.edit().putString("Units", "feet").apply();
                        break;
                    case R.id.rb_meters:
                        //save meters in SP
                        sp.edit().putString("Units", "meters").apply();
                        break;
                }
            }
        });

    }

}

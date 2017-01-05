package com.thehobbygardener.greenr;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Map;

public class MyGarden extends AppCompatActivity implements DialogInterface.OnDismissListener{

    public static final String MY_PLOTS = "MyPlotsFile";

    //TODO: View doesn't currently refresh and populate new plot info like I want it to...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_garden);

        populateGardenPlots();

        FloatingActionButton newPlot = (FloatingActionButton) findViewById(R.id.fab_plot);
        newPlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launch fragment specifying plot dimensions
                FragmentManager manager = getFragmentManager();
                Fragment frag = manager.findFragmentByTag("fragment_edit_id");
                if (frag != null) {
                    manager.beginTransaction().remove(frag).commit();
                }

                //passing null bundle to avoid launch issues
                Bundle nullBundle = new Bundle();
                nullBundle.putString("Name", "NULL");
                FragPlotDimens plotDimens = new FragPlotDimens();
                plotDimens.setArguments(nullBundle);
                plotDimens.show(manager, "fragment_edit_id");
            }
        });
    }

    //Works together with override method in dialog fragment to trigger onDismiss event
    @Override
    public void onDismiss(final DialogInterface dialog){
        populateGardenPlots();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public void populateGardenPlots () {
        //Iterates through saved plots that have been created
        TableRow.LayoutParams buttonRowParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        buttonRowParams.setMargins(8, 8, 8, 8);

        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvParams.setMargins(8,8,8,8);

        final SharedPreferences plotSP = getSharedPreferences(MY_PLOTS, MODE_PRIVATE);
        final LinearLayout plotContainer = (LinearLayout) findViewById(R.id.ll_plot_container);
        //Remove previously existing groups before populating:
        plotContainer.removeAllViews();
        plotContainer.setBackgroundColor(Color.WHITE);

        Map<String, ?> allPlots = plotSP.getAll();
        for (final Map.Entry<String, ?> entry: allPlots.entrySet()){
            //Start by parsing each plot as its own object

            Gson retrieveGson = new Gson();
            String retrieveJson = plotSP.getString(entry.getKey(), "");
            final PlotTemplate currentPlot = retrieveGson.fromJson(retrieveJson, PlotTemplate.class);

            //Add plot information to a Linear Layout
            //Title first:
            final TextView tvPlotTitle = new TextView(getApplicationContext());
            tvPlotTitle.setLayoutParams(tvParams);
            tvPlotTitle.setTextSize(16);
            tvPlotTitle.setPadding(8, 8, 8, 8);
            tvPlotTitle.setText(currentPlot.name);
            tvPlotTitle.setTextColor(Color.BLACK);
            plotContainer.addView(tvPlotTitle);
            launchPlotManager(tvPlotTitle);

            //Dimensions and units
            final TextView tvPlotDimens = new TextView(getApplicationContext());
            tvPlotDimens.setLayoutParams(tvParams);
            tvPlotDimens.setTextSize(12);
            tvPlotDimens.setTextColor(Color.GRAY);
            String units;
            /*
            //TODO: update this with grabbing units from settings SP
            if (currentPlot.metric){
                units = " meters";
            } else {
                units = " feet";
            }
            */
            String dimens = currentPlot.length + " by " + currentPlot.width;
            tvPlotDimens.setText(dimens);
            plotContainer.addView(tvPlotDimens);

            //add edit and delete buttons
            final Button editButton = new Button(getApplicationContext());
            editButton.setText("modify details");
            editButton.setTextColor(Color.WHITE);
            editButton.setBackgroundColor(Color.parseColor("#FF7DFF7D"));
            editButton.setLayoutParams(buttonRowParams);
            editButton.setAllCaps(false);

            final Button deleteButton = new Button(getApplicationContext());
            deleteButton.setText("delete");
            deleteButton.setTextColor(Color.WHITE);
            deleteButton.setBackgroundColor(Color.parseColor("#FFFB4280"));
            deleteButton.setLayoutParams(buttonRowParams);
            deleteButton.setAllCaps(false);

            final LinearLayout buttonHolder = new LinearLayout(getApplicationContext());
            buttonHolder.setOrientation(LinearLayout.HORIZONTAL);

            buttonHolder.addView(editButton);
            buttonHolder.addView(deleteButton);
            plotContainer.addView(buttonHolder);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Pass bundle to fragment with plot name for easy retrieval
                    Bundle bundle = new Bundle();
                    bundle.putString("Name", currentPlot.name);
                    //launch edit dialog
                    FragmentManager manager = getFragmentManager();
                    Fragment frag = manager.findFragmentByTag("fragment_edit_id");
                    if (frag != null) {
                        manager.beginTransaction().remove(frag).commit();
                    }

                    FragPlotDimens plotDimens = new FragPlotDimens();
                    plotDimens.setArguments(bundle);
                    plotDimens.show(manager, "fragment_edit_id");
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Delete from storage
                    plotSP.edit().remove(currentPlot.name).apply();
                    //Update views in this activity
                    buttonHolder.removeAllViews();
                    plotContainer.removeView(tvPlotTitle);
                    plotContainer.removeView(tvPlotDimens);
                }
            });

        }
    }

    public void launchPlotManager (final TextView textView) {
        //This function should launch the plot manager for each plot that a user has created

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Grab name to pass to plot manager activity
                String key = textView.getText().toString();

                Intent intent = new Intent(getApplicationContext(), PlotManager.class);
                intent.putExtra("Name", key);
                startActivity(intent);
                finish();
            }
        });
    }
}

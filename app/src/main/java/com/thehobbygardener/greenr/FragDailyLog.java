package com.thehobbygardener.greenr;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.Date;


public class FragDailyLog extends DialogFragment {

    public static final String MY_DAILY_LOG = "MyDailyLogFile";
    SharedPreferences sp;

    public static final String EVENT_DAYS = "MyEventDays";
    SharedPreferences eventSP;

    public static final String MY_REMINDERS = "MyRemindersFile";
    SharedPreferences reminderSP;

    Button bSave, bCancel;
    private EditText etLogNote, etFrequency;
    private DailyLogTemplate logToday;
    private Date d;
    private CheckBox cbSetReminder, cbRepeating;
    private LinearLayout llRemStart, llRemRepeat, llRemFreq;

    public FragDailyLog() {
        // Required empty public constructor
    }

    public interface UserDialog {
        void onFinishUserDialog(String user);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_daily_log, container);
        etLogNote = (EditText) view.findViewById(R.id.et_log_notes);
        TextView title = (TextView) view.findViewById(R.id.frag_title);

        //Grab the selected date
        d = new Date();
        d.setTime(this.getArguments().getLong("date", -1));
        String strMonth = (String) android.text.format.DateFormat.format("MMMM", d);
        String day = (String) android.text.format.DateFormat.format("dd", d);
        String year = (String) android.text.format.DateFormat.format("YYY", d);
        //Update the TextView
        String dateTitle = strMonth+" "+day;
        title.setText(dateTitle);
        final String key = day+strMonth+year;


        //Set up icon buttons and prepare to store click information
        ImageButton iWater = (ImageButton) view.findViewById(R.id.icon_watering);
        ImageButton iPrune = (ImageButton) view.findViewById(R.id.icon_pruning);
        ImageButton iWeed = (ImageButton) view.findViewById(R.id.icon_weeding);
        ImageButton iPlant = (ImageButton) view.findViewById(R.id.icon_planting);

        //Prep to retrieve log, if it exists
        String nullNote = "NULL";
        logToday = new DailyLogTemplate(day, strMonth, year, nullNote, d.getTime(), false, false, false, false);
        sp = this.getActivity().getSharedPreferences(MY_DAILY_LOG, Context.MODE_PRIVATE);
        if (sp.contains(key)){
            Gson retrieveGson = new Gson();
            String retrieveJson = sp.getString(key, "");
            logToday = retrieveGson.fromJson(retrieveJson, DailyLogTemplate.class);
        }

        //Update images and set icon click handlers based on task status
        //TODO: confirm this works as well as I think it does
        if (!logToday.watering){
            iWater.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.watering_can_32_gray));
            toggleWateredOn(iWater, logToday);
        } else {
            toggleWateredOff(iWater, logToday);
        }
        //TODO: update pruning image
        if (!logToday.pruning){
            iPrune.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.watering_can_32_gray));
            togglePruningOn(iPrune, logToday);
        } else {
            togglePruningOff(iPrune, logToday);
        }
        //TODO: update weeding image
        if (!logToday.weeding){
            iWeed.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.watering_can_32_gray));
            toggleWateredOn(iWeed, logToday);
        } else {
            toggleWateredOff(iWeed, logToday);
        }
        //TODO: update planting image
        if (!logToday.planting){
            iPlant.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.watering_can_32_gray));
            toggleWateredOn(iPlant, logToday);
        } else {
            toggleWateredOff(iPlant, logToday);
        }

        //Update text box with notes, if they exist
        if (!nullNote.equals(logToday.note)){
            etLogNote.setText(logToday.note);
        }

        //Handle reminder setup/creation, including layout views
        cbSetReminder = (CheckBox) view.findViewById(R.id.cb_set_reminder);
        cbRepeating = (CheckBox) view.findViewById(R.id.cb_repeating_reminder);
        llRemStart = (LinearLayout) view.findViewById(R.id.ll_reminder_start_date_holder);
        llRemRepeat = (LinearLayout) view.findViewById(R.id.ll_repeating_reminder_holder);
        llRemFreq = (LinearLayout) view.findViewById(R.id.ll_reminder_frequency_holder);
        //Setting up save/cancel buttons
        bSave = (Button) view.findViewById(R.id.bSave);
        bCancel= (Button) view.findViewById(R.id.bCancel);

        eventSP = this.getActivity().getSharedPreferences(EVENT_DAYS, Context.MODE_PRIVATE);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Grab and set note text before saving
                logToday.note = etLogNote.getText().toString();
                //Update date, for good measure and backwards compatibility
                logToday.date = d.getTime();

                SharedPreferences.Editor editor = sp.edit();
                Gson gson = new Gson();
                String json = gson.toJson(logToday);
                editor.putString(key, json);
                editor.apply();

                //Save event day
                SharedPreferences.Editor eventEditor = eventSP.edit();
                eventEditor.putLong(key, d.getTime());
                eventEditor.apply();

                //Reminder checkbox handling //TODO: setting visibility doesn't work like I want it to
                cbSetReminder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //If set to create a reminder, that needs to get done here
                        if (cbSetReminder.isChecked()){
                            llRemStart.setVisibility(View.VISIBLE);
                            llRemRepeat.setVisibility(View.VISIBLE);

                            //TODO: how to store the start date and frequency
                            cbRepeating.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (cbRepeating.isChecked()){
                                        llRemFreq.setVisibility(View.VISIBLE);
                                    } else {
                                        llRemFreq.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                        } else {
                            llRemStart.setVisibility(View.INVISIBLE);
                            llRemRepeat.setVisibility(View.INVISIBLE);
                            //Including frequency layout here just in case
                            llRemFreq.setVisibility(View.INVISIBLE);
                        }
                    }
                });

                Toast.makeText(getActivity(), "saved!", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onDismiss(final DialogInterface dialog){
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener){
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
    //Watering button handlers
    public void toggleWateredOn (final ImageButton button, final DailyLogTemplate log){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log.watering = true;
                button.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.watering_can_32));
                Toast.makeText(getActivity(), "watered = true", Toast.LENGTH_SHORT).show();
                toggleWateredOff(button, log);
            }
        });
    }

    public void toggleWateredOff (final ImageButton button, final DailyLogTemplate log){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log.watering = false;
                button.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.watering_can_32_gray));
                Toast.makeText(getActivity(), "watered = false", Toast.LENGTH_SHORT).show();
                toggleWateredOn(button, log);
            }
        });
    }
    //Pruning button handlers
    public void togglePruningOn (final ImageButton button, final DailyLogTemplate log){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log.pruning = true;
                //TODO: update image
                button.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.watering_can_32_gray));
                Toast.makeText(getActivity(), "pruned!", Toast.LENGTH_SHORT).show();
                togglePruningOff(button, log);
            }
        });
    }

    public void togglePruningOff (final ImageButton button, final DailyLogTemplate log){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log.pruning = false;
                button.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.pruning_shears_32));
                togglePruningOn(button, log);
            }
        });
    }
    //Weeding button handlers
    public void toggleWeedingOn (final ImageButton button, final DailyLogTemplate log){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log.weeding = true;
                //TODO: update image
                button.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.watering_can_32_gray));
                Toast.makeText(getActivity(), "weeded!", Toast.LENGTH_SHORT).show();
                toggleWeedingOff(button, log);
            }
        });
    }

    public void toggleWeedingOff (final ImageButton button, final DailyLogTemplate log){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log.weeding = false;
                //TODO: update image
                button.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.watering_can_32_gray));
                toggleWeedingOn(button, log);
            }
        });
    }
    //Planting button handlers
    public void togglePlantingOn (final ImageButton button, final DailyLogTemplate log){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log.planting = true;
                //TODO: update image
                button.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.watering_can_32_gray));
                Toast.makeText(getActivity(), "planted!", Toast.LENGTH_SHORT).show();
                togglePlantingOff(button, log);
            }
        });
    }

    public void togglePlantingOff (final ImageButton button, final DailyLogTemplate log){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log.planting = false;
                //TODO: update image
                button.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.watering_can_32_gray));
                togglePlantingOn(button, log);
            }
        });
    }
}

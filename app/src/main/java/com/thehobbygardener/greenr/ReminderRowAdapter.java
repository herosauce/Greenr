package com.thehobbygardener.greenr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class ReminderRowAdapter extends BaseAdapter{

    ArrayList<ReminderRow> list;
    Context c;

    private ArrayList<ReminderRow> privateArray;

    public ReminderRowAdapter(Context c, ArrayList<ReminderRow> list) {
        this.list = list;
        this.c = c;
        privateArray = new ArrayList<>();
        privateArray.addAll(list);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row =  inflater.inflate(R.layout.reminder_item,null,true);

        TextView name   = (TextView) row.findViewById(R.id.task_name);
        TextView frequency = (TextView) row.findViewById(R.id.task_frequency);
        TextView nextReminder = (TextView) row.findViewById(R.id.next_occurrence);
        ImageView image = (ImageView) row.findViewById(R.id.plant_icon);

        ReminderRow reminderRow = list.get(position);

        name.setText(reminderRow.taskName);
        frequency.setText(reminderRow.frequency);

        return row;
    }


    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length()==0){
            list.addAll(privateArray);
        } else {
            for (ReminderRow s : privateArray) {
                if (s.taskName.toLowerCase(Locale.getDefault()).contains(charText)){
                    list.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }

}

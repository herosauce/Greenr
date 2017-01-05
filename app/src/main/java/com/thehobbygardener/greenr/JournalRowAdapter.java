package com.thehobbygardener.greenr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class JournalRowAdapter extends BaseAdapter {

    ArrayList<DailyLogTemplate> list;
    Context c;
    private ArrayList<DailyLogTemplate> privateArray;

    public JournalRowAdapter(Context c, ArrayList<DailyLogTemplate> list){
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
        View row = inflater.inflate(R.layout.journal_item,null, false);

        //Textviews for the date
        TextView month = (TextView) row.findViewById(R.id.tv_journal_month);
        TextView day = (TextView) row.findViewById(R.id.tv_journal_day);
        //Textview for log note
        TextView note = (TextView) row.findViewById(R.id.tv_journal_notes);
        //TODO: define imageviews

        DailyLogTemplate currentLog = list.get(position);
        month.setText(currentLog.month);
        day.setText(currentLog.day);
        note.setText(currentLog.note);
        //TODO: set/update/remove images based on task bools

        return row;
    }
}

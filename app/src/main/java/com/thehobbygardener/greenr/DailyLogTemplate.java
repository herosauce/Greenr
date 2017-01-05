package com.thehobbygardener.greenr;

public class DailyLogTemplate {
    String day;
    String month;
    String year;
    String note;
    Long date;

    Boolean weeding;
    Boolean watering;
    Boolean planting;
    Boolean pruning;

    public DailyLogTemplate(String day, String month, String year, String note,Long date, Boolean weeding, Boolean watering, Boolean planting, Boolean pruning) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.note = note;
        this.date = date;

        this.weeding = weeding;
        this.watering = watering;
        this.planting = planting;
        this.pruning = pruning;
    }
}

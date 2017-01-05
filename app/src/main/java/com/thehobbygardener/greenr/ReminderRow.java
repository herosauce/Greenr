package com.thehobbygardener.greenr;

public class ReminderRow {

    String taskName;
    String plantName;
    Long startDate;
    Boolean repeating;
    Integer frequency;
    Boolean taskComplete;

    public ReminderRow(String taskName, String plantName, Long startDate, Boolean repeating, Integer frequency, Boolean taskComplete) {
        this.taskName = taskName;
        this.plantName = plantName;
        this.startDate = startDate;
        this.repeating = repeating;
        this.frequency = frequency;
        this.taskComplete = taskComplete;
    }
}

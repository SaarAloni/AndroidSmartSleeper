package com.example.atry.activities;

public class SleepData {
    String sleep_date;
    String start;
    String end;

    String quality;

    SleepData(String sleep_date, String start, String end, String quality)
    {
        this.sleep_date = sleep_date;
        this.start = start;
        this.end = end;
        this.quality = quality;
    }
}

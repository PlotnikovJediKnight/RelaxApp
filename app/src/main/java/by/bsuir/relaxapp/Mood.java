package by.bsuir.relaxapp;

import android.graphics.Bitmap;

public class Mood {
    public int moodImageResourceID;
    public String name;
    public String tableColumnName;
    public int clickedCount;

    public Mood(int moodImageResourceID, String name, String tableColumnName, int clickedCount) {
        this.moodImageResourceID = moodImageResourceID;
        this.name = name;
        this.tableColumnName = tableColumnName;
        this.clickedCount = clickedCount;
    }
}

package by.bsuir.relaxapp;

import static by.bsuir.relaxapp.MainActivity.CURR_USER_DB_INFO;
import static by.bsuir.relaxapp.MainActivity.DB_HELPER;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import org.intellij.lang.annotations.PrintFormat;

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

    public void incrementMoodClick(){
        clickedCount++;
        updateMoodClickInDatabase();
    }

    private void updateMoodClickInDatabase(){
        DB_HELPER = new DatabaseHelper(MainActivity.MAIN_ACTIVITY_CONTEXT);
        SQLiteDatabase sQlitedatabase = DB_HELPER.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(tableColumnName, clickedCount);
        String where = DatabaseHelper.KEY_id + " = '" + ProfileFragment.userID + "'";
        sQlitedatabase.update(DatabaseHelper.TABLE_MOODS, contentValues, where, null);

        DB_HELPER.close();
    }
}

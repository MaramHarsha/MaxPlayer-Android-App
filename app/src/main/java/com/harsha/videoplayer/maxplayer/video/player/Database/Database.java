package com.harsha.videoplayer.maxplayer.video.player.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.harsha.videoplayer.maxplayer.video.player.Model.HideData;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final String COLUMNHIDENAME = "hide_name";
    private static final String COLUMNHIDEPATH = "hide_path";
    private static final String DATABASENAME = "hide_video.db";
    private static final int DATABASEVERSION = 1;
    private static final String TABLEHIDE = "Hide";

    public Database(Context context) {
        super(context, DATABASENAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        String CREATEHIDETABLE = "CREATE TABLE Hide ( hide_name TEXT PRIMARY KEY, hide_path TEXT  ) ";
        sQLiteDatabase.execSQL(CREATEHIDETABLE);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        String DROPHIDETABLE = "DROP TABLE IF EXISTS Hide";
        sQLiteDatabase.execSQL(DROPHIDETABLE);
        onCreate(sQLiteDatabase);
    }

    public void addHide(HideData hide_Data) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String replace = hide_Data.getName().replace("'", "@#");
        String replace2 = hide_Data.getPath().replace("'", "@#");
        contentValues.put(COLUMNHIDENAME, replace);
        contentValues.put(COLUMNHIDEPATH, replace2);
        writableDatabase.insert(TABLEHIDE, null, contentValues);
        writableDatabase.close();
    }

    public int getID() {
        Cursor rawQuery = getWritableDatabase().rawQuery("SELECT * FROM Hide", null);
        if (rawQuery == null) {
            return 0;
        }
        rawQuery.moveToFirst();
        int count = rawQuery.getCount();
        rawQuery.close();
        return count;
    }

    public List<HideData> getAllHide() {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("select * from Hide", null);
        if (rawQuery.moveToFirst()) {
            do {
                HideData hide_Data = new HideData();
                String replace = rawQuery.getString(rawQuery.getColumnIndex(COLUMNHIDENAME)).replace("@#", "'");
                String replace2 = rawQuery.getString(rawQuery.getColumnIndex(COLUMNHIDEPATH)).replace("@#", "'");
                hide_Data.setName(replace);
                hide_Data.setPath(replace2);
                arrayList.add(hide_Data);
            } while (rawQuery.moveToNext());
        }
        rawQuery.close();
        readableDatabase.close();
        return arrayList;
    }

    public HideData getHideData(String str) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        HideData hide_Data = null;
        Cursor rawQuery = readableDatabase.rawQuery("select * from Hide where hide_name='" + str + "'", null);
        if (rawQuery.moveToFirst()) {
            String replace = rawQuery.getString(rawQuery.getColumnIndex(COLUMNHIDENAME)).replace("@#", "'");
            String replace2 = rawQuery.getString(rawQuery.getColumnIndex(COLUMNHIDEPATH)).replace("@#", "'");
            hide_Data = new HideData();
            hide_Data.setName(replace);
            hide_Data.setPath(replace2);
        }
        rawQuery.close();
        readableDatabase.close();
        return hide_Data;
    }

    public Integer deleteHide(String str) {
        return Integer.valueOf(getWritableDatabase().delete(TABLEHIDE, "hide_name = ?", new String[]{str}));
    }
}

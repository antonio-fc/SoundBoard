package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String BUTTON_TABLE = "BUTTON_TABLE";
    public static final String COLUMN_ID = "COLUMN_ID";
    public static final String COLUMN_NAME = "COLUMN_NAME";
    public static final String COLUMN_PATH = "COLUMN_PATH";
    public static final String DB_NAME = "soundBtn.db";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + BUTTON_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " + COLUMN_PATH + " TEXT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(ButtonModel buttonModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String countQuery = "SELECT COUNT(*) FROM " + BUTTON_TABLE;

        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int numberRows = cursor.getInt(0);

        cv.put(COLUMN_NAME, buttonModel.getName());
        cv.put(COLUMN_PATH, buttonModel.getPath());
        cursor.close();
        if(numberRows < 3){
            long insert = db.insert(BUTTON_TABLE, null, cv);
            db.close();
            return insert != -1;
        }
        else return false;
    }

    public boolean deleteOne(ButtonModel buttonModel){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + BUTTON_TABLE + " WHERE " + COLUMN_ID + " = " + buttonModel.getId();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            cursor.close();
            return true;
        }
        else {
            cursor.close();
            return false;
        }
    }

    public ArrayList<ButtonModel> getAll(){
        ArrayList<ButtonModel> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + BUTTON_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                int buttonID = cursor.getInt(0);
                String buttonName = cursor.getString(1);
                String buttonPath = cursor.getString(2);
                ButtonModel buttonModel = new ButtonModel(buttonID, buttonName, buttonPath);
                returnList.add(buttonModel);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public boolean restartTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.execSQL("DROP TABLE IF EXISTS " + BUTTON_TABLE);
            onCreate(db);
        }
        catch(Exception e){
            db.close();
            return false;
        }
        db.close();
        return true;
    }
}

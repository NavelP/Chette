package com.nvdeveloper.Chette;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.nvdeveloper.Chette.track_response.complaint_p;

import java.util.ArrayList;

public class StoreFilesLocally extends SQLiteOpenHelper {
    SQLiteDatabase db;

    ArrayList<complaint_p> complaints = new ArrayList<>();

    private static final String DB_NAME = "chette_offline_data.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_COMPLAINTS = "_complaints";

    private static final String KEY_COMPLAINT_ID = "id";
    private static final String KEY_DEPARTMENT = "department_name";
    private static final String KEY_CAPTION = "caption";
    private static final String KEY_BOOSTS = "boosts";
    private static final String KEY_USER_PHONE_NUMBER = "user_phone";

    public StoreFilesLocally(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String Query_Table = " CREATE TABLE " + TABLE_COMPLAINTS + "(" + KEY_COMPLAINT_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DEPARTMENT + " TEXT, " + KEY_CAPTION +
                " TEXT, " + KEY_BOOSTS + " TEXT, " + KEY_USER_PHONE_NUMBER + " TEXT);";

        db.execSQL(Query_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINTS);
        onCreate(db);
    }

    public long InsertComplaint(String department_name, String caption, String boosts, String phone_number){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DEPARTMENT, department_name);
        values.put(KEY_CAPTION, caption);
        values.put(KEY_BOOSTS, boosts);
        values.put(KEY_USER_PHONE_NUMBER, phone_number);

        return db.insert(TABLE_COMPLAINTS, null, values);
    }

    public ArrayList<complaint_p> getData(String clmn, String department){
        db = this.getReadableDatabase();
        String [] columns = new String[]{
                KEY_COMPLAINT_ID, KEY_DEPARTMENT,KEY_CAPTION, KEY_BOOSTS, KEY_USER_PHONE_NUMBER
        };

        Cursor cursor ;
        //for other complaints
        if(clmn.equals("")){
            //others in all departments
            if(department.equals("")){
                cursor = db.query(TABLE_COMPLAINTS, columns, null, null,
                        null, null, null);
            }
            //others in the chosen department
            else{
                cursor = db.rawQuery("SELECT * FROM " + TABLE_COMPLAINTS + " WHERE " +
                        KEY_DEPARTMENT + " = " + department, null);
            }
        }
        //for your own posts
        else{
            //for all departments
            if(department.equals("")) {
                cursor = db.query(TABLE_COMPLAINTS, columns, columns[4] + "=" + clmn,
                        null, null, null, null);
            }
            //for a specific department
            else{
                cursor = db.rawQuery("SELECT * FROM " + TABLE_COMPLAINTS + " WHERE " +
                        KEY_DEPARTMENT + " = " + department + " AND " + KEY_USER_PHONE_NUMBER +
                        " = " + clmn, null);
            }
        }

        int iId = cursor.getColumnIndex(KEY_COMPLAINT_ID);
        int iDepartment_name = cursor.getColumnIndex(KEY_DEPARTMENT);
        int iCaption = cursor.getColumnIndex(KEY_CAPTION);
        int iBoosts = cursor.getColumnIndex(KEY_BOOSTS);

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            complaints.add(new complaint_p(
                    cursor.getString(iDepartment_name), "", "",
                    cursor.getString(iCaption), "", "", "", ""
                    , "", cursor.getString(iBoosts)
            ));
        }
        db.close();
        return complaints;
    }
}

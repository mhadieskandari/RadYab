package com.example.asus.makanyab.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Work on 21/09/2015.
 */
public class GPSMonitorDBOpenHelper extends SQLiteOpenHelper {


    private  static final String LOGTAG="mylogtag";
    private static final String DATABASE_NAME = "gpsMonitor.db" ;
    private static final int DATABASE_VERSION = 3 ;

    //Contact
    public static final String TABLE_CONTACT ="T_Contact";
    public static final String CONTACT_COLUMN_ID ="ID";
    public static final String CONTACT_COLUMN_CONTACTNAME ="ContactName";
    public static final String CONTACT_COLUMN_PHONENUMBER ="PhoneNumber";
    public static final String CONTACT_COLUMN_STATE ="State";

    //setting
    public static final String TABLE_SETTING ="T_Setting";
    public static final String SETTING_COLUMN_ID ="ID";
    public static final String SETTING_COLUMN_SETTINGNAME ="SettingName";
    public static final String SETTING_COLUMN_SETTINGVAL ="SettingVal";

    //Location
    public static final String TABLE_LOCATION ="T_Location";
    public static final String LOCATION_COLUMN_ID ="ID";
    public static final String LOCATION_COLUMN_CID ="CID";
    public static final String LOCATION_COLUMN_LATITUDE ="Latitude";
    public static final String LOCATION_COLUMN_LONGITUDE ="Logitude";
    public static final String LOCATION_COLUMN_DATETIME ="DateTime";


    private static final String TABLE_LOCATION_CREATE ="CREATE TABLE "+TABLE_LOCATION+" (" +
            LOCATION_COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            LOCATION_COLUMN_CID+" INTEGER REFERENCES [T_Contact]([ID]) ON DELETE CASCADE ON UPDATE CASCADE," +
            LOCATION_COLUMN_LATITUDE+" text," +
            LOCATION_COLUMN_LONGITUDE+" text,"+
            LOCATION_COLUMN_DATETIME+ " DATETIME );";

    private static final String TABLE_CONTACT_CREATE ="CREATE TABLE "+TABLE_CONTACT+" (" +
            CONTACT_COLUMN_ID+" integer PRIMARY KEY AUTOINCREMENT," +
            CONTACT_COLUMN_CONTACTNAME+" text," +
            CONTACT_COLUMN_PHONENUMBER+" text," +
            CONTACT_COLUMN_STATE+" smallint );";

    private static final String TABLE_SETTING_CREATE ="CREATE TABLE "+TABLE_SETTING+" (" +
            SETTING_COLUMN_ID+" integer PRIMARY KEY AUTOINCREMENT," +
            SETTING_COLUMN_SETTINGNAME+" text," +
            SETTING_COLUMN_SETTINGVAL+" text );";


    public GPSMonitorDBOpenHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLE_CONTACT_CREATE);
        db.execSQL(TABLE_SETTING_CREATE);
        db.execSQL(TABLE_LOCATION_CREATE);

        Log.i(LOGTAG, "tables has been created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       // db.beginTransaction();
        // db.setTransactionSuccessful();
        //db.endTransaction();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        //db.execSQL();
        Log.i(LOGTAG, "tables has been droped");
        onCreate(db);
        Log.i(LOGTAG, "tables has been created");
    }


}

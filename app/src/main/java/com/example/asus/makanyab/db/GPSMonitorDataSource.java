package com.example.asus.makanyab.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.asus.makanyab.models.Contact;
import com.example.asus.makanyab.models.Location;
import com.example.asus.makanyab.models.Setting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Work on 22/09/2015.
 */
public class GPSMonitorDataSource {

    public static final String LOGTAG = "mylogtag";

    private static final String ARG_Setting_MyName = "MyName";
    private static final String ARG_Setting_MyPhone = "MyPhone";

    SQLiteOpenHelper dbHelper;
    SQLiteDatabase database;


    private static final String[] Contact_AllColumn ={
            GPSMonitorDBOpenHelper.CONTACT_COLUMN_ID,
            GPSMonitorDBOpenHelper.CONTACT_COLUMN_CONTACTNAME,
            GPSMonitorDBOpenHelper.CONTACT_COLUMN_PHONENUMBER,
            GPSMonitorDBOpenHelper.CONTACT_COLUMN_STATE
    };
    private static final String[] Setting_AllColumn ={
            GPSMonitorDBOpenHelper.SETTING_COLUMN_ID,
            GPSMonitorDBOpenHelper.SETTING_COLUMN_SETTINGNAME,
            GPSMonitorDBOpenHelper.SETTING_COLUMN_SETTINGVAL
    };

    private static final String[] Location_AllColumn ={
            GPSMonitorDBOpenHelper.LOCATION_COLUMN_ID,
            GPSMonitorDBOpenHelper.LOCATION_COLUMN_CID,
            GPSMonitorDBOpenHelper.LOCATION_COLUMN_LATITUDE,
            GPSMonitorDBOpenHelper.LOCATION_COLUMN_LONGITUDE,
            GPSMonitorDBOpenHelper.LOCATION_COLUMN_DATETIME
    };


    public GPSMonitorDataSource(Context context){
        dbHelper =new GPSMonitorDBOpenHelper(context);

    }


    public void open(){
        Log.i(LOGTAG, "database opened");
        database= dbHelper.getWritableDatabase();
    }

    public void close(){
        Log.i(LOGTAG, "database closed");
        dbHelper.close();
    }

    //Location
    public Location Location_Insert(Location location){
        ContentValues values = new ContentValues();

//        values.put(GPSMonitorDBOpenHelper.LOCATION_COLUMN_CID,location.getCID());
//        values.put(GPSMonitorDBOpenHelper.LOCATION_COLUMN_LATITUDE,location.getLatitude());
//        values.put(GPSMonitorDBOpenHelper.LOCATION_COLUMN_LONGITUDE,location.getLongitude());
//        values.put(GPSMonitorDBOpenHelper.LOCATION_COLUMN_DATETIME, location.getDateTime());
//        int insertid = (int) database.insert(GPSMonitorDBOpenHelper.TABLE_LOCATION, null, values);
//        location.setLocationID(insertid);
        return  location;
    }

    public List<Location> Location_FindAll(Long CID){
        List<Location> list = new ArrayList<Location>() ;

        String WhereCluase =GPSMonitorDBOpenHelper.LOCATION_COLUMN_CID+" = "+ String.valueOf(CID);

        Cursor cursor = database.query(GPSMonitorDBOpenHelper.TABLE_LOCATION, Location_AllColumn, WhereCluase, null, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount()>0) {
            while (!cursor.isAfterLast()) {
//                Location location = new Location();
//                location.setLocationID(cursor.getInt(cursor.getColumnIndex(GPSMonitorDBOpenHelper.LOCATION_COLUMN_ID)));
//                location.setCID(cursor.getInt(cursor.getColumnIndex(GPSMonitorDBOpenHelper.LOCATION_COLUMN_CID)));
//                location.setLatitude(cursor.getString(cursor.getColumnIndex(GPSMonitorDBOpenHelper.LOCATION_COLUMN_LATITUDE)));
//                location.setLongitude(cursor.getString(cursor.getColumnIndex(GPSMonitorDBOpenHelper.LOCATION_COLUMN_LONGITUDE)));
//                location.setDateTime(cursor.getString(cursor.getColumnIndex(GPSMonitorDBOpenHelper.LOCATION_COLUMN_DATETIME)));
//
//                list.add(location);
//                cursor.moveToNext();
            }
        }
        return list;
    }

    public List<Location> Location_Find(Long ID){
        List<Location> list = new ArrayList<Location>() ;

        String WhereCluase =GPSMonitorDBOpenHelper.LOCATION_COLUMN_ID+" = "+ String.valueOf(ID);

        Cursor cursor = database.query(GPSMonitorDBOpenHelper.TABLE_LOCATION, Location_AllColumn, WhereCluase, null, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount()>0) {
//            while (!cursor.isAfterLast()) {
//                Location location = new Location();
//                location.setLocationID(cursor.getInt(cursor.getColumnIndex(GPSMonitorDBOpenHelper.LOCATION_COLUMN_ID)));
//                location.setCID(cursor.getInt(cursor.getColumnIndex(GPSMonitorDBOpenHelper.LOCATION_COLUMN_CID)));
//                location.setLatitude(cursor.getString(cursor.getColumnIndex(GPSMonitorDBOpenHelper.LOCATION_COLUMN_LATITUDE)));
//                location.setLongitude(cursor.getString(cursor.getColumnIndex(GPSMonitorDBOpenHelper.LOCATION_COLUMN_LONGITUDE)));
//                location.setDateTime(cursor.getString(cursor.getColumnIndex(GPSMonitorDBOpenHelper.LOCATION_COLUMN_DATETIME)));
//
//                list.add(location);
//                cursor.moveToNext();
//            }
        }
        return list;
    }


    //Setting

    public Setting Setting_Insert(Setting setting){
        ContentValues values = new ContentValues();
        values.put(GPSMonitorDBOpenHelper.SETTING_COLUMN_SETTINGNAME,setting.getSetting_Name());
        values.put(GPSMonitorDBOpenHelper.SETTING_COLUMN_SETTINGVAL,setting.getSetting_Val());
        int insertid = (int) database.insert(GPSMonitorDBOpenHelper.TABLE_SETTING,null,values);
        setting.setSettingID(insertid);
        return  setting;
    }

    public Setting Setting_GetVal(String SettingName){
        String WhereCluase =GPSMonitorDBOpenHelper.SETTING_COLUMN_SETTINGNAME+" = '"+SettingName+"'";
        Setting setting;
        Cursor cursor = database.query(GPSMonitorDBOpenHelper.TABLE_SETTING, Setting_AllColumn, WhereCluase, null, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount()>0) {

            setting = new Setting();
            setting.setSettingID(cursor.getInt(cursor.getColumnIndex(GPSMonitorDBOpenHelper.SETTING_COLUMN_ID)));
            setting.setSetting_Name(cursor.getString(cursor.getColumnIndex(GPSMonitorDBOpenHelper.SETTING_COLUMN_SETTINGNAME)));
            setting.setSetting_Val(cursor.getString(cursor.getColumnIndex(GPSMonitorDBOpenHelper.SETTING_COLUMN_SETTINGVAL)));

            return  setting;

        }
        return  null;
    }

    public boolean Setting_IsSet(){
        String WhereCluase =GPSMonitorDBOpenHelper.SETTING_COLUMN_SETTINGNAME+" = '"+ARG_Setting_MyName+"'";
        boolean f1=false,f2=false;
        Cursor cursor = database.query(GPSMonitorDBOpenHelper.TABLE_SETTING, Setting_AllColumn, WhereCluase, null, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount()>0) {
            if(!cursor.getString(cursor.getColumnIndex(GPSMonitorDBOpenHelper.SETTING_COLUMN_SETTINGVAL)).equals("")){
                f1=true;
            }
        }

        String WhereCluase1 =GPSMonitorDBOpenHelper.SETTING_COLUMN_SETTINGNAME+" = '"+ARG_Setting_MyPhone+"'";

        Cursor cursor2 = database.query(GPSMonitorDBOpenHelper.TABLE_SETTING, Setting_AllColumn, WhereCluase1, null, null, null, null);
        cursor2.moveToFirst();
        if(cursor2.getCount()>0) {
            if(!cursor.getString(cursor2.getColumnIndex(GPSMonitorDBOpenHelper.SETTING_COLUMN_SETTINGVAL)).equals("")){
                f2=true;
            }
        }

        return f1 & f2;
    }




    //Contact--------------------------------------------------------------------------------------

    public Contact Contact_Find(long id){
        //List<Contact> list = new ArrayList<Contact>() ;

        String WhereCluase =GPSMonitorDBOpenHelper.CONTACT_COLUMN_ID+" = "+ String.valueOf(id);
        Contact contact;
        Cursor cursor = database.query(GPSMonitorDBOpenHelper.TABLE_CONTACT, Contact_AllColumn, WhereCluase, null, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount()>0) {

                contact = new Contact();
                contact.setContactID(cursor.getInt(cursor.getColumnIndex(GPSMonitorDBOpenHelper.CONTACT_COLUMN_ID)));
                contact.setContactName(cursor.getString(cursor.getColumnIndex(GPSMonitorDBOpenHelper.CONTACT_COLUMN_CONTACTNAME)));
                contact.setPhoneNumber(cursor.getString(cursor.getColumnIndex(GPSMonitorDBOpenHelper.CONTACT_COLUMN_PHONENUMBER)));
                contact.setState(cursor.getInt(cursor.getColumnIndex(GPSMonitorDBOpenHelper.CONTACT_COLUMN_STATE)));
                return  contact;

        }
        return null;
    }

    public Contact Contact_Find(String PhoneNumber){
        //List<Contact> list = new ArrayList<Contact>() ;

        String WhereCluase =GPSMonitorDBOpenHelper.CONTACT_COLUMN_PHONENUMBER+"='"+PhoneNumber.replace("+98","0")+"'";
        Contact contact;
        Cursor cursor = database.query(GPSMonitorDBOpenHelper.TABLE_CONTACT, Contact_AllColumn, WhereCluase, null, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount()>0) {

            contact = new Contact();
            contact.setContactID(cursor.getInt(cursor.getColumnIndex(GPSMonitorDBOpenHelper.CONTACT_COLUMN_ID)));
            contact.setContactName(cursor.getString(cursor.getColumnIndex(GPSMonitorDBOpenHelper.CONTACT_COLUMN_CONTACTNAME)));
            contact.setPhoneNumber(cursor.getString(cursor.getColumnIndex(GPSMonitorDBOpenHelper.CONTACT_COLUMN_PHONENUMBER)));
            contact.setState(cursor.getInt(cursor.getColumnIndex(GPSMonitorDBOpenHelper.CONTACT_COLUMN_STATE)));
            return  contact;

        }
        return null;
    }


    public List<Contact> Contact_FindAll(){
        List<Contact> list = new ArrayList<Contact>() ;

        //String WhereCluase =GPSMonitorDBOpenHelper.LEARNING_COLUMN_UDID+" = "+String.valueOf(UDID);

        Cursor cursor = database.query(GPSMonitorDBOpenHelper.TABLE_CONTACT, Contact_AllColumn, null, null, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount()>0) {
            while (!cursor.isAfterLast()) {
                Contact contact = new Contact();
                contact.setContactID(cursor.getInt(cursor.getColumnIndex(GPSMonitorDBOpenHelper.CONTACT_COLUMN_ID)));
                contact.setContactName(cursor.getString(cursor.getColumnIndex(GPSMonitorDBOpenHelper.CONTACT_COLUMN_CONTACTNAME)));
                contact.setPhoneNumber(cursor.getString(cursor.getColumnIndex(GPSMonitorDBOpenHelper.CONTACT_COLUMN_PHONENUMBER)));
                contact.setState(cursor.getInt(cursor.getColumnIndex(GPSMonitorDBOpenHelper.CONTACT_COLUMN_STATE)));

                list.add(contact);
                cursor.moveToNext();
            }
        }
        return list;
    }

    public Contact Contact_Insert(Contact contact){
        ContentValues values = new ContentValues();
        //values.put(GPSMonitorDBOpenHelper.CONTACT_COLUMN_ID, contact.getId());
        values.put(GPSMonitorDBOpenHelper.CONTACT_COLUMN_CONTACTNAME, contact.getContactName());
        values.put(GPSMonitorDBOpenHelper.CONTACT_COLUMN_PHONENUMBER, contact.getPhoneNumber());
        values.put(GPSMonitorDBOpenHelper.CONTACT_COLUMN_STATE, contact.getState());
        int insertid = (int) database.insert(GPSMonitorDBOpenHelper.TABLE_CONTACT, null, values);
        contact.setContactID(insertid);
        return contact;
    }



    public void Contact_Edit(Contact contact){
        ContentValues values = new ContentValues();
        String WhereCluase =GPSMonitorDBOpenHelper.CONTACT_COLUMN_ID+" = "+ String.valueOf(contact.getContactID());

        //values.put(GPSMonitorDBOpenHelper.CONTACT_COLUMN_ID, contact.getId());
        values.put(GPSMonitorDBOpenHelper.CONTACT_COLUMN_CONTACTNAME, contact.getContactName());
        values.put(GPSMonitorDBOpenHelper.CONTACT_COLUMN_PHONENUMBER, contact.getPhoneNumber());
        database.update(GPSMonitorDBOpenHelper.TABLE_CONTACT, values, WhereCluase, null);
        //contact.setId(insertid);

    }

    public void Contact_RegisteredOnTarget(Contact contact){
        ContentValues values = new ContentValues();
        String ss=contact.getPhoneNumber().replace("+98","0").toString();
        String WhereCluase =GPSMonitorDBOpenHelper.CONTACT_COLUMN_PHONENUMBER+" = '"+ ss+"'";

        //values.put(GPSMonitorDBOpenHelper.CONTACT_COLUMN_ID, contact.getId());
//        values.put(GPSMonitorDBOpenHelper.CONTACT_COLUMN_CONTACTNAME, contact.getContactName());
        values.put(GPSMonitorDBOpenHelper.CONTACT_COLUMN_STATE, contact.getState());
        database.update(GPSMonitorDBOpenHelper.TABLE_CONTACT,values,WhereCluase,null);
        //contact.setId(insertid);

    }



//    public void Learning_Read(long id){
//        ContentValues values = new ContentValues();
//        values.put(GPSMonitorDBOpenHelper.LEARNING_COLUMN_STATE, 1);
//        String WhereCluase =GPSMonitorDBOpenHelper.LEARNING_COLUMN_ID+" = "+String.valueOf(id);
//
//        database.update(GPSMonitorDBOpenHelper.TABLE_LEARNING, values,WhereCluase,null);
//
//    }


    public void execSql(String sql){

        database.execSQL(sql);


    }








}

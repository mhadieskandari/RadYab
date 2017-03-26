package com.example.asus.makanyab.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Asus on 12/22/2015.
 */

@DatabaseTable(tableName = "Locations")
public class Location {
    public final static String Field_LocationID="LocationID";
    public final static String Field_ContactID="ContactID";
    public final static String Field_dateTime="dateTime";




    @DatabaseField(generatedId = true,index = true)
    private int LocationID;
    @DatabaseField(foreign = true,canBeNull = false,foreignAutoRefresh = true,maxForeignAutoRefreshLevel = 3,columnName = "ContactID")
    private Contact ContactID;
    @DatabaseField()
    private String Latitude;
    @DatabaseField()
    private String Longitude;
    @DatabaseField()
    private String dateTime;
    @DatabaseField()
    private String Altitude;

    public Location(Contact contactID, String latitude, String longitude,String altitude, String dateTime) {
        ContactID = contactID;
        Latitude = latitude;
        Longitude = longitude;
        this.dateTime = dateTime;
        this.Altitude=altitude;
    }

    public Location() {
    }

    public Location(Contact contactID) {
        ContactID = contactID;
    }

    public String getAltitude() {
        return Altitude;
    }

    public void setAltitude(String altitude) {
        Altitude = altitude;
    }

    public Contact getContactID() {
        return ContactID;
    }

    public void setContactID(Contact contactID) {
        ContactID = contactID;
    }

    public void setLocationID(int ID) {
        this.LocationID = ID;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getLocationID() {
        return LocationID;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    @Override
    public String toString() {
        return "Latitude:"+getLatitude()+" Longitude:"+getLongitude() +" On DateTime:"+getDateTime();
    }
}

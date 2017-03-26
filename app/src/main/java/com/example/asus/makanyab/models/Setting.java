package com.example.asus.makanyab.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Asus on 12/21/2015.
 */
@DatabaseTable(tableName = "Settings")
public class Setting {

    public final static String Field_SettingID="SettingID";
    public final static String Field_Setting_Name="Setting_Name";
    public final static String Field_Setting_Val="Setting_Val";


    public static final String Field_Setting_MyName = "MyName";
    public static final String Field_Setting_MyPhone = "MyPhone";
    public final static String Field_Setting_PassWord="Password";
    public final static String Field_Setting_IsPremium="IsPremium";


    @DatabaseField(generatedId = true,index = true)
    private int SettingID ;
    @DatabaseField(unique = true)
    private String Setting_Name;
    @DatabaseField()
    private String Setting_Val;

    public Setting() {
    }

    public Setting(String setting_Name, String setting_Val) {
        Setting_Name = setting_Name;
        Setting_Val = setting_Val;
    }

    public int getSettingID() {
        return SettingID;
    }

    public String getSetting_Name() {
        return Setting_Name;
    }

    public String getSetting_Val() {
        return Setting_Val;
    }

    public void setSettingID(int ID) {
        this.SettingID = ID;
    }

    public void setSetting_Name(String setting_Name) {
        Setting_Name = setting_Name;
    }

    public void setSetting_Val(String setting_Val) {
        Setting_Val = setting_Val;
    }
}

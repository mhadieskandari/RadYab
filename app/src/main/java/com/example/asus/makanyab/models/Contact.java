package com.example.asus.makanyab.models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Work on 21/09/2015.
 */

@DatabaseTable(tableName = "Contacts")
public class Contact {

    public final static String Field_ContactID="ContactID";
    public final static String Field_ContactName="ContactName";
    public final static String Field_PhoneNumber="PhoneNumber";
    public final static String Field_State="State";
    public final static String Field_IsMonitor="IsMonitor";


    @DatabaseField(generatedId = true,index = true)
    private int ContactID;
    @DatabaseField()
    private String ContactName;
    @DatabaseField()
    private String PhoneNumber;
    @DatabaseField()
    private int State;
    @DatabaseField()
    private boolean IsMonitor;

    public Contact() {
    }

    public Contact( String contactName, String phoneNumber, int state, boolean isMonitor) {

        this.ContactName = contactName;
        this.PhoneNumber = phoneNumber;
        this.State = state;
        this.IsMonitor = isMonitor;
    }

    public boolean getIsMonitor() {
        return IsMonitor;
    }

    public void setIsMonitor(boolean monitor) {
        IsMonitor = monitor;
    }

    public void setContactID(int id) {
        this.ContactID = id;
    }

    public int getContactID() {
        return ContactID;
    }

    public int getState() {
        return State;
    }

    public String getStateName() {
        String res="";
        if(this.State==0){
            res="تایید نشده";
        }else if(this.State==1){
            res="منتظر تایید";
        }
        else if(this.State==2){
            res="تایید شده";
        }
        return res;
    }

    public void setState(int state) {
        this.State = state;
    }

    public void setContactName(String contactName) {
        this.ContactName = contactName;
    }

    public String getContactName() {
        return ContactName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }




    @Override
    public String toString() {
        return getContactName()+" : "+getPhoneNumber();
    }
}

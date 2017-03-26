package com.example.asus.makanyab;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.telephony.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.widget.Toast;

import com.example.asus.makanyab.db.MakanYabDataBase;
import com.example.asus.makanyab.models.Contact;
import com.example.asus.makanyab.models.Location;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class MainReceiver extends BroadcastReceiver{
    Context context ;

    MakanYabDataBase database;

    public MainReceiver() {

    }


    private MakanYabDataBase getHelper() {
        if ( database== null) {
            database = OpenHelperManager.getHelper(context, MakanYabDataBase.class);
        }
        return database;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;

        //startService(new Intent(getBaseContext(), MainService.class));
        database=getHelper();





        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {

            Bundle bundle = intent.getExtras();

            Object messages[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];
            for (int n = 0; n < messages.length; n++) {
                smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
                if (smsMessage[n].getMessageBody().startsWith("RCD:")) {
                    String msg = smsMessage[n].getMessageBody();
                    Contact contact ;
                    String PhoneNumber=smsMessage[n].getOriginatingAddress().replace("+98", "0");
                    try {
                        if(database.getContactDao().queryForEq(Contact.Field_PhoneNumber,PhoneNumber).size()==1){
                            contact=database.getContactDao().queryForEq(Contact.Field_PhoneNumber,PhoneNumber).get(0);
                            contact.setState(2);
                            contact.setIsMonitor(true);
                            database.getContactDao().update(contact);

                            new ContactFragment.OnFragmentInteractionListener() {
                                @Override
                                public void onFragmentInteraction(Uri uri) {

                                }
                            };
                        }
                        else {

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
                else if (smsMessage[n].getMessageBody().startsWith("RC:")) {
                    List<Contact> contacts=null;
                    String PhoneNumber=smsMessage[n].getOriginatingAddress().replace("+98", "0");
                    try {
                        contacts=database.getContactDao().queryForEq(Contact.Field_PhoneNumber,PhoneNumber);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (contacts.size() == 0) {
                        String msg = smsMessage[n].getMessageBody();
                        Contact contact = new Contact();
                        contact.setContactName(msg.substring(msg.indexOf(":") + 1, msg.indexOf(",")));
                        contact.setPhoneNumber(msg.substring(msg.indexOf(",") + 1, msg.indexOf(";")));
                        contact.setIsMonitor(false);
                        contact.setState(1);
                        try {
                            database.getContactDao().create(contact);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(contacts.size() == 1){
                        Toast.makeText(context,"این درخواست از قبل وجود دارد ",Toast.LENGTH_SHORT).show();
//                        String msg = smsMessage[n].getMessageBody();
//                        Contact contact = new Contact();
//                        contact.setContactName(msg.substring(msg.indexOf(":") + 1, msg.indexOf(",")));
//                        contact.setPhoneNumber(msg.substring(msg.indexOf(",") + 1, msg.indexOf(";")));
//                        contact.setIsMonitor(false);
//                        contact.setState(1);
//                        try {
//                            database.getContactDao().update(contact);
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
                    }
                }
                else if (smsMessage[n].getMessageBody().startsWith("GLR:")) {
                    try {


                        String msg = smsMessage[n].getMessageBody();
                        Location location = new Location();
                        location.setLatitude(msg.substring(msg.indexOf(":") + 1, msg.indexOf(",")));
                        int second=msg.indexOf(",");
                        location.setLongitude(msg.substring(second + 1, msg.indexOf(",",second+1)));
                        int third=msg.indexOf(",",second+1);
                        location.setAltitude(msg.substring(third + 1, msg.indexOf(";")));
                        location.setContactID(database.getContactDao().queryForEq(Contact.Field_PhoneNumber,smsMessage[n].getOriginatingAddress().toString()).get(0));
                        List<Location> locations=database.getLocationDao().queryForEq(Location.Field_ContactID,database.getContactDao().queryForEq(Contact.Field_PhoneNumber,smsMessage[n].getOriginatingAddress().toString()).get(0));
                        location.setLocationID(locations.get(locations.size()-1).getLocationID());
                        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                        location.setDateTime(currentDateTimeString);
                        database.getLocationDao().update(location);

//                        FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
//                        ((FragmentActivity) context).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                        fragmentTransaction.add(R.id.content_main, LocationFragment.newInstance("1","2")).addToBackStack("Location").commit();
                    }
                    catch (SQLException e){
                        e.printStackTrace();
                    }

                }
               else if (smsMessage[n].getMessageBody().startsWith("GL:")) {
                    boolean isExist= false;
                    Contact contact=null;
                    try {
                        String msg=smsMessage[n].getOriginatingAddress().replace("+98", "0");
                        contact=database.getContactDao().queryForEq(Contact.Field_PhoneNumber,smsMessage[n].getOriginatingAddress().replace("+98", "0")).get(0);
                        if( database.getContactDao().queryForEq(Contact.Field_PhoneNumber,msg).size()==1 ) {
                            isExist = contact.getState() == 2;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    if (isExist) {
                        GPSTracker gps = new GPSTracker(context);
                        double latitude = 0;
                        double longitude = 0;
                        double altitude = 0;
                        // check if GPS enabled
                        if (gps.canGetLocation()) {

                            latitude = gps.getLatitude();
                            longitude = gps.getLongitude();
                            altitude =gps.getAltitude();

                            String dateNow=DateFormat.getDateTimeInstance().format(new Date()).toString();
                            Location l=new Location(contact,String.valueOf(latitude),String.valueOf(longitude),String.valueOf(altitude),dateNow);
                            sendSMSMessage(smsMessage[n].getOriginatingAddress(), "GLR:" + String.valueOf(latitude) + "," + String.valueOf(longitude)+ "," + String.valueOf(altitude) + ";");
                            try {
                                database.getLocationDao().create(l);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            // \n is for new line
                            //Toast.makeText(context.getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(context,context.getString(R.string.msg_turn_on_gps),Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(myIntent);
                        }
                    }
                }
            }


        }
    }





    protected void sendSMSMessage(String phoneNo,String message) {


        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            //Toast.makeText(context.getApplicationContext(), "geo code sent.", Toast.LENGTH_LONG).show();
        }

        catch (Exception e) {
            //Toast.makeText(context.getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


}

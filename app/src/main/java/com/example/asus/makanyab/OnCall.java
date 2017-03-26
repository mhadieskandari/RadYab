package com.example.asus.makanyab;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Debug;
import android.util.Log;
import android.widget.Toast;

import com.example.asus.makanyab.db.MakanYabDataBase;
import com.example.asus.makanyab.models.Contact;
import com.example.asus.makanyab.models.Setting;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

public class OnCall extends BroadcastReceiver {
    MakanYabDataBase dataBase;
    List<Setting> ss;
    //Context context;



    public OnCall() {
    }



    private MakanYabDataBase getHelper(Context context) {

        if ( dataBase== null) {
            dataBase = OpenHelperManager.getHelper(context, MakanYabDataBase.class);
        }
        return dataBase;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")){
            dataBase=getHelper(context.getApplicationContext());
            String phonenumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

            Setting password=new Setting();
            password.setSetting_Name(Setting.Field_Setting_PassWord);
            password.setSetting_Val(phonenumber);

            boolean isTruePass=false;

            try {
                ss=dataBase.getSettingDao().queryForEq(Setting.Field_Setting_Name,Setting.Field_Setting_PassWord);
                if(ss.size()==1){
                    isTruePass = ss.get(0).getSetting_Val().equals(phonenumber);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


            //Toast.makeText(context,"phone call go",Toast.LENGTH_LONG).show();

            if(isTruePass){
                PackageManager p = context.getPackageManager();
                ComponentName componentName = new ComponentName(context.getApplicationContext(), MainActivity.class);
                p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                Toast.makeText(context,"phone call accepted",Toast.LENGTH_LONG).show();
            }
            else {

            }

        }

//        if(intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")){
//            Toast.makeText(context,"phone call go",Toast.LENGTH_LONG).show();
//            String phonenumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
//            if(phonenumber.equals("866127002")){
//                PackageManager p = context.getPackageManager();
//                ComponentName componentName = new ComponentName(context.getApplicationContext(), MainActivity.class);
//                p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//                Toast.makeText(context,"phone call accepted",Toast.LENGTH_LONG).show();
//            }
//
//        }

    }
}

package com.example.asus.makanyab.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.asus.makanyab.R;
import com.example.asus.makanyab.models.Contact;
import com.example.asus.makanyab.models.Location;
import com.example.asus.makanyab.models.Setting;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Asus on 07/09/2016.
 */
public class MakanYabDataBase extends OrmLiteSqliteOpenHelper {

    private Dao<Location, Integer> locationDao=null;
    private Dao<Contact, Integer> contactDao=null;
    private Dao<Setting, Integer> settingDao=null;

    private static final String DATABASE_NAME = "MakanYabDataBase.db";
    private static final int DATABASE_VERSION = 1;

    private RuntimeExceptionDao<Setting,Integer> settingRuntimeDao=null;

    public MakanYabDataBase(Context context) {
        //,R.raw.ormlite_config
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {

            TableUtils.createTable(connectionSource, Setting.class);

            TableUtils.createTable(connectionSource, Contact.class);
            TableUtils.createTable(connectionSource, Location.class);

            InitualizeDB();






        } catch (SQLException e) {
            Log.e("mylog", "Unable to create datbases", e);
        }
    }
//658918977583000

    private void InitualizeDB() throws SQLException {

//        Setting password=new Setting();
//        password.setSetting_Name(Setting.Field_Setting_PassWord);
//        password.setSetting_Val("866127002");
//        getSettingDao().create(password);



//        Contact contact=new Contact("هادی اسکندری","09353364377",0,false);
//        getContactDao().create(contact);


//        Location l=new Location(contact,"12.4","53.24","22.50","1");
//        getLocationDao().create(l);

//        NeighborState neighborState=new NeighborState();
//        NeighborType neighborType=new NeighborType();
//        PayTitle payTitle =new PayTitle();
//        PayType payType = new PayType();
//        Period period=new Period();
//
//
//        neighborState.setNeighborStateName("ساکن");
//        getNeighborStateDao().create(neighborState);
//        neighborState.setNeighborStateName("خالی");
//        getNeighborStateDao().create(neighborState);
//
//        neighborType.setNeighborTypeName("صاحب ملک");
//        getNeighborTypeDao().create(neighborType);
//        neighborType.setNeighborTypeName("مستاجر");
//        getNeighborTypeDao().create(neighborType);
//
//        payTitle.setPayTitleName("قبض آب");
//        getPayTitleDao().create(payTitle);
//        payTitle.setPayTitleName("قبض برق");
//        getPayTitleDao().create(payTitle);
//
//        payType.setPayTypeName("پرداختی");
//        getPayTypeDao().create(payType);
//        payType.setPayTypeName("دریافتی");
//        getPayTypeDao().create(payType);
//
//        period.setPeriodName("فروردین و اردیبهشت 95");
//        period.setPeriodDate(new Date());
//        getPeriodDao().create(period);



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {

            TableUtils.dropTable(connectionSource, Location.class,true);
            TableUtils.dropTable(connectionSource, Contact.class, true);
            TableUtils.dropTable(connectionSource, Setting.class, true);

            onCreate(sqLiteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.e("mylog", "Unable to upgrade database from version " + i + " to new "
                    + i1, e);
        }
    }




    public Dao<Contact, Integer> getContactDao() throws SQLException {
        if (contactDao == null) {
            contactDao = getDao(Contact.class);
        }
        return contactDao;
    }

    public Dao<Location, Integer> getLocationDao() throws SQLException {
        if (locationDao == null) {
            locationDao = getDao(Location.class);
        }
        return locationDao;
    }
//
//    public Dao<NeighborType, Integer> getNeighborTypeDao() throws SQLException {
//        if (neighborTypeDao == null) {
//            neighborTypeDao = getDao(NeighborType.class);
//        }
//        return neighborTypeDao;
//    }
//
//    public Dao<NeighborState, Integer> getNeighborStateDao() throws SQLException {
//        if (neighborStateDao == null) {
//            neighborStateDao = getDao(NeighborState.class);
//        }
//        return neighborStateDao;
//    }
//
//    public Dao<Period, Integer> getPeriodDao() throws SQLException {
//        if (periodDao == null) {
//            periodDao = getDao(Period.class);
//        }
//        return periodDao;
//    }
//
//    public Dao<Pay, Integer> getPayDao() throws SQLException {
//        if (payDao == null) {
//            payDao = getDao(Pay.class);
//        }
//        return payDao;
//    }
//
//    public Dao<Neighbor, Integer> getNeighborDao() throws SQLException {
//        if (neighborDao == null) {
//            neighborDao = getDao(Neighbor.class);
//        }
//        return neighborDao;
//    }

    public Dao<Setting, Integer> getSettingDao() throws SQLException {
        if (settingDao == null) {
            settingDao = getDao(Setting.class);
        }
        return settingDao;
    }

    public RuntimeExceptionDao<Setting, Integer> getSettingRuntimeDao() {
        if(settingRuntimeDao==null)
            settingRuntimeDao=getRuntimeExceptionDao(Setting.class);
        return settingRuntimeDao;
    }

}

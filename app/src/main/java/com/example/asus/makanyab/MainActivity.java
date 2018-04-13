package com.example.asus.makanyab;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.makanyab.db.MakanYabDataBase;
import com.example.asus.makanyab.models.Setting;
import com.example.asus.makanyab.util.IabHelper;
import com.example.asus.makanyab.util.IabResult;
import com.example.asus.makanyab.util.Inventory;
import com.example.asus.makanyab.util.Purchase;
import com.farsitel.bazaar.IUpdateCheckService;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.norbsoft.typefacehelper.TypefaceCollection;
import com.norbsoft.typefacehelper.TypefaceHelper;

import java.sql.SQLException;
import java.util.List;

//// TODO: 11/5/2017 check simcart changes
//String imei = android.os.SystemProperties.get(android.telephony.TelephonyProperties.PROPERTY_IMSI);
//with permission
//android.permission.READ_PHONE_STATE


//// TODO: 11/15/2017  add font to whole of app

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,SettingFragment.OnFragmentInteractionListener ,ContactFragment.OnFragmentInteractionListener,LocationFragment.OnFragmentInteractionListener{
    private static final String DEVELOPER_ID ="992180686072" ;

    FragmentTransaction fragmentTransaction;
    MakanYabDataBase dataBase;

    IUpdateCheckService service;
    UpdateServiceConnection connection;


    NavigationView navigationView;
    // Debug tag, for logging
    static final String TAG = "makanyab";

    // SKUs for our products: the premium upgrade (non-consumable)
    static final String SKU_PREMIUM = "fullversion";

    // Does the user have the premium upgrade?
    public static boolean mIsPremium = false;

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 1001;

    // The helper object
    IabHelper mHelper;
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener;
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TypefaceCollection typeface = new TypefaceCollection.Builder()
                .set(Typeface.NORMAL, Typeface.createFromAsset(getAssets(), "fonts/Yekan.ttf"))
                .create();
        TypefaceHelper.init(typeface);
        TypefaceHelper.typeface(this);


        turnOnGPS();
        dataBase = getHelper();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);


        toggle.syncState();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);


        List<Setting> prem = null;
        try {
            prem = dataBase.getSettingDao().queryForEq(Setting.Field_Setting_Name, Setting.Field_Setting_IsPremium);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (prem.size() == 1) {
            if (prem.get(0).getSetting_Val().equals("1")) {
                mIsPremium = true;

            } else {
                try {
                    getBazarCheck();
                } catch (Exception e) {
                    Toast.makeText(this, getResources().getString(R.string.msg_bazar_is_not_installed), Toast.LENGTH_LONG).show();
                }

            }

        } else {
            try {
                getBazarCheck();
            } catch (Exception e) {
                Toast.makeText(this, getResources().getString(R.string.msg_bazar_is_not_installed), Toast.LENGTH_LONG).show();
            }
        }

        MenuItem m = navigationView.getMenu().findItem(R.id.nav_fullversion);
        if (mIsPremium) {
            m.setVisible(false);

        } else {
            m.setVisible(true);
        }


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CALL_PHONE, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.READ_SMS},
                    1);
        }






        //432118200971278
        //432350398229387


//        TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        String imsi = mTelephonyMgr.getSubscriberId();
//        Log.d("imsi",getMyPhoneNO());

    }
    private String getMyPhoneNO() {
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        return mPhoneNumber;
    }
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    public void turnOnGPS(){

        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        // getting GPS status
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            View view = getLayoutInflater().inflate(R.layout.turn_on_gps_layout, null);

            Button btnSave = (Button) view.findViewById(R.id.btnTurnOnGPS);
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            final android.support.v7.app.AlertDialog alertDialog_CreateOwner = builder.create();
            alertDialog_CreateOwner.setView(view);

            alertDialog_CreateOwner.setCancelable(false);
            alertDialog_CreateOwner.show();
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    alertDialog_CreateOwner.dismiss();

                }
            });
        }
    }


    private void getBazarCheck(){
        //bazar
        String base64EncodedPublicKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDKgOjzA0ZPTICl3+IzHe9Sja+CcpqXMOs287iQU7JY+9nXW8T4E4jB7eoZbI7RMyJhets4L1oXGlXwGLgl2sh9f/tPPO1RxSuBjcU80yht2joJ7zI9K/QPNj1bV+heoa8maCNNspNs+XNX8o00mh8hZyTYcJbIbAGMRgJNvsjJesaSJ/QFInIALGfR4MXVnswhbQzWMrJvswXiXnG6ZffKk3bj6VyhwwJ/Dlf3wIkCAwEAAQ==";

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                }
                // Hooray, IAB is fully set up!
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });



        mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                Log.d(TAG, "Query inventory finished.");
                if (result.isFailure()) {
                    Log.d(TAG, "Failed to query inventory: " + result);
                    return;
                }
                else {
                    Log.d(TAG, "Query inventory was successful.");
                    // does the user have the premium upgrade?
                    mIsPremium = inventory.hasPurchase(SKU_PREMIUM);

                    MenuItem m= navigationView.getMenu().findItem(R.id.nav_fullversion);
                    if(mIsPremium) {
                        m.setVisible(false);
                        try {
                            List<Setting> prem=dataBase.getSettingDao().queryForEq(Setting.Field_Setting_Name,Setting.Field_Setting_IsPremium);
                            if(prem.size()==0 ){
                                dataBase.getSettingDao().create(new Setting(Setting.Field_Setting_IsPremium,"1"));
                            }else if(prem.size()==1){
                                if(!prem.get(0).getSetting_Val().equals("1")){
                                    prem.get(0).setSetting_Val("1");
                                    dataBase.getSettingDao().update(prem.get(0));
                                }

                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else {
                        m.setVisible(true);
                    }
                    // update UI accordingly




                    Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
                }

                Log.d(TAG, "Initial inventory query finished; enabling main UI.");
            }
        };

        mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
            public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
                if (result.isFailure()) {
                    Log.d(TAG, "Error purchasing: " + result);
                    return;
                }
                else if (purchase.getSku().equals(SKU_PREMIUM)) {
                    // give user access to premium content and update the UI

                }
            }
        };
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }



    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        List<Setting> settings = null;
        try {
            settings = dataBase.getSettingDao().queryForEq(Setting.Field_Setting_Name, Setting.Field_Setting_PassWord);
        } catch (SQLException e) {

            e.printStackTrace();
        }
        if (settings.size() == 0 ) {
            View view = getLayoutInflater().inflate(R.layout.create_password_first_layout, null);
            final EditText etPassword = (EditText) view.findViewById(R.id.etNewPass);
            final EditText etRePassword = (EditText) view.findViewById(R.id.etRePassword);
            Button btnSave = (Button) view.findViewById(R.id.btnSave);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog alertDialog_CreatePasswrod = builder.create();
            alertDialog_CreatePasswrod.setView(view);

            alertDialog_CreatePasswrod.setCancelable(false);
            alertDialog_CreatePasswrod.show();
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etPassword.getText().toString().equals("") || etRePassword.getText().toString().equals("")) {
                        Toast.makeText(getApplication(), getResources().getString(R.string.msg_pass_repass_could_not_empty), Toast.LENGTH_LONG).show();
                    } else if (etPassword.getText().toString().equals(etRePassword.getText().toString())) {
                        Setting pass = new Setting();
                        pass.setSetting_Name(Setting.Field_Setting_PassWord);
                        pass.setSetting_Val(etPassword.getText().toString());

                        try {
                            dataBase.getSettingDao().create(pass);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }


                        Toast.makeText(getApplication(), getResources().getString(R.string.saved), Toast.LENGTH_LONG).show();

                        alertDialog_CreatePasswrod.dismiss();
                    } else if (!etPassword.getText().toString().equals(etRePassword.getText().toString())) {
                        Toast.makeText(getApplication(), getResources().getString(R.string.msg_pass_repass_is_not_match), Toast.LENGTH_LONG).show();


                    }
                }
            });


        }else if(settings.size()==1 && settings.get(0).getSetting_Val().equals("")){
            View view = getLayoutInflater().inflate(R.layout.create_password_first_layout, null);
            final EditText etPassword = (EditText) view.findViewById(R.id.etNewPass);
            final EditText etRePassword = (EditText) view.findViewById(R.id.etRePassword);
            Button btnSave = (Button) view.findViewById(R.id.btnSave);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog alertDialog_CreatePassword = builder.create();
            alertDialog_CreatePassword.setView(view);

            alertDialog_CreatePassword.setCancelable(false);
            alertDialog_CreatePassword.show();
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etPassword.getText().toString().equals("") || etRePassword.getText().toString().equals("")) {
                        Toast.makeText(getApplication(), getResources().getString(R.string.msg_pass_repass_could_not_empty), Toast.LENGTH_LONG).show();
                    } else if (etPassword.getText().toString().equals(etRePassword.getText().toString())) {
                        Setting pass = new Setting();
                        pass.setSetting_Name(Setting.Field_Setting_PassWord);
                        pass.setSetting_Val(etPassword.getText().toString());

                        try {
                            dataBase.getSettingDao().update(pass);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }


                        Toast.makeText(getApplication(), getResources().getString(R.string.saved), Toast.LENGTH_LONG).show();

                        alertDialog_CreatePassword.dismiss();
                    } else if (!etPassword.getText().toString().equals(etRePassword.getText().toString())) {
                        Toast.makeText(getApplication(), getResources().getString(R.string.msg_pass_repass_is_not_match), Toast.LENGTH_LONG).show();


                    }
                }
            });


        }

        List<Setting> name=null;
        List<Setting> phone=null;

        try {
            name = dataBase.getSettingDao().queryForEq(Setting.Field_Setting_Name, Setting.Field_Setting_MyName);
            phone=dataBase.getSettingDao().queryForEq(Setting.Field_Setting_Name,Setting.Field_Setting_MyPhone);
        } catch (SQLException e) {

            e.printStackTrace();
        }

        if(name.size()==0 ||phone.size()==0){
            View view = getLayoutInflater().inflate(R.layout.create_owner_first_layout, null);
            final EditText etName = (EditText) view.findViewById(R.id.etOwnerName);
            final EditText etPhone = (EditText) view.findViewById(R.id.etOwnerPhone);
            Button btnSave = (Button) view.findViewById(R.id.btnSave);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog alertDialog_CreateOwner = builder.create();
            alertDialog_CreateOwner.setView(view);

            alertDialog_CreateOwner.setCancelable(false);
            alertDialog_CreateOwner.show();
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etName.getText().toString().equals("") || etPhone.getText().toString().equals("")) {
                        Toast.makeText(getApplication(), getResources().getString(R.string.msg_name_phone_could_not_empty), Toast.LENGTH_LONG).show();
                    }
                    else {
                        Setting name = new Setting();
                        name.setSetting_Name(Setting.Field_Setting_MyName);
                        name.setSetting_Val(etName.getText().toString());

                        Setting phone=new Setting();
                        phone.setSetting_Name(Setting.Field_Setting_MyPhone);
                        phone.setSetting_Val(etPhone.getText().toString());

                        try {
                            dataBase.getSettingDao().create(name);
                            dataBase.getSettingDao().create(phone);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }


                        Toast.makeText(getApplication(), getResources().getString(R.string.saved), Toast.LENGTH_LONG).show();

                        alertDialog_CreateOwner.dismiss();
                    }
                }
            });

        }
        else if(name.get(0).getSetting_Name().equals("")||phone.get(0).getSetting_Name().equals("")) {
            View view = getLayoutInflater().inflate(R.layout.create_owner_first_layout, null);
            final EditText etName = (EditText) view.findViewById(R.id.etOwnerName);
            final EditText etPhone = (EditText) view.findViewById(R.id.etOwnerPhone);
            Button btnSave = (Button) view.findViewById(R.id.btnSave);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog alertDialog_CreateOwner = builder.create();
            alertDialog_CreateOwner.setView(view);

            alertDialog_CreateOwner.setCancelable(false);
            alertDialog_CreateOwner.show();
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etName.getText().toString().equals("") || etPhone.getText().toString().equals("")) {
                        Toast.makeText(getApplication(), getResources().getString(R.string.msg_name_phone_could_not_empty), Toast.LENGTH_LONG).show();
                    }
                    else {
                        Setting name = new Setting();
                        name.setSetting_Name(Setting.Field_Setting_MyName);
                        name.setSetting_Val(etName.getText().toString());

                        Setting phone=new Setting();
                        phone.setSetting_Name(Setting.Field_Setting_MyPhone);
                        phone.setSetting_Val(etPhone.getText().toString());

                        try {
                            dataBase.getSettingDao().create(name);
                            dataBase.getSettingDao().create(phone);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }


                        Toast.makeText(getApplication(), getResources().getString(R.string.saved), Toast.LENGTH_LONG).show();

                        alertDialog_CreateOwner.dismiss();
                    }
                }
            });
        }



    }

    private MakanYabDataBase getHelper() {
        if ( dataBase== null) {
            dataBase = OpenHelperManager.getHelper(this, MakanYabDataBase.class);
        }
        return dataBase;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();

            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 1) {
                fm.popBackStack();
            } else {
                //super.onBackPressed();
                if (doubleBackToExitPressedOnce) {
                    //super.onBackPressed();
                    System.exit(0);
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "برای خروج دوبار کلید Back را بزنید", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragmentTransaction =getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if (id == R.id.nav_contact) {
            fragmentTransaction.add(R.id.content_main, ContactFragment.newInstance("1","2")).addToBackStack("Contact").commit();
        }
        else if (id == R.id.nav_location_requests) {
            fragmentTransaction.add(R.id.content_main, LocationFragment.newInstance("1","2")).addToBackStack("Location").commit();
        }
        else if (id == R.id.nav_fullversion) {
            try {
                if (!mIsPremium) {
                    mHelper.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST, mPurchaseFinishedListener, "payload-string");
                } else {
                    Toast.makeText(getApplication(), R.string.msg_isPremium, Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e){
                Toast.makeText(this,getString(R.string.msg_bazar_is_not_installed),Toast.LENGTH_LONG).show();
            }
        }
        else if (id == R.id.nav_setting) {
           fragmentTransaction.add(R.id.content_main, SettingFragment.newInstance("1","2")).addToBackStack("Setting").commit();
        }
        else if (id == R.id.nav_contactus) {
            fragmentTransaction.add(R.id.content_main, AboutFragment.newInstance()).addToBackStack("About").commit();
        }
        else if (id == R.id.nav_update) {
            try{
                initServiceUpdate();
            }
            catch (Exception e){
                Toast.makeText(this,getString(R.string.msg_bazar_is_not_installed),Toast.LENGTH_LONG).show();
            }

        }
        else if (id == R.id.nav_help) {
            fragmentTransaction.add(R.id.content_main, HelpFragment.newInstance()).addToBackStack("Help").commit();
        }
        else if (id == R.id.nav_vote) {
            try{
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setData(Uri.parse("bazaar://details?id=" + getPackageName()));
                intent.setPackage("com.farsitel.bazaar");
                startActivity(intent);
            }
            catch (Exception e){
                Toast.makeText(this,getString(R.string.msg_bazar_is_not_installed),Toast.LENGTH_LONG).show();
            }

        }
        else if (id == R.id.nav_another_apps) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("bazaar://collection?slug=by_author&aid=" + DEVELOPER_ID));
                intent.setPackage("com.farsitel.bazaar");
                startActivity(intent);
            }
            catch (Exception e){
                Toast.makeText(this,getString(R.string.msg_bazar_is_not_installed),Toast.LENGTH_LONG).show();
            }
        }
        else if (id == R.id.nav_exit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("می خواهید خارج شوید؟");
            builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            });
            builder.setNegativeButton("خیر",null);
            builder.create().show();
        }else if (id == R.id.nav_channel_telegram) {
            TelegramChannel();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    public void TelegramChannel() {
//        Intent telegram = new Intent(android.content.Intent.ACTION_SEND);
//        telegram.setData(Uri.parse("t.me/RadyabGolbarg"));
//        telegram.setPackage("org.telegram.messenger");
//        startActivity(Intent.createChooser(telegram, getString(R.string.share_with_telegram)));
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=RadyabGolbarg"));
            startActivity(intent);
        }
        catch (Exception e){
            Toast.makeText(this,getString(R.string.msg_telegram_is_not_installed),Toast.LENGTH_LONG).show();

        }

    }




    @Override
    public void onFragmentInteraction(Uri uri) {
//        ContactFragment contactFragment=(ContactFragment)getSupportFragmentManager().findFragmentById(R.id.contact_fragment);
//        getSupportFragmentManager().beginTransaction().replace(R.id.content_main,contactFragment);



    }

    boolean doubleBackToExitPressedOnce = false;



    private void initServiceUpdate() {
        Log.i(TAG, "initServiceUpdate()");
        connection = new UpdateServiceConnection();
        Intent i = new Intent(
                "com.farsitel.bazaar.service.UpdateCheckService.BIND");
        i.setPackage("com.farsitel.bazaar");
        boolean ret = bindService(i, connection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "initServiceUpdate() bound value: " + ret);
    }

    /** This is our function to un-binds this activity from our service. */
    private void releaseService() {
        unbindService(connection);
        connection = null;
        Log.d(TAG, "releaseService(): unbound.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseService();
    }



    class UpdateServiceConnection implements ServiceConnection {

        IUpdateCheckService service;
        UpdateServiceConnection connection;
        public void onServiceConnected(ComponentName name, IBinder boundService) {
            service = IUpdateCheckService.Stub
                    .asInterface(boundService);
            try {

                long vCode = service.getVersionCode(getPackageName());
                if(vCode==-1) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.last_updated),
                            Toast.LENGTH_LONG).show();
                }
                else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "onServiceConnected(): Connected");
        }

        public void onServiceDisconnected(ComponentName name) {
            service = null;
            Log.d(TAG, "onServiceDisconnected(): Disconnected");
        }
    }






}

package com.example.asus.makanyab.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.makanyab.GPSTracker;
import com.example.asus.makanyab.MainActivity;
import com.example.asus.makanyab.MyMapFragment;
import com.example.asus.makanyab.R;
import com.example.asus.makanyab.db.MakanYabDataBase;
import com.example.asus.makanyab.models.Location;
import com.google.android.gms.maps.MapFragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Asus on 26/01/2017.
 */

public class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationRecyclerAdapter.LocationViewHolder> {
    private List<Location> mData;
    private LayoutInflater mInflator;
    private MakanYabDataBase dataBase;
    private Context context;

    public LocationRecyclerAdapter( Context context,List<Location> mData) {
        this.mData = mData;
        this.mInflator =LayoutInflater.from(context);
        this.context = context;
    }

    private MakanYabDataBase getHelper() {
        if ( dataBase== null) {
            dataBase = OpenHelperManager.getHelper(context, MakanYabDataBase.class);
        }
        return dataBase;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflator.inflate(R.layout.location_item,parent,false);
        LocationRecyclerAdapter.LocationViewHolder holder =new LocationRecyclerAdapter.LocationViewHolder(view);
        dataBase=getHelper();
        return holder;
    }

    @Override
    public void onBindViewHolder(final LocationViewHolder holder, final int position) {
        final Location location=mData.get(position);
        holder.setData(location,position);
        holder.setListener();
        if(mData.get(position).getLatitude()!=null){
            holder.itemView.findViewById(R.id.location_cardview_insidelayout).setBackgroundColor(Color.parseColor("#D5FFA5"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View alertView = LayoutInflater.from(context).inflate(R.layout.location_detail_alert_dialog_layout, null);
                final TextView tvContactName = (TextView) alertView.findViewById(R.id.tvContactName);
                final TextView tvContactPhone = (TextView) alertView.findViewById(R.id.tvContactPhoneNumber);
                final TextView tvLongitude = (TextView) alertView.findViewById(R.id.tvLongitude);
                final TextView tvLatitude = (TextView) alertView.findViewById(R.id.tvLatitude);
                final TextView tvAltitude = (TextView) alertView.findViewById(R.id.tvAltitude);
                tvContactName.setText(mData.get(position).getContactID().getContactName());
                tvContactPhone.setText(mData.get(position).getContactID().getPhoneNumber());
                tvLongitude.setText("Longitude: "+mData.get(position).getLongitude());
                tvLatitude.setText("Latitude: "+mData.get(position).getLatitude());
                tvAltitude.setText("Altitude: "+mData.get(position).getAltitude());
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(alertView).setCancelable(true).setPositiveButton("مشاهده روی نقشه", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri gmmIntentUri = Uri.parse("geo:"+mData.get(position).getLatitude()+","+mData.get(position).getLongitude());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(mapIntent);
                        }
                        else {
                            Toast.makeText(context, context.getResources().getString(R.string.msg_install_googlemaps),Toast.LENGTH_LONG).show();
                        }





                    }
                }).setNegativeButton("بستن",null).show();

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("برای حذف این ایتم اطمینان دارید؟").setPositiveButton("بله", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(position);
                    }
                }).setNegativeButton("خیر", null).show();
                return true;
            }
        });




    }


    private void removeItem(int position){
        if(MainActivity.mIsPremium){
            try {
                dataBase.getLocationDao().delete(mData.get(position));
                mData.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,mData.size());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(context,context.getResources().getString(R.string.msg_cannot_delete_locationrequest),Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView name,longitude,latitude,altitude;
        Location current;
        int position;

        public LocationViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.tvContactName);
            longitude=(TextView)itemView.findViewById(R.id.tvLongitude);
            latitude=(TextView)itemView.findViewById(R.id.tvLatitude);
            altitude=(TextView)itemView.findViewById(R.id.tvAltitude);
        }

        public void setData(Location current, int position) {
            this.position=position;
            this.current=current;
            this.name.setText(current.getContactID().getContactName());
            this.longitude.setText(current.getLongitude());
            this.latitude.setText(current.getLatitude());
            this.altitude.setText(current.getAltitude());
        }

        public void setListener() {

        }


    }
}

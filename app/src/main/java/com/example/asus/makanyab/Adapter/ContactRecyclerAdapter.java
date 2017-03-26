package com.example.asus.makanyab.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.makanyab.ContactFragment;
import com.example.asus.makanyab.R;
import com.example.asus.makanyab.db.MakanYabDataBase;
import com.example.asus.makanyab.models.Contact;
import com.example.asus.makanyab.models.Setting;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Asus on 23/01/2017.
 */

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ContactViewHolder> {

    private List<Contact> mData;
    private LayoutInflater mInflator;
    private MakanYabDataBase dataBase;
    private Context context;

    public ContactRecyclerAdapter(Context context, List<Contact> mData) {
        this.mData = mData;
        this.mInflator = LayoutInflater.from(context);
        this.context=context;
    }

    private MakanYabDataBase getHelper() {
        if ( dataBase== null) {
            dataBase = OpenHelperManager.getHelper(context, MakanYabDataBase.class);
        }
        return dataBase;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflator.inflate(R.layout.contact_item,parent,false);
        ContactViewHolder holder =new ContactViewHolder(view);
        dataBase=getHelper();
        return holder;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, final int position) {
        final Contact contact=mData.get(position);
        holder.setData(contact,position);
        holder.setListener();
        if(mData.get(position).getState()==2){
            holder.itemView.findViewById(R.id.contact_cardview_layout).setBackgroundColor(Color.parseColor("#D5FFA5"));
        }else if(mData.get(position).getState()==1){
            holder.itemView.findViewById(R.id.contact_cardview_layout).setBackgroundColor(Color.parseColor("#EE9EF5"));
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
//                if(mData.get(position).getState()==2){
//                    Toast.makeText(context," قبلا تایید نهایی فرستاده است "+mData.get(position).getContactName(),Toast.LENGTH_SHORT).show();
//                }else if(mData.get(position).getState()==1){
//
//                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(mData.get(position).getContactName()+" ارسال درخواست تایید از ").setPositiveButton("بله", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                                baraye ersal code taiide mokhatab  // SendRequest();

                            if(mData.get(position).getIsMonitor() &&mData.get(position).getState()==0 || mData.get(position).getIsMonitor() &&mData.get(position).getState()==1) {
                                String msg = "";
                                try {
                                    Setting Myname = dataBase.getSettingDao().queryForEq(Setting.Field_Setting_Name, Setting.Field_Setting_MyName).get(0);
                                    Setting MyPhone = dataBase.getSettingDao().queryForEq(Setting.Field_Setting_Name, Setting.Field_Setting_MyPhone).get(0);

                                    msg = "RC:" + Myname.getSetting_Val() + "," + MyPhone.getSetting_Val() + ";";

                                    Contact contact1=mData.get(position);

                                    try {
                                        contact1.setState(1);
                                        dataBase.getContactDao().update(contact1);
                                        sendSMSMessage(mData.get(position).getPhoneNumber(), msg,"request");
                                        v.findViewById(R.id.contact_cardview_layout).setBackgroundColor(Color.parseColor("#EE9EF5"));
                                    }catch (Exception e){
                                        Toast.makeText(context, context.getString(R.string.msg_sms_send_failed), Toast.LENGTH_LONG).show();
                                    }



                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                            }else if(!mData.get(position).getIsMonitor() && mData.get(position).getState()!=2) {
                                String msg = "";
                                try {
                                    Setting Myname = dataBase.getSettingDao().queryForEq(Setting.Field_Setting_Name, Setting.Field_Setting_MyName).get(0);
                                    Setting MyPhone = dataBase.getSettingDao().queryForEq(Setting.Field_Setting_Name, Setting.Field_Setting_MyPhone).get(0);

                                    msg = "RCD:" + Myname.getSetting_Val() + "," + MyPhone.getSetting_Val() + ";";
                                    Contact contact1=mData.get(position);
                                    try{
                                        contact1.setState(2);
                                        dataBase.getContactDao().update(contact1);
                                        sendSMSMessage(mData.get(position).getPhoneNumber(), msg,"accept");
                                    }
                                    catch (Exception e){
                                        Toast.makeText(context, context.getString(R.string.msg_sms_send_failed), Toast.LENGTH_LONG).show();
                                    }

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }



                        }
                    }).setNegativeButton("خیر", null).show();
//                }
                return true;
            }
        });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View alertView = LayoutInflater.from(context).inflate(R.layout.contact_detail_alert_dialog_layout, null);
                final TextView tvContactName = (TextView) alertView.findViewById(R.id.name);
                final TextView tvContactPhone = (TextView) alertView.findViewById(R.id.phone);
                final TextView tvContactState = (TextView) alertView.findViewById(R.id.state);
                tvContactName.setText(mData.get(position).getContactName());
                tvContactPhone.setText(mData.get(position).getPhoneNumber());
                tvContactState.setText(mData.get(position).getStateName());
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(alertView).setCancelable(true).setPositiveButton("بستن",null).show();

            }
        });


    }

    protected void sendSMSMessage(String phoneNo,String message,String type) {
//        Log.i("Send SMS", "");
//        String phoneNo = "";
//        String message = "";


            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            if(type.equals("request")){
                Toast.makeText(context, context.getString(R.string.msg_send_registration_request), Toast.LENGTH_LONG).show();
            }
            else if(type.equals("accept")){
                Toast.makeText(context, context.getString(R.string.msg_send_registration_accept), Toast.LENGTH_LONG).show();
            }



    }

    public void editItem(int position,Contact newContact){

        newContact.setContactID(mData.get(position).getContactID());
        newContact.setState(mData.get(position).getState());
        newContact.setIsMonitor(mData.get(position).getIsMonitor());

        try {
            dataBase.getContactDao().update(newContact);
            mData.set(position,newContact);
            notifyItemChanged(position);
            notifyItemRangeChanged(position,mData.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }




    }

    public void removeItem(int position){
        try {
            dataBase.getContactDao().delete(mData.get(position));
            mData.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,mData.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    class ContactViewHolder extends RecyclerView.ViewHolder{
        TextView name,phoneNumber;
        ImageView profile,delete,edit;
        Contact current;
        int position;

        public ContactViewHolder(View itemView) {
            super(itemView);




            name= (TextView) itemView.findViewById(R.id.tvContactName);
            phoneNumber=(TextView)itemView.findViewById(R.id.tvContactPhoneNumber);
            profile=(ImageView)itemView.findViewById(R.id.imgProfile);
            delete=(ImageView)itemView.findViewById(R.id.btnDelete);
            edit=(ImageView)itemView.findViewById(R.id.btnEdit);





        }

        public void setData(Contact current, int position) {
            this.position=position;
            this.current=current;
            this.name.setText(current.getContactName());
            this.phoneNumber.setText(current.getPhoneNumber());
            this.profile.setImageResource(R.drawable.ic_menu_person);
        }

        public void setListener(){
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("برای حذف این ایتم اطمینان دارید؟").setPositiveButton("بله", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeItem(position);
                        }
                    }).setNegativeButton("خیر", null).show();

                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    // Get the layout inflater
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View alertView = inflater.inflate(R.layout.contact_create_alert_dialog_layout,null);

                    final EditText etContactName = (EditText) alertView.findViewById(R.id.name);
                    final EditText etContactPhone = (EditText) alertView.findViewById(R.id.phone);

                    etContactName.setText(current.getContactName().toString());
                    etContactPhone.setText(current.getPhoneNumber().toString());

                    builder.setView(alertView)
                            // Add action buttons
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    String name,phone;
                                    name=etContactName.getText().toString();
                                    phone=etContactPhone.getText().toString();
                                    Contact c=new Contact(name,phone,0,false);
                                    editItem(position,c);
                                }
                            })
                            .setNegativeButton(R.string.cancel,null).show();
                }
            });



        }
    }
}

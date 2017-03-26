package com.example.asus.makanyab.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.makanyab.R;
import com.example.asus.makanyab.models.AboutItem;

import java.util.List;

/**
 * Created by Asus on 09/02/2017.
 */

public class AboutRecyclerAdapter extends RecyclerView.Adapter<AboutRecyclerAdapter.AboutViewHolder> {
    private List<AboutItem> mData;
    private LayoutInflater mInflator;
    private Context context;

    public AboutRecyclerAdapter(Context context, List<AboutItem> mData) {
        this.context = context;
        this.mData = mData;
        this.mInflator = LayoutInflater.from(context);
    }

    @Override
    public AboutRecyclerAdapter.AboutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflator.inflate(R.layout.about_item, parent, false);
        AboutViewHolder holder = new AboutViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(AboutRecyclerAdapter.AboutViewHolder holder, final int position) {
        final AboutItem aboutItem = mData.get(position);
        holder.setData(aboutItem, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData.get(position).getType().equals("name")) {
                    AlertDialog builder=new AlertDialog.Builder(context).setPositiveButton(context.getResources().getString(R.string.cancel).toString(), null).setTitle(context.getResources().getString(R.string.author_name)).setMessage(mData.get(position).getTitle()).show();

                }
                else if (mData.get(position).getType().equals("phone")) {

                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + aboutItem.getTitle()));
                        context.startActivity(callIntent);
                    }

                }
                else if(mData.get(position).getType().equals("email")){
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + Uri.encode(mData.get(position).getTitle())));
                    final PackageManager packageManager = context.getPackageManager();
                    final List<ResolveInfo> list = packageManager.queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    if (!list.isEmpty()) {
                        context.startActivity(Intent.createChooser(emailIntent, "Send email via..."));
                    }
                    else {
                        Toast.makeText(context,context.getResources().getString(R.string.msg_email_app_isnotinstalled),Toast.LENGTH_LONG).show();
                    }
//                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
//                    emailIntent.putExtra(Intent.EXTRA_TEXT, body);

                }
                else if(mData.get(position).getType().equals("linkedin")){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://hadi-eskandari-62291985"));
                    final PackageManager packageManager = context.getPackageManager();
                    final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    if (list.isEmpty()) {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/hadi-eskandari-62291985/"));
                    }
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class AboutViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView tvTitle;
        int position;
        AboutItem current;

        public AboutViewHolder(View itemView) {
            super(itemView);
            imgIcon= (ImageView) itemView.findViewById(R.id.imgIcon);
            tvTitle= (TextView) itemView.findViewById(R.id.tvTitle);
        }

        public void setData(AboutItem current, int position) {
            this.position=position;
            this.current=current;
            this.imgIcon.setImageDrawable(current.getImg());
            this.tvTitle.setText(current.getTitle());
        }
    }
}

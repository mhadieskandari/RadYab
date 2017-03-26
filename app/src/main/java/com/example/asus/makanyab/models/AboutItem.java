package com.example.asus.makanyab.models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.asus.makanyab.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 09/02/2017.
 */
public class AboutItem {
    private String Title;
    private Drawable Img;
    private String Type;
    private Context context;

    public AboutItem(String title, Drawable img,String type) {
        Title = title;
        Img = img;
        Type=type;
    }
    public AboutItem(Context context) {
        this.context=context;
    }

    public String getTitle() {
        return Title;
    }

    public Drawable getImg() {
        return Img;
    }

    public String getType() {
        return Type;
    }

    public List<AboutItem> setData(){
        List<AboutItem> list=new ArrayList<AboutItem>();
        list.add(new AboutItem(context.getResources().getString(R.string.programmer),context.getResources().getDrawable(R.drawable.ic_menu_person),"name"));
        list.add(new AboutItem("+989353364377",context.getResources().getDrawable(R.drawable.ic_menu_about_us),"phone"));
        list.add(new AboutItem("hadi.eskandari@ymail.com",context.getResources().getDrawable(R.drawable.ic_email),"email"));
        list.add(new AboutItem("https://www.linkedin.com/in/hadi-eskandari-62291985/",context.getResources().getDrawable(R.drawable.ic_linkedin),"linkedin"));
        return  list;
    }

}

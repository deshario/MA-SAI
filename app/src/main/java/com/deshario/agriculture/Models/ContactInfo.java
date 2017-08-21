package com.deshario.agriculture.Models;

import android.widget.ImageView;

/**
 * Created by Deshario on 8/21/2017.
 */

public class ContactInfo {

    public ImageView logo;
    public String title;
    public String desc;

    public ImageView getLogo() {
        return logo;
    }

    public void setLogo(ImageView logo) {
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
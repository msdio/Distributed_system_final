package com.example.cdstermproject;

import android.graphics.drawable.Drawable;

public class user_login {
    String email;
    Drawable poster;
/*
    public user_login(String email,Drawable poster) {
        this.email = email;
        this.poster = poster;
    }
*/

    public Drawable getPoster() {
        return poster;
    }

    public void setPoster(Drawable poster) {
        this.poster = poster;
    }

    public String getName() {
        return email;
    }

    public void setName(String name) {
        this.email = name;
    }

}

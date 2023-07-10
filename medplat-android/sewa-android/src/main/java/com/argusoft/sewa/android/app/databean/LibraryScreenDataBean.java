package com.argusoft.sewa.android.app.databean;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

/**
 * Created by prateek on 15/2/19.
 */
public class LibraryScreenDataBean {

    public final Bitmap image;
    public final String name;

    public LibraryScreenDataBean(Bitmap image, String name) {
        this.image = image;
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "LibraryScreenDataBean{" +
                "image=" + image +
                ", name='" + name + '\'' +
                '}';
    }
}

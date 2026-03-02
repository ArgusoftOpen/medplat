package com.argusoft.sewa.android.app.databean;

import android.graphics.Bitmap;

public class LmsScreenDataBean {

    public final Bitmap image;
    public final String name;
    public final Long actualId;
    public final boolean isBookMarked;

    public LmsScreenDataBean(Bitmap image, String name, Long actualId, boolean isBookMarked) {
        this.image = image;
        this.name = name;
        this.actualId = actualId;
        this.isBookMarked = isBookMarked;
    }
}

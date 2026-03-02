package com.argusoft.sewa.android.app.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.argusoft.sewa.android.app.R;

/**
 * Created by prateek on 11/29/19
 */
public class LoadingView extends LinearLayout {

    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.loading_layout, this);
    }

}

package com.argusoft.sewa.android.app.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.util.UtilBean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author alpeshkyada
 */
public class MyMenuAdapter extends BaseAdapter {

    private final Context context;
    private final int[] images;
    private final String[] names;
    private final Map<Integer, View> adapterView;
    private boolean isElevated;

    public MyMenuAdapter(Context context, int[] images, String[] names, boolean isElevated) {
        this.context = context;
        this.images = images;
        this.names = names;
        this.isElevated = isElevated;
        this.adapterView = new HashMap<>();
    }

    @Override
    public int getCount() {
        if (names != null) {
            return names.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (names != null) {
            return names[position];
        }
        return null;
    }

    public View getLayoutAtPosition(int position) {
        if (!adapterView.isEmpty() && adapterView.containsKey(position)) {
            return adapterView.get(position);
        }
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = adapterView.get(position);
        if (view == null && names != null && images != null) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.home_screen_cards, null);
            if (!isElevated) {
                inflate.setPadding(-20, 0, 0, 0);
                View cardView = inflate.findViewById(R.id.card_view);
                cardView.setElevation(0f);
                if (position % 3 != 0) {
                    cardView.setBackground(context.getDrawable(R.drawable.line_background_left));
                }
            }
            ImageView image = inflate.findViewById(R.id.card_imageView);
            image.setImageResource(images[position]);
            TextView textView = inflate.findViewById(R.id.card_textView);
            textView.setText(UtilBean.getMyLabel(names[position]));
            adapterView.put(position, inflate);
            return inflate;
        }
        return view;
    }
}

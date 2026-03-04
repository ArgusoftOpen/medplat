package com.argusoft.sewa.android.app.component;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alpeshkyada
 */
public class MyArrayAdapter extends ArrayAdapter<String> {

    private List<String> myitems;

    public MyArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
        super(context, textViewResourceId, objects);
        myitems = new ArrayList<>();
        for (String a : objects) {
            myitems.add(UtilBean.getMyLabel(a));
        }
    }

    public MyArrayAdapter(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
        myitems = new ArrayList<>();
        for (String a : objects) {
            myitems.add(UtilBean.getMyLabel(a));
        }
    }

    public void setItems(List<String> items) {
        myitems.clear();
        myitems.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        MaterialTextView textView = (MaterialTextView) super.getView(position, convertView, parent);
        textView.setText(myitems.get(position));
        textView.setTextAppearance(parent.getContext(), R.style.CustomAnswerView);
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        MaterialTextView textView = (MaterialTextView) super.getDropDownView(position, convertView, parent);
        textView.setText(myitems.get(position));
        textView.setTextAppearance(parent.getContext(), R.style.CustomAnswerView);
        return textView;
    }
}

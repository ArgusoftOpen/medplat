package com.argusoft.sewa.android.app.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.constants.LibraryConstants;
import com.argusoft.sewa.android.app.databean.LmsScreenDataBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LMSVideoAdapter extends BaseAdapter {
    private Context context;
    private List<LmsScreenDataBean> libraryList;
    private Map<Integer, View> adapterView;
    private String fileType;
    private int colCount;

    public LMSVideoAdapter(Context context, List<LmsScreenDataBean> libraryList, String fileType) {
        this.context = context;
        this.libraryList = libraryList;
        this.adapterView = new HashMap<>();
        this.fileType = fileType;
        setColCountAccordingToFileType();
    }

    private void setColCountAccordingToFileType() {
        switch (fileType) {
            case LibraryConstants.VIDEOS:
            case LibraryConstants.IMAGES:
                colCount = 2;
                break;
            default:
                colCount = 3;
                break;
        }
    }

    @Override
    public int getCount() {
        if (libraryList != null) {
            return libraryList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (libraryList != null) {
            return libraryList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = adapterView.get(position);
        if (view == null && libraryList != null && !libraryList.isEmpty()) {
            final LmsScreenDataBean libraryScreenDataBean = libraryList.get(position);

            View inflate = getViewAccordingToFileType();
            View cardView = inflate.findViewById(R.id.card_view);
            setRightBorderBackground(cardView, position);
            if (libraryScreenDataBean.image != null) {
                ImageView image = inflate.findViewById(R.id.card_imageView);
                image.setImageBitmap(libraryScreenDataBean.image);
            }
            TextView textView = inflate.findViewById(R.id.card_textView);
            textView.setText(libraryScreenDataBean.name);

            adapterView.put(position, inflate);
            return inflate;
        }
        return view;
    }

    private void setRightBorderBackground(View cardView, int position) {
        switch (fileType) {
            case LibraryConstants.FOLDERS:
            case LibraryConstants.PDFS:
            case LibraryConstants.UNSUPPORTED_FILES:
                if (position % colCount != 0) {
                    cardView.setBackground(context.getDrawable(R.drawable.line_background_left));
                }
                break;
            default:
        }
    }

    private View getViewAccordingToFileType() {
        View inflate;
        switch (fileType) {

            case LibraryConstants.VIDEOS:
            case LibraryConstants.IMAGES:
                inflate = LayoutInflater.from(context).inflate(R.layout.library_video_cards, null);
                inflate.setPadding(-20, 0, 0, 0);
                return inflate;

            default:
                inflate = LayoutInflater.from(context).inflate(R.layout.library_folder_cards, null);
                inflate.setPadding(-20, 0, 0, 0);
                return inflate;

        }
    }
}

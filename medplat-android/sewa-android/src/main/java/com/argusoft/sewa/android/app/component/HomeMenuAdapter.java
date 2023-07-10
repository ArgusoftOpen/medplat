package com.argusoft.sewa.android.app.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.argusoft.sewa.android.app.BuildConfig;
import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.listeners.OnMenuItemViewVClickListener;
import com.argusoft.sewa.android.app.constants.MenuConstants;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.UtilBean;

/**
 * @author utkarsh jadav
 * @since 21/07/2021
 */
public class HomeMenuAdapter extends RecyclerView.Adapter<HomeMenuAdapter.MyViewHolder> {

    private final Context context;
    private final int[] images;
    private final String[] names;
    private final String[] constant;
    private boolean isElevated;
    private boolean isNewNotification;
    private boolean isAnyLmsUpdate;
    OnMenuItemViewVClickListener onItemClickListener;

    public HomeMenuAdapter(Context context, int[] images, String[] names,String[] constant,
                           boolean isElevated, boolean isNewNotification, boolean isAnyLmsUpdate,
                           OnMenuItemViewVClickListener onItemClickListener) {
        this.context = context;
        this.images = images;
        this.names = names;
        this.constant = constant;
        this.isElevated = isElevated;
        this.isNewNotification = isNewNotification;
        this.isAnyLmsUpdate = isAnyLmsUpdate;
        this.onItemClickListener = onItemClickListener;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_screen_cards, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (BuildConfig.FLAVOR.equalsIgnoreCase(GlobalTypes.UTTARAKHAND_FLAVOR)){
            if (names[position].equalsIgnoreCase("Work Log")){
                names[position] = "Sync Status";
            }
        }
        holder.textView.setText(UtilBean.getMyLabel(names[position]));
        holder.image.setImageResource(images[position]);
        if (!isElevated) {
            holder.cardView.setElevation(0f);
            if (position % 3 != 0) {
                holder.cardView.setBackground(context.getDrawable(R.drawable.line_background_left));
            }
        }
        if (isNewNotification && constant[position].equalsIgnoreCase(MenuConstants.ANNOUNCEMENTS))
            holder.notificationBadge.setVisibility(View.VISIBLE);
        if (isAnyLmsUpdate && constant[position].equalsIgnoreCase(MenuConstants.LEARNING_MANAGEMENT_SYSTEM))
            holder.notificationBadge.setVisibility(View.VISIBLE);
        else
            holder.notificationBadge.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(view -> onItemClickListener.onItemClickListener(position, holder.itemView));
    }

    @Override
    public int getItemCount() {
        if (names != null) {
            return names.length;
        }
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        ImageView notificationBadge;
        TextView textView;
        CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.card_imageView);
            notificationBadge = view.findViewById(R.id.notification_badge);
            textView = view.findViewById(R.id.card_textView);
            cardView = view.findViewById(R.id.card_view);
        }
    }

    public void setNewNotification(boolean newNotification, boolean isAnyLmsUpdate) {
        this.isNewNotification = newNotification;
        this.isAnyLmsUpdate = isAnyLmsUpdate;
        notifyDataSetChanged();
    }

}


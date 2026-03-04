package com.argusoft.sewa.android.app.treeview.holder;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.treeview.model.TreeNode;


/**
 * Created by admin on 31-03-2016.
 */
public class TreeHolder extends TreeNode.BaseNodeViewHolder<TreeHolder.IconTreeItem> {
    public static final int DEFAULT = 0;

    View view;
    ImageView img;
    TextView tvValue;
    boolean isCompleted;
    boolean toggle;
    int child;
    int leftMargin;

    public TreeHolder(Context context, boolean toggle, int child, int leftMargin) {
        super(context);
        this.toggle = toggle;
        this.child = child;
        this.leftMargin = leftMargin;
    }

    @Override
    public View createNodeView(TreeNode node, IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        if (child == DEFAULT) {
            view = inflater.inflate(R.layout.parent, null, false);
        } else {
            view = inflater.inflate(child, null, false);
        }

        if (leftMargin == DEFAULT) {
            leftMargin = 20;
        }
        view.setPadding(leftMargin, 20, 20, 20);

        img = view.findViewById(R.id.image);
        tvValue = view.findViewById(R.id.text);
        isCompleted = value.isCompleted;
        img.setImageResource(value.icon);
        tvValue.setText(value.text);
        if (Boolean.TRUE.equals(isCompleted)) {
            tvValue.setTextColor(context.getResources().getColor(R.color.hofTextColor));
        }
        return view;
    }

    public void toggle(boolean active) {
        if (toggle)
            img.setImageResource(active ? R.drawable.ic_arrow_down : R.drawable.ic_arrow_right);
    }

    public static class IconTreeItem {
        public int icon;
        public String text;
        public boolean isCompleted;

        public IconTreeItem(int icon, String text, boolean isCompleted) {
            this.icon = icon;
            this.text = text;
            this.isCompleted = isCompleted;
        }
    }

    private int getDimens(int resId) {
        return (int) (context.getResources().getDimension(resId) / context.getResources().getDisplayMetrics().density);
    }
}

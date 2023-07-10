package com.argusoft.sewa.android.app.comparator;

import com.argusoft.sewa.android.app.model.MenuBean;

import java.util.Comparator;

public class MenuConstantComparator  implements Comparator<MenuBean> {
    @Override
    public int compare(MenuBean o1, MenuBean o2) {
        return o1.getOrder() - o2.getOrder();
    }
}

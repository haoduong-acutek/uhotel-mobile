package com.uhotel.fragment.listener;

/**
 * Created by kiemhao on 8/31/16.
 */
public interface ToolbarListener {
    void changeNavIcon(int resId);

    void changeNavTitle(String text);

    void openMenu();

    void toHomeFragment();
}

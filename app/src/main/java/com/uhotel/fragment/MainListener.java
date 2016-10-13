package com.uhotel.fragment;

/**
 * Created by kiemhao on 8/28/16.
 */
public interface MainListener {
    void openMenu();

    void onMenuItemSelect(int position);

    void onCloseClick();

    void onLogoClick();

    void toMenuItem(int landingPos);
}

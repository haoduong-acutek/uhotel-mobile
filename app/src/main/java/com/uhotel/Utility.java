package com.uhotel;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Utility {

    public static void showMessage(Context context, String message) {

        if (context == null)
            return;
        if (TextUtils.isEmpty(message))
            return;
        //showDialog(context, message);
        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        showDefaultSnackbar(context,
                message, Snackbar.LENGTH_SHORT);

    }

    public static void showMessage(Context context, int resourceId) {

        if (context == null)
            return;
        if (TextUtils.isEmpty(context.getResources().getString(resourceId)))
            return;
        //showDialog(context, message);
        //Toast.makeText(context, context.getResources().getString(resourceId), Toast.LENGTH_SHORT).show();
        showMessage(context, context.getResources().getString(resourceId));
    }

    public static void showShortMessage(Context context, String message) {
        // MyApplicationContext.log(message);
        if (context == null)
            return;
        if (TextUtils.isEmpty(message))
            return;
        showDefaultSnackbar(context
                , message, Snackbar.LENGTH_SHORT);
        //showDialog(context, message);
        // Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showException(Context context, Exception exp) {
        if (context == null)
            return;
        MyApplicationContext.log(exp);
        //showMessage(context, exp.getMessage());
        showMessage(context, "Unable to retrieve data");

    }

    public static void showOfflineMessage(Context context) {
        //showDialog(context, context.getString(R.string.offline_connect_msg));
        showMessage(context, "Unable to retrieve data");
    }

    public static void setListViewAutoHeight(Context context,
                                             BaseAdapter baseAdapter, ListView listview) {
        Integer lvHeight = 0;
        for (int i = 0; i < baseAdapter.getCount(); i++) {
            View listItem = baseAdapter.getView(i, null, listview);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            lvHeight += listItem.getMeasuredHeight();
        }
        LayoutParams params = listview.getLayoutParams();
        params.height = lvHeight
                + (listview.getDividerHeight() * (baseAdapter.getCount() - 1))
                + 5;
        listview.setLayoutParams(params);
        listview.requestLayout();
    }

    public static void setExpandableListViewAutoHeight(Context context,
                                                       BaseExpandableListAdapter baseAdapter, ExpandableListView listview) {

        Integer lvHeight = 0;
        int childCount = 0;

        for (int i = 0; i < baseAdapter.getGroupCount(); i++) {

            ViewGroup parent = (ViewGroup) baseAdapter.getGroupView(i, true,
                    null, listview);
            if (parent instanceof ViewGroup) {
                parent.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            }
            parent.measure(0, 0);
            lvHeight += parent.getMeasuredHeight();

            for (int y = 0; y < baseAdapter.getChildrenCount(i); y++) {
                View childItem = baseAdapter.getChildView(i, y, true, null,
                        parent);
                if (childItem instanceof ViewGroup) {
                    childItem.setLayoutParams(new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
                }
                childItem.measure(0, 0);

                lvHeight += childItem.getMeasuredHeight();
                childCount++;
            }
        }
        if (baseAdapter.getGroupCount() > 0) {
            LayoutParams params = listview.getLayoutParams();
            params.height = lvHeight
                    + (listview.getDividerHeight() * baseAdapter
                    .getGroupCount())
                    + (listview.getDividerHeight() * childCount);
            listview.setLayoutParams(params);
        }
        if (lvHeight == 0)
            listview.setVisibility(View.GONE);
        else
            listview.setVisibility(View.VISIBLE);
        listview.requestLayout();
    }

    public static void setGridViewAutoHeight(Context context,
                                             BaseAdapter baseAdapter, GridView gridview) {

        View lastChild = gridview.getChildAt(gridview.getChildCount() - 1);
        LayoutParams params = gridview.getLayoutParams();
        params.height = lastChild.getHeight() * 7;
        gridview.setLayoutParams(params);
        gridview.requestLayout();
    }


//
//    public static void toFragmentWithoutBackstack(
//            FragmentActivity fragmentActivity, Fragment sherlockFragment,
//            String tagFragmentNew, int fragmentId, boolean isNext) {
//        try {
//            final FragmentManager fm = fragmentActivity
//                    .getSupportFragmentManager();
//            final FragmentTransaction ft = fm.beginTransaction();
//            Fragment old = fm.findFragmentByTag(tagFragmentNew);
//            if (old != null) {
//
//                ft.remove(old);
//            }
//
//            ft.setCustomAnimations(isNext ? R.anim.slide_in_left
//                    : R.anim.slide_in_right, isNext ? R.anim.slide_out_right
//                    : R.anim.slide_out_left);
//            ft.replace(fragmentId, sherlockFragment, tagFragmentNew);
//            ft.commit();
//            hideKeyboard(fragmentActivity);
//        } catch (Exception exp) {
//            Utility.showException(fragmentActivity, exp);
//        }
//
//    }



    public static void hideKeyboard(final View view,Context context) {
        final InputMethodManager imm = (InputMethodManager) ((Activity)context).getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    public static String formatWebViewContent(String content) {

        content = content.replace("#", "%23").replace("%", "%25")
                .replace("\\", "%27").replace("?", "%3f");
        content = "<html><head>"
                + "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />"
                + "<head><body>" + content
                + "</body>";
        return content;

    }

    public static String getMacAddess() {
        return "00000000ffaa";
//        WifiManager manager = (WifiManager) MyApplicationContext.getInstance().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = manager.getConnectionInfo();
//        Log.d(Utility.class.getName(),info.getMacAddress());
//        return info.getMacAddress() != null ? info.getMacAddress().replace(":","")
//                : "00000000ffaa";

    }

    public static String getTokenKey(String key, String message) {
        String sEncodedString = null;
        try {
            SecretKeySpec secKey = new SecretKeySpec((key).getBytes("UTF-8"),
                    "HmacMD5");
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(secKey);

            byte[] bytes = mac.doFinal(message.getBytes("ASCII"));

            StringBuffer hash = new StringBuffer();

            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            sEncodedString = hash.toString();
        } catch (UnsupportedEncodingException e) {
        } catch (InvalidKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return sEncodedString;
    }

    public static long getFileFolderSize(File dir) {
        long size = 0;
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    size += file.length();
                } else
                    size += getFileFolderSize(file);
            }
        } else if (dir.isFile()) {
            size += dir.length();
        }
        return size;
    }

    public static void showNetworkErrorMessage(Context context) {
        showMessage(context, context.getString(R.string.offline_connect_msg));
    }


    public static float convertDPtoPIXEL(int typeValue, int number) {
        DisplayMetrics metrics = MyApplicationContext.getInstance()
                .getAppContext().getResources().getDisplayMetrics();
        return TypedValue.applyDimension(typeValue, number, metrics);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static String getAppVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static boolean isHigher2x() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB;
    }

    public static int getActionHeight() {
        if (isHigher2x()) {

            final TypedArray styledAttributes = MyApplicationContext
                    .getInstance()
                    .getAppContext()
                    .getTheme()
                    .obtainStyledAttributes(
                            new int[]{android.R.attr.actionBarSize});
            int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
            styledAttributes.recycle();
            return mActionBarSize;
        } else
            return Math.round(Utility.convertDPtoPIXEL(
                    TypedValue.COMPLEX_UNIT_DIP, 48));

    }

    public static String getFormat2Character(int value) {
        if (value < 10)
            return "0" + value;
        else
            return value + "";
    }


    public static void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    public static Point getScreenSize(Context context) {
        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

//    public  static void showSnackbar(View view,String text){
//        final Snackbar snackbar = Snackbar
//                .make(view, text, Snackbar.LENGTH_LONG);
//        snackbar.setAction("Dismiss", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                snackbar.dismiss();
//            }
//        });
//        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
//        snackbar.show();
//    }

//
//    public static Snackbar showIndefiniteSnackbar(Context context) {
//        View view = ((AppCompatActivity) context).getWindow().getDecorView().findViewById(R.id.fragment);
//        final Snackbar snackbar = Snackbar
//                .make(view, "", Snackbar.LENGTH_INDEFINITE);
//
//        ViewGroup snackBarView = (ViewGroup) snackbar.getView();
//        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setVisibility(View.INVISIBLE);
//        View progressView = LayoutInflater.from(context).inflate(R.layout.init_progress_dialog, null);
//        snackBarView.addView(progressView, 0);
//        snackbar.show();
//        return snackbar;
//
//    }

    public static void showDefaultSnackbar(Context context, String text, int length) {
        View view = ((AppCompatActivity) context).getWindow().getDecorView().findViewById(R.id.fragment);
        final Snackbar snackbar = Snackbar
                .make(view, text, length);
        ViewGroup snackBarView = (ViewGroup) snackbar.getView();
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);


        snackBarView.setBackgroundColor(Color.BLACK);
        textView.setBackgroundColor(Color.BLACK);
        textView.setTextColor(Color.WHITE);


        snackbar.show();
    }

    public static void toFragment(Fragment fragment, Fragment target, int frameId) {
        fragment.getFragmentManager()
                .beginTransaction()
                .replace(frameId, target, target.getClass().getName()).commitNow();

    }

    public static void toChildFragment(Fragment fragment, Fragment target, int frameId) {
        fragment.getChildFragmentManager()
                .beginTransaction()
                .replace(frameId, target, target.getClass().getName()).commitNow();

    }

    public static String durationInSecondsToString(long miliSec) {
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(miliSec),
                TimeUnit.MILLISECONDS.toMinutes(miliSec) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(miliSec)),
                TimeUnit.MILLISECONDS.toSeconds(miliSec) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(miliSec)));

        return hms;
    }

    public static boolean isNetworkOn(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

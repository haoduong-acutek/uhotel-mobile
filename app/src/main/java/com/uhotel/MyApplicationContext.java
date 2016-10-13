package com.uhotel;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.appender.FileAppender;
import com.google.code.microlog4android.config.PropertyConfigurator;
import com.uhotel.config.Config;
import com.uhotel.handler.MyUncaughtExceptionHandler;
import com.uhotel.helper.DateHelper;

import java.io.File;
import java.io.IOException;


public class MyApplicationContext extends Application {
    private Context context;
    private static Logger logger = LoggerFactory.getLogger();

    private static MyApplicationContext instance;

    public static final String TAG = MyApplicationContext.class.getSimpleName();


    public MyApplicationContext() {
        // TODO Auto-generated constructor stub
        super();

        // this.indexVersion=indexVersion;

    }

    public static synchronized MyApplicationContext getInstance() {
        // TODO Auto-generated method stub
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = this;
        init();
        // hashmap = new HashMap<String, Object>();
    }

    // public Object getFromHashmap(String key) {
    // return hashmap.get(key);
    // }
    //
    // public void setToHashmap(String key, Object value) {
    // this.hashmap.put(key, value);
    // }

    void init() {
        // init image loader

        // init logger
        File dir = new File(Config.APP_DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // initDB();
        initLogger();
        // init sqlite
        // Vitamio.initialize(this, context.getResources().getIdentifier("libarm", "raw", getPackageName()));

    }


    private static void createLogFileIfNeed() {

        File file = new File(Environment.getExternalStorageDirectory() + "/"
                + Config.LOG_NAME + "/" + Config.LOGFILE_NAME);
        try {
            if (!file.exists())
                file.createNewFile();
            else if (file.length() / 1024 > Config.MAX_LOGFILE_KB) {
                file.delete();
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void initLogger() {
        new File(Environment.getExternalStorageDirectory() + "/"
                + Config.LOG_NAME).mkdir();
        createLogFileIfNeed();
        PropertyConfigurator.getConfigurator(this).configure();
        FileAppender appender = new FileAppender();
        appender.setFileName(Config.LOG_NAME + "/" + Config.LOGFILE_NAME);
        appender.setAppend(true);
        logger.addAppender(appender);
    }

    public Context getAppContext() {
        return context;
    }

    public static void log(String msg) {
        // createLogFileIfNeed();

        logger.debug(DateHelper.getcurrentDateString() + "-----" + msg);
    }

    public static void log(Exception exp) {
        logger.debug(DateHelper.getcurrentDateString()
                + Log.getStackTraceString(exp));
    }


    // volley


    public static void setEnableUncatchException(Context context) {
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(
                context));

    }


}

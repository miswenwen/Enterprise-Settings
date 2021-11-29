package com.honeywell.ezservicetest;

import android.app.Application;

public class App extends Application {
    public static String SCAN_RESULT_ACTION = "com.honeywell.action.CUSTOMIZED_SCAN_RESULT";
    public static String SCAN_KEY_CODEID = "codeId";
    public static String SCAN_KEY_DATABYTES = "dataBytes";
    public static String SCAN_KEY_DATA = "data";
    public static String SCAN_KEY_TIMESTAMP = "timestamp";
    public static String SCAN_KEY_AIMID = "aimId";
    public static String SCAN_KEY_VERSION = "version";
    public static String SCAN_KEY_CHARSET = "charset";
    public static String SCAN_KEY_SCANNER = "scanner";

    public static String SCAN_BUTTION_KEY_DOWN_ACTION = "com.honeywell.intent.action.SCAN_BUTTON_DOWN";
    public static String SCAN_BUTTION_KEY_UP_ACTION = "com.honeywell.intent.action.SCAN_BUTTON_UP";
    @Override
    public void onCreate() {
        super.onCreate();
    }
}

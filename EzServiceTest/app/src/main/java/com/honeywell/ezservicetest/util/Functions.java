package com.honeywell.ezservicetest.util;

import android.os.Build;

public class Functions {
    //general settings
    public static final boolean REBOOT_DEVICE = true;
    public static final boolean POWER_OFF_DEVICE = true;
    public static final boolean GET_SERIAL_NUMBER = true;
    public static final boolean SET_SYSTEM_TIME = true;
    public static final boolean SET_TIME_ZONE = true;
    public static final boolean GET_BATTERY_IFNO = true;
    public static final boolean SET_STATUS_BAR = true;
    public static final boolean ENABLE_HOME = true;
    public static final boolean ENABLE_APP_SWITCH = true;
    public static final boolean ENABLE_BACK = true;
    public static final boolean SET_NAVIGATION_BAR = true;
    public static final boolean SET_WIFI_RSSI = true;
    public static final boolean SET_BATTERY_LEVEL = true;
    public static final boolean ENABLE_USB_DEBUG_MODE = true;
    public static final boolean RESTRICT_CELLULAR_DATA = true;
    public static final boolean RESTRICT_WIFI = true;
    public static final boolean RESTRICT_BLUETOOTH = true;
    public static final boolean RESTRICT_USB_MTP = true;
    public static final boolean REPLACE_BOOT_ANIMATION = true;
    public static final boolean ENABLE_CUSTOM_BOOTANIMATION = true;

    public static final boolean NEVER_SLEEP = true;
    public static final boolean NOSIP = true;
    public static final boolean DEFAULT_USB_MTP = true;
    public static final boolean MEDIA_SCAN = true;
    public static final boolean ENABLE_PORTAL_DETECTION = true;
    public static final boolean RUNTIME_PERMISSIONS = true;
    public static final boolean DEFAULT_LAUNCHER = true;
    public static final boolean FIRMWARE_UPDATE = false;

    public static final boolean SILENT_INSTALL = true;
    public static final boolean SILENT_UNINSTALL = true;
    public static final boolean WHITELIST_ENABLE = false;
    public static final boolean WHITELIST = true;
    public static final boolean BLACKLIST_ENABLE = false;
    public static final boolean BLACKLIST = true;
    public static final boolean WHITE_BLACK_LIST_DISABLE = true;
    //scan settings
    public static final boolean SCAN_RESULT_ACTION = true;
    public static final boolean SCAN_RESULT_KEY = true;
    public static final boolean SCAN_BTN_KEYDOWN = true;
    public static final boolean SCAN_BTN_KEYUP = true;
    public static final boolean SCAN_SETTINGS_TEST = true;
    public static String getModel(){
        return Build.MODEL;
    }
}

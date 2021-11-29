package com.honeywell.ezservicetest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.honeywell.ezservice.EzServiceManager;
import com.honeywell.ezservice.FunctionCode;
import com.honeywell.ezservicetest.general.RuntimePermissionsSetActivity;
import com.honeywell.ezservicetest.util.FileUtil;
import com.honeywell.ezservicetest.util.Functions;
import com.honeywell.ezservicetest.util.LogUtils;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public  class GeneralPreferenceFragment extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener,
        Preference.OnPreferenceClickListener {
    private static final String KEY_REBOOT_DEVICE = "reboot_device";
    private static final String KEY_POWER_OFF_DEVICE = "power_off_device";
    private static final String KEY_GET_SERIAL_NUMBER = "serial_number";
    private static final String KEY_SET_SYSTEM_TIME = "set_sys_time";
    private static final String KEY_SET_TIME_ZONE = "set_time_zone";
    private static final String KEY_GET_BATTERY_INFO = "battery_info";
    private static final String KEY_SET_STATUS_BAR = "set_status_bar";
    private static final String KEY_ENABLE_HOME = "enable_home";
    private static final String KEY_ENABLE_APP_SWITCH = "enable_app_switch";
    private static final String KEY_ENABLE_BACK = "back_switch";
    private static final String KEY_SET_NAVIGATION_BAR = "navigationbar_switch";
    private static final String KEY_SET_WIFI_RSSI = "wifi_rssi";
    private static final String KEY_SET_BATTERY_LEVEL = "battery_level";
    private static final String KEY_ENABLE_USB_DEBUG_MODE = "enable_usb_debug_mode";
    private static final String KEY_RESTRICT_CELLULAR_DATA = "restrict_cellular_data";
    private static final String KEY_RESTRICT_WIFI = "restrict_wifi";
    private static final String KEY_RESTRICT_BLUETOOTH = "restrict_bluetooth";
    private static final String KEY_RESTRICT_USB_MTP = "restrict_usb_mtp";
    private static final String KEY_REPLACE_BOOT_ANIMATION = "replace_boot_animation";
    private static final String KEY_ENABLE_CUSTOM_BOOTANIMATION = "enable_custom_boot_animtion";

    private static final String KEY_NEVER_SLEEP = "enable_never_sleep";
    private static final String KEY_NOSIP = "enable_nosip";
    private static final String KEY_RUNTIME_PERMISSIONS = "runtime_permissions_set_key";
    private static final String KEY_DEFAULT_LAUNCHER = "default_launcher_set_key";
    private static final String KEY_DEFAULT_USB_MTP = "enable_usb_mtp";
    private static final String KEY_MEDIA_SCAN = "enable_media_scan";
    private static final String KEY_PORTAL_DETECTION = "enable_portal_detection";
    private static final String KEY_FIRMWARE_UPDATE = "firmware_update";

    private static final String KEY_SILENT_INSTALL = "silent_install";
    private static final String KEY_SILENT_UNINSTALL = "silent_uninstall";
    private static final String KEY_WHITELIST_ENABLE = "whitelist_enable";
    private static final String KEY_WHITELIST = "whitelist";
    private static final String KEY_BLACKLIST_ENABLE = "blacklist_enable";
    private static final String KEY_BLACKLIST = "blacklist";
    private static final String KEY_WHITE_BLACK_LIST_DISABLE = "while_black_list_disable";

    private Preference mRebootDevicePreference;
    private Preference mPowerOffDevicePreference;
    private Preference mGetSerialNumberPreference;
    private Preference mSetSystemTimePreference;
    private Preference mSetTimeZonePreference;
    private Preference mGetBatteryInfoPreference;
    private SwitchPreference mSetStatusBarPreference;
    private SwitchPreference mEnbleHomePreference;
    private SwitchPreference mEnbleAppSwitchPreference;
    private SwitchPreference mEnbleBackPreference;
    private SwitchPreference mSetNavigationBarPreference;
    private SwitchPreference mSetWifiRssiPreference;
    private SwitchPreference mSetBatteryLevelPreference;
    private SwitchPreference mEnableUsbDebugModePreference;
    private SwitchPreference mRestrictCellularDataPreference;
    private SwitchPreference mRestrictWifiPreference;
    private SwitchPreference mRestrictBluetoothPreference;
    private SwitchPreference mRestrictUsbMtpPreference;
    private SwitchPreference mEnableNeverSleepPreference;
    private SwitchPreference mEnableNoSipPreference;
    private SwitchPreference mEnableDefaultUsbMtpPreference;
    private SwitchPreference mEnableMediaScanPreference;
    private SwitchPreference mEnablePortalDetectionPreference;
    private Preference mReplaceBootAnimationPreference;
    private SwitchPreference mEnableCustomBootAnimationPreference;
    private Preference mRuntimePermissionsPreference;
    private ListPreference mDefaultLauncherPreference;
    private Preference mFirewareUpdatePreference;

    private Preference mSilentInstallPreference;
    private EditTextPreference mSilentUninstallPreference;
    private SwitchPreference mWhiteListEnablePreference;
    private Preference mWhitelistPreference;
    private SwitchPreference mBlacklistEnablePreference;
    private Preference mBlacklistPreference;
    private Preference mWhiteBlackListDisablePreference;
    public final static String SETTINGS_LAUNCHER_CLS="ezservice_launcher_class";

    private int mMinus = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        initPreference();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.honeywell.ezservice.SERIAL_NUMBER");
        filter.addAction("com.honeywell.ezservice.BATTERY_INFO");
        getActivity().registerReceiver(getSnBroadcast, filter, null, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(((EzServiceTestActivity)getActivity()).hasBindedService()) {
            updateState();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getSnBroadcast != null) {
            try {
                getActivity().unregisterReceiver(getSnBroadcast);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void removePreference(String key) {
        Preference pref = findPreference(key);
        if (pref != null) {
            getPreferenceScreen().removePreference(pref);
        }
    }
    private void initPreference() {
        if(Functions.REBOOT_DEVICE){
            mRebootDevicePreference = findPreference(KEY_REBOOT_DEVICE);
            mRebootDevicePreference.setOnPreferenceClickListener(this);
        }else {
            removePreference(KEY_REBOOT_DEVICE);
        }

        if(Functions.POWER_OFF_DEVICE){
            mPowerOffDevicePreference = findPreference(KEY_POWER_OFF_DEVICE);
            mPowerOffDevicePreference.setOnPreferenceClickListener(this);
        }else {
            removePreference(KEY_POWER_OFF_DEVICE);
        }

        if(Functions.GET_SERIAL_NUMBER){
            mGetSerialNumberPreference = findPreference(KEY_GET_SERIAL_NUMBER);
            mGetSerialNumberPreference.setOnPreferenceClickListener(this);
        }else {
            removePreference(KEY_GET_SERIAL_NUMBER);
        }

        if(Functions.SET_SYSTEM_TIME){
            mSetSystemTimePreference = findPreference(KEY_SET_SYSTEM_TIME);
            mSetSystemTimePreference.setOnPreferenceClickListener(this);
        }else {
            removePreference(KEY_SET_SYSTEM_TIME);
        }

        if(Functions.SET_TIME_ZONE){
            mSetTimeZonePreference = findPreference(KEY_SET_TIME_ZONE);
            mSetTimeZonePreference.setOnPreferenceClickListener(this);
        }else {
            removePreference(KEY_SET_TIME_ZONE);
        }

        if(Functions.GET_BATTERY_IFNO){
            mGetBatteryInfoPreference = findPreference(KEY_GET_BATTERY_INFO);
            mGetBatteryInfoPreference.setOnPreferenceClickListener(this);
        }else {
            removePreference(KEY_GET_BATTERY_INFO);
        }

        if(Functions.SET_STATUS_BAR){
            mSetStatusBarPreference = (SwitchPreference) findPreference(KEY_SET_STATUS_BAR);
            mSetStatusBarPreference.setOnPreferenceChangeListener(this);
        }else {
            removePreference(KEY_SET_STATUS_BAR);
        }

        if(Functions.ENABLE_HOME){
            mEnbleHomePreference = (SwitchPreference) findPreference(KEY_ENABLE_HOME);
            mEnbleHomePreference.setOnPreferenceChangeListener(this);
        }else {
            removePreference(KEY_ENABLE_HOME);
        }

        if(Functions.ENABLE_APP_SWITCH){
            mEnbleAppSwitchPreference = (SwitchPreference) findPreference(KEY_ENABLE_APP_SWITCH);
            mEnbleAppSwitchPreference.setOnPreferenceChangeListener(this);
        }else {
            removePreference(KEY_ENABLE_APP_SWITCH);
        }

        if(Functions.ENABLE_BACK){
            mEnbleBackPreference = (SwitchPreference) findPreference(KEY_ENABLE_BACK);
            mEnbleBackPreference.setOnPreferenceChangeListener(this);
        }else {
            removePreference(KEY_ENABLE_BACK);
        }
        if (Functions.SET_NAVIGATION_BAR && Functions.getModel().equals("EDA61K")) {
            mSetNavigationBarPreference = (SwitchPreference) findPreference(KEY_SET_NAVIGATION_BAR);
            mSetNavigationBarPreference.setOnPreferenceChangeListener(this);
        } else {
            removePreference(KEY_SET_NAVIGATION_BAR);
        }
        if (Functions.SET_WIFI_RSSI) {
            mSetWifiRssiPreference = (SwitchPreference) findPreference(KEY_SET_WIFI_RSSI);
            mSetWifiRssiPreference.setOnPreferenceChangeListener(this);
        } else {
            removePreference(KEY_SET_WIFI_RSSI);
        }
        if (Functions.SET_BATTERY_LEVEL) {
            mSetBatteryLevelPreference = (SwitchPreference) findPreference(KEY_SET_BATTERY_LEVEL);
            mSetBatteryLevelPreference.setOnPreferenceChangeListener(this);
        } else {
            removePreference(KEY_SET_BATTERY_LEVEL);
        }
        if(Functions.ENABLE_USB_DEBUG_MODE){
            mEnableUsbDebugModePreference=(SwitchPreference)findPreference(KEY_ENABLE_USB_DEBUG_MODE);
            mEnableUsbDebugModePreference.setOnPreferenceChangeListener(this);
        }else {
            removePreference(KEY_ENABLE_USB_DEBUG_MODE);
        }

        if(Functions.RESTRICT_CELLULAR_DATA){
            mRestrictCellularDataPreference = (SwitchPreference) findPreference(KEY_RESTRICT_CELLULAR_DATA);
            mRestrictCellularDataPreference.setOnPreferenceChangeListener(this);
        }else {
            removePreference(KEY_RESTRICT_CELLULAR_DATA);
        }

        if(Functions.RESTRICT_WIFI){
            mRestrictWifiPreference=(SwitchPreference)findPreference(KEY_RESTRICT_WIFI);
            mRestrictWifiPreference.setOnPreferenceChangeListener(this);
        }else {
            removePreference(KEY_RESTRICT_WIFI);
        }
        if(Functions.RESTRICT_BLUETOOTH){
            mRestrictBluetoothPreference=(SwitchPreference)findPreference(KEY_RESTRICT_BLUETOOTH);
            mRestrictBluetoothPreference.setOnPreferenceChangeListener(this);
        }else {
            removePreference(KEY_RESTRICT_BLUETOOTH);
        }
        if(Functions.RESTRICT_USB_MTP){
            mRestrictUsbMtpPreference=(SwitchPreference)findPreference(KEY_RESTRICT_USB_MTP);
            mRestrictUsbMtpPreference.setOnPreferenceChangeListener(this);
        }else {
            removePreference(KEY_RESTRICT_USB_MTP);
        }
        if(Functions.REPLACE_BOOT_ANIMATION){
            mReplaceBootAnimationPreference = findPreference(KEY_REPLACE_BOOT_ANIMATION);
            mReplaceBootAnimationPreference.setOnPreferenceClickListener(this);
        }else {
            removePreference(KEY_REPLACE_BOOT_ANIMATION);
        }
        if(Functions.ENABLE_CUSTOM_BOOTANIMATION){
            mEnableCustomBootAnimationPreference = (SwitchPreference) findPreference(KEY_ENABLE_CUSTOM_BOOTANIMATION);
            mEnableCustomBootAnimationPreference.setOnPreferenceChangeListener(this);
        }else {
            removePreference(KEY_ENABLE_CUSTOM_BOOTANIMATION);
        }
        if(Functions.NEVER_SLEEP){
            mEnableNeverSleepPreference = (SwitchPreference) findPreference(KEY_NEVER_SLEEP);
            mEnableNeverSleepPreference.setOnPreferenceChangeListener(this);
        }else {
            removePreference(KEY_NEVER_SLEEP);
        }
        if(Functions.NOSIP){
            mEnableNoSipPreference = (SwitchPreference) findPreference(KEY_NOSIP);
            mEnableNoSipPreference.setOnPreferenceChangeListener(this);
        }else {
            removePreference(KEY_NOSIP);
        }
        if(Functions.DEFAULT_USB_MTP){
            mEnableDefaultUsbMtpPreference = (SwitchPreference) findPreference(KEY_DEFAULT_USB_MTP);
            mEnableDefaultUsbMtpPreference.setOnPreferenceChangeListener(this);
        }else {
            removePreference(KEY_DEFAULT_USB_MTP);
        }
        if(Functions.MEDIA_SCAN){
            mEnableMediaScanPreference = (SwitchPreference) findPreference(KEY_MEDIA_SCAN);
            mEnableMediaScanPreference.setOnPreferenceChangeListener(this);
        }else {
            removePreference(KEY_MEDIA_SCAN);
        }
        if(Functions.ENABLE_PORTAL_DETECTION){
            mEnablePortalDetectionPreference = (SwitchPreference) findPreference(KEY_PORTAL_DETECTION);
            mEnablePortalDetectionPreference.setOnPreferenceChangeListener(this);
        }else {
            removePreference(KEY_PORTAL_DETECTION);
        }
        if(Functions.RUNTIME_PERMISSIONS){
            mRuntimePermissionsPreference = findPreference(KEY_RUNTIME_PERMISSIONS);
            mRuntimePermissionsPreference.setOnPreferenceClickListener(this);
        }else {
            removePreference(KEY_RUNTIME_PERMISSIONS);
        }

        if(Functions.DEFAULT_LAUNCHER){
            initLauncherPreference();
        }else {
            removePreference(KEY_DEFAULT_LAUNCHER);
        }
        if(Functions.FIRMWARE_UPDATE){
            mFirewareUpdatePreference = findPreference(KEY_FIRMWARE_UPDATE);
            mFirewareUpdatePreference.setOnPreferenceClickListener(this);
        }else {
            removePreference(KEY_FIRMWARE_UPDATE);
        }

        if (Functions.SILENT_INSTALL){
            mSilentInstallPreference = findPreference(KEY_SILENT_INSTALL);
            mSilentInstallPreference.setOnPreferenceClickListener(this);
        }else {
            removePreference(KEY_SILENT_INSTALL);
        }

        if(Functions.SILENT_UNINSTALL){
            mSilentUninstallPreference = (EditTextPreference)findPreference(KEY_SILENT_UNINSTALL);
            mSilentUninstallPreference.setOnPreferenceChangeListener(this);
        }else {
            removePreference(KEY_SILENT_UNINSTALL);
        }

        if(Functions.WHITELIST_ENABLE){
            mWhiteListEnablePreference = (SwitchPreference) findPreference(KEY_WHITELIST_ENABLE);
            mWhiteListEnablePreference.setOnPreferenceChangeListener(this);
        }else {
            removePreference(KEY_WHITELIST_ENABLE);
        }

        if(Functions.WHITELIST){
            mWhitelistPreference = findPreference(KEY_WHITELIST);
            mWhitelistPreference.setOnPreferenceClickListener(this);
        }else {
            removePreference(KEY_WHITELIST);
        }

        if(Functions.BLACKLIST_ENABLE){
            mBlacklistEnablePreference = (SwitchPreference) findPreference(KEY_BLACKLIST_ENABLE);
            mBlacklistEnablePreference.setOnPreferenceChangeListener(this);
        }else {
            removePreference(KEY_BLACKLIST_ENABLE);
        }

        if(Functions.BLACKLIST){
            mBlacklistPreference = findPreference(KEY_BLACKLIST);
            mBlacklistPreference.setOnPreferenceClickListener(this);
        }else {
            removePreference(KEY_BLACKLIST);
        }

        if(Functions.WHITE_BLACK_LIST_DISABLE){
            mWhiteBlackListDisablePreference = findPreference(KEY_WHITE_BLACK_LIST_DISABLE);
            mWhiteBlackListDisablePreference.setOnPreferenceClickListener(this);
        }else{
            removePreference(KEY_WHITE_BLACK_LIST_DISABLE);
        }

    }
    private List<ResolveInfo> getHomeResolveInfos(){
        Intent mainIntent = new Intent(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_HOME);
        //mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = getActivity().getPackageManager().queryIntentActivities(mainIntent, 0);

//            Intent secIntent = new Intent(Intent.ACTION_MAIN);
//            secIntent.addCategory(Intent.CATEGORY_HOME);
//            secIntent.addCategory("android.intent.category.LAUNCHER_APP");
//            List<ResolveInfo> resolveInfosSec = getActivity().getPackageManager().queryIntentActivities(secIntent, 0);
//            resolveInfos.addAll(resolveInfosSec);

        Iterator<ResolveInfo> it=resolveInfos.iterator();
        while (it.hasNext()){
            if(it.next().activityInfo.name.equals("com.android.settings.FallbackHome")){
                it.remove();
            }
        }
        return resolveInfos;
    }
    private void initLauncherPreference(){
        mDefaultLauncherPreference = (ListPreference)findPreference(KEY_DEFAULT_LAUNCHER);
        List<ResolveInfo> resolveInfos = getHomeResolveInfos();
        String[] entries=new String[resolveInfos.size()+1];
        String[] values=new String[resolveInfos.size()+1];
        for (int i = 0; i < resolveInfos.size(); i++) {
            ResolveInfo r = resolveInfos.get(i);
            entries[i]=r.activityInfo.name;
            values[i]=r.activityInfo.packageName+"|"+r.activityInfo.name;
        }
        entries[resolveInfos.size()]="NULL";
        values[resolveInfos.size()]="NULL|NULL";
        mDefaultLauncherPreference.setEntries(entries);
        mDefaultLauncherPreference.setEntryValues(values);
        String selectLaucnherCLS = Settings.Global.getString(getActivity().getContentResolver(), SETTINGS_LAUNCHER_CLS);
        if (selectLaucnherCLS == null){
            mDefaultLauncherPreference.setValueIndex(entries.length - 1);
        }else {
            for (int i = 0; i < entries.length; i++) {
                if (selectLaucnherCLS.equals(entries[i])) {
                    mDefaultLauncherPreference.setValueIndex(i);
                    break;
                }
            }
        }
        mDefaultLauncherPreference.setOnPreferenceChangeListener(this);
    }
    public void updateState(){
        if(mRebootDevicePreference!=null){

        }
        if(mPowerOffDevicePreference!=null){

        }
        if(mGetSerialNumberPreference!=null){
            mGetSerialNumberPreference.setSummary(EzServiceManager.getSerialNumber());
        }
        if(mSetSystemTimePreference!=null){

        }
        if(mSetStatusBarPreference!=null){
            mSetStatusBarPreference.setChecked(EzServiceManager.isEnable(FunctionCode.STATUS_BAR));
        }
        if(mEnbleHomePreference!=null){
            mEnbleHomePreference.setChecked(EzServiceManager.isEnable(FunctionCode.HOME_KEY));
        }
        if(mEnbleAppSwitchPreference!=null){
            mEnbleAppSwitchPreference.setChecked(EzServiceManager.isEnable(FunctionCode.APP_SWITCH_KEY));
        }
        if(mEnbleBackPreference!=null){
            mEnbleBackPreference.setChecked(EzServiceManager.isEnable(FunctionCode.BACK_KEY));
        }
        if (mSetNavigationBarPreference != null) {
            mSetNavigationBarPreference.setChecked(EzServiceManager.isEnable(FunctionCode.NAVIGATION_BAR));
        }
        if (mSetWifiRssiPreference != null) {
            mSetWifiRssiPreference.setChecked(EzServiceManager.isEnable(FunctionCode.WIFI_RSSI));
        }
        if (mSetBatteryLevelPreference != null) {
            mSetBatteryLevelPreference.setChecked(EzServiceManager.isEnable(FunctionCode.BATTERY_LEVEL));
        }
        if(mEnableUsbDebugModePreference!=null){
            mEnableUsbDebugModePreference.setChecked(EzServiceManager.isEnable(FunctionCode.USB_DEBUG_MODE));
        }
        if(mReplaceBootAnimationPreference!=null){
            mReplaceBootAnimationPreference.setSummary("Make sure file location is /storage/emulated/0/bootanimation.zip!");
        }
        if(mEnableCustomBootAnimationPreference!=null){
            mEnableCustomBootAnimationPreference.setChecked(EzServiceManager.isEnable(FunctionCode.CUSTOM_BOOTANIMATION));
        }
        if(mRestrictCellularDataPreference!=null){
            mRestrictCellularDataPreference.setChecked(EzServiceManager.isRestriction(FunctionCode.CELLULAR_DATA));
        }
        if(mRestrictWifiPreference!=null){
            mRestrictWifiPreference.setChecked(EzServiceManager.isRestriction(FunctionCode.WIFI));
        }
        if(mRestrictBluetoothPreference!=null){
            mRestrictBluetoothPreference.setChecked(EzServiceManager.isRestriction(FunctionCode.BLUETOOTH));
        }
        if(mRestrictUsbMtpPreference!=null){
            mRestrictUsbMtpPreference.setChecked(EzServiceManager.isRestriction(FunctionCode.USB_MTP));
        }
        if(mEnableNeverSleepPreference!=null){
            mEnableNeverSleepPreference.setChecked(EzServiceManager.isEnable(FunctionCode.NEVER_SLEEP));
        }
        if(mEnableNoSipPreference!=null){
            mEnableNoSipPreference.setChecked(EzServiceManager.isEnable(FunctionCode.NOSIP));
        }
        if(mEnableDefaultUsbMtpPreference!=null){
            mEnableDefaultUsbMtpPreference.setChecked(EzServiceManager.isEnable(FunctionCode.USB_MTP));
        }
        if(mEnableMediaScanPreference!=null){
            mEnableMediaScanPreference.setChecked(EzServiceManager.isEnable(FunctionCode.MEDIA_SCAN));
        }
        if(mEnablePortalDetectionPreference!=null){
            mEnablePortalDetectionPreference.setChecked(EzServiceManager.isEnable(FunctionCode.PORTAL_DETECTION));
        }
        if(mRuntimePermissionsPreference!=null){

        }
        if(mDefaultLauncherPreference!=null){
        }
        if(mFirewareUpdatePreference!=null){

        }
    }
    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        Intent intent;
        if(preference==mSetStatusBarPreference){
            boolean state = (Boolean) objValue;
            //传入boolean
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("setStatusbar",state);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mEnbleHomePreference){
            boolean state = (Boolean) objValue;
            //传入boolean
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("setHomeKey",state);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mEnbleAppSwitchPreference){
            boolean state = (Boolean) objValue;
            //传入boolean
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("setAppSwitchKey",state);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mEnbleBackPreference){
            boolean state = (Boolean) objValue;
            //传入boolean
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("setBackKey",state);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mSetNavigationBarPreference){
            boolean state = (Boolean) objValue;
            //传入boolean
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("setVirtualNavigationbar",state);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mSetWifiRssiPreference){
            boolean state = (Boolean) objValue;
            //传入boolean
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("setWifiRssi",state);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mSetBatteryLevelPreference){
            boolean state = (Boolean) objValue;
            //传入boolean
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("setBatteryLevel",state);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mEnableUsbDebugModePreference){
            boolean state = (Boolean) objValue;
            //传入boolean
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("setUsbDebug",state);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mRestrictCellularDataPreference){
            boolean state = (Boolean) objValue;
            //传入boolean
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("restrictCellularData",state);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mRestrictWifiPreference){
            boolean state = (Boolean) objValue;
            //传入boolean
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("restrictWifi",state);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mRestrictBluetoothPreference){
            boolean state = (Boolean) objValue;
            //传入boolean
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("restrictBluetooth",state);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mRestrictUsbMtpPreference){
            boolean state = (Boolean) objValue;
            //传入boolean
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("restrictUsbMtp",state);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mEnableCustomBootAnimationPreference){
            boolean state = (Boolean) objValue;
            //传入boolean
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("setCustomBootAnim",state);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mEnableNeverSleepPreference){
            boolean state = (Boolean) objValue;
            //传入boolean
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("setNeverSleep",state);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mEnableNoSipPreference){
            boolean state = (Boolean) objValue;
            //传入boolean
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("setNoSip",state);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mEnableDefaultUsbMtpPreference){
            boolean state = (Boolean) objValue;
            //传入boolean
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("setDefaultUsbMtp",state);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mEnableMediaScanPreference){
            boolean state = (Boolean) objValue;
            //传入boolean
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("setMediaScan",state);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mEnablePortalDetectionPreference){
            boolean state = (Boolean) objValue;
            //传入boolean
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("setPortalDetection",state);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mDefaultLauncherPreference){
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("setChangeDefaultLauncher",(String)objValue);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mSilentUninstallPreference){
            String val = (String) objValue;
            //传入String[]
            String [] silentUninstallPkgs = val.split("\\|");
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("setSilentUnnstallApks",silentUninstallPkgs);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mWhiteListEnablePreference) {
            boolean state = (Boolean) objValue;
            //传入boolean
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("setWhiteList_enable",state);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mBlacklistEnablePreference) {
            boolean state = (Boolean) objValue;
            //传入boolean
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("setBlackList_enable",state);
            getActivity().sendBroadcast(intent);
        }
        return true;
    }
    private BroadcastReceiver getSnBroadcast=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.honeywell.ezservice.SERIAL_NUMBER")){
                String sn=intent.getStringExtra("sn");
                Toast.makeText(getActivity(),sn,Toast.LENGTH_SHORT).show();
            }else if(intent.getAction().equals("com.honeywell.ezservice.BATTERY_INFO")){
                String battery_sn=intent.getStringExtra("battery_sn");
                String battery_cycle_count=intent.getStringExtra("battery_cycle_count");
                Toast.makeText(getActivity(),"Battery SN:" + battery_sn + "     Battery Cycle Count:" + battery_cycle_count,Toast.LENGTH_SHORT).show();
            }

        }
    };
    @Override
    public boolean onPreferenceClick(Preference preference) {
        Intent intent;
        if (preference == mRebootDevicePreference) {
            intent = new Intent("com.honeywell.ezservice.REBOOT_DEVICE");
            getActivity().sendBroadcast(intent);
        }
        if (preference == mPowerOffDevicePreference) {
            intent = new Intent("com.honeywell.ezservice.POWEROFF_DEVICE");
            getActivity().sendBroadcast(intent);
        }
        if (preference == mGetSerialNumberPreference) {
            intent = new Intent("com.honeywell.ezservice.GET_SERIAL_NUMBER");
            getActivity().sendBroadcast(intent);
        }
        if(preference==mSetSystemTimePreference){
            //传入String
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            mMinus = mMinus + 5;
            if(mMinus > 60){
                mMinus = 0;
            }
            String time = "2020-10-22 17:" + String.valueOf(mMinus) + ":22";
            intent.putExtra("setSysTime",time);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mSetTimeZonePreference){
            //传入String
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            String timeZone = "America/Tijuana";
            intent.putExtra("setTimeZone",timeZone);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mGetBatteryInfoPreference){
            intent = new Intent("com.honeywell.ezservice.GET_BATTERY_INFO");
            getActivity().sendBroadcast(intent);
        }
        if(preference==mReplaceBootAnimationPreference){
            //传入String
            String location = "/storage/emulated/0/bootanimation.zip";
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("setReplaceBootAnim",location);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mRuntimePermissionsPreference){
            intent=new Intent(getActivity(),RuntimePermissionsSetActivity.class);
            startActivity(intent);
        }
        if(preference==mFirewareUpdatePreference){

        }
        if(preference==mSilentInstallPreference){
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");//无类型限制
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 1);
        }
        if(preference==mWhitelistPreference){
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            boolean whiteState=true;
            String[] whitePkgs={"com.honeywell.ezreceiver","com.honeywell.ezservicetest"};
            intent.putExtra("setWhiteList_enable",whiteState);
            intent.putExtra("setWhiteList_pkgs",whitePkgs);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mBlacklistPreference){
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            boolean blackState=true;
            String[] blackPkgs={"com.android.contacts","org.codeaurora.snapcam","com.android.music"};
            intent.putExtra("setBlackList_enable",blackState);
            intent.putExtra("setBlackList_pkgs",blackPkgs);
            getActivity().sendBroadcast(intent);
        }
        if(preference==mWhiteBlackListDisablePreference) {
            intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
            intent.putExtra("setWhiteBlacklistFuncDisable",-1);
            getActivity().sendBroadcast(intent);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path;
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            path=new FileUtil().getAbsolutePath(getActivity(),uri);
            if(path!=null) {
                String val = (String) path;
                LogUtils.e("mSilentInstallPreference val:"+val);
                //传入String[]
                String [] silentInstallPkgs = val.split("\\|");;
                Intent intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
                intent.putExtra("setSilentInstallApks",silentInstallPkgs);
                getActivity().sendBroadcast(intent);
            }
        }
    }
}

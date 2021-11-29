package com.honeywell.ezservicetest;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.honeywell.ezservicetest.scan.ScanResultKeySetActivity;
import com.honeywell.ezservicetest.scan.ScanSettingsTestActivity;
import com.honeywell.ezservicetest.util.Functions;
import com.honeywell.ezservice.EzServiceManager;
import com.honeywell.ezservice.FunctionCode;

public class ScanPreferenceFragment extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener,
        Preference.OnPreferenceClickListener {
    private static final String KEY_SCAN_RESULT_ACTION = "scan_result_action";
    private static final String KEY_SCAN_RESULT_KEY = "scan_result_key";
    private static final String KEY_SCAN_BTN_KEYDOWN = "scan_btn_keydown";
    private static final String KEY_SCAN_BTN_KEYUP = "scan_btn_keyup";
    private static final String KEY_SCAN_SETTINGS_TEST = "scan_settings_test";

    private EditTextPreference mScanResultActionPreference;
    private Preference mScanResultKeyPreference;
    private SwitchPreference mScanBtnKeyDownPreference;
    private SwitchPreference mScanBtnKeyUpPreference;
    private Preference mScanSettingsTestPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_scan);
        initPreference();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (((EzServiceTestActivity) getActivity()).hasBindedService()) {
            updateState();
        }
    }

    protected void removePreference(String key) {
        Preference pref = findPreference(key);
        if (pref != null) {
            getPreferenceScreen().removePreference(pref);
        }
    }

    private void initPreference() {
        if (Functions.SCAN_RESULT_ACTION) {
            mScanResultActionPreference = (EditTextPreference) findPreference(KEY_SCAN_RESULT_ACTION);
            mScanResultActionPreference.setOnPreferenceChangeListener(this);
        } else {
            removePreference(KEY_SCAN_RESULT_ACTION);
        }
        if (Functions.SCAN_RESULT_KEY) {
            mScanResultKeyPreference = findPreference(KEY_SCAN_RESULT_KEY);
            mScanResultKeyPreference.setOnPreferenceClickListener(this);
        } else {
            removePreference(KEY_SCAN_RESULT_KEY);
        }
        if (Functions.SCAN_BTN_KEYDOWN) {
            mScanBtnKeyDownPreference = (SwitchPreference) findPreference(KEY_SCAN_BTN_KEYDOWN);
            mScanBtnKeyDownPreference.setOnPreferenceChangeListener(this);
        } else {
            removePreference(KEY_SCAN_BTN_KEYDOWN);
        }
        if (Functions.SCAN_BTN_KEYUP) {
            mScanBtnKeyUpPreference = (SwitchPreference) findPreference(KEY_SCAN_BTN_KEYUP);
            mScanBtnKeyUpPreference.setOnPreferenceChangeListener(this);
        } else {
            removePreference(KEY_SCAN_BTN_KEYUP);
        }
        if (Functions.SCAN_SETTINGS_TEST) {
            mScanSettingsTestPreference = findPreference(KEY_SCAN_SETTINGS_TEST);
            mScanSettingsTestPreference.setOnPreferenceClickListener(this);
        } else {
            removePreference(KEY_SCAN_SETTINGS_TEST);
        }
    }

    public void updateState() {
        if (mScanResultActionPreference != null) {
            mScanResultActionPreference.setText(EzServiceManager.getScanResultAction());
        }
        if (mScanResultKeyPreference != null) {

        }
        if (mScanBtnKeyDownPreference != null) {
            mScanBtnKeyDownPreference.setChecked(EzServiceManager.isScanFuncEnable(FunctionCode.SCAN_BTN_KEYDOWN_BROADCAST));
        }
        if (mScanBtnKeyUpPreference != null) {
            mScanBtnKeyUpPreference.setChecked(EzServiceManager.isScanFuncEnable(FunctionCode.SCAN_BTN_KEYUP_BROADCAST));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Intent intent;
        if (preference == mScanResultActionPreference) {
            intent = new Intent("com.honeywell.ezservice.ACTION_SCAN_EXPAND_SETTINGS");
            intent.putExtra("setScanResultAction", (String) newValue);
            getActivity().sendBroadcast(intent);
        }
        if (preference == mScanBtnKeyDownPreference) {
            intent = new Intent("com.honeywell.ezservice.ACTION_SCAN_EXPAND_SETTINGS");
            intent.putExtra("setScanBtnKeyDownBro", (boolean) newValue);
            getActivity().sendBroadcast(intent);
        }
        if (preference == mScanBtnKeyUpPreference) {
            intent = new Intent("com.honeywell.ezservice.ACTION_SCAN_EXPAND_SETTINGS");
            intent.putExtra("setScanBtnKeyUpBro", (boolean) newValue);
            getActivity().sendBroadcast(intent);
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mScanResultKeyPreference) {
            Intent intent = new Intent(getActivity(), ScanResultKeySetActivity.class);
            startActivity(intent);
        }
        if (preference == mScanSettingsTestPreference) {
            Intent intent = new Intent(getActivity(), ScanSettingsTestActivity.class);
            startActivity(intent);
        }
        return true;
    }
}

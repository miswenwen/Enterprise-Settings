package com.honeywell.ezservicetest.scan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.honeywell.ezservicetest.App;
import com.honeywell.ezservicetest.R;

public class ScanSettingsTestActivity extends AppCompatActivity {
    private EditText mScanResultActionEv;
    private TextView mScanResultActionVal;
    private EditText mKey_codeid_Ev;
    private TextView mKey_codeid_Val;
    private EditText mKey_dataBytes_Ev;
    private TextView mKey_dataBytes_Val;
    private EditText mKey_data_Ev;
    private TextView mKey_data_Val;
    private EditText mKey_timestamp_Ev;
    private TextView mKey_timestamp_Val;
    private EditText mKey_aimid_Ev;
    private TextView mKey_aimid_Val;
    private EditText mKey_version_Ev;
    private TextView mKey_version_Val;
    private EditText mKey_charset_Ev;
    private TextView mKey_charset_Val;
    private EditText mKey_scanner_Ev;
    private TextView mKey_scanner_Val;

    private EditText mScanKeyUpActionEv;
    private TextView mScanKeyUpActionVal;

    private EditText mScanKeyDownActionEv;
    private TextView mScanKeyDownActionVal;

    private App mApp;

    private int mCountScanResult = 0;
    private int mCountScanUp = 0;
    private int mCountScanDown = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_settings_test_main);
        setupActionBar();
        mApp = (App) getApplication();
        initUI();
        registerReceiver();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setTitle(getString(R.string.scan_settings_test_title));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    public void reset(View v){
        unregisterReceiver();
        registerReceiver();
    }
    BroadcastReceiver mScanResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mCountScanResult++;
            mScanResultActionVal.setText("" + mCountScanResult);
            mKey_codeid_Val.setText(intent.getStringExtra(getText(mKey_codeid_Ev)));
            if (intent.getByteArrayExtra(getText(mKey_dataBytes_Ev)) == null) {
                mKey_dataBytes_Val.setText("");
            } else {
                mKey_dataBytes_Val.setText("" + new String(intent.getByteArrayExtra(getText(mKey_dataBytes_Ev))));
            }
            mKey_data_Val.setText(intent.getStringExtra(getText(mKey_data_Ev)));
            mKey_timestamp_Val.setText(intent.getStringExtra(getText(mKey_timestamp_Ev)));
            mKey_aimid_Val.setText(intent.getStringExtra(getText(mKey_aimid_Ev)));
            if (intent.getIntExtra(getText(mKey_version_Ev), -1) == -1) {
                mKey_version_Val.setText("");
            }else {
                mKey_version_Val.setText("" + intent.getIntExtra(getText(mKey_version_Ev), -1));
            }
            mKey_charset_Val.setText(intent.getStringExtra(getText(mKey_charset_Ev)));
            mKey_scanner_Val.setText(intent.getStringExtra(getText(mKey_scanner_Ev)));
        }
    };
    BroadcastReceiver mScanBtnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(getText(mScanKeyUpActionEv))) {
                mCountScanUp++;
                mScanKeyUpActionVal.setText("" + mCountScanUp);
            } else if (intent.getAction().equals(getText(mScanKeyDownActionEv))) {
                mCountScanDown++;
                mScanKeyDownActionVal.setText("" + mCountScanDown);
            }
        }
    };

    private void registerReceiver() {
        IntentFilter mScanResultFilter = new IntentFilter(getText(mScanResultActionEv));
        registerReceiver(mScanResultReceiver, mScanResultFilter);


        IntentFilter mScanBtnFilter = new IntentFilter();
        mScanBtnFilter.addAction(getText(mScanKeyUpActionEv));
        mScanBtnFilter.addAction(getText(mScanKeyDownActionEv));
        registerReceiver(mScanBtnReceiver, mScanBtnFilter);
    }

    private void unregisterReceiver() {
        unregisterReceiver(mScanResultReceiver);
        unregisterReceiver(mScanBtnReceiver);
    }

    private void tempStoreVals() {
        App.SCAN_RESULT_ACTION = getText(mScanResultActionEv);
        App.SCAN_KEY_CODEID = getText(mKey_codeid_Ev);
        App.SCAN_KEY_DATABYTES = getText(mKey_dataBytes_Ev);
        App.SCAN_KEY_DATA = getText(mKey_data_Ev);
        App.SCAN_KEY_TIMESTAMP = getText(mKey_timestamp_Ev);
        App.SCAN_KEY_AIMID = getText(mKey_aimid_Ev);
        App.SCAN_KEY_VERSION = getText(mKey_version_Ev);
        App.SCAN_KEY_CHARSET = getText(mKey_charset_Ev);
        App.SCAN_KEY_SCANNER = getText(mKey_scanner_Ev);
        App.SCAN_BUTTION_KEY_UP_ACTION = getText(mScanKeyUpActionEv);
        App.SCAN_BUTTION_KEY_DOWN_ACTION = getText(mScanKeyDownActionEv);
    }

    private String getText(TextView tv) {
        return tv.getText().toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initUI() {
        mScanResultActionEv = (EditText) findViewById(R.id.scan_result_action);
        mScanResultActionVal = (TextView) findViewById(R.id.tv_action_result);
        mKey_codeid_Ev = (EditText) findViewById(R.id.scan_result_key_codeid);
        mKey_codeid_Val = (TextView) findViewById(R.id.tv_key_codeid_val);
        mKey_dataBytes_Ev = (EditText) findViewById(R.id.scan_result_dataBytes);
        mKey_dataBytes_Val = (TextView) findViewById(R.id.tv_key_dataBytes_val);
        mKey_data_Ev = (EditText) findViewById(R.id.scan_result_data);
        mKey_data_Val = (TextView) findViewById(R.id.tv_key_data_val);
        mKey_timestamp_Ev = (EditText) findViewById(R.id.scan_result_timestamp);
        mKey_timestamp_Val = (TextView) findViewById(R.id.tv_key_timestamp_val);
        mKey_aimid_Ev = (EditText) findViewById(R.id.scan_result_aimid);
        mKey_aimid_Val = (TextView) findViewById(R.id.tv_key_aimid_val);
        mKey_version_Ev = (EditText) findViewById(R.id.scan_result_version);
        mKey_version_Val = (TextView) findViewById(R.id.tv_key_version_val);
        mKey_charset_Ev = (EditText) findViewById(R.id.scan_result_charset);
        mKey_charset_Val = (TextView) findViewById(R.id.tv_key_charset_val);
        mKey_scanner_Ev = (EditText) findViewById(R.id.scan_result_scanner);
        mKey_scanner_Val = (TextView) findViewById(R.id.tv_key_scanner_val);

        mScanKeyUpActionEv = (EditText) findViewById(R.id.scan_button_key_up_action);
        mScanKeyUpActionVal = (TextView) findViewById(R.id.tv_scan_button_key_up_result);

        mScanKeyDownActionEv = (EditText) findViewById(R.id.scan_button_key_down_action);
        mScanKeyDownActionVal = (TextView) findViewById(R.id.tv_scan_button_key_down_result);


        mScanResultActionEv.setText(App.SCAN_RESULT_ACTION);
        mKey_codeid_Ev.setText(App.SCAN_KEY_CODEID);
        mKey_dataBytes_Ev.setText(App.SCAN_KEY_DATABYTES);
        mKey_data_Ev.setText(App.SCAN_KEY_DATA);
        mKey_timestamp_Ev.setText(App.SCAN_KEY_TIMESTAMP);
        mKey_aimid_Ev.setText(App.SCAN_KEY_AIMID);
        mKey_version_Ev.setText(App.SCAN_KEY_VERSION);
        mKey_charset_Ev.setText(App.SCAN_KEY_CHARSET);
        mKey_scanner_Ev.setText(App.SCAN_KEY_SCANNER);

        mScanKeyUpActionEv.setText(App.SCAN_BUTTION_KEY_UP_ACTION);
        mScanKeyDownActionEv.setText(App.SCAN_BUTTION_KEY_DOWN_ACTION);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
        tempStoreVals();
    }
}

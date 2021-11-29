package com.honeywell.ezservicetest.scan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.honeywell.ezservicetest.R;
import com.honeywell.ezservice.EzServiceManager;
import com.honeywell.ezservice.FunctionCode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ScanResultKeySetActivity extends AppCompatActivity {
    private Button mKeyBtn;
    private Button mConfirmBtn;
    private EditText mValEditTv;
    String[] SCAN_KEYS = {FunctionCode.CODEID_KEY, FunctionCode.DATABYTES_KEY, FunctionCode.DATA_KEY,
            FunctionCode.TIMESTAMP_KEY, FunctionCode.AIMID_KEY, FunctionCode.VERSION_KEY,
            FunctionCode.CHARSET_KEY, FunctionCode.SCANNER_KEY};
    List<String> SCAN_KEYS_LIST = Arrays.asList(SCAN_KEYS);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_result_key_set_main);
        setupActionBar();
        EzServiceManager.initService(this);
        initUI();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setTitle(getString(R.string.scan_result_key_title));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initUI() {
        mKeyBtn = (Button) findViewById(R.id.scan_key_name);
        mConfirmBtn = (Button) findViewById(R.id.confirm_btn);
        mValEditTv = (EditText) findViewById(R.id.scan_value);
        mKeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(mKeyBtn);
            }
        });
        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newKey = mValEditTv.getText().toString();
                Intent intent;
                if (SCAN_KEYS_LIST.contains(mKeyBtn.getText().toString())) {
                    intent = new Intent("com.honeywell.ezservice.ACTION_SCAN_EXPAND_SETTINGS");
                    String[] strArr = new String[]{mKeyBtn.getText().toString() + "|" + newKey};
                    intent.putExtra("setScanResultKey", strArr);
                    sendBroadcast(intent);
                }
            }
        });
        /*
        mValEditTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EzServiceManager.destroyService(this);
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        //popupMenu.getMenuInflater().inflate(R.menu.scan_keys, popupMenu.getMenu());
        for (int i = 0; i < SCAN_KEYS_LIST.size(); i++) {
            popupMenu.getMenu().add(SCAN_KEYS_LIST.get(i));
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mValEditTv.setVisibility(View.VISIBLE);
                mConfirmBtn.setVisibility(View.VISIBLE);
                mKeyBtn.setText(item.getTitle());
                HashMap<String, String> map = new HashMap<>();
                String[] keys = EzServiceManager.getScanResultKey();
                for (int i = 0; i < keys.length; i++) {
                    String[] twoKeys = keys[i].split("\\|");
                    map.put(twoKeys[0], twoKeys[1]);
                }
                if (map.containsKey(item.getTitle())) {
                    mValEditTv.setText(map.get(item.getTitle()));
                } else {
                    mValEditTv.setText(item.getTitle());
                }
                return false;
            }
        });
        popupMenu.show();
    }
}

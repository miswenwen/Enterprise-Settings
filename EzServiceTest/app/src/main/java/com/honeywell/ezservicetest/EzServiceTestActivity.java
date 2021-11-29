package com.honeywell.ezservicetest;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.honeywell.ezservice.EzServiceManager;

import java.util.List;

public class EzServiceTestActivity extends AppCompatPreferenceActivity {
    private boolean mHasBindedService=false;
    private Fragment mFragment;
    EzServiceManager.CallBack mCallBack=new EzServiceManager.CallBack() {
        @Override
        public void onServiceConnected() {
            mHasBindedService=true;
            if(mFragment!=null){
                if(mFragment instanceof ScanPreferenceFragment){
                    try {
                        ((ScanPreferenceFragment) mFragment).updateState();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(mFragment instanceof GeneralPreferenceFragment){
                    try {
                        ((GeneralPreferenceFragment) mFragment).updateState();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


        }

        @Override
        public void onServiceDisconnected() {
            mHasBindedService=false;
        }
    };
    public boolean hasBindedService(){
        return mHasBindedService;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        EzServiceManager.initService(this,mCallBack);
    }
//    @Override
//    public void setContentView(int layoutResID) {
//        ViewGroup contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.settings_activity,null);
//        ViewGroup contentWrapper = (ViewGroup) contentView.findViewById(R.id.content_wrapper);
//        getLayoutInflater().inflate(layoutResID, contentWrapper, true);
//        super.setContentView(contentView);
//    }
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        mFragment=fragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EzServiceManager.destroyService(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            if(Build.VERSION.SDK_INT<26)
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }
}

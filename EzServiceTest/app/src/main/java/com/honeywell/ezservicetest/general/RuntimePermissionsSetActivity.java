package com.honeywell.ezservicetest.general;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.honeywell.ezservicetest.R;

import java.util.ArrayList;
import java.util.List;

public class RuntimePermissionsSetActivity extends AppCompatActivity implements
        OnItemClickListener,View.OnClickListener{
    private ListView mListView;
    private TextView mPkgNameTv;
    private Button mGrantBtn;
    private Button mRevokeBtn;
    private List<AppInfo> mListAppInfo = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.runtime_permission_set_main);
        setupActionBar();
        initUI();
    }
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setTitle(getString(R.string.runtime_permissions_set_key_title));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initUI(){
        mListView = (ListView) findViewById(R.id.listviewApp);
        mPkgNameTv=(TextView)findViewById(R.id.pkgName_tv);
        mGrantBtn=(Button) findViewById(R.id.grant_btn);
        mRevokeBtn=(Button)findViewById(R.id.revoke_btn);
        mGrantBtn.setOnClickListener(this);
        mRevokeBtn.setOnClickListener(this);
        mListAppInfo = new ArrayList<AppInfo>();
        getDesktopApp();
        BrowseApplicationInfoAdapter browseAppAdapter = new BrowseApplicationInfoAdapter(
                this, mListAppInfo);
        mListView.setAdapter(browseAppAdapter);
        mListView.setOnItemClickListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.grant_btn:
                intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
                intent.putExtra("setGrantAllRuntimePermissions",mPkgNameTv.getText().toString());
                sendBroadcast(intent);
                break;
            case R.id.revoke_btn:
                intent = new Intent("com.honeywell.ezservice.ACTION_EZ_SETTINGS");
                intent.putExtra("setRevokeAllRuntimePermissions",mPkgNameTv.getText().toString());
                sendBroadcast(intent);
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void onItemClick(AdapterView<?> arg0, View view, int position,
                            long arg3) {
        mPkgNameTv.setText(mListAppInfo.get(position).getPkgName());
        mGrantBtn.setVisibility(View.VISIBLE);
        mRevokeBtn.setVisibility(View.VISIBLE);
    }
    public void getDesktopApp(){
        List<ResolveInfo> resolveInfos;
        PackageManager packageManager = this.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveInfos = packageManager.queryIntentActivities(mainIntent, 0);
        for (ResolveInfo reInfo : resolveInfos) {
            String pkgName = reInfo.activityInfo.packageName;
            String appLabel = (String) reInfo.loadLabel(packageManager);
            Drawable appIcon = reInfo.loadIcon(packageManager);
            AppInfo appInfo = new AppInfo();
            appInfo.setAppLabel(appLabel);
            appInfo.setPkgName(pkgName);
            appInfo.setAppIcon(appIcon);
            mListAppInfo.add(appInfo);
        }
    }
}


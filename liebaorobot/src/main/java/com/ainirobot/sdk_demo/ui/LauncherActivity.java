package com.ainirobot.sdk_demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ainirobot.sdk_demo.R;
import com.ainirobot.sdk_demo.control.MessageManager;
import com.ainirobot.sdk_demo.model.BaseFragment;
import com.ainirobot.sdk_demo.model.bean.MessageEvent;
import com.ainirobot.sdk_demo.service.ModuleService;
import com.ainirobot.sdk_demo.ui.fragment.LeadFragment;
import com.ainirobot.sdk_demo.ui.fragment.SkillListFragment;
import com.ainirobot.sdk_demo.ui.fragment.WelcomeFragment;
import com.ainirobot.sdk_demo.utils.Constants;
import com.ainirobot.sdk_demo.utils.SystemUtils;
import com.ainirobot.sdk_demo.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.ainirobot.sdk_demo.utils.Constants.FRAGMENT_WELCOME_SCENE;

public class LauncherActivity extends AppCompatActivity implements BaseFragment.OnFragmentChangedListener {

    private static final String TAG = "LauncherActivity";

    /**
     * 当前显示的页面
     */
    private Fragment mCurrentFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private TextView tvApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Log.d(TAG, "onCreate: ");
        if (!SystemUtils.isServiceRunning(ModuleService.class, getApplicationContext())) {
            Log.d(TAG, "onCreate: start service.");
            Intent intent = new Intent(this, ModuleService.class);
            startService(intent);
        }
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setCurrentFragment();
        ToastUtil.getInstance().init(this);
        EventBus.getDefault().register(this);
        tvApi = findViewById(R.id.tv_api);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 注册欢迎界面和导航界面监听
     *
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void replaceFragment(MessageEvent messageEvent) {
        if (messageEvent != null) {
            switch (messageEvent.getFragmentType()) {
                case Constants.FRAGMENT_TYPE.WELCOME:
                    exchangeFragment(new WelcomeFragment(), FRAGMENT_WELCOME_SCENE);
                    break;
                case Constants.FRAGMENT_TYPE.NAVIGATION:
                    exchangeFragment(new LeadFragment(), Constants.FRAGMENT_NAVIGATION_SCENE);
                    break;
                case Constants.FRAGMENT_TYPE.UPDATE_API:
                    if (messageEvent.getWhat() != null) {
                        tvApi.setVisibility(View.VISIBLE);
                        tvApi.setText(messageEvent.getWhat());
                    } else {
                        tvApi.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void exchangeFragment(Fragment newFragment, String tag) {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.container, newFragment, tag);
        mFragmentTransaction.commit();
        onChanged(newFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        if (!MessageManager.getInstance().isSuspend()) {
            Toast.makeText(this, "应用已恢复", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "应用为挂起状态，请通过 Home 启动本应用", Toast.LENGTH_SHORT).show();
        }
    }

    private void setCurrentFragment() {
        mCurrentFragment = new SkillListFragment();
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.container, mCurrentFragment, Constants.FRAGMENT_FUNC_LIST);
        mFragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (mCurrentFragment instanceof SkillListFragment) {
            super.onBackPressed();
        } else {
            setCurrentFragment();
        }
    }

    @Override
    public void onChanged(Fragment fragment) {
        this.mCurrentFragment = fragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ToastUtil.getInstance().release();
        EventBus.getDefault().unregister(this);
    }
}

package com.zxy.demo._issue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zxy.demo.R;
import com.zxy.demo.entity.VersionUpdateEntity;
import com.zxy.frame.base.BaseActivity;

import butterknife.BindView;

public class VersionUpdateActivity extends BaseActivity {

    public static final String EXTRA_VERSION_UPDATE_ENTITY = "extra_version_update_entity";

    @BindView(R.id.fl_content)
    FrameLayout flContent;

    VersionUpdateEntity mEntity;

    public static void start(Activity activity, VersionUpdateEntity entity) {
        Intent intent = new Intent(activity, VersionUpdateActivity.class);
        intent.putExtra(EXTRA_VERSION_UPDATE_ENTITY, entity);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mNeedInsetStatusBar = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_version_update;
    }

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        if (getIntent() != null) {
            mEntity = (VersionUpdateEntity) getIntent().getSerializableExtra(EXTRA_VERSION_UPDATE_ENTITY);
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(VersionUpdateFragment.class.getSimpleName());
        if(!(fragment instanceof VersionUpdateFragment)){
            fragment = VersionUpdateFragment.getInstance(mEntity);
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_content, fragment, VersionUpdateFragment.class.getSimpleName()).commitAllowingStateLoss();
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}

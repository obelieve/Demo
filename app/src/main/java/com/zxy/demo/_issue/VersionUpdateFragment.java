package com.zxy.demo._issue;

import android.Manifest;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.demo.entity.VersionUpdateEntity;
import com.zxy.frame.base.BaseActivity;
import com.zxy.frame.base.BaseFragment;
import com.zxy.frame.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 版本更新
 */
public class VersionUpdateFragment extends BaseFragment {

    public static final String EXTRA_VERSION_UPDATE_ENTITY = "extra_version_update_entity";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_update)
    TextView tvUpdate;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.rl_close)
    RelativeLayout rlClose;
    @BindView(R.id.rl_content)
    RelativeLayout rlContent;

    private VersionUpdateEntity mEntity;


    public static VersionUpdateFragment getInstance(VersionUpdateEntity entity) {
        VersionUpdateFragment fragment = new VersionUpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_VERSION_UPDATE_ENTITY, entity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_version_update;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            mEntity = (VersionUpdateEntity) getArguments().getSerializable(EXTRA_VERSION_UPDATE_ENTITY);
        }
        if (mEntity == null)
            return;
        tvTitle.setText(String.format("发现新版本V%1$s", mEntity.getVersion_new()));
        tvContent.setText(Html.fromHtml(mEntity.getContent()));
        tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        rlClose.setVisibility(mEntity.getEnforce() == 1 ? View.GONE : View.VISIBLE);
    }

    @OnClick({R.id.tv_update, R.id.rl_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_update:
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, new BaseActivity.OnRequestPermissionListener() {
                        @Override
                        public void onSuccess() {
                            ToastUtil.show("成功");
                        }

                        @Override
                        public void onFailure() {
                            ToastUtil.show("失败");
                        }
                    });
                }
                break;
            case R.id.rl_close:
                ToastUtil.show("关闭");
                break;
        }
    }
}

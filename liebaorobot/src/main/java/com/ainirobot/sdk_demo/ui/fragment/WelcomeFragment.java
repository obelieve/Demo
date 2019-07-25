package com.ainirobot.sdk_demo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ainirobot.sdk_demo.R;
import com.ainirobot.sdk_demo.model.BaseFragment;
import com.ainirobot.sdk_demo.module.LeadingModule;
import com.ainirobot.sdk_demo.module.WelcomeModule;

/**
 * 专门用来展示场景的界面
 *
 * @author simon
 */
public class WelcomeFragment extends BaseFragment implements WelcomeModule.WelcomeListener {
    private static final String TAG = WelcomeFragment.class.getSimpleName();
    private View mContainer;
    private TextView tvWakeUp;
    private TextView tvQuestion;
    private TextView tvAnswer;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainer = inflater.inflate(R.layout.layout_welcome, container, false);
        initView();
        initData();
        registCallBack();
        return mContainer;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unRegistCallBack();
    }

    private void registCallBack() {
        WelcomeModule.getInstance().registerWelcomeListener(this);
    }
    private void unRegistCallBack() {
        LeadingModule.getInstance().unRegistLeadListener();
        WelcomeModule.getInstance().unWelcomeLeadListener();
    }
    private void initData() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tvWakeUp = mContainer.findViewById(R.id.tv_func_wakeup);
        tvQuestion = mContainer.findViewById(R.id.tv_func_chat_question);
        tvAnswer = mContainer.findViewById(R.id.tv_func_chat_answer);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void wakeUp() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvWakeUp.setText(getString(R.string.orion_had_wakeup));
                }
            });
        }
    }

    @Override
    public void standby() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvWakeUp.setText(getString(R.string.orion_standby));
                }
            });
        }
    }

    @Override
    public void chat(final String question, final String answer) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvQuestion.setText(question);
                    tvAnswer.setText(answer);
                }
            });
        }
    }

    @Override
    public void stop() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }
}

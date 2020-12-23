package com.zxy.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.zxy.demo.R;
import com.zxy.frame.utils.log.LogUtil;

/**
 * Created by zxy
 * on 2020/12/23
 */
public class OneFragment extends Fragment {

    private static int count = 0;
    private final String uuid = "" + (count++);
    private OneViewModel mOneViewModel;

    private TextView tv;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LogUtil.e(uuid);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e(uuid);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.e(uuid);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.e(uuid);
        mOneViewModel = ViewModelProviders.of(this).get(OneViewModel.class);
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        tv = view.findViewById(R.id.tv);
        mOneViewModel.mLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                /** 只会在屏幕旋转等配置变更Fragment重建时，
                 * 才会对最后保存的数据，进行通知观察者。 **/

                LogUtil.e("获取 值 =================================" + integer);
                tv.setText("" + integer);
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOneViewModel.doSomething();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.e(uuid);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.e(uuid);
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.e(uuid);
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.e(uuid);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.e(uuid);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e(uuid);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.e(uuid);
    }

    public static class OneViewModel extends ViewModel {
        private MutableLiveData<Integer> mLiveData = new MutableLiveData<Integer>();

        public void doSomething() {
            int value = (mLiveData.getValue() != null) ? mLiveData.getValue() : 0;
            mLiveData.setValue(value + 1);
        }
    }
}

package com.zxy.demo;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 * Created by Admin
 * on 2020/11/4
 */
public class LifecycleViewModel extends ViewModel {

    private MutableLiveData<A> mValueLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mNameLiveData = new MutableLiveData<>();

    /**
     * 监听LiveData
     */
    private MediatorLiveData<Object> mAMediatorLiveData = new MediatorLiveData();
    {
        mAMediatorLiveData.addSource(mNameLiveData, new Observer<String>() {
            @Override
            public void onChanged(String str) {
                mAMediatorLiveData.setValue(str);
            }
        });
        mAMediatorLiveData.addSource(mValueLiveData, new Observer<A>() {
            @Override
            public void onChanged(A a) {
                mAMediatorLiveData.setValue(a);
            }
        });

    }
    private int mCount = 0;
    private boolean mOpen = true;

    /**
     * 异步发送LiveData数据
     */
    public void open(){
        A a = new A();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mOpen){
                    a.setCount(mCount++);
                    mValueLiveData.postValue(a);
                    mNameLiveData.postValue(mCount+"值");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    /**
     * 取消发送LiveData数据
     */
    public void close(){
        mOpen = false;
    }

    public MutableLiveData<A> getValue() {
        return mValueLiveData;
    }

    public MediatorLiveData<Object> getAMediatorLiveData() {
        return mAMediatorLiveData;
    }

    public static class A{
        private int mCount;

        public int getCount() {
            return mCount;
        }

        public void setCount(int count) {
            mCount = count;
        }

        @Override
        public String toString() {
            return "A{" +
                    "mCount=" + mCount +
                    '}';
        }
    }
}

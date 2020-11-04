package com.zxy.demo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Created by Admin
 * on 2020/11/4
 */
public class LifecycleViewModel extends ViewModel {

    private MutableLiveData<A> mValueLiveData = new MutableLiveData<>();

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

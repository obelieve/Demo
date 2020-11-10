package com.zxy.frame.view.pagestatus;

import android.content.Context;
import android.view.ViewStub;

import com.zxy.frame.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin
 * on 2020/11/10
 */
public class PageStatus {

    public interface Status {
        int LOADING = 0;
        int FAILURE = 1;
        int SUCCESS = 2;
    }

    public static Map<Integer, ViewStub> getPageStatusView(Context context) {
        Map<Integer, ViewStub> map = new HashMap<>();
        map.put(Status.LOADING, new ViewStub(context, R.layout.viewstub_loading_page_status));
        map.put(Status.FAILURE, new ViewStub(context, R.layout.viewstub_failure_page_status));
        return map;
    }
}

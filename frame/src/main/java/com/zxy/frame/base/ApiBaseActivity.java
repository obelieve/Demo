package com.zxy.frame.base;

import androidx.lifecycle.ViewModel;
import androidx.viewbinding.ViewBinding;

public abstract class ApiBaseActivity <T extends ViewBinding> extends ApiBaseActivity2<T,ViewModel> {
    @Override
    protected void initViewModel() {

    }
}

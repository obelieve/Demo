package com.zxy.demo

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import com.obelieve.frame.dialog.BaseDialog
import com.obelieve.frame.utils.info.SystemInfoUtil
import com.zxy.demo.databinding.DialogMainBinding

class MainDialog(activity: Activity?) : BaseDialog(activity) {

    private var mBinding: DialogMainBinding =
        DialogMainBinding.inflate(LayoutInflater.from(activity))

    init {
        var view: View = mBinding.root
        setContentView(view)
        setWidth(SystemInfoUtil.screenWidth(mActivity))
        mBinding.btnDismiss.setOnClickListener {
            dismiss()
        }
    }

}

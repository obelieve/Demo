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
//        mDialog.window?.attributes = mDialog.window?.getAttributes()
//        mDialog.window?.attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
//        mDialog.window?.attributes = mDialog.window?.attributes
        mBinding.btnDismiss.setOnClickListener {
            dismiss()
        }
    }

}

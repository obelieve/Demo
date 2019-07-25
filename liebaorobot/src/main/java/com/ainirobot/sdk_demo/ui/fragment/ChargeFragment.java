package com.ainirobot.sdk_demo.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ainirobot.sdk_demo.R;
import com.ainirobot.sdk_demo.model.BaseFragment;
import com.ainirobot.sdk_demo.skill.ChargeSkill;
import com.ainirobot.sdk_demo.utils.TestUtil;


/**
 * @author Orion
 * @time 2018/9/20
 */
public class ChargeFragment extends BaseFragment {

    private View mContainer;

    private Button mBtnSetChargePile;
    private Button mBtnAutoCharge;
    private Button mBtnStopCharge;
    private Button mBtnleaveCharge;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainer = inflater.inflate(R.layout.layout_charge, container, false);
        //
        mBtnSetChargePile = (Button) mContainer.findViewById(R.id.button_charge_set_charge_pile);
        mBtnAutoCharge = (Button) mContainer.findViewById(R.id.button_charge_auto_charge);
        mBtnStopCharge = (Button) mContainer.findViewById(R.id.button_charge_stop_auto_charge);
        mBtnleaveCharge = (Button) mContainer.findViewById(R.id.button_charge_leave_auto_charge);
        setOnClickListener(mBtnAutoCharge, mBtnSetChargePile,mBtnStopCharge,mBtnleaveCharge);
        return mContainer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_charge_set_charge_pile:
                ChargeSkill.getInstance().setStartChargePoseAction();
                TestUtil.updateApi("setStartChargePoseAction");

                break;
            case R.id.button_charge_auto_charge:
                ChargeSkill.getInstance().startNaviToAutoChargeAction();
                TestUtil.updateApi("startNaviToAutoChargeAction");
                break;
            case R.id.button_charge_stop_auto_charge:
                ChargeSkill.getInstance().stopAutoChargeAction();
                TestUtil.updateApi("stopAutoChargeAction");
                break;
            case R.id.button_charge_leave_auto_charge:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ChargeSkill.getInstance().stopChargingByApp();
                        TestUtil.updateApi("stopChargingByApp");
                    }
                }, 5000);

                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        ChargeSkill.getInstance().stopAutoChargeAction();
    }
}

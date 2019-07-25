package com.ainirobot.sdk_demo.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.ainirobot.sdk_demo.R;
import com.ainirobot.sdk_demo.model.BaseFragment;
import com.ainirobot.sdk_demo.skill.ControlManager;
import com.ainirobot.sdk_demo.utils.Constants;
import com.ainirobot.sdk_demo.utils.TestUtil;

import static com.ainirobot.sdk_demo.utils.TestUtil.SCENE_MODE;
import static com.ainirobot.sdk_demo.utils.TestUtil.SINGLE_MODE;


/**
 * @author Orion
 * @time 2018/9/11
 */
public class SkillListFragment extends BaseFragment {

    private static final String TAG = "SkillListFragment";

    private View mContainer;
    private FragmentManager mFragmentManager;

    private OnFragmentChangedListener onFragmentChangedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.onFragmentChangedListener = (OnFragmentChangedListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getActivity().getSupportFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainer = inflater.inflate(R.layout.layout_function_list, container, false);
        LinearLayout root = mContainer.findViewById(R.id.layout_function_list_container);
        mContainer.findViewById(R.id.layout_function_list_doc).setOnClickListener(this);
        int count = root.getChildCount();
        for (int i = 0; i < count; i++) {
            setOnClickListener(root.getChildAt(i));
        }
        return mContainer;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout_function_list_doc:
                TestUtil.setTestMode(SINGLE_MODE);
                exchangeFragment(new DocFragment(), Constants.FRAGMENT_DOC);
                break;
            case R.id.button_func_move:
                TestUtil.setTestMode(SINGLE_MODE);
                exchangeFragment(new MoveFragment(), Constants.FRAGMENT_MOVE);
                break;
            case R.id.button_func_speech:
                TestUtil.setTestMode(SINGLE_MODE);
                exchangeFragment(new SpeechFragment(), Constants.FRAGMENT_SPEECH);
                break;
            case R.id.button_func_navigation:
                TestUtil.setTestMode(SINGLE_MODE);
                exchangeFragment(new NavigationFragment(), Constants.FRAGMENT_NAVIGATION);
                break;
            case R.id.button_func_register_face:
                TestUtil.setTestMode(SINGLE_MODE);
                exchangeFragment(new FaceFragment(), Constants.FRAGMENT_FACE);
                break;
            case R.id.button_func_maps:
                TestUtil.setTestMode(SINGLE_MODE);
                exchangeFragment(new MapFragment(), Constants.FRAGMENT_MAP);
                break;
            case R.id.button_func_test:
                TestUtil.setTestMode(SINGLE_MODE);
                exchangeFragment(new TestFragment(), Constants.FRAGMENT_TEST);
                break;
            case R.id.button_func_charge:
                TestUtil.setTestMode(SINGLE_MODE);
                exchangeFragment(new ChargeFragment(), Constants.FRAGMENT_CHARGE);
                break;
            case R.id.button_func_system:
                TestUtil.setTestMode(SINGLE_MODE);
                exchangeFragment(new SystemFragment(), Constants.FRAGMENT_SYSTEM);
                break;
            case R.id.button_func_scene:
                TestUtil.setTestMode(SCENE_MODE);
                ControlManager.getInstance().stopCurrentModule();
                ControlManager.getInstance().resetMode();
                ControlManager.getInstance().setMode(Constants.Mode.WELCOME_MODE,null);
                break;
            default:
                break;
        }
    }

    private void exchangeFragment(Fragment newFragment, String tag) {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.container, newFragment, tag);
        mFragmentTransaction.commit();
        onFragmentChangedListener.onChanged(newFragment);
    }
}

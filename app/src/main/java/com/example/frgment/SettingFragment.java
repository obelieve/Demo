package com.example.frgment;
import com.example.mynotezxy.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingFragment extends Fragment{
	public SettingFragment(){}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
         
        return rootView;
	}

}

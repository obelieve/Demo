package com.example.frgment;
import com.example.mynotezxy.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi")
public class SkinFragment extends Fragment{
	public SkinFragment(){}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 
        View rootView = inflater.inflate(R.layout.fragment_skin, container, false);
         
        return rootView;
	}

}

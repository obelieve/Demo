package com.example.frgment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

@SuppressLint("NewApi")
public class SpinnerNavigationFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Context context=this.getActivity();
		TextView tv=new TextView(context);
		Bundle bundle=this.getArguments();
		int tabs=bundle.getInt("key");
		tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));//设定自定义布局控件的属性
		tv.setText("hello"+tabs);
		return tv;
	}

}

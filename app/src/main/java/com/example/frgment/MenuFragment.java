package com.example.frgment;

import java.util.ArrayList;

import com.example.adapter.SlidingListAdapter;
import com.example.entity.SlidingItem;
import com.example.mynotezxy.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class MenuFragment extends Fragment implements OnItemClickListener{
	private ListView listView;
	private String[] menuTitles;
	private TypedArray menuIcons;
	private ArrayList<SlidingItem> slidingItems;
	private SlidingListAdapter listAdapter;
	private SLMenuListOnItemClickListener callback;
	private int selected =-1;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		try {
			callback=(SLMenuListOnItemClickListener)activity;
		} catch (ClassCastException e) {
			// TODO: handle exception
			throw new ClassCastException(activity.toString()
					+"must implement OnResolveTelsCompletedListener");
		}
		super.onAttach(activity);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView=inflater.inflate(R.layout.fragment_menu,null);
		findView(rootView);
		return rootView;
	}
	private void findView(View rootView){
		listView=(ListView)rootView.findViewById(R.id.left_menu);
		menuTitles=getResources().getStringArray(R.array.drawer_items);
		menuIcons=getResources().obtainTypedArray(R.array.drawer_icons);
		slidingItems=new ArrayList<SlidingItem>();
		slidingItems.add(new SlidingItem(menuTitles[0],menuIcons
				.getResourceId(0, -1)));//getResourceId后面int defValue参数是不存在前面值时使用这个参数
		slidingItems.add(new SlidingItem(menuTitles[1],menuIcons
				.getResourceId(1, -1)));
		slidingItems.add(new SlidingItem(menuTitles[2],menuIcons
				.getResourceId(2, -1)));
		slidingItems.add(new SlidingItem(menuTitles[3],menuIcons
				.getResourceId(3, -1)));
		slidingItems.add(new SlidingItem(menuTitles[4],menuIcons
				.getResourceId(4, -1)));
		menuIcons.recycle();
		listAdapter=new SlidingListAdapter(getActivity(), slidingItems);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(this);
		if (selected!=-1) {
			listView.setItemChecked(selected, true);
			listView.setSelection(selected);
		}else {
			listView.setItemChecked(0, true);//设置listView选中的Item
			listView.setSelection(0);//设置listView开始位置
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		listView.setItemChecked(position, true);
		listView.setSelection(position);
		if(callback !=null){
			callback.selectItem(position, menuTitles[position]);
		}
		selected=position;
	}
	public interface SLMenuListOnItemClickListener{
		public void selectItem(int position,String title);
	}

}

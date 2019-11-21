package com.example.mynotezxy;

import com.example.frgment.AboutFragment;
import com.example.frgment.ComposerButtonFragment;
import com.example.frgment.HomeFragment;
import com.example.frgment.MenuFragment;
import com.example.frgment.SettingFragment;
import com.example.frgment.SkinFragment;
import com.example.frgment.MenuFragment.SLMenuListOnItemClickListener;
import com.example.frgment.NoteFileFragment;
import com.example.frgment.SpinnerNavigationFragment;
import com.example.mynotezxy.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

@SuppressLint("NewApi")
public class MainActivity extends SlidingActivity implements SLMenuListOnItemClickListener{
	private SlidingMenu slidingMenu;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("首页");
		setContentView(R.layout.frame_content);
		setBehindContentView(R.layout.frame_menu);
		slidingMenu=getSlidingMenu();
		slidingMenu.setShadowDrawable(R.drawable.drawer_shadow);
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.menu, new MenuFragment());
		fragmentTransaction.replace(R.id.content,new HomeFragment());
		fragmentTransaction.replace(R.id.composer,new ComposerButtonFragment() );//弹出按钮
		fragmentTransaction.commit();
		getActionBar().setDisplayHomeAsUpEnabled(true);//显示左边图标返回
	}
	//implement OnNavigationListener
//		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);//下拉选项导航栏
//        SpinnerAdapter spinnerAdapter=ArrayAdapter.createFromResource(this, R.array.notefile,
//                R.layout.spinner_nav_tv);
//		getActionBar().setListNavigationCallbacks(spinnerAdapter,this);//添加适配器
//	}
//
//	@Override
//	public boolean onNavigationItemSelected(int itemPosition, long itemId) {//处理下拉导航点击时，响应
//		// TODO Auto-generated method stub
//		//String []listNames=getResources().getStringArray(R.array.notefile);
//		SpinnerNavigationFragment fragment=new SpinnerNavigationFragment();
//		Bundle bundle=new Bundle();
//		bundle.putInt("key", itemPosition);
//		fragment.setArguments(bundle);
//		FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
//		fragmentTransaction.replace(R.id.content, fragment);//显示在fragment_content.xml文件中
//		fragmentTransaction.commit();
//		return true;
//	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case android.R.id.home:
				toggle();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressLint("NewApi")
	@Override
	public void selectItem(int position, String title) {
		// TODO Auto-generated method stub
		Fragment fragment=null;
		switch (position) {
			case 0:
				fragment=new HomeFragment();
				break;
			case 1:
				fragment=new NoteFileFragment();
				break;
			case 2:
				fragment=new SkinFragment();
				break;
			case 3:
				fragment=new SettingFragment();
				break;
			case 4:
				fragment=new AboutFragment();
				break;
			default:
				break;
		}
		if (fragment!=null) {
			FragmentManager fragmentManager=getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content, fragment).commit();
			setTitle(title);
			slidingMenu.showContent();
		}else {
			Log.e("mainActivity", "Error ,Create Fragment");
		}
	}
}

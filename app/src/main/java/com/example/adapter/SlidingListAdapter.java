package com.example.adapter;

import java.util.List;

import com.example.entity.SlidingItem;
import com.example.mynotezxy.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class SlidingListAdapter extends BaseAdapter{
	private Context context;
	private List<SlidingItem> slidingItems;
	public SlidingListAdapter(Context context,List<SlidingItem> slidingItems) {
		this.context=context;
		this.slidingItems=slidingItems;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return slidingItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return slidingItems.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView==null) {
			LayoutInflater mInflater=(LayoutInflater)
					context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView=mInflater.inflate(R.layout.drawer_list_item, null);
		}
		ImageView imgIcon=(ImageView)convertView.findViewById(R.id.icon);
		TextView tvTitle=(TextView)convertView.findViewById(R.id.title);
		imgIcon.setImageResource(slidingItems.get(position).getIcon());
		tvTitle.setText(slidingItems.get(position).getTitle());
		return convertView;
	}

}

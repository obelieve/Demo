package com.example.frgment;

import com.example.mynotezxy.R;
import com.example.variety.VariedActivity;
import com.example.variety.define.DatabaseOperation;

import android.R.array;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class HomeFragment extends Fragment{
	ListView lvMain;
	private SQLiteDatabase db;
	private DatabaseOperation dOperation;
	TextView tv_note_id;
	View rootView;
	public HomeFragment(){}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		rootView = inflater.inflate(R.layout.fragment_home, container, false);
	        return rootView;
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
        lvMain=(ListView)rootView.findViewById(R.id.lvMain);
        dOperation = new DatabaseOperation(getActivity(), db);
		lvMain.setOnItemClickListener(new ItemClickEvent());
		lvMain.setOnItemLongClickListener(new ItemLongClickEvent());
		showNotesList();
	}
	private void showNotesList() {//刷新列表
		dOperation.create_db();
        Cursor cursor = dOperation.query_db();
        String[] from = new String[] {"_id","title", "time"};
        int[] to = new int[] {R.id.tv_note_id,R.id.tvTitle, R.id.tvTime};
		SimpleCursorAdapter adapter=new SimpleCursorAdapter(getActivity(),R.layout.note_item,
				cursor,from,to);
		lvMain.setAdapter(adapter);
		dOperation.close_db();

	}
	class ItemClickEvent implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			tv_note_id=(TextView)view.findViewById(R.id.tv_note_id);
			int item_id=Integer.parseInt(tv_note_id.getText().toString());
			Intent intent=new Intent(getActivity(),VariedActivity.class);
			intent.putExtra("back", "update");
			intent.putExtra("noteId", item_id);
			startActivity(intent);
		}

	}
	class ItemLongClickEvent implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			tv_note_id=(TextView)view.findViewById(R.id.tv_note_id);
			final int item_id=Integer.parseInt(tv_note_id.getText().toString());
			new AlertDialog.Builder(getActivity())
			.setTitle("删除")
			.setMessage("是否删除笔记")
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			})
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dOperation.create_db();
					dOperation.delete_db(item_id);
					dOperation.close_db();
					//刷新列表显示
					lvMain.invalidate();
					showNotesList();
				}
			})
			.create().show();
			return true;
		}
	}
}

package com.zxy.demo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.utility.SystemInfoUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv_content;
    OKRecycleAdapter mAdapter;

    List<String> mList = new ArrayList<String>();

    public int dp2px(int size) {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return (int) (dm.density * size);
    }

    public interface ItemTagCallback {
        String getTag(int position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv_content = (RecyclerView) findViewById(R.id.rv_content);
        rv_content.setLayoutManager(new LinearLayoutManager(this));
        rv_content.addItemDecoration(new RecyclerView.ItemDecoration() {
            ItemTagCallback callback;
            Paint mPaint = new Paint();

            {
                mPaint.setColor(Color.DKGRAY);
                mPaint.setStrokeWidth(30.0f);
                callback = new ItemTagCallback() {
                    @Override
                    public String getTag(int position) {
                        if (position >= 0 && position < 5)
                            return "A";
                        else if (position >= 5 && position < 15) {
                            return "B";
                        } else
                            return "C";
                    }
                };
            }

            public boolean isDrawGroupItem(int position) {
                if (position == 0) {
                    return true;
                } else {
                    if (callback.getTag(position).equals(callback.getTag(position - 1))) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
                for (int i = 0; i < parent.getChildCount(); i++) {
                    //dp2px(46)
                    View view = parent.getChildAt(i);
                    if (isDrawGroupItem(parent.getChildAdapterPosition(view)))
                        c.drawRect(0, parent.getChildAt(i).getTop() - 90, parent.getChildAt(i).getRight(), parent.getChildAt(i).getTop(), mPaint);
                }
            }

            @Override
            public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDrawOver(c, parent, state);
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#aaaa0000"));
                View view = parent.getChildAt(0);
                if (view.getBottom() <= 90 && isDrawGroupItem(parent.getChildAdapterPosition(view) + 1)) {
                    c.drawRect(0, 0, view.getRight(), view.getBottom(), paint);
                } else {
                    c.drawRect(0, 0, view.getRight(), 90, paint);
                }
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (isDrawGroupItem(parent.getChildAdapterPosition(view)))
                    outRect.top = 90;
            }
        });
        mAdapter = new OKRecycleAdapter();
        for (int i = 0; i < 20; i++) {
            mList.add("i:" + i);
        }
        mAdapter.setData(mList);
        rv_content.setAdapter(mAdapter);
    }

    public static class OKRecycleAdapter extends RecyclerAdapter<String> {

        @Override
        public int createViewHolder() {
            return R.layout.item_main;
        }

        @Override
        public void bindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, String s) {
            TextView tv = holder.itemView.findViewById(R.id.tv_name);
            tv.setText(s);
        }
    }

    public static abstract class RecyclerAdapter<T> extends RecyclerView.Adapter {

        private List<T> mList = new ArrayList<>();

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(createViewHolder(), parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            bindViewHolder(holder, position, mList.get(position));
        }

        public abstract int createViewHolder();

        public abstract void bindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, T t);

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public void setData(List<T> list) {
            if (list == null) {
                list = new ArrayList<>();
            }
            mList = list;
            notifyDataSetChanged();
        }

        public List<T> getList() {
            return mList;
        }
    }
}

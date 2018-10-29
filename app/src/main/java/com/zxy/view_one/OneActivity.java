package com.zxy.view_one;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxy.demo.R;
import com.zxy.utility.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by zxy on 2018/10/26 16:11.
 */

public class OneActivity extends Activity
{
    SwipeRefreshLayout refreshView;
    RecyclerView rv_content;

    RecycleBaseAdapter<String> mAdapter;
    List<String> mList;
    boolean mIsLoading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        mList = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "21", "22", "23", "24", "25", "26", "31", "32", "33", "34", "35", "36"));
        mAdapter = new RecycleBaseAdapter<>(mList);
        refreshView = (SwipeRefreshLayout) findViewById(R.id.refreshView);
        rv_content = (RecyclerView) findViewById(R.id.rv_content);
        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                LogUtil.e("onRefresh");
                refreshView.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mList.clear();
                        mList.addAll(Arrays.asList("1", "2", "3", "4", "5", "6", "21", "22", "23", "24", "25", "26", "31", "32", "33", "34", "35", "36"));
                        mAdapter.notifyDataSetChanged();
                        refreshView.setRefreshing(false);
                    }
                }, 500);
            }
        });
        rv_content.setLayoutManager(new LinearLayoutManager(this));
        rv_content.addItemDecoration(new RecyclerView.ItemDecoration()
        {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
            {
                if (parent.getChildAdapterPosition(view) != 0)
                {
                    outRect.top = 10;
                }
            }
        });
        rv_content.setAdapter(mAdapter);
        rv_content.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int itemCount = layoutManager.getItemCount();
                int lastPosition = layoutManager.findLastVisibleItemPosition();

                final View view = layoutManager.getChildAt(lastPosition);
                final int type = layoutManager.getItemViewType(view);

                if (!mIsLoading && lastPosition >= (itemCount - 1))
                {
                    mIsLoading = true;
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            if (new Random().nextInt(100) % 2 == 0)
                            {
                                for (int i = 100; i < 110; i++)
                                {
                                    mList.add("" + (char) i + "" + (char) (new Random().nextInt(100)));
                                }
                                mAdapter.notifyDataSetChanged();
                                if (type == RecycleBaseAdapter.FOOTER_TYPE)
                                {
                                    view.findViewById(R.id.ll).setVisibility(View.VISIBLE);
                                    ((TextView) view.findViewById(R.id.tv)).setText("正在加载...");
                                }
                            } else
                            {
                                if (type == RecycleBaseAdapter.FOOTER_TYPE)
                                {
                                    view.findViewById(R.id.ll).setVisibility(View.VISIBLE);
                                    ((TextView) view.findViewById(R.id.tv)).setText("-----我是有底线的-----");
                                }
                            }
                            mIsLoading = false;


                        }
                    }, 2000);
                } else
                {
                    if (type == RecycleBaseAdapter.FOOTER_TYPE)
                    {
                        view.findViewById(R.id.ll).setVisibility(View.GONE);
                        ((TextView) view.findViewById(R.id.tv)).setText("");
                    }
                }
            }
        });
    }

    public static class RecycleBaseAdapter<T> extends RecyclerView.Adapter
    {

        public static final int NORMAL_TYPE = 0;
        public static final int FOOTER_TYPE = 1;

        private List<T> mList;
        private FooterViewHolder mFooterViewHolder;

        public RecycleBaseAdapter(List<T> list)
        {
            mList = list;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            RecyclerView.ViewHolder viewHolder = null;
            View view;
            switch (viewType)
            {
                case NORMAL_TYPE:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_normal, parent, false);
                    viewHolder = new NormalViewHolder(view);
                    break;
                case FOOTER_TYPE:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_footer, parent, false);
                    viewHolder = new FooterViewHolder(view);
                    mFooterViewHolder = (FooterViewHolder) viewHolder;
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
        {
            if (holder instanceof NormalViewHolder)
            {
                NormalViewHolder viewHolder = (NormalViewHolder) holder;
                viewHolder.tv.setText(mList.get(position).toString());
            }
        }


        @Override
        public int getItemCount()
        {
            return mList.size() + 1;
        }

        @Override
        public int getItemViewType(int position)
        {
            if (position == mList.size())
            {
                return FOOTER_TYPE;
            }
            return NORMAL_TYPE;
        }

        public static class NormalViewHolder extends RecyclerView.ViewHolder
        {
            TextView tv;

            public NormalViewHolder(View itemView)
            {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.tv);
            }
        }

        public static class FooterViewHolder extends RecyclerView.ViewHolder
        {
            TextView tv_loading, tv_no_data;

            public FooterViewHolder(View itemView)
            {
                super(itemView);
                tv_loading = (TextView) itemView.findViewById(R.id.tv_loading);
                tv_no_data = (TextView) itemView.findViewById(R.id.tv_no_data);
            }

            public void setState(boolean isMore)
            {
                if (isMore)
                {
                    tv_loading.setVisibility(View.VISIBLE);
                    tv_no_data.setVisibility(View.GONE);
                } else
                {
                    tv_loading.setVisibility(View.GONE);
                    tv_no_data.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}

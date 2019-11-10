package com.zxy.frame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zxy.demo.R;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<DATA> extends RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder> {

    private final int LOAD_MORE_TYPE = -2;
    private final int EMPTY_TYPE = -1;
    private final int NORMAL_TYPE = 0;


    private final int LOADING_STATE = 0;
    private final int LOAD_END_STATE = 1;
    private final int LOAD_ERROR_STATE = 2;

    private Context mContext;
    private View mEmptyView;
    private int mLoadMoreState = 0;
    private RecyclerView mRecyclerView;
    private RecyclerView.OnScrollListener mOnScrollListener;

    private BaseDataHolder<DATA> mDataHolder;
    private OnItemClickCallback<DATA> mItemClickCallback;
    private OnItemLongClickCallback<DATA> mItemLongClickCallback;
    private OnLoadMoreListener mOnLoadMoreListener;

    public BaseRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public BaseDataHolder<DATA> getDataHolder() {
        if (mDataHolder == null) {
            mDataHolder = new BaseDataHolder<>(this);
        }
        return mDataHolder;
    }

    public abstract BaseRecyclerViewAdapter.BaseViewHolder getViewHolder(ViewGroup parent, int viewType);

    public abstract void loadViewHolder(BaseRecyclerViewAdapter.BaseViewHolder holder, int position);

    public void setLoadMore(RecyclerView rv, OnLoadMoreListener listener) {
        mRecyclerView = rv;
        mOnLoadMoreListener = listener;
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    public void setItemClickCallback(OnItemClickCallback<DATA> itemClickCallback) {
        mItemClickCallback = itemClickCallback;
    }

    public void setItemLongClickCallback(OnItemLongClickCallback<DATA> itemLongClickCallback) {
        mItemLongClickCallback = itemLongClickCallback;
    }

    @NonNull
    @Override
    public BaseRecyclerViewAdapter.BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseRecyclerViewAdapter.BaseViewHolder vh = null;
        switch (viewType) {
            case -2:
                int layoutId = R.layout.view_loading;
                switch (mLoadMoreState) {
                    case LOADING_STATE:
                        layoutId = R.layout.view_loading;
                        break;
                    case LOAD_END_STATE:
                        layoutId = R.layout.view_load_end;
                        break;
                    case LOAD_ERROR_STATE:
                        layoutId = R.layout.view_load_error;
                        break;
                }
                vh = new LoadMoreViewHolder(parent, layoutId);
                break;
            case -1:
                vh = new EmptyViewHolder(mEmptyView);
                break;
            default:
                vh = getViewHolder(parent, viewType);
                break;
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewAdapter.BaseViewHolder holder, int position) {
        final int pos = position;
        if (!(holder instanceof EmptyViewHolder) && !(holder instanceof LoadMoreViewHolder)) {
            loadViewHolder(holder, position);
            if (mItemClickCallback != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickCallback.onItemClick(v, getDataHolder().getList().get(pos), pos);
                    }
                });
            }
            if (mItemLongClickCallback != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return mItemLongClickCallback.onItemLongClick(v, getDataHolder().getList().get(pos), pos);
                    }
                });
            }
        } else {
            if (mItemClickCallback != null) {
                holder.itemView.setOnClickListener(null);
            }
            if (mItemLongClickCallback != null) {
                holder.itemView.setOnLongClickListener(null);
            }
        }
    }

    @Override
    public int getItemCount() {
        int size = getDataHolder().getList().size();
        if (size == 0) {
            if (mEmptyView != null) {
                return 1;//空视图
            } else {
                return 0;//无
            }
        } else {
            if (mOnLoadMoreListener != null)
                return size + 1;//加载更多
            else
                return size;//普通
        }
    }

    @Override
    public int getItemViewType(int position) {
        int size = getDataHolder().getList().size();
        if (size == 0) {
            return EMPTY_TYPE;
        } else {
            if (position < size) {
                return NORMAL_TYPE;
            } else {
                return LOAD_MORE_TYPE;
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (recyclerView == mRecyclerView && mOnLoadMoreListener != null) {
            if (mOnScrollListener == null) {
                mOnScrollListener = new RecyclerView.OnScrollListener() {
                    /**
                     * 标记是否正在向上滑动
                     */
                    boolean isSlidingUpward = false;

                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                        if (newState == RecyclerView.SCROLL_STATE_IDLE && manager != null) {
                            //当状态是不滑动的时候
                            int lastItemPosition = 0;
                            int itemCount = manager.getItemCount();
                            if (manager instanceof GridLayoutManager) {
                                lastItemPosition = ((GridLayoutManager) manager).findLastCompletelyVisibleItemPosition();
                            } else if (manager instanceof LinearLayoutManager) {
                                lastItemPosition = ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
                            } else if (manager instanceof StaggeredGridLayoutManager) {
                                int[] lastPositions = new int[((StaggeredGridLayoutManager) manager).getSpanCount()];
                                ((StaggeredGridLayoutManager) manager).findLastVisibleItemPositions(lastPositions);
                                lastItemPosition = findMax(lastPositions);
                            }
                            if (lastItemPosition == (itemCount - 1) && isSlidingUpward) {
                                mOnLoadMoreListener.onLoadMore();
                            }
                        }
                    }

                    private int findMax(int[] lastPositions) {
                        int max = lastPositions[0];
                        for (int value : lastPositions) {
                            if (value > max) {
                                max = value;
                            }
                        }
                        return max;
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
                        isSlidingUpward = dy > 0;
                    }
                };
                mRecyclerView.addOnScrollListener(mOnScrollListener);
            }
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (recyclerView == mRecyclerView && mOnLoadMoreListener != null) {
            mRecyclerView.removeOnScrollListener(mOnScrollListener);
        }
    }

    public interface OnItemClickCallback<DATA> {
        void onItemClick(View view, DATA t, int position);
    }

    public interface OnItemLongClickCallback<DATA> {
        boolean onItemLongClick(View view, DATA t, int position);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }


    public static class BaseDataHolder<DATA> {

        private List<DATA> mList = new ArrayList<>();
        private RecyclerView.Adapter mAdapter;

        public BaseDataHolder(RecyclerView.Adapter adapter) {
            mAdapter = adapter;
        }

        public List<DATA> getList() {
            return mList;
        }

        public BaseDataHolder setList(List<DATA> list) {
            if (list == null) {
                list = new ArrayList<>();
            }
            mList = list;
            return this;
        }

        public BaseDataHolder addAll(List<DATA> list) {
            if (list != null) {
                mList.addAll(list);
            }
            return this;
        }

        public void notifyItemChanged(int position) {
            mAdapter.notifyItemChanged(position);
        }

        public void notifyDataSetChanged() {
            mAdapter.notifyDataSetChanged();
        }
    }

    public static class EmptyViewHolder extends BaseViewHolder {

        public EmptyViewHolder(View view) {
            super(view);
        }
    }

    public static class LoadMoreViewHolder extends BaseViewHolder {

        public LoadMoreViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
        }
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        private BaseViewHolder(View view) {
            super(view);
        }

        public BaseViewHolder(ViewGroup parent, int layoutId) {
            super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        }
    }


}

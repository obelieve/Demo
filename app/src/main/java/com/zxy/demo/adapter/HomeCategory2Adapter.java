package com.zxy.demo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.zxy.demo.R;
import com.zxy.demo.adapter.item_decoration.HorizontalItemDivider;
import com.zxy.demo.utils.GlideUtil;
import com.zxy.demo.view.ShoppingCartView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeCategory2Adapter extends RecyclerView.Adapter {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_home2_type1, parent, false);
                viewHolder = new RecyclerView.ViewHolder(view) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
                break;
            case 1:
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_home1_type3_child, parent, false);
                viewHolder = new HomeCategory1Adapter.Home1Type3ViewHolder.Type3ViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position) {

        return position == 0 ? 0 : 1;
    }
}

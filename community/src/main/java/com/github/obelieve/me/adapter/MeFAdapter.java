package com.github.obelieve.me.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.obelieve.me.bean.MenuDataEntity;
import com.github.obelieve.community.R;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.application.BaseApplication;

import butterknife.BindView;

public class MeFAdapter extends BaseRecyclerViewAdapter<MenuDataEntity> {

    public MeFAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void loadViewHolder(BaseViewHolder holder, int position) {
        holder.bind(getDataHolder().getList().get(position));
    }

    public int getResource(String imageName) {
        Context ctx = BaseApplication.getContext();
        int resId = ctx.getResources().getIdentifier(imageName, "drawable", ctx.getPackageName());
        return resId == 0 ? R.drawable.ic_ss_my_set_up_the : resId;
    }

    class ViewHolder extends BaseViewHolder<MenuDataEntity> {

        @BindView(R.id.iv_left)
        ImageView iv_left;
        @BindView(R.id.tv_item_name)
        TextView tv_item_name;
        @BindView(R.id.tv_item_num)
        TextView tv_item_num;
        @BindView(R.id.rl_father)
        RelativeLayout ll_father;

        public ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_me_fragment);
        }

        @Override
        public void bind(MenuDataEntity menuDataEntity) {
            Drawable drawable = BaseApplication.getContext().getResources().getDrawable(getResource("ic_" + menuDataEntity.getMenu_ico_name()));
            iv_left.setImageDrawable(drawable);
            tv_item_name.setText(menuDataEntity.getMenu_name());
            tv_item_num.setText(menuDataEntity.getMenu_number() + "");
            tv_item_num.setVisibility(menuDataEntity.getMenu_number() == 0 ? View.GONE : View.VISIBLE);
        }
    }
}

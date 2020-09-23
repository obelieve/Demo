package com.github.obelieve.community.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.github.obelieve.community.R;
import com.github.obelieve.community.bean.SquareListsEntity;
import com.github.obelieve.community.ui.view.NineGridView;
import com.zxy.frame.utils.SystemUtil;
import com.zxy.frame.utils.image.GlideRoundTransform;

import java.util.List;

/**
 * @author KCrason
 * @date 2018/4/27
 */
public class NineImageAdapter implements NineGridView.NineGridAdapter<SquareListsEntity.PostListBean.MediaBean.MediaListBean> {

    private List<SquareListsEntity.PostListBean.MediaBean.MediaListBean>mImageBeans;

    private Context mContext;

    int itemSize;

    public NineImageAdapter(Context context,  List<SquareListsEntity.PostListBean.MediaBean.MediaListBean> imageBeans) {
        this.mContext = context;
        this.mImageBeans = imageBeans;
        itemSize = (SystemUtil.screenWidth(context) - 2 * SystemUtil.dp2px(context,4) - SystemUtil.dp2px(context,54)) / 3;

    }


    @Override
    public int getCount() {
        return mImageBeans == null ? 0 : mImageBeans.size();
    }

    @Override
    public SquareListsEntity.PostListBean.MediaBean.MediaListBean getItem(int position) {
        return mImageBeans == null ? null :
                position < mImageBeans.size() ? mImageBeans.get(position) : null;
    }

    @Override
    public View getView(int position, View itemView) {
        ImageView imageView;
        if (itemView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            imageView = (ImageView) itemView;
        }
        SquareListsEntity.PostListBean.MediaBean.MediaListBean mediaListBean = mImageBeans.get(position);
        //todo 头像
        RequestOptions transform = new RequestOptions()
                .transform(new MultiTransformation(new CenterCrop(),  new GlideRoundTransform(mContext,mediaListBean.getThumbnail().contains(".gif"))));
        transform.override(itemSize, itemSize);
        Glide.with(mContext).setDefaultRequestOptions(transform).load(mediaListBean.getThumbnail()).placeholder(R.drawable.failed_to_load_1).error(R.drawable.failed_to_load).into(imageView);
        return imageView;
    }
}

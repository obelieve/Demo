package com.github.obelieve.community.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.github.obelieve.community.R;
import com.github.obelieve.thirdsdklib.ImageSelectorUtil;
import com.zxy.frame.utils.info.SystemInfoUtil;
import com.zxy.frame.utils.image.GlideRoundTransform;

import java.util.ArrayList;

public class IssuePhotoAdapter extends BaseAdapter {

    private ArrayList<String> pathList=new ArrayList<>();
    int itemSize;
    Activity context;
    public IssuePhotoAdapter(Activity context){
        this.context=context;
        itemSize = (SystemInfoUtil.screenWidth(context) - SystemInfoUtil.dp2px(context,34+4)) / 3;


    }

    public ArrayList<String> getPathList() {
        return pathList;
    }

    @Override
    public int getCount() {
        return pathList.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_grid_issue_photo, parent, false);
        inflate.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,itemSize));
        View layoutAddPhoto = inflate.findViewById(R.id.layout_add_photo);
        View layoutPhoto = inflate.findViewById(R.id.layout_photo);
        View image_remove = inflate.findViewById(R.id.image_remove);
        ImageView imagePhoto = inflate.findViewById(R.id.image_photo);
        if(position==pathList.size()){
            layoutAddPhoto.setVisibility(View.VISIBLE);
            layoutAddPhoto.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,itemSize));
            layoutPhoto.setVisibility(View.GONE);
           layoutAddPhoto.setOnClickListener(v -> {
               ImageSelectorUtil.openPhoto(context,1,9,pathList);
           });
        }else{
            layoutAddPhoto.setVisibility(View.GONE);
            layoutPhoto.setVisibility(View.VISIBLE);
            RequestOptions transform = new RequestOptions()
                    .transform(new MultiTransformation(new CenterCrop(), new GlideRoundTransform(context,pathList.get(position).contains(".gif"))));
            transform.override(itemSize, itemSize);
            Glide.with(context).setDefaultRequestOptions(transform).load(pathList.get(position)).into(imagePhoto);
            image_remove.setOnClickListener(v -> {
                pathList.remove(position);
                notifyDataSetChanged();
            });
        }
        return inflate;
    }
}

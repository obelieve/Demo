package com.m7.imkfsdk.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.m7.imkfsdk.R;
import com.moor.imkf.model.entity.Investigate;

import java.util.List;

/**
 * 评价列表adapter
 */
public class InvestigateAdapter extends BaseAdapter {

    private List<Investigate> investigates;

    private Context context;

    public InvestigateAdapter(Context context, List<Investigate> investigates) {
        this.context = context;
        this.investigates = investigates;
    }

    @Override
    public int getCount() {
        return investigates.size();
    }

    @Override
    public Object getItem(int position) {
        return investigates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.kf_investigate_list, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.investigate_list_tv_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(investigates.get(position).name);
        return convertView;
    }

    public static class ViewHolder{
        TextView tv_name;
    }
}

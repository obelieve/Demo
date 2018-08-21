package com.m7.imkfsdk.chat;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.m7.imkfsdk.R;
import com.moor.imkf.IMChatManager;
import com.moor.imkf.SubmitInvestigateListener;
import com.moor.imkf.model.entity.Investigate;

import java.util.ArrayList;
import java.util.List;

/**
 * 评价列表界面
 */
public class InvestigateDialog extends DialogFragment {

    private ListView investigateListView;

    private List<Investigate> investigates = new ArrayList<Investigate>();

    private InvestigateAdapter adapter;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle("提交评价");
        getDialog().setCanceledOnTouchOutside(false);

        // Get the layout inflater
        View view = inflater.inflate(R.layout.kf_dialog_investigate, null);
        investigateListView = (ListView) view.findViewById(R.id.investigate_list);

        investigates = IMChatManager.getInstance().getInvestigate();

        adapter = new InvestigateAdapter(getActivity(), investigates);

        investigateListView.setAdapter(adapter);

        investigateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Investigate investigate = (Investigate) parent.getAdapter().getItem(position);
                IMChatManager.getInstance().submitInvestigate(investigate, new SubmitInvestigateListener() {
                    @Override
                    public void onSuccess() {
//                        Toast.makeText(getActivity(), "评价提交成功", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                    @Override
                    public void onFailed() {
//                        Toast.makeText(getActivity(), "评价提交失败", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void show(android.app.FragmentManager manager, String tag) {
        if(!this.isAdded()) {
            try {
                super.show(manager, tag);
            }catch (Exception e) {}
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        }catch (Exception e) {}

    }

}

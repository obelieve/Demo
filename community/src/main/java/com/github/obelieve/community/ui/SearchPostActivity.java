package com.github.obelieve.community.ui;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.github.obelieve.community.R;
import com.github.obelieve.community.ui.view.FlowLayout;
import com.github.obelieve.community.viewmodel.UpdatesViewModel;
import com.github.obelieve.repository.cache.PreferenceUtil;
import com.github.obelieve.repository.cache.constant.PreferenceConst;
import com.github.obelieve.utils.others.SoftKeyBoardListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zxy.frame.base.ApiBaseActivity;
import com.zxy.frame.dialog.CommonDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 搜索帖子
 */
public class SearchPostActivity extends ApiBaseActivity {


    @BindView(R.id.edit_search_content)
    EditText editSearchContent;
    @BindView(R.id.image_clear)
    ImageView imageClear;
    @BindView(R.id.txt_cancel)
    TextView txtCancel;
    @BindView(R.id.txt_search)
    TextView txtSearch;
    @BindView(R.id.layout_content)
    FrameLayout layoutContent;
    @BindView(R.id.fl_body)
    FlowLayout fl_body;
    @BindView(R.id.ll_body)
    LinearLayout ll_body;
    UpdatesViewModel updatesViewModel;
    List<String> spStringList;
    boolean changeSP = true;
    String mLastSearchContent;

    @Override
    protected int layoutId() {
        return R.layout.activity_search_post;
    }

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        updatesViewModel = ViewModelProviders.of(this).get(UpdatesViewModel.class);
        editSearchContent.requestFocus();
        editSearchContent.post(() -> {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        });
        editSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtSearch.setVisibility(editable.toString().length() > 0 ? View.VISIBLE : View.GONE);
                txtCancel.setVisibility(editable.toString().length() > 0 ? View.GONE : View.VISIBLE);
                if (editable.toString().length() == 0) {
                    searchContent(false);
                }
            }
        });

        String spString = PreferenceUtil.getString(this, PreferenceConst.SP_SEARCH);

        if ("".equals(spString)) {
            spStringList = new ArrayList<>();
        } else {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<String>>() {
            }.getType();
            spStringList = gson.fromJson(spString, listType);
        }

        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                searchContent(false);
                if (changeSP) {
                    createFlowLayout();
                }

            }

            @Override
            public void keyBoardHide(int height) {
                ll_body.setVisibility(View.GONE);
            }
        });
        editSearchContent.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    @OnClick({R.id.txt_search, R.id.image_clear, R.id.txt_cancel})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.txt_search:
                searchContent(true);

                spStringList.remove(editSearchContent.getText().toString());
                spStringList.add(0, editSearchContent.getText().toString());
                if (spStringList.size() > 8) {
                    spStringList.remove(8);
                }
                changeSP = true;
                Gson gson = new Gson();
                String data = gson.toJson(spStringList);
                PreferenceUtil.putString(SearchPostActivity.this, PreferenceConst.SP_SEARCH, data);
                hideInput();
                createFlowLayout();
                break;
            case R.id.image_clear:
                editSearchContent.setText("");
                break;
            case R.id.txt_cancel:
                finish();
                break;
        }
    }

    UpdatesFragment updatesFragment;

    /**
     * 搜索
     */
    private void searchContent(boolean refresh) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        String content = editSearchContent.getText().toString();
        updatesViewModel.setKeyword(content);
        if (content.length() > 0) {
            if (updatesFragment == null) {
                updatesFragment = new UpdatesFragment(2);
                transaction.add(R.id.layout_content, updatesFragment, updatesFragment.getClass().getName());
                transaction.commitAllowingStateLoss();
            } else {
                if (!(mLastSearchContent != null && mLastSearchContent.equals(content))) {
                    updatesFragment.clearData();
                }
                transaction.show(updatesFragment);
                transaction.commitAllowingStateLoss();
                if (refresh) {
                    updatesFragment.startRefresh();
                }
            }

        } else {
            if (updatesFragment != null) {
                transaction.hide(updatesFragment);
                transaction.commitAllowingStateLoss();
            }
            searchHistoryVisibility();
        }
        mLastSearchContent = content;
    }

    /**
     * 无记录时，隐藏搜索历史栏
     */
    private void searchHistoryVisibility() {
        if (spStringList != null) {
            if (spStringList.size() > 0) {
                ll_body.setVisibility(View.VISIBLE);
            } else {
                ll_body.setVisibility(View.GONE);
            }
        }
    }


    public void createFlowLayout() {
        changeSP = false;
        if (fl_body != null) {
            fl_body.removeAllViews();
        }
        for (int i = 0; i < spStringList.size(); i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 0);
            View inflate = LayoutInflater.from(this).inflate(R.layout.item_search_flowlayout, null, false);
            TextView textView = inflate.findViewById(R.id.tv_item_name);
            textView.setText(spStringList.get(i));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editSearchContent.setText(((TextView) view).getText());
                    editSearchContent.setSelection(editSearchContent.getText().length());
                    ll_body.setVisibility(View.GONE);
                    searchContent(true);
                    hideInput();
                }
            });
            fl_body.addView(inflate, layoutParams);
        }
    }

    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @OnClick({R.id.iv_delete})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_delete:
                new CommonDialog(SearchPostActivity.this)
                        .setContent("确定清空搜索历史吗")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", dialog1 -> {
                            dialog1.dismiss();
                            if (fl_body != null) {
                                fl_body.removeAllViews();
                            }
                            spStringList.clear();
                            searchHistoryVisibility();
                            PreferenceUtil.putString(SearchPostActivity.this, PreferenceConst.SP_SEARCH, "");
                        })
                        .show();
                break;
        }
    }

}

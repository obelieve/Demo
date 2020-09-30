package com.github.obelieve.community.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.donkingliang.imageselector.utils.ImageSelector;
import com.github.obelieve.community.R;
import com.github.obelieve.community.adapter.IssuePhotoAdapter;
import com.github.obelieve.community.bean.CommentData;
import com.github.obelieve.community.viewmodel.IssueUpdateViewModel;
import com.github.obelieve.net.ApiServiceWrapper;
import com.github.obelieve.repository.CacheRepository;
import com.github.obelieve.repository.bean.TempEditPostBean;
import com.github.obelieve.repository.cache.constant.SystemValue;
import com.github.obelieve.thirdsdklib.ImageSelectorUtil;
import com.github.obelieve.thirdsdklib.QiNiuUploadUtil;
import com.zxy.frame.base.ApiBaseActivity;
import com.zxy.frame.dialog.CommonDialog;
import com.zxy.frame.utils.log.LogUtil;
import com.zxy.frame.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 发布帖子
 */
public class IssueUpdateActivity extends ApiBaseActivity {

    @BindView(R.id.txt_cancel)
    TextView txtCancel;
    @BindView(R.id.btn_issue)
    Button btnIssue;
    @BindView(R.id.edit_issue_content)
    EditText editIssueContent;
    @BindView(R.id.txt_issue_num)
    TextView txtIssueNum;
    @BindView(R.id.gv_photos)
    GridView gvPhotos;

    IssuePhotoAdapter mIssuePhotoAdapter;
    IssueUpdateViewModel issueUpdateViewModel;

    private CommonDialog mCancelDialog;

    @Override
    protected int layoutId() {
        return R.layout.activity_issue_update;
    }

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        issueUpdateViewModel = ViewModelProviders.of(this).get(IssueUpdateViewModel.class);
        mIssuePhotoAdapter = new IssuePhotoAdapter(this);
        gvPhotos.setAdapter(mIssuePhotoAdapter);

        getTempEdit();
        txtCancel.setOnClickListener(v -> {
            clickBack();
        });
        issueUpdateViewModel.getIsSendstate().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == IssueUpdateViewModel.State.SENDERROR) {
                    dismissLoading();
                } else if (integer == IssueUpdateViewModel.State.SENDSUCCEED) {
                    dismissLoading();
                    ToastUtil.show("发布成功");
                    finish();
                }

            }
        });
        editIssueContent.requestFocus();
        editIssueContent.post(() -> {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        });
        editIssueContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txtIssueNum.setText(s.toString().length() + "/280");
            }
        });
        btnIssue.setOnClickListener(view -> {
            if (editIssueContent.getText().toString().length() == 0) {
                ToastUtil.show("内容不能为空");
                return;
            }
            if (mIssuePhotoAdapter.getPathList().size() == 0) {
                issueUpdateViewModel.sendUpdata(mActivity, editIssueContent.getText().toString(), "", "");
                return;
            }
            showLoading();
            ArrayList<String> pathList = mIssuePhotoAdapter.getPathList();
            List<CommentData> commentDataArrayList = new ArrayList<>();
            if (pathList != null) {
                List<File> files = new ArrayList<>();
                for (int i = 0; i < pathList.size(); i++) {
                    files.add(new File(pathList.get(i)));
                }
                QiNiuUploadUtil.getInstance().upload(files, new QiNiuUploadUtil.Callback() {
                    @Override
                    public void getToken(String token) {
                        SystemValue.uploadToken = token;
                    }

                    @Override
                    public void onSuccess(List<String> urlList) {
                        CommentData commentData;
                        if (urlList.size() == 1) {
                            int[] imageWidthHeight = files.size() > 0 ? getImageWidthHeight(files.get(0).getPath()) : new int[]{0, 0};
                            commentData = new CommentData(imageWidthHeight[0] + "," + imageWidthHeight[1], "image", urlList.get(0));
                            commentDataArrayList.add(commentData);
                        } else {
                            for(int i=0;i<urlList.size();i++){
                                commentDataArrayList.add(new CommentData("", "image", urlList.get(i)));
                            }
                        }
                        if (commentDataArrayList.size() == mIssuePhotoAdapter.getPathList().size()) {
                            StringBuffer stringBuffer = new StringBuffer();
                            for (CommentData commentData1 : commentDataArrayList) {
                                stringBuffer.append(commentData1.getMedia() + ",");
                            }
                            String mediaScale = "";
                            if (commentDataArrayList.size() == 1) {
                                mediaScale = commentDataArrayList.get(0).getMedia_scale();
                            }
                            issueUpdateViewModel.sendUpdata(mActivity, editIssueContent.getText().toString(), stringBuffer.toString(), mediaScale);
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ApiServiceWrapper.errorLog(mActivity, "上传失败 QiNiu Upload Fail:" + msg);
                                LogUtil.e("qiniu", "上传失败 Upload Fail:" + msg);
                                ToastUtil.show("上传失败");
                                dismissLoading();
                            }
                        });
                    }
                }, mActivity);
            }
        });
    }

    @Override
    public void onBackPressed() {
        clickBack();
    }


    private void getTempEdit() {
        TempEditPostBean bean = CacheRepository.getInstance().getTempEditPostBean();
        if (bean != null) {
            editIssueContent.setText(bean.getContent());
            mIssuePhotoAdapter.getPathList().addAll(bean.getPaths());
            mIssuePhotoAdapter.notifyDataSetChanged();
            CacheRepository.getInstance().clearTempEditPostBean();
        }
    }

    private void setTempEdit() {
        String content = editIssueContent.getText().toString();
        List<String> paths = mIssuePhotoAdapter.getPathList();
        TempEditPostBean bean = new TempEditPostBean();
        bean.setContent(content);
        bean.setPaths(paths);
        CacheRepository.getInstance().saveTempEditPostBean(bean);
    }

    private void clickBack() {
        if (editIssueContent.getText().toString().length() > 0 || mIssuePhotoAdapter.getPathList().size() > 0) {
            if (mCancelDialog == null) {
                mCancelDialog = new CommonDialog(this);
                mCancelDialog.setContent("是否保存编辑？");
                mCancelDialog.setNegativeButton("不保存", new CommonDialog.onCancelListener() {
                    @Override
                    public void onClickCancel(Dialog dialog) {
                        dialog.dismiss();
                        finish();
                    }
                });
                mCancelDialog.setPositiveButton("保存", new CommonDialog.onSubmitListener() {
                    @Override
                    public void onClickSubmit(Dialog dialog) {
                        setTempEdit();
                        dialog.dismiss();
                        finish();
                    }
                });
            }
            mCancelDialog.show();
        } else {
            finish();
        }
    }

    public static int[] getImageWidthHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return new int[]{options.outWidth, options.outHeight};
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            //获取选择器返回的数据
            ArrayList<String> images = data.getStringArrayListExtra(
                    ImageSelectorUtil.SELECT_RESULT);
            //是否为拍照，拍照则只返回单张，
            boolean isCameraImage = data.getBooleanExtra(ImageSelector.IS_CAMERA_IMAGE, false);
            if (!isCameraImage) {
                mIssuePhotoAdapter.getPathList().clear();
            }
            mIssuePhotoAdapter.getPathList().addAll(images);
            mIssuePhotoAdapter.notifyDataSetChanged();
        }
    }
}

package com.zxy.demo._issue;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.NoCopySpan;
import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zxy.demo.R;
import com.zxy.frame.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.Unbinder;

/**
 * @其他人 使用例子
 */
public class ForTAFragment extends BaseFragment {
    @BindView(R.id.btn)
    Button mBtn;
    @BindView(R.id.et)
    EditText mEt;

    EditTextForATHelper<ForegroundColorSpan> mEditTextForATHelper;

    @Override
    public int layoutId() {
        return R.layout.fragment_for_ta;
    }

    @Override
    protected void initView() {
        mEditTextForATHelper = new EditTextForATHelper<>(mEt, ForegroundColorSpan.class);
        mEditTextForATHelper.setCallback(new EditTextForATHelper.Callback<ForegroundColorSpan>() {
            @Override
            public void onDeletedSpan(ForegroundColorSpan span) {
                Toast.makeText(getContext(), "delete " + span, Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnLongClick(R.id.btn)
    public void onViewLongClicked() {
        Toast.makeText(getContext(), mEt.getText().toString(), Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btn)
    public void onViewClicked() {
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#999999"));
        mEditTextForATHelper.insertSpan(colorSpan, "@【123】");
//        mEditTextForATHelper.insertSpan(new A(), "@【AAA】");
    }

//    public static class A {
//
//        private static int a = 0;
//        private final int b = a++;
//
//        @NonNull
//        @Override
//        public String toString() {
//            return "b:" + b;
//        }
//    }

    public static class EditTextForATHelper<T> {

        private Class<T> mSpanClass;
        private EditText mEditText;
        private Callback<T> mCallback;

        public EditTextForATHelper(EditText editText, Class<T> spanClass) {
            mEditText = editText;
            mSpanClass = spanClass;
            mEditText.setEditableFactory(new NoCopySpanEditableFactory(new SelectionSpanWatcher<>(mSpanClass)));
            mEditText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    return onKeyDownDel(mEditText.getText(), keyCode, event.getAction());
                }
            });
        }

        public void setCallback(Callback<T> callback) {
            mCallback = callback;
        }

        public <T> void insertSpan(T span, String string) {
            Editable editable = mEditText.getText();
            editable.insert(Selection.getSelectionEnd(editable), string);
            editable.setSpan(span,
                    Selection.getSelectionEnd(editable) - string.length(),
                    Selection.getSelectionEnd(editable), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        private boolean onKeyDownDel(Editable editable, int keyCode, int action) {
            if (keyCode == KeyEvent.KEYCODE_DEL
                    && action == KeyEvent.ACTION_DOWN) {
                int start = Selection.getSelectionStart(editable);
                int end = Selection.getSelectionEnd(editable);
                T[] spans = editable.getSpans(start, end, mSpanClass);
                for (T span : spans) {
                    int startS = editable.getSpanStart(span);
                    int endE = editable.getSpanEnd(span);
                    if (end == endE) {
                        Selection.setSelection(editable, startS, endE);
                        if (mCallback != null) {
                            mCallback.onDeletedSpan(span);
                        }
                    }
                }
            }
            return false;
        }

        public interface Callback<T> {
            void onDeletedSpan(T span);
        }


        private static class NoCopySpanEditableFactory extends Editable.Factory {

            private NoCopySpan spans;

            public NoCopySpanEditableFactory(NoCopySpan spans) {
                this.spans = spans;
            }

            @Override
            public Editable newEditable(CharSequence source) {
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder(source);
                stringBuilder.setSpan(spans, 0, source.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                return stringBuilder;
            }
        }

        private static class SelectionSpanWatcher<T> implements SpanWatcher {

            private int selStart = 0;
            private int selEnd = 0;
            private Class<T> mSpanClass;

            public SelectionSpanWatcher(Class<T> spanClass) {
                mSpanClass = spanClass;
            }

            @Override
            public void onSpanAdded(Spannable text, Object what, int start, int end) {

            }

            @Override
            public void onSpanRemoved(Spannable text, Object what, int start, int end) {

            }

            @Override
            public void onSpanChanged(Spannable text, Object what, int ostart, int oend, int nstart, int nend) {
                if (what == Selection.SELECTION_END && selEnd != nstart) {
                    selEnd = nstart;
                    T[] spans = text.getSpans(nstart, nend, mSpanClass);
                    if (spans != null && spans.length > 0) {
                        T span = spans[0];
                        int spanStart = text.getSpanStart(span);
                        int spanEnd = text.getSpanEnd(span);
                        int index;
                        if (Math.abs(selEnd - spanEnd) > Math.abs(selEnd - spanStart))
                            index = spanStart;
                        else
                            index = spanEnd;
                        Selection.setSelection(text, Selection.getSelectionStart(text), index);
                    }
                }

                if (what == Selection.SELECTION_START && selStart != nstart) {
                    selStart = nstart;
                    T[] spans = text.getSpans(nstart, nend, mSpanClass);
                    if (spans != null && spans.length > 0) {
                        T span = spans[0];
                        int spanStart = text.getSpanStart(span);
                        int spanEnd = text.getSpanEnd(span);
                        int index = 0;
                        if (Math.abs(selStart - spanEnd) > Math.abs(selStart - spanStart))
                            index = spanStart;
                        else index = spanEnd;
                        Selection.setSelection(text, index, Selection.getSelectionEnd(text));
                    }

                }
            }
        }
    }
}

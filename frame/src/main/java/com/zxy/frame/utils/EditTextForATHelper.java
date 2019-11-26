package com.zxy.frame.utils;

import android.text.Editable;
import android.text.NoCopySpan;
import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

/**
 * 这个类用于设置EditText “@其他人” 这种标签式插入和删除操作。
 * 用法：
 * 1.初始化对象
 * 2.调用EditTextForATHelper#insertSpan(T,String) 插入标签
 * * #setCallback(Callback<T>) 删除标签时，回调方法
 *
 * @param <T>
 */
public class EditTextForATHelper<T> {

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

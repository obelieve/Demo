package com.github.obelieve.repository.bean;

import java.util.List;

public class TempEditPostBean {

    private String mContent;
    private List<String> mPaths;

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public List<String> getPaths() {
        return mPaths;
    }

    public void setPaths(List<String> paths) {
        mPaths = paths;
    }

    @Override
    public String toString() {
        return "TempEditPostBean{" +
                "mContent='" + mContent + '\'' +
                ", mPaths=" + mPaths +
                '}';
    }
}

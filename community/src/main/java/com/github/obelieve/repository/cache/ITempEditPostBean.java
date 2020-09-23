package com.github.obelieve.repository.cache;

import com.github.obelieve.repository.bean.TempEditPostBean;

/**
 * Created by Admin
 * on 2020/9/3
 */
public interface ITempEditPostBean {
    TempEditPostBean getTempEditPostBean();
    void saveTempEditPostBean(TempEditPostBean bean);
    void clearTempEditPostBean();
}

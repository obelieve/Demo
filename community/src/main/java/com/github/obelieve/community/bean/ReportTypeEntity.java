package com.github.obelieve.community.bean;

import java.util.List;

/**
 * 举报类型
 */
public class ReportTypeEntity {

    private List<ReportTypeBean> report_type;

    public List<ReportTypeBean> getReport_type() {
        return report_type;
    }

    public void setReport_type(List<ReportTypeBean> report_type) {
        this.report_type = report_type;
    }

    public static class ReportTypeBean {
        /**
         * rt_id : 4
         * rt_name : 广告
         */

        private int rt_id;
        private String rt_name;

        public int getRt_id() {
            return rt_id;
        }

        public void setRt_id(int rt_id) {
            this.rt_id = rt_id;
        }

        public String getRt_name() {
            return rt_name;
        }

        public void setRt_name(String rt_name) {
            this.rt_name = rt_name;
        }
    }
}

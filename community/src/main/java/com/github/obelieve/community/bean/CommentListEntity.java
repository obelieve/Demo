package com.github.obelieve.community.bean;

import java.util.List;

public class CommentListEntity {


    /**
     * current_page : 1
     * data : [{"user_id":122,"nickname":"取个名字1","avatar":"https://image.209291305","comment_id":3,"comment_time":"15小时前","content":"曼联无敌了","reply_num":5,"zan_num":0,"post_id":5,"is_zan":0,"is_del":1,"is_report":1,"reply_list":[{"user_id":1,"nickname":"罗纳多多","avatar":"https://image.1565010581","reply_id":8,"reply_time":"2019-10-09 18:22:39","content":"你过来呀?","post_id":5,"to_uid":122,"to_uname":"取个名字1"}]}]
     * from : 1
     * last_page : 1
     * per_page : 10
     * to : 2
     * total : 2
     */

    private int current_page;
    private int from;
    private int last_page;
    private int per_page;
    private int to;
    private int total;
    private List<DataBean> data;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * user_id : 122
         * nickname : 取个名字1
         * avatar : https://image.209291305
         * comment_id : 3
         * comment_time : 15小时前
         * content : 曼联无敌了
         * reply_num : 5
         * zan_num : 0
         * post_id : 5
         * is_zan : 0
         * is_del : 1
         * is_report : 1
         * reply_list : [{"user_id":1,"nickname":"罗纳多多","avatar":"https://image.1565010581","reply_id":8,"reply_time":"2019-10-09 18:22:39","content":"你过来呀?","post_id":5,"to_uid":122,"to_uname":"取个名字1"}]
         */

        private int user_id;
        private int user_level;
        private String nickname;
        private String avatar;
        private int comment_id;
        private String comment_time;
        private String content;
        private int reply_num;
        private int zan_num;
        private int post_id;
        private int is_zan;
        private int is_del;
        private int is_report;
        private List<ReplyListBean> reply_list;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getUser_level() {
            return user_level;
        }

        public void setUser_level(int user_level) {
            this.user_level = user_level;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getComment_id() {
            return comment_id;
        }

        public void setComment_id(int comment_id) {
            this.comment_id = comment_id;
        }

        public String getComment_time() {
            return comment_time;
        }

        public void setComment_time(String comment_time) {
            this.comment_time = comment_time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getReply_num() {
            return reply_num;
        }

        public void setReply_num(int reply_num) {
            this.reply_num = reply_num;
        }

        public int getZan_num() {
            return zan_num;
        }

        public void setZan_num(int zan_num) {
            this.zan_num = zan_num;
        }

        public int getPost_id() {
            return post_id;
        }

        public void setPost_id(int post_id) {
            this.post_id = post_id;
        }

        public int getIs_zan() {
            return is_zan;
        }

        public void setIs_zan(int is_zan) {
            this.is_zan = is_zan;
        }

        public int getIs_del() {
            return is_del;
        }

        public void setIs_del(int is_del) {
            this.is_del = is_del;
        }

        public int getIs_report() {
            return is_report;
        }

        public void setIs_report(int is_report) {
            this.is_report = is_report;
        }

        public List<ReplyListBean> getReply_list() {
            return reply_list;
        }

        public void setReply_list(List<ReplyListBean> reply_list) {
            this.reply_list = reply_list;
        }

        public static class ReplyListBean {
            /**
             * user_id : 1
             * nickname : 罗纳多多
             * avatar : https://image.1565010581
             * reply_id : 8
             * reply_time : 2019-10-09 18:22:39
             * content : 你过来呀?
             * post_id : 5
             * to_uid : 122
             * to_uname : 取个名字1
             */

            private int user_id;
            private String nickname;
            private String avatar;
            private int reply_id;
            private String reply_time;
            private String content;
            private int post_id;
            private int to_uid;
            private String to_uname;
            private int is_del;
            private int is_report;

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getReply_id() {
                return reply_id;
            }

            public void setReply_id(int reply_id) {
                this.reply_id = reply_id;
            }

            public String getReply_time() {
                return reply_time;
            }

            public void setReply_time(String reply_time) {
                this.reply_time = reply_time;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getPost_id() {
                return post_id;
            }

            public void setPost_id(int post_id) {
                this.post_id = post_id;
            }

            public int getTo_uid() {
                return to_uid;
            }

            public void setTo_uid(int to_uid) {
                this.to_uid = to_uid;
            }

            public String getTo_uname() {
                return to_uname;
            }

            public void setTo_uname(String to_uname) {
                this.to_uname = to_uname;
            }

            public int getIs_del() {
                return is_del;
            }

            public void setIs_del(int is_del) {
                this.is_del = is_del;
            }

            public int getIs_report() {
                return is_report;
            }

            public void setIs_report(int is_report) {
                this.is_report = is_report;
            }
        }
    }
}

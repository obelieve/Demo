package com.zxy.im.model;

/**
 * Created by zxy on 2018/12/14 11:05.
 */

public class LoginModel extends BaseModel
{

    /**
     * data : {"id":3,"mobile":"13799362685","mobile_country_code":"0086","country":null,"email":null,"avatar":"http://www.imweb.co/static/asset/noavatar.gif","sex":null,"nickname":"杨志高","first_words":null,"autograph":null,"is_certified":0,"realname":null,"idcard_type":0,"idcard":null,"invitation_code":null,"inviter_id":null,"inviter_username":null,"pid":null,"pid2":null,"pid3":null,"status":1,"last_login_ip":"127.0.0.1","last_login_time":"2018-12-07 10:44:37","is_online":1,"im_token":"U/I1Z7PuxMofGUvqpguwCT89hY1lmdiGk5tHCn8oh9xYmj7FI1G7x3sO4N0NfqiXohd0X3rrYaSGby7bqfO+dA==","sdk_type":null,"truename":null,"created_at":"2018-12-07 10:20:46","updated_at":"2018-12-07 10:44:37","deleted_at":"2018-12-07 10:36:25","_token":"U_851b4fe45a2a19e45872fa536cb88fdb5c09de95ac176","IM_APP_KEY":"---------"}
     */

    private DataBean data;

    public DataBean getData()
    {
        return data;
    }

    public void setData(DataBean data)
    {
        this.data = data;
    }

    public static class DataBean
    {
        /**
         * id : 3
         * mobile : 13799362685
         * mobile_country_code : 0086
         * country : null
         * email : null
         * avatar : http://www.imweb.co/static/asset/noavatar.gif
         * sex : null
         * nickname : 杨志高
         * first_words : null
         * autograph : null
         * is_certified : 0
         * realname : null
         * idcard_type : 0
         * idcard : null
         * invitation_code : null
         * inviter_id : null
         * inviter_username : null
         * pid : null
         * pid2 : null
         * pid3 : null
         * status : 1
         * last_login_ip : 127.0.0.1
         * last_login_time : 2018-12-07 10:44:37
         * is_online : 1
         * im_token : U/I1Z7PuxMofGUvqpguwCT89hY1lmdiGk5tHCn8oh9xYmj7FI1G7x3sO4N0NfqiXohd0X3rrYaSGby7bqfO+dA==
         * sdk_type : null
         * truename : null
         * created_at : 2018-12-07 10:20:46
         * updated_at : 2018-12-07 10:44:37
         * deleted_at : 2018-12-07 10:36:25
         * _token : U_851b4fe45a2a19e45872fa536cb88fdb5c09de95ac176
         * IM_APP_KEY : ---------
         */

        private int id;
        private String mobile;
        private String mobile_country_code;
        private Object country;
        private Object email;
        private String avatar;
        private Object sex;
        private String nickname;
        private Object first_words;
        private Object autograph;
        private int is_certified;
        private Object realname;
        private int idcard_type;
        private Object idcard;
        private Object invitation_code;
        private Object inviter_id;
        private Object inviter_username;
        private Object pid;
        private Object pid2;
        private Object pid3;
        private int status;
        private String last_login_ip;
        private String last_login_time;
        private int is_online;
        private String im_token;
        private Object sdk_type;
        private Object truename;
        private String created_at;
        private String updated_at;
        private String deleted_at;
        private String _token;
        private String IM_APP_KEY;

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public String getMobile()
        {
            return mobile;
        }

        public void setMobile(String mobile)
        {
            this.mobile = mobile;
        }

        public String getMobile_country_code()
        {
            return mobile_country_code;
        }

        public void setMobile_country_code(String mobile_country_code)
        {
            this.mobile_country_code = mobile_country_code;
        }

        public Object getCountry()
        {
            return country;
        }

        public void setCountry(Object country)
        {
            this.country = country;
        }

        public Object getEmail()
        {
            return email;
        }

        public void setEmail(Object email)
        {
            this.email = email;
        }

        public String getAvatar()
        {
            return avatar;
        }

        public void setAvatar(String avatar)
        {
            this.avatar = avatar;
        }

        public Object getSex()
        {
            return sex;
        }

        public void setSex(Object sex)
        {
            this.sex = sex;
        }

        public String getNickname()
        {
            return nickname;
        }

        public void setNickname(String nickname)
        {
            this.nickname = nickname;
        }

        public Object getFirst_words()
        {
            return first_words;
        }

        public void setFirst_words(Object first_words)
        {
            this.first_words = first_words;
        }

        public Object getAutograph()
        {
            return autograph;
        }

        public void setAutograph(Object autograph)
        {
            this.autograph = autograph;
        }

        public int getIs_certified()
        {
            return is_certified;
        }

        public void setIs_certified(int is_certified)
        {
            this.is_certified = is_certified;
        }

        public Object getRealname()
        {
            return realname;
        }

        public void setRealname(Object realname)
        {
            this.realname = realname;
        }

        public int getIdcard_type()
        {
            return idcard_type;
        }

        public void setIdcard_type(int idcard_type)
        {
            this.idcard_type = idcard_type;
        }

        public Object getIdcard()
        {
            return idcard;
        }

        public void setIdcard(Object idcard)
        {
            this.idcard = idcard;
        }

        public Object getInvitation_code()
        {
            return invitation_code;
        }

        public void setInvitation_code(Object invitation_code)
        {
            this.invitation_code = invitation_code;
        }

        public Object getInviter_id()
        {
            return inviter_id;
        }

        public void setInviter_id(Object inviter_id)
        {
            this.inviter_id = inviter_id;
        }

        public Object getInviter_username()
        {
            return inviter_username;
        }

        public void setInviter_username(Object inviter_username)
        {
            this.inviter_username = inviter_username;
        }

        public Object getPid()
        {
            return pid;
        }

        public void setPid(Object pid)
        {
            this.pid = pid;
        }

        public Object getPid2()
        {
            return pid2;
        }

        public void setPid2(Object pid2)
        {
            this.pid2 = pid2;
        }

        public Object getPid3()
        {
            return pid3;
        }

        public void setPid3(Object pid3)
        {
            this.pid3 = pid3;
        }

        public int getStatus()
        {
            return status;
        }

        public void setStatus(int status)
        {
            this.status = status;
        }

        public String getLast_login_ip()
        {
            return last_login_ip;
        }

        public void setLast_login_ip(String last_login_ip)
        {
            this.last_login_ip = last_login_ip;
        }

        public String getLast_login_time()
        {
            return last_login_time;
        }

        public void setLast_login_time(String last_login_time)
        {
            this.last_login_time = last_login_time;
        }

        public int getIs_online()
        {
            return is_online;
        }

        public void setIs_online(int is_online)
        {
            this.is_online = is_online;
        }

        public String getIm_token()
        {
            return im_token;
        }

        public void setIm_token(String im_token)
        {
            this.im_token = im_token;
        }

        public Object getSdk_type()
        {
            return sdk_type;
        }

        public void setSdk_type(Object sdk_type)
        {
            this.sdk_type = sdk_type;
        }

        public Object getTruename()
        {
            return truename;
        }

        public void setTruename(Object truename)
        {
            this.truename = truename;
        }

        public String getCreated_at()
        {
            return created_at;
        }

        public void setCreated_at(String created_at)
        {
            this.created_at = created_at;
        }

        public String getUpdated_at()
        {
            return updated_at;
        }

        public void setUpdated_at(String updated_at)
        {
            this.updated_at = updated_at;
        }

        public String getDeleted_at()
        {
            return deleted_at;
        }

        public void setDeleted_at(String deleted_at)
        {
            this.deleted_at = deleted_at;
        }

        public String get_token()
        {
            return _token;
        }

        public void set_token(String _token)
        {
            this._token = _token;
        }

        public String getIM_APP_KEY()
        {
            return IM_APP_KEY;
        }

        public void setIM_APP_KEY(String IM_APP_KEY)
        {
            this.IM_APP_KEY = IM_APP_KEY;
        }
    }
}

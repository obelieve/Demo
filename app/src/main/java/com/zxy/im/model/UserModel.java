package com.zxy.im.model;

import android.net.Uri;

import io.rong.imlib.model.UserInfo;

public class UserModel
{
    private String id;
    private String mobile;
    private String avatar;
    private String nickname;
    private String im_token;
    private String _token;

    public UserInfo newUserInfo()
    {
        return new UserInfo(id, nickname, getAvatarUri());
    }

    public Uri getAvatarUri()
    {
        try
        {
            return Uri.parse(avatar);
        } catch (Exception e)
        {
            return null;
        }
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
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

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getIm_token()
    {
        return im_token;
    }

    public void setIm_token(String im_token)
    {
        this.im_token = im_token;
    }

    public String get_token()
    {
        return _token;
    }

    public void set_token(String _token)
    {
        this._token = _token;
    }
}

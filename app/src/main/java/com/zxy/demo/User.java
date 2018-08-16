package com.zxy.demo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

/**
 * Created by zxy on 2018/8/15 10:10.
 */

public class User extends BaseObservable
{
    public   String firstName;
    public String lastName;
    public ObservableBoolean bool = new ObservableBoolean();

    public User(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Bindable
    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }
    @Bindable
    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }

    public ObservableBoolean getBool()
    {
        return bool;
    }

    public void setBool(ObservableBoolean bool)
    {
        this.bool = bool;
    }

}

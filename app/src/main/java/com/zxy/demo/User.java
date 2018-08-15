package com.zxy.demo;

/**
 * Created by zxy on 2018/8/15 10:10.
 */

public class User
{
    private String firstName;
    private String lastName;

    public User(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

}

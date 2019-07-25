package com.ainirobot.sdk_demo.model.bean;

import java.io.Serializable;

/**
 * @author Orion
 * @time 2019/3/28
 */
public class PlaceEntity implements Serializable {
    private static final long serialVersionUID = -2060776378404117415L;


    /**
     * name : 接待点
     * postype : 0
     * theta : -1.8217154
     * x : 5.0079327
     * y : 3.0787046
     * messageType : 11
     * time : 1553649193187
     */

    private String name;
    private int postype;
    private double theta;
    private double x;
    private double y;
    private int messageType;
    private long time;

    public PlaceEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPostype() {
        return postype;
    }

    public void setPostype(int postype) {
        this.postype = postype;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

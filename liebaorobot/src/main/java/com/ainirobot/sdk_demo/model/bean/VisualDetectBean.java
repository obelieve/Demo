package com.ainirobot.sdk_demo.model.bean;

public class VisualDetectBean {

    private boolean isNeedRecognize = true;
    private boolean isNeedTrack = true;
    private boolean isNeedPreWakeUp = true;
    private boolean isNeedAssignedPersonLostTimer = false;
    private long assignedPersonLostTimer = 7000;

    public void setNeedRecognize(boolean needRecognize) {
        isNeedRecognize = needRecognize;
    }

    /**
     * 是否需要远程识别
     * @return true 是
     * false 否
     */
    public boolean isNeedRecognize() {
        return isNeedRecognize;
    }

    public void setNeedTrack(boolean needTrack) {
        isNeedTrack = needTrack;
    }

    public boolean isNeedTrack() {
        return isNeedTrack;
    }

    public void setNeedPreWakeUp(boolean needPreWakeUp) {
        isNeedPreWakeUp = needPreWakeUp;
    }

    public boolean isNeedPreWakeUp() {
        return isNeedPreWakeUp;
    }

    public void setNeedAssignedPersonLostTimer(boolean needAssignedPersonLostTimer) {
        isNeedAssignedPersonLostTimer = needAssignedPersonLostTimer;
    }

    public boolean isNeedAssignedPersonLostTimer() {
        return isNeedAssignedPersonLostTimer;
    }

    public long getAssignedPersonLostTimer() {
        return assignedPersonLostTimer;
    }
}

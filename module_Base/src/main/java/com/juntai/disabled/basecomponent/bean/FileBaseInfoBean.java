package com.juntai.disabled.basecomponent.bean;

/**
 * @Author: tobato
 * @Description: 作用描述   文件的基础信息
 * @CreateDate: 2021-12-03 16:30
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-12-03 16:30
 */
public class FileBaseInfoBean {
    //旋转角度
    private  String   rotation;

    //视频文件总时长
    private  String  duration;

    public FileBaseInfoBean(String rotation, String duration) {
        this.rotation = rotation;
        this.duration = duration;
    }

    public String getRotation() {
        return rotation == null ? "" : rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation == null ? "" : rotation;
    }

    public String getDuration() {
        return duration == null ? "" : String.format("%02d:%02d", Integer.parseInt(duration)/1000 / 60, (Integer.parseInt(duration)/1000 ) % 60);
    }

    public void setDuration(String duration) {
        this.duration = duration == null ? "" : duration;
    }
}

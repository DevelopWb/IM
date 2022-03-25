package com.juntai.wisdom.im.bean;

import android.text.TextUtils;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021/4/21 9:35
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/21 9:35
 */
public class TextKeyValueBean {

    private  String key;
    private  String value;
    private String hint;
    private int type;//0代表高度固定的edittext  1代表高度不固定的edittext
    private boolean isTurnOn;//是否打开
    private boolean valueGravityToRight;//value靠右
    //行政区域码  地址有关
    private String adcode;
    private String level;
    public TextKeyValueBean(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public TextKeyValueBean(String key, String value, String adcode, String level) {
        this.key = key;
        this.value = value;
        this.adcode = adcode;
        this.level = level;
    }

    public String getLevel() {
        return level == null ? "" : level;
    }

    public void setLevel(String level) {
        this.level = level == null ? "" : level;
    }

    public TextKeyValueBean(String key, String value, boolean isTurnOn) {
        this.key = key;
        this.value = value;
        this.isTurnOn = isTurnOn;
    }

    public boolean isValueGravityToRight() {
        return valueGravityToRight;
    }

    public void setValueGravityToRight(boolean valueGravityToRight) {
        this.valueGravityToRight = valueGravityToRight;
    }

    public String getAdcode() {
        return adcode == null ? "" : adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode == null ? "" : adcode;
    }

    public String getHint() {
        return TextUtils.isEmpty(hint) ? "暂无" : hint;
    }

    public void setHint(String hint) {
        this.hint = hint == null ? "" : hint;
    }

    public boolean isTurnOn() {
        return isTurnOn;
    }

    public void setTurnOn(boolean turnOn) {
        isTurnOn = turnOn;
    }

    public String getKey() {
        return TextUtils.isEmpty(key) ? "暂无" : key;
    }

    public void setKey(String key) {
        this.key = key == null ? "" : key;
    }

    public String getValue() {
        return TextUtils.isEmpty(value) ? "" : value;
    }

    public void setValue(String value) {
        this.value = value == null ? "" : value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

package com.negier.emojifragment.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/29 0029.
 */

public class Emoji implements Serializable {
    private String name;
    private int imageUri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageUri() {
        return imageUri;
    }

    public void setImageUri(int imageUri) {
        this.imageUri = imageUri;
    }
}

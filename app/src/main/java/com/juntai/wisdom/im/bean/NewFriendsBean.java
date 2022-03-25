package com.juntai.wisdom.im.bean;

import com.juntai.disabled.basecomponent.base.BaseResult;

import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述  新朋友
 * @CreateDate: 2021-11-05 11:11
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-11-05 11:11
 */
public class NewFriendsBean extends BaseResult {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * userId : 101
         * nickname : 铁人王进喜
         * headPortrait : https://www.juntaikeji.com:17002/head_img/8150129b631a4cc89702083d0ddb54a1.jpeg
         * intro : null
         * state : 1
         */

        private Integer id;//用户id

        private String nickname;//昵称

        private String headPortrait;//头像

        private String intro;//添加简介

        private Integer state;//1未读，2已读，3拒绝，4接受

        private String createTime;//申请时间

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname == null ? "" : nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname == null ? "" : nickname;
        }

        public String getHeadPortrait() {
            return headPortrait == null ? "" : headPortrait;
        }

        public void setHeadPortrait(String headPortrait) {
            this.headPortrait = headPortrait == null ? "" : headPortrait;
        }

        public String getIntro() {
            return intro == null ? "" : intro;
        }

        public void setIntro(String intro) {
            this.intro = intro == null ? "" : intro;
        }

        public Integer getState() {
            return state;
        }

        public void setState(Integer state) {
            this.state = state;
        }

        public String getCreateTime() {
            return createTime == null ? "" : createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime == null ? "" : createTime;
        }
    }
}

package com.juntai.wisdom.im.bean;

import com.juntai.disabled.basecomponent.base.BaseResult;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-01-18 15:21
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-01-18 15:21
 */
public class GroupInfoByUuidBean extends BaseResult {


    /**
     * data : {"id":4,"uuid":"77c48017ebb7437e977cc8eea9226022","groupName":"哈哈哈","groupPicture":"/group/groupPicture.jpg","userId":5,"userName":"铁人王进喜","qrCode":"/group/77c48017ebb7437e977cc8eea9226022/2021-11-19/74784a061cc54d609c52c2049bdcf2db.jpeg","createTime":"2021-11-19 09:44:51","statusType":0}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 4
         * uuid : 77c48017ebb7437e977cc8eea9226022
         * groupName : 哈哈哈
         * groupPicture : /group/groupPicture.jpg
         * userId : 5
         * userName : 铁人王进喜
         * qrCode : /group/77c48017ebb7437e977cc8eea9226022/2021-11-19/74784a061cc54d609c52c2049bdcf2db.jpeg
         * createTime : 2021-11-19 09:44:51
         * statusType : 0
         */

        private int id;
        private String uuid;
        private String groupName;
        private String groupPicture;
        private int userId;
        private String userName;
        private String qrCode;
        private String createTime;
        private int statusType;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getGroupPicture() {
            return groupPicture;
        }

        public void setGroupPicture(String groupPicture) {
            this.groupPicture = groupPicture;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getQrCode() {
            return qrCode;
        }

        public void setQrCode(String qrCode) {
            this.qrCode = qrCode;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getStatusType() {
            return statusType;
        }

        public void setStatusType(int statusType) {
            this.statusType = statusType;
        }
    }
}

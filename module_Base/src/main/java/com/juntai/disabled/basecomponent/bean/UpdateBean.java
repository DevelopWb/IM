package com.juntai.disabled.basecomponent.bean;

import com.juntai.disabled.basecomponent.base.BaseResult;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2020/4/10 16:51
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/4/10 16:51
 */
public class UpdateBean extends BaseResult {


    /**
     * error : null
     * returnValue : null
     * msg : null
     * code : null
     * data : {"id":2,"fileName":"客户管理.apk","versionsCode":2,"versionsName":"v1.1","updateContent":"新的重大更新","downloadLink":"http://61.156.157.132:32280/downloadLocal/客户管理.apk","constraintUpdate":false,"gmtCreate":"2020-04-10 10:08:53"}
     * type : null
     * message : null
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
         * id : 2
         * fileName : 客户管理.apk
         * versionsCode : 2
         * versionsName : v1.1
         * updateContent : 新的重大更新
         * downloadLink : http://61.156.157.132:32280/downloadLocal/客户管理.apk
         * constraintUpdate : false
         * gmtCreate : 2020-04-10 10:08:53
         */

        private int id;
        private String fileName;
        private int versionsCode;
        private String versionsName;
        private String updateContent;
        private String downloadLink;
        private boolean constraintUpdate;
        private String gmtCreate;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFileName() {
            return fileName == null? "" : fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public int getVersionsCode() {
            return versionsCode;
        }

        public void setVersionsCode(int versionsCode) {
            this.versionsCode = versionsCode;
        }

        public String getVersionsName() {
            return versionsName == null? "" : versionsName;
        }

        public void setVersionsName(String versionsName) {
            this.versionsName = versionsName;
        }

        public String getUpdateContent() {
            return updateContent == null? "" : updateContent;
        }

        public void setUpdateContent(String updateContent) {
            this.updateContent = updateContent;
        }

        public String getDownloadLink() {
            return downloadLink == null? "" : downloadLink;
        }

        public void setDownloadLink(String downloadLink) {
            this.downloadLink = downloadLink;
        }

        public boolean isConstraintUpdate() {
            return constraintUpdate;
        }

        public void setConstraintUpdate(boolean constraintUpdate) {
            this.constraintUpdate = constraintUpdate;
        }

        public String getGmtCreate() {
            return gmtCreate == null? "" : gmtCreate;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }
    }
}

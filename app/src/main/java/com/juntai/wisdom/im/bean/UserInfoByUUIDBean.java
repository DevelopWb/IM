package com.juntai.wisdom.im.bean;

import com.juntai.disabled.basecomponent.base.BaseResult;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-01-18 15:16
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-01-18 15:16
 */
public class UserInfoByUUIDBean extends BaseResult {


    /**
     * data : {"id":5,"uuid":"6d9b0683413948d89ad2693e1d121a19","accountNumber":"csj_btv93lg8iv2t5g","accountModifyTime":null,"phoneNumber":"18669505929","nickname":"铁人王进喜","headPortrait":"/csj_btv93lg8iv2t5g/2021-11-20/e0e1d1d72489438b83e233f7d8e8bde7.png","gender":2,"qrCode":"/csj_btv93lg8iv2t5g/2021-11-17/deed0a079acd4ba69569a817d796d9eb.jpeg","address":"山东临沂"}
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
         * id : 5
         * uuid : 6d9b0683413948d89ad2693e1d121a19
         * accountNumber : csj_btv93lg8iv2t5g
         * accountModifyTime : null
         * phoneNumber : 18669505929
         * nickname : 铁人王进喜
         * headPortrait : /csj_btv93lg8iv2t5g/2021-11-20/e0e1d1d72489438b83e233f7d8e8bde7.png
         * gender : 2
         * qrCode : /csj_btv93lg8iv2t5g/2021-11-17/deed0a079acd4ba69569a817d796d9eb.jpeg
         * address : 山东临沂
         */

        private int id;
        private String uuid;
        private String accountNumber;
        private Object accountModifyTime;
        private String phoneNumber;
        private String nickname;
        private String headPortrait;
        private int gender;
        private String qrCode;
        private String address;

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

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public Object getAccountModifyTime() {
            return accountModifyTime;
        }

        public void setAccountModifyTime(Object accountModifyTime) {
            this.accountModifyTime = accountModifyTime;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadPortrait() {
            return headPortrait;
        }

        public void setHeadPortrait(String headPortrait) {
            this.headPortrait = headPortrait;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getQrCode() {
            return qrCode;
        }

        public void setQrCode(String qrCode) {
            this.qrCode = qrCode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}

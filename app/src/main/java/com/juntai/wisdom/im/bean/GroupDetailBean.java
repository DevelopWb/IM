package com.juntai.wisdom.im.bean;

import com.juntai.disabled.basecomponent.base.BaseResult;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-01-16 11:31
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-01-16 11:31
 */
public class GroupDetailBean extends BaseResult {


    /**
     * data : {"groupId":18,"uuid":"f3a6714211f746078ede255d7eb0ce10","groupName":"tomato153、铁人王进喜、tobato、","groupPicture":"/group/f3a6714211f746078ede255d7eb0ce10/2022-01-16/f3a6714211f746078ede255d7eb0ce10.png","isTop":0,"userNickname":"aa","userInfoVoList":[{"id":10,"uuid":"9aef4a4544ed43c6a57e64ae0a2389a5","accountNumber":"66666666","phoneNumber":"15311810032","nickname":"tomato153","myNickname":null,"groupNickname":"aa","headPortrait":"/9aef4a4544ed43c6a57e64ae0a2389a5/2021-11-27/b79909374a934e79a99a051c5e584f74.png","gender":0,"qrCode":"/9aef4a4544ed43c6a57e64ae0a2389a5/2021-12-08/9aef4a4544ed43c6a57e64ae0a2389a5.jpeg","address":"山东临沂"},{"id":5,"uuid":"6d9b0683413948d89ad2693e1d121a19","accountNumber":"csj_btv93lg8iv2t5g","phoneNumber":"18669505929","nickname":"铁人王进喜","myNickname":null,"groupNickname":null,"headPortrait":"/6d9b0683413948d89ad2693e1d121a19/2021-12-04/d48e5c7ff2f243bc897f7a6743361047.png","gender":1,"qrCode":"/6d9b0683413948d89ad2693e1d121a19/2021-12-08/6d9b0683413948d89ad2693e1d121a19.jpeg","address":"山东临沂"},{"id":9,"uuid":"2fc39f48adf24c66915239c0918af2de","accountNumber":"88888888","phoneNumber":"17568086930","nickname":"tobato","myNickname":"bbb","groupNickname":null,"headPortrait":"/2fc39f48adf24c66915239c0918af2de/2021-12-13/2b61ed1e7b074109ae8f96aa3a70dfea.png","gender":1,"qrCode":"/2fc39f48adf24c66915239c0918af2de/2022-01-15/2fc39f48adf24c66915239c0918af2de.jpeg","address":null}]}
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
         * groupId : 18
         * uuid : f3a6714211f746078ede255d7eb0ce10
         * groupName : tomato153、铁人王进喜、tobato、
         * groupPicture : /group/f3a6714211f746078ede255d7eb0ce10/2022-01-16/f3a6714211f746078ede255d7eb0ce10.png
         * isTop : 0
         * userNickname : aa
         * userInfoVoList : [{"id":10,"uuid":"9aef4a4544ed43c6a57e64ae0a2389a5","accountNumber":"66666666","phoneNumber":"15311810032","nickname":"tomato153","myNickname":null,"groupNickname":"aa","headPortrait":"/9aef4a4544ed43c6a57e64ae0a2389a5/2021-11-27/b79909374a934e79a99a051c5e584f74.png","gender":0,"qrCode":"/9aef4a4544ed43c6a57e64ae0a2389a5/2021-12-08/9aef4a4544ed43c6a57e64ae0a2389a5.jpeg","address":"山东临沂"},{"id":5,"uuid":"6d9b0683413948d89ad2693e1d121a19","accountNumber":"csj_btv93lg8iv2t5g","phoneNumber":"18669505929","nickname":"铁人王进喜","myNickname":null,"groupNickname":null,"headPortrait":"/6d9b0683413948d89ad2693e1d121a19/2021-12-04/d48e5c7ff2f243bc897f7a6743361047.png","gender":1,"qrCode":"/6d9b0683413948d89ad2693e1d121a19/2021-12-08/6d9b0683413948d89ad2693e1d121a19.jpeg","address":"山东临沂"},{"id":9,"uuid":"2fc39f48adf24c66915239c0918af2de","accountNumber":"88888888","phoneNumber":"17568086930","nickname":"tobato","myNickname":"bbb","groupNickname":null,"headPortrait":"/2fc39f48adf24c66915239c0918af2de/2021-12-13/2b61ed1e7b074109ae8f96aa3a70dfea.png","gender":1,"qrCode":"/2fc39f48adf24c66915239c0918af2de/2022-01-15/2fc39f48adf24c66915239c0918af2de.jpeg","address":null}]
         */

        private int groupId;
        private String uuid;
        private String groupName;
        private String groupPicture;
        private int isTop;
        private int groupCreateUserId;
        private String userNickname;
        private List<GroupPeoplesBean.DataBean> userInfoVoList;

        public int getGroupCreateUserId() {
            return groupCreateUserId;
        }

        public void setGroupCreateUserId(int groupCreateUserId) {
            this.groupCreateUserId = groupCreateUserId;
        }

        public List<GroupPeoplesBean.DataBean> getUserInfoVoList() {
            if (userInfoVoList == null) {
                return new ArrayList<>();
            }
            return userInfoVoList;
        }

        public void setUserInfoVoList(List<GroupPeoplesBean.DataBean> userInfoVoList) {
            this.userInfoVoList = userInfoVoList;
        }

        public int getGroupId() {
            return groupId;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
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

        public int getIsTop() {
            return isTop;
        }

        public void setIsTop(int isTop) {
            this.isTop = isTop;
        }

        public String getUserNickname() {
            return userNickname;
        }

        public void setUserNickname(String userNickname) {
            this.userNickname = userNickname;
        }

    }
}

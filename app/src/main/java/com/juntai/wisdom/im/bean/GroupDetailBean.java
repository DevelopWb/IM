package com.juntai.wisdom.im.bean;

import com.juntai.disabled.basecomponent.base.BaseResult;


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

    private GroupDetailInfoBean data;

    public GroupDetailInfoBean getData() {
        return data;
    }

    public void setData(GroupDetailInfoBean data) {
        this.data = data;
    }
}

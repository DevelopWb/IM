package com.juntai.wisdom.im.bean;

import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述
 * 未读消息体  首次登陆的时候接收
 * @CreateDate: 2021-11-23 11:40
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-11-23 11:40
 */
public class UnReadMsgsBean extends BaseWsMessageBean {


    /**
     * friendApplyCount : 0
     * groupMsg : 群聊
     * groupMsgList : [{"chatType":2,"content":"群消息22","createTime":1637544062000,"duration":"0","fromUserId":5,"groupId":0,"id":19,"msgType":0,"readBurn":1,"toUserId":6,"unreadCount":3}]
     * msg : 单聊
     * msgList : [{"chatType":1,"content":"111111","createTime":1637543038000,"duration":"0","fromUserId":5,"groupId":0,"id":59,"msgType":0,"readBurn":1,"toUserId":10,"unreadCount":2},{"chatType":1,"content":"999","createTime":1637487701000,"duration":"0","fromUserId":9,"groupId":0,"id":57,"msgType":0,"readBurn":1,"toUserId":10,"unreadCount":9}]
     */

    private int friendApplyCount;
    private String groupMsg;
    private String msg;
    private List<MessageBodyBean> groupMsgList;
    private List<MessageBodyBean> msgList;

    public int getFriendApplyCount() {
        return friendApplyCount;
    }

    public void setFriendApplyCount(int friendApplyCount) {
        this.friendApplyCount = friendApplyCount;
    }

    public String getGroupMsg() {
        return groupMsg;
    }

    public void setGroupMsg(String groupMsg) {
        this.groupMsg = groupMsg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<MessageBodyBean> getGroupMsgList() {
        return groupMsgList;
    }

    public void setGroupMsgList(List<MessageBodyBean> groupMsgList) {
        this.groupMsgList = groupMsgList;
    }

    public List<MessageBodyBean> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<MessageBodyBean> msgList) {
        this.msgList = msgList;
    }

}

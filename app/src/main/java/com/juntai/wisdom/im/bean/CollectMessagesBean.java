package com.juntai.wisdom.im.bean;

import com.juntai.disabled.basecomponent.base.BaseResult;

import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-02-28 17:12
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-02-28 17:12
 */
public class CollectMessagesBean extends BaseResult {


    /**
     * data : {"totalCount":5,"pageSize":10,"totalPage":1,"currPage":1,"list":[{"id":5,"msgId":2267,"userId":10,"groupId":0,"nickname":"tomato153","createTime":"2022-02-09","msgType":0,"chatType":1,"content":"111","duration":"","fileSize":"","fileName":"","rotation":"","lat":null,"lng":null,"addrName":null,"addrDes":null,"otherUserId":0,"otherAccount":"","otherNickname":"","otherHead":""}]}
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
         * totalCount : 5
         * pageSize : 10
         * totalPage : 1
         * currPage : 1
         * list : [{"id":5,"msgId":2267,"userId":10,"groupId":0,"nickname":"tomato153","createTime":"2022-02-09","msgType":0,"chatType":1,"content":"111","duration":"","fileSize":"","fileName":"","rotation":"","lat":null,"lng":null,"addrName":null,"addrDes":null,"otherUserId":0,"otherAccount":"","otherNickname":"","otherHead":""}]
         */

        private int totalCount;
        private int pageSize;
        private int totalPage;
        private int currPage;
        private List<MessageBodyBean> list;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getCurrPage() {
            return currPage;
        }

        public void setCurrPage(int currPage) {
            this.currPage = currPage;
        }

        public List<MessageBodyBean> getList() {
            return list;
        }

        public void setList(List<MessageBodyBean> list) {
            this.list = list;
        }

        }
}

package com.juntai.wisdom.im.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2020/4/15 10:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/4/15 10:08
 */
public class MultipleItem implements MultiItemEntity {
    public static final String SET_REMARK = "设置备注";
    public static final String MORE_INFO = "更多信息";
    public static final String SEND_MSG = "发消息";
    public static final String CALL_IN_VIDEO = "音视频通话";
    public static final String ADD_TO_CONTRACT = "添加到通讯录";
    public static final String SEARCH_CHAT_RECORD = "查找聊天记录";
    public static final String GROUP_CHAT_NAME = "群聊名称";
    public static final String GROUP_QRCODE = "群二维码";
    public static final String CHAT_IN_TOP = "置顶聊天";
    public static final String DELETE_CHAT_RECORD = "清空聊天记录";
    public static final String DELETE_QUIT_GROUP = "删除并退出";
    public static final String NICK_NAME_IN_GROUP = "我在群里的昵称";
    public static final String SHARE_TO_FRIENDS = "把他(她)推荐给朋友";
    public static final String ADD_TO_BLACK_LIST = "加入黑名单";
    public static final String CHECK_WHEN_ADD = "加我为朋友时需要验证";
    public static final String RECOMMAND_CONTACT_FRIENDS = "向我推荐通讯录朋友";
    public static final String BLACK_LIST = "通讯录黑名单";
    public static final String DELETE = "删除";





    public static final int ITEM_DIVIDER = 1;//  divider
    public static final int ITEM_TITLE = 1001;//  divider
    public static final int ITEM_MYCENTER_MENUS = 2;//  菜单
    public static final int ITEM_PIC_TEXT_MENUS = 3;//
    public static final int ITEM_CONTACT = 4;//
    public static final int ITEM_GROUP = 40;//
    public static final int ITEM_SELECT_GROUP = 401;//
    public static final int ITEM_SELECT_CONTACT = 41;//
    public static final int ITEM_TEXT_VALUE = 5;//
    public static final int ITEM_RECYCLE = 6;//
    public static final int ITEM_KEY_VALUE = 7;//
    //带有switch的控件
    public static final int ITEM_KEY_VALUE_WITH_SWITCH = 8;


    /*====================================================    聊天   ==============================================================*/
    public static final int ITEM_CHAT_TEXT_MSG = 10;//文本聊天
    public static final int ITEM_CHAT_DATE_MSG = 11;//日期
    public static final int ITEM_CHAT_LIST_CONTACT = 12;//
    public static final int ITEM_CHAT_LIST_GROUP = 1201;//
    public static final int ITEM_SEND_AUDIO = 13;//
    public static final int ITEM_RECEIVE_AUDIO = 14;//
    public static final int ITEM_CHAT_PIC_VIDEO = 15;//
    //视频通话记录
    public static final int ITEM_CHAT_VIDEO_CALL = 16;//
    //分享位置信息
    public static final int ITEM_CHAT_LOCATE = 17;//
    //发送名片
    public static final int ITEM_CHAT_CARD = 18;//
    public static final int ITEM_CHAT_FILE = 19;//
    public static final int ITEM_CHAT_RECORD = 20;//
    //提醒类消息  群创建成功 等
    public static final int ITEM_CHAT_NOTICE = 21;//
    /**
     * 外部分享
     */
    public static final int ITEM_CHAT_OUTSIDE_SHARE = 22;//



    public static final int ITEM_CHAT_FILE_DIR = 100;//
    public static final int ITEM_CHAT_FILE_TEXT = 101;//



        /*====================================================    收藏   ==============================================================*/


    public static final int ITEM_COLLECTION_TEXT = 201;//  普通消息
    public static final int ITEM_COLLECTION_PIC = 202;//  图片
    public static final int ITEM_COLLECTION_VIDEO = 203;//  视频
    public static final int ITEM_COLLECTION_AUDIO = 204;//  语音
    public static final int ITEM_COLLECTION_FILE = 205;//  文件
    public static final int ITEM_COLLECTION_LOCATE = 206;//  位置
    public static final int ITEM_COLLECTION_CHAT_RECORD = 207;//  聊天记录
    public static final int ITEM_COLLECTION_OUTSIDE_SHARE = 208;//  外部分享



    private int itemType;
    private Object object;

    public MultipleItem(int itemType, Object object) {
        this.itemType = itemType;
        this.object = object;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}

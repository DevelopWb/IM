package com.juntai.wisdom.im;

public class AppHttpPath {


//    public static final String BASE = "http://192.168.124.118/server";
        public static final String BASE = "http://www.juntaikeji.com:21213/server";
//    public static final String BASE_SOCKET = "ws://192.168.124.118/webSocket/";
        public static final String BASE_SOCKET = "ws://www.juntaikeji.com:21213/webSocket/";
//    public static final String BASE_IMAGE = "http://192.168.124.118:9598";
    public static final String BASE_IMAGE = "http://www.juntaikeji.com:19170";

    public static final String CHAT_VIDEO_URL = "turn:stun.juntaikeji.com:19603";
    public static final String ALL_CITYS = "https://restapi.amap.com/v3/config/district?";


    /**
     * 上传图片或视频
     */
    public static final String UPLOAD_FILES = BASE + "/uploadFile/upload";
    /**
     * 登录
     */
    public static final String LOGIN = BASE + "/member/login";
    /**
     * 登录
     */
    public static final String REGIST = BASE + "/member/register";

    public static final String ADD_FRIEND_BY_UUID = BASE + "/member/getUserInfoByUuid";
    public static final String JOIN_GROUP_BY_UUID = BASE + "/group/selectGroupByUuid";


    /**
     * /**
     * 检查更新
     */
//    public static final String APP_UPDATE = BASE + "/detectionAppVersions.shtml";
    public static final String APP_UPDATE = BASE + "/appUpdate/detectionAppVersions";

    /**
     * 获取短信验证码
     */
    public static final String GET_SMS_CODE = BASE + "/member/getSMSCode";



    /*====================================================    好友   ==============================================================*/

    /**
     * 好友列表
     */
    public static final String GET_CONTACT_LISTE = BASE + "/friend/getUserFriendList";
    /**
     * 通讯录匹配好友
     */
    public static final String MATCH_CONTACT_LISTE = BASE + "/member/selectMatchingFriends";
    public static final String TOP_CHAT = BASE + "/friend/topFriendChat";

    /**
     * 查找好友
     */
    public static final String SEARCH_FRIENDS = BASE + "/friend/searchFriend";

    /**
     * 申请添加好友
     */
    public static final String APPLY_ADD_FRIEND = BASE + "/friendApply/addFriend";
    public static final String UNREAD_APPLY_FRIEND = BASE + "/friendApply/getFriendApplyUnread";
    /**
     * 同意好友
     */
    public static final String AGREE_FRIEND_APPLY = BASE + "/friendApply/friendVerify";


    /**
     * 新朋友列表
     */
    public static final String NEW_FRIEND_LIST = BASE + "/friendApply/selectNewFriendList";
    /**
     * 修改好友备注
     */
    public static final String MODIFY_FRIEND_REMARK = BASE + "/friend/updateFriendRemarks";
    /**
     * 删除好友
     */
    public static final String DELETE_FRIEND = BASE + "/friend/deleteFriend";
    /**
     *
     */
    public static final String BLOCK_FRIEND = BASE + "/friend/blockFriend";

    /**
     * 添加好友验证
     */
    public static final String ADD_FRIEND_VERIFY = BASE + "/member/addFriendVerification";


    /*====================================================    个人中心   ==============================================================*/


    /**
     * 个人详情
     */
    public static final String GET_USER_INFO = BASE + "/member/getUserInfo";
    /**
     * 提交意见反馈
     */
    public static final String COMMIT_SUGGESTION = BASE + "/opinion/save";
    /**
     * 退出登录
     */
    public static final String LOGOUT = BASE + "/member/logout";

    /**
     * 修改密码
     */
    public static final String MODIFY_PWD = BASE + "/member/updatePassword";
    /**
     * 修改个人信息
     */
    public static final String MODIFY_USER_INFO = BASE + "/member/updateUserInfo";

    /**
     * 修改账户
     */
    public static final String MODIFY_USER_ACCOUNT = BASE + "/member/updateUserAccountNumber";
    /**
     * 获取用户二维码名片
     */
    public static final String GET_QRCODE = BASE + "/member/getAccountQrCode";





    /*====================================================    消息   ==============================================================*/

    /**
     * 发送消息
     */
    public static final String SEND_MSG = BASE + "/msg/sendMassage";
    /**
     * 群聊消息
     */
    public static final String SEND_GROUP_MSG = BASE + "/msg/sendGroupMassage";

    /**
     * 查询未读消息详情
     */
    public static final String GET_UNREAD_PRIVATE_MSG = BASE + "/msg/selectMsgInfo";
    /**
     * 查询未读消息详情  群消息
     */
    public static final String GET_UNREAD_GROUP_MSG = BASE + "/msg/selectGroupMsgInfo";
    /**
     * 发起视频通话
     */
    public static final String REQUEST_VIDEO_CALL = BASE + "/msg/sendVideoCall";
    /**
     * 接受视频通话
     */
    public static final String ACCESS_VIDEO_CALL = BASE + "/msg/acceptCall";
    /**
     * 结束视频通话
     */
    public static final String REJECT_VIDEO_CALL = BASE + "/msg/refuseCall";





    /*====================================================    群聊   ==============================================================*/
    /**
     * 创建群聊
     */
    public static final String CREAT_GROUP = BASE + "/group/createGroup";

    /**
     * 群聊列表
     */
    public static final String GET_GROUP_LIST = BASE + "/group/selectGroupListByUserId";
    /**
     * 修改群名称
     */
    public static final String MODIFY_GROUP_NAME = BASE + "/group/updateGroupInfo";
    /**
     * 群二维码
     */
    public static final String GET_GROUP_QRCODE = BASE + "/group/getGroupQrCodePoster";
    /**
     * 获取群聊详情
     */
    public static final String GET_GROUP_INFO = BASE + "/group/selectGroupByGroupId";
    /**
     * 加入群聊
     */
    public static final String JOIN_GROUP = BASE + "/groupUser/joinGroup";
    /**
     * 修改群里的昵称
     */
    public static final String MODIFY_NICKNAME_OF_GROUP = BASE + "/groupUser/updateGroupNickname";
    /**
     * 退出群聊
     */
    public static final String QUIT_GROUP = BASE + "/groupUser/outGroup";
    /**
     * 群主退出
     */
    public static final String OWNER_QUIT_GROUP = BASE + "/groupUser/leaderOutGroup";
    public static final String GROUP_PEOPLES = BASE + "/groupUser/selectGroupUserByGroupId";

    /**
     * 置顶群聊
     */
    public static final String TOP_GROUP_CHAT = BASE + "/groupUser/topGroupChat";





        /*====================================================    收藏   ==============================================================*/


    /**
     * 收藏
     */
    public static final String COLLECT = BASE + "/collection/save";

    /**
     * 删除收藏
     */
    public static final String DELETE_COLLECT = BASE + "/collection/delete";

    /**
     * 所有的收藏
     */
    public static final String ALL_COLLECTS = BASE + "/collection/selectList";



}
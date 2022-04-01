package com.juntai.wisdom.im;


import com.juntai.disabled.basecomponent.base.BaseResult;
import com.juntai.wisdom.im.base.uploadFile.UploadFileBean;
import com.juntai.wisdom.im.bean.CitysBean;
import com.juntai.wisdom.im.bean.CollectMessagesBean;
import com.juntai.wisdom.im.bean.ContactInfoBean;
import com.juntai.wisdom.im.bean.ContactListBean;
import com.juntai.wisdom.im.bean.GroupBean;
import com.juntai.wisdom.im.bean.GroupDetailBean;
import com.juntai.wisdom.im.bean.GroupInfoByUuidBean;
import com.juntai.wisdom.im.bean.GroupListBean;
import com.juntai.wisdom.im.bean.GroupPeoplesBean;
import com.juntai.wisdom.im.bean.MessageListBean;
import com.juntai.wisdom.im.bean.NewFriendsBean;
import com.juntai.wisdom.im.bean.UserBean;
import com.juntai.wisdom.im.bean.UserInfoByUUIDBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * responseBody里的数据只能调用(取出)一次，第二次为空。可赋值给新的变量使用
 */
public interface AppServer {
    /**
     * 登录
     *
     * @param account
     * @param password
     * @param mobileName
     * @param regId
     * @return
     */
    @POST(AppHttpPath.LOGIN)
    Observable<UserBean> login(@Query("account") String account, @Query("password") String password,
                               @Query("mobileName") String mobileName, @Query("regId") String regId);

    /**
     * 登录
     *
     * @return
     */
    @POST(AppHttpPath.REGIST)
    Observable<UserBean> regist(@Body RequestBody requestBody);

    /**
     * 上传文件
     *
     * @return
     */
    @POST(AppHttpPath.UPLOAD_FILES)
    Observable<UploadFileBean> uploadFiles(@Body RequestBody requestBody);

    @POST(AppHttpPath.MODIFY_PWD)
    Observable<BaseResult> modifyPwd(@Query("userId") int userId, @Query("phoneNumber") String account, @Query("password") String password, @Query("code") String code
    );

    /**
     * account  手机号
     *
     * @return
     */
    @GET(AppHttpPath.GET_SMS_CODE + "/{phoneNumber}")
    Observable<BaseResult> getSMSCode(@Path("phoneNumber") String path);






    /*====================================================    个人中心   ==============================================================*/


    /**
     * account  手机号
     *
     * @return
     */
    @POST(AppHttpPath.GET_USER_INFO)
    Observable<UserBean> getUserInfo(@Body RequestBody requestBody);

    /**
     * 修改账户
     *
     * @param requestBody
     * @return
     */
    @POST(AppHttpPath.MODIFY_USER_ACCOUNT)
    Observable<BaseResult> modifyAccount(@Body RequestBody requestBody);


    @POST(AppHttpPath.COMMIT_SUGGESTION)
    Observable<BaseResult> commitSuggestion(@Body RequestBody requestBody);

    /**
     * 修改用户信息
     *
     * @param requestBody
     * @return
     */
    @POST(AppHttpPath.MODIFY_USER_INFO)
    Observable<BaseResult> modifyUserInfo(@Body RequestBody requestBody);

    @POST(AppHttpPath.LOGOUT)
    Observable<BaseResult> logout(@Body RequestBody requestBody);





    /*====================================================    好友   ==============================================================*/

    @POST(AppHttpPath.GET_CONTACT_LISTE)
    Observable<ContactListBean> getFriendList(@Body RequestBody requestBody);

    @POST(AppHttpPath.MATCH_CONTACT_LISTE)
    Observable<ContactListBean> matchMobileContacts(@Body RequestBody requestBody);

    @POST(AppHttpPath.SEARCH_FRIENDS)
    Observable<ContactInfoBean> searchFriends(@Body RequestBody requestBody);

    @POST(AppHttpPath.APPLY_ADD_FRIEND)
    Observable<BaseResult> applyAddFriend(@Body RequestBody requestBody);

    @POST(AppHttpPath.UNREAD_APPLY_FRIEND)
    Observable<BaseResult> getUnreadFriendApply(@Body RequestBody requestBody);

    @POST(AppHttpPath.ADD_FRIEND_BY_UUID)
    Observable<UserInfoByUUIDBean> addFriendByUuid(@Body RequestBody requestBody);

    @POST(AppHttpPath.JOIN_GROUP_BY_UUID)
    Observable<GroupInfoByUuidBean> joinGroupByUuid(@Body RequestBody requestBody);

    @POST(AppHttpPath.TOP_CHAT)
    Observable<BaseResult> topChat(@Body RequestBody requestBody);

    @GET(AppHttpPath.ALL_CITYS)
    Observable<CitysBean> getAllCitys(@Query("keywords") String keywords, @Query("subdistrict") String subdistrict, @Query("key") String key);

    @POST(AppHttpPath.AGREE_FRIEND_APPLY)
    Observable<BaseResult> agreeFriendsApply(@Body RequestBody requestBody);

    @POST(AppHttpPath.NEW_FRIEND_LIST)
    Observable<NewFriendsBean> newFriendList(@Body RequestBody requestBody);

    @POST(AppHttpPath.MODIFY_FRIEND_REMARK)
    Observable<BaseResult> modifyFriendRemark(@Body RequestBody requestBody);

    @POST(AppHttpPath.DELETE_FRIEND)
    Observable<BaseResult> deleteFriend(@Body RequestBody requestBody);

    @POST(AppHttpPath.BLOCK_FRIEND)
    Observable<BaseResult> blockFriend(@Body RequestBody requestBody);

    @POST(AppHttpPath.ADD_FRIEND_VERIFY)
    Observable<BaseResult> verifyAddFriend(@Body RequestBody requestBody);

    /**
     * 发送消息
     *
     * @param requestBody
     * @return
     */
    @POST(AppHttpPath.SEND_MSG)
    Observable<BaseResult> sendMessage(@Body RequestBody requestBody);

    /**
     * 发送消息
     *
     * @param requestBody
     * @return
     */
    @POST(AppHttpPath.SEND_GROUP_MSG)
    Observable<BaseResult> sendGroupMessage(@Body RequestBody requestBody);

    @POST(AppHttpPath.GET_UNREAD_PRIVATE_MSG)
    Observable<MessageListBean> getPrivateUnreadMsg(@Body RequestBody requestBody);

    @POST(AppHttpPath.GET_UNREAD_GROUP_MSG)
    Observable<MessageListBean> getGroupUnreadMsg(@Body RequestBody requestBody);

    @POST(AppHttpPath.REQUEST_VIDEO_CALL)
    Observable<BaseResult> requestVideoCall(@Body RequestBody requestBody);

    @POST(AppHttpPath.ACCESS_VIDEO_CALL)
    Observable<BaseResult> accessVideoCall(@Body RequestBody requestBody);

    @POST(AppHttpPath.REJECT_VIDEO_CALL)
    Observable<BaseResult> rejectVideoCall(@Body RequestBody requestBody);

    @POST(AppHttpPath.MODIFY_GROUP_NAME)
    Observable<BaseResult> modifyGroupName(@Body RequestBody requestBody);

    @POST(AppHttpPath.MODIFY_NICKNAME_OF_GROUP)
    Observable<BaseResult> modifyNickNameOfGroup(@Body RequestBody requestBody);


    @POST(AppHttpPath.GET_GROUP_LIST)
    Observable<GroupListBean> getGroupList(@Body RequestBody requestBody);


    @POST(AppHttpPath.GET_GROUP_INFO)
    Observable<GroupDetailBean> getGroupInfo(@Body RequestBody requestBody);


    @POST(AppHttpPath.CREAT_GROUP)
    Observable<GroupBean> creatGroup(@Query("userId") int userId, @Query("token") String token, @Query("type") int type, @Query("userNickname") String userNickname, @Query("userList") List<Integer> ids);

    @POST(AppHttpPath.JOIN_GROUP)
    Observable<BaseResult> joinGroup(@Query("userId") int userId, @Query("token") String token, @Query("type") int type, @Query("groupUuid") String groupUuid, @Query("userList") List<Integer> ids);

    @POST(AppHttpPath.QUIT_GROUP)
    Observable<BaseResult> quitGroup(@Query("groupUuid") String groupUuid, @Query("userList") List<Integer> ids);

    @POST(AppHttpPath.OWNER_QUIT_GROUP)
    Observable<BaseResult> ownerQuitGroup(@Query("groupUuid") String groupUuid, @Query("userId") int userId);

    @POST(AppHttpPath.TOP_GROUP_CHAT)
    Observable<BaseResult> topGroupChat(@Body RequestBody requestBody);

    @POST(AppHttpPath.GROUP_PEOPLES)
    Observable<GroupPeoplesBean> getGroupPeoples(@Body RequestBody requestBody);


    @POST(AppHttpPath.COLLECT)
    Observable<BaseResult> collect(@Body RequestBody requestBody);

    @POST(AppHttpPath.DELETE_COLLECT)
    Observable<BaseResult> deleteCollection(@Body RequestBody requestBody);

    @POST(AppHttpPath.ALL_COLLECTS)
    Observable<CollectMessagesBean> getAllCollection(@Body RequestBody requestBody);
}
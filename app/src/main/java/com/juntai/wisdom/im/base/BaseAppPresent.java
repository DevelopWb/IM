package com.juntai.wisdom.im.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.juntai.disabled.basecomponent.base.BaseObserver;
import com.juntai.disabled.basecomponent.base.BaseResult;
import com.juntai.disabled.basecomponent.bean.BaseMenuBean;
import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.basecomponent.mvp.IModel;
import com.juntai.disabled.basecomponent.mvp.IView;
import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.basecomponent.utils.RxScheduler;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.AppNetModule;
import com.juntai.wisdom.im.base.uploadFile.UploadFileBean;
import com.juntai.wisdom.im.bean.CollectMessagesBean;
import com.juntai.wisdom.im.bean.GroupDetailBean;
import com.juntai.wisdom.im.bean.GroupInfoByUuidBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MessageBodyBean_;
import com.juntai.wisdom.im.bean.UserBean;
import com.juntai.wisdom.im.bean.UserInfoByUUIDBean;
import com.juntai.wisdom.im.utils.MyFileProvider;
import com.juntai.wisdom.im.utils.ObjectBox;
import com.juntai.wisdom.im.utils.OperateMsgUtil;
import com.juntai.wisdom.im.utils.UserInfoManager;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import io.objectbox.query.QueryBuilder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2020/6/3 8:38
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/6/3 8:38
 */
public abstract class BaseAppPresent<M extends IModel, V extends IView> extends BasePresenter<M, V> {

    public void addFriendByUuid(RequestBody requestBody, String tag) {
        AppNetModule.createrRetrofit()
                .addFriendByUuid(requestBody)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<UserInfoByUUIDBean>(getView()) {
                    @Override
                    public void onSuccess(UserInfoByUUIDBean o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }

    public void joinGroupByUuid(RequestBody requestBody, String tag) {
        AppNetModule.createrRetrofit()
                .joinGroupByUuid(requestBody)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<GroupInfoByUuidBean>(getView()) {
                    @Override
                    public void onSuccess(GroupInfoByUuidBean o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }
    public void regist(String tag, RequestBody body) {
        AppNetModule.createrRetrofit()
                .regist(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<BaseResult>(getView()) {
                    @Override
                    public void onSuccess(BaseResult o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }
    /**
     * 获取文件名称
     *
     * @param messageBodyBean
     * @return
     */
    public String getSavedFileName(MessageBodyBean messageBodyBean) {
        String content = messageBodyBean.getContent();
        if (TextUtils.isEmpty(content)) {
            return null;
        }

        if (content.contains("/")) {
            content = content.substring(content.lastIndexOf("/") + 1, content.length());
        }
        return content;
//        return messageBodyBean.getFileName().substring(0,messageBodyBean.getFileName().lastIndexOf("."))+content;
    }

    /**
     * 收藏文件
     * @param appVideoPath
     */
    public void collectFile(Context mContext, MessageBodyBean messageBodyBean, String appVideoPath) {
        if (messageBodyBean.getFromUserId() == UserInfoManager.getUserId()) {
            if (TextUtils.isEmpty(messageBodyBean.getLocalCatchPath())) {
                //文件不存在
                ToastUtils.toast(mContext, "当前选择的音频/图片/视频/文件还没有下载完成,下载完成之后才可收藏");
            } else {
                collect(OperateMsgUtil.getMsgBuilder(messageBodyBean).build(), AppHttpPath.COLLECT);
            }
        } else {
            if (!FileCacheUtils.isFileExists(appVideoPath + getSavedFileName(messageBodyBean))) {
                //文件不存在
                ToastUtils.toast(mContext, "当前选择的音频/图片/视频/文件还没有下载完成,下载完成之后才可收藏");
            } else {
               collect(OperateMsgUtil.getMsgBuilder(messageBodyBean).build(), AppHttpPath.COLLECT);
            }
        }
    }
    /**
     * 转发方式
     *
     * @return
     */
    public List<BaseMenuBean> getCalls() {
        List<BaseMenuBean> calls = new ArrayList<>();
        calls.add(new BaseMenuBean("视频通话"));
        calls.add(new BaseMenuBean("语音通话"));
        return calls;
    }

    /**
     * 查找和联系人的聊天记录
     *
     * @param contactId
     * @return
     */
    public List<MessageBodyBean> findPrivateChatRecordList(int contactId) {
        ObjectBox.get().boxFor(MessageBodyBean.class).query(
                MessageBodyBean_.groupId.equal(0)
                        .and(MessageBodyBean_.canDelete.equal(true))
                        .and(MessageBodyBean_.owner.equal(UserInfoManager.getUserUUID())
                                .and(MessageBodyBean_.toUserId.oneOf(new int[]{contactId, UserInfoManager.getUserId()}))
                                .and(MessageBodyBean_.fromUserId.oneOf(new int[]{contactId, UserInfoManager.getUserId()}))
                        )).build().remove();
        QueryBuilder<MessageBodyBean> builder = ObjectBox.get().boxFor(MessageBodyBean.class).query(
                MessageBodyBean_.groupId.equal(0)
                        .and(MessageBodyBean_.canDelete.equal(false))
                        .and(MessageBodyBean_.owner.equal(UserInfoManager.getUserUUID())
                                .and(MessageBodyBean_.toUserId.oneOf(new int[]{contactId, UserInfoManager.getUserId()}))
                                .and(MessageBodyBean_.fromUserId.oneOf(new int[]{contactId, UserInfoManager.getUserId()}))
                        ));
        return builder
                .build()
                .find();
    }
    /**
     * 删除私聊记录
     *
     * @param contactId
     * @return
     */
    public void  deletePrivateChatRecord(int contactId) {
        QueryBuilder<MessageBodyBean> builder = ObjectBox.get().boxFor(MessageBodyBean.class).query(
                MessageBodyBean_.groupId.equal(0)
                        .and(MessageBodyBean_.owner.equal(UserInfoManager.getUserUUID())
                                .and(MessageBodyBean_.toUserId.oneOf(new int[]{contactId, UserInfoManager.getUserId()}))
                                .and(MessageBodyBean_.fromUserId.oneOf(new int[]{contactId, UserInfoManager.getUserId()}))
                        ));

        builder.build().remove();
    }

    /**
     * 查找和联系人的文件聊天记录
     *
     * @param contactId
     * @return
     */
    public List<MessageBodyBean> findPrivateChatFileRecordList(int contactId) {
        QueryBuilder<MessageBodyBean> builder = ObjectBox.get().boxFor(MessageBodyBean.class).query(
                MessageBodyBean_.groupId.equal(0)
                        .and(MessageBodyBean_.owner.equal(UserInfoManager.getUserUUID())
                                .and(MessageBodyBean_.msgType.equal(8))
                                .and(MessageBodyBean_.toUserId.oneOf(new int[]{contactId, UserInfoManager.getUserId()}))
                                .and(MessageBodyBean_.fromUserId.oneOf(new int[]{contactId, UserInfoManager.getUserId()}))
                        )).orderDesc(MessageBodyBean_.createTime);
        return builder
                .build()
                .find();
    }

    /**
     * 查找和联系人的文件聊天记录
     *
     * @return
     */
    public List<MessageBodyBean> findCollectFileRecordList() {
        QueryBuilder<MessageBodyBean> builder = ObjectBox.get().boxFor(MessageBodyBean.class).query(
                MessageBodyBean_.msgType.equal(8)
                        .and(MessageBodyBean_.owner.equal(UserInfoManager.getUserUUID())
                                .and(MessageBodyBean_.isCollected.equal(true)
                                )
                        )).orderDesc(MessageBodyBean_.createTime);
        return builder
                .build()
                .find();
    }

    /**
     * 查找和联系人的聊天记录中最后一条记录
     *
     * @param contactId
     * @return
     */
    public MessageBodyBean findPrivateChatRecordLastMessage(int contactId) {
        List<MessageBodyBean> arrays = ObjectBox.get().boxFor(MessageBodyBean.class).query(
                MessageBodyBean_.groupId.equal(0)
                        .and(MessageBodyBean_.owner.equal(UserInfoManager.getUserUUID())
                                .and(MessageBodyBean_.toUserId.oneOf(new int[]{contactId, UserInfoManager.getUserId()}))
                                .and(MessageBodyBean_.fromUserId.oneOf(new int[]{contactId, UserInfoManager.getUserId()}))
                        )).build().find();

        if (arrays == null || arrays.size() == 0) {
            return null;
        }

        return arrays.get(arrays.size() - 1);
    }

    /**
     * 查找群的聊天记录中最后一条记录
     *
     * @param groupId
     * @return
     */
    public MessageBodyBean findGroupChatRecordLastMessage(int groupId) {
        List<MessageBodyBean> arrays = ObjectBox.get().boxFor(MessageBodyBean.class).query(
                MessageBodyBean_.groupId.equal(groupId)
                        .and(MessageBodyBean_.owner.equal(UserInfoManager.getUserUUID())
                        )).build().find();
        if (arrays == null || arrays.size() == 0) {
            return null;
        }
        return arrays.get(arrays.size() - 1);
    }
    /**
     * 查找群的未读消息中是否有@信息
     *
     * @param groupId
     * @return
     */
    public boolean isGroupChatRecordUnreadHasNoAtMsg(int groupId) {
        List<MessageBodyBean> arrays = ObjectBox.get().boxFor(MessageBodyBean.class).query(
                MessageBodyBean_.groupId.equal(groupId)
                        .and(MessageBodyBean_.owner.equal(UserInfoManager.getUserUUID())
                                .and(MessageBodyBean_.isRead.equal(false))
                                .and(MessageBodyBean_.atUserId.oneOf(new String[]{"-1",String.valueOf(UserInfoManager.getUserId())}))
                        )).build().find();

        return arrays.isEmpty();
    }
    /**
     * 安装APK
     */
    public void installApk(Context mContext,String filePath) {
        File file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = MyFileProvider.getUriFromFile(mContext,file);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //不加入此行代码 容易提示解析包错误
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }
    /**
     * 删除群聊信息
     */
    public  void  deleteGroupInfo(int groupId){

        ObjectBox.get().boxFor(MessageBodyBean.class).query(
                MessageBodyBean_.groupId.equal(groupId)
                        .and(MessageBodyBean_.owner.equal(UserInfoManager.getUserUUID())
                        )).build().remove();

    }

    /**
     * 查找群的聊天记录
     *
     * @param groupId
     * @return
     */
    public List<MessageBodyBean> findGroupChatRecord(int groupId) {
      ObjectBox.get().boxFor(MessageBodyBean.class).query(
                MessageBodyBean_.groupId.equal(groupId)
                        .and(MessageBodyBean_.owner.equal(UserInfoManager.getUserUUID())
                                .and(MessageBodyBean_.canDelete.equal(true))
                        )).build().remove();


        return ObjectBox.get().boxFor(MessageBodyBean.class).query(
                MessageBodyBean_.groupId.equal(groupId)
                        .and(MessageBodyBean_.owner.equal(UserInfoManager.getUserUUID())
                                .and(MessageBodyBean_.canDelete.equal(false))
                        )).build().find();
    }

    /**
     * 查找群的文件聊天记录
     *
     * @param groupId
     * @return
     */
    public List<MessageBodyBean> findGroupChatFileRecord(int groupId) {
        return ObjectBox.get().boxFor(MessageBodyBean.class).query(
                MessageBodyBean_.groupId.equal(groupId)
                        .and(MessageBodyBean_.msgType.equal(8))
                        .and(MessageBodyBean_.owner.equal(UserInfoManager.getUserUUID())
                        )).orderDesc(MessageBodyBean_.createTime).build().find();
    }

    /**
     * 查找群的文件聊天记录
     *
     * @param groupId
     * @return
     */
    public List<MessageBodyBean> findGroupChatCollectFileRecord(int groupId) {
        return ObjectBox.get().boxFor(MessageBodyBean.class).query(
                MessageBodyBean_.groupId.equal(groupId)
                        .and(MessageBodyBean_.isCollected.equal(true))
                        .and(MessageBodyBean_.owner.equal(UserInfoManager.getUserUUID())
                        )).build().find();
    }

    public void collect(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .collect(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<BaseResult>(null) {
                    @Override
                    public void onSuccess(BaseResult o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }

    public void deleteCollection(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .deleteCollection(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<BaseResult>(getView()) {
                    @Override
                    public void onSuccess(BaseResult o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }

    public void getAllCollection(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .getAllCollection(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<CollectMessagesBean>(null) {
                    @Override
                    public void onSuccess(CollectMessagesBean o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }

    /**
     * @param body
     * @param tag
     */
    public void getGroupInfo(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .getGroupInfo(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<GroupDetailBean>(null) {
                    @Override
                    public void onSuccess(GroupDetailBean o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }

    /**
     * @param tag
     */
    public void joinGroup(String groupUuid, List<Integer> userIds, String tag) {
        AppNetModule.createrRetrofit()
                .joinGroup(UserInfoManager.getUserId(), UserInfoManager.getUserToken(), 1, groupUuid, userIds)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<BaseResult>(getView()) {
                    @Override
                    public void onSuccess(BaseResult o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }


    public void modifyPwd(int userId, String account, String pwd, String code, String tag) {
        AppNetModule.createrRetrofit()
                .modifyPwd(userId, account, pwd, code)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<BaseResult>(getView()) {
                    @Override
                    public void onSuccess(BaseResult o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }

                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }

    /**
     * 上传文件
     *
     * @param
     * @param body
     */
    private void uploadFiles(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .uploadFiles(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<UploadFileBean>(getView()) {
                    @Override
                    public void onSuccess(UploadFileBean o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o.getFilePaths());
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }


    /**
     * 上传文件
     *
     * @param filePaths
     */
    public void uploadFile(String tag, String... filePaths) {
        if (filePaths.length > 0) {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("account", UserInfoManager.getUserUUID());
            for (String filePath : filePaths) {
                try {
                    builder.addFormDataPart("file", URLEncoder.encode(filePath, "utf-8"), RequestBody.create(MediaType.parse("file"), new File(filePath)));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            uploadFiles(builder.build(), tag);
        }

    }

    /**
     * 上传文件
     *
     * @param filePaths
     */
    public void uploadFile(List<String> filePaths, String tag) {
        if (filePaths.size() > 0) {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("account", UserInfoManager.getUserUUID());
            for (String filePath : filePaths) {
                try {
                    builder.addFormDataPart("file", URLEncoder.encode(filePath, "utf-8"), RequestBody.create(MediaType.parse("file"), new File(filePath)));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            uploadFiles(builder.build(), tag);
        }

    }

    /**
     * 获取用户信息
     *
     * @param tag
     * @param body
     */
    public void getUserInfo(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .getUserInfo(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<UserBean>(null) {
                    @Override
                    public void onSuccess(UserBean o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }


    /**
     * 发送私聊消息
     *
     * @param body
     * @param tag
     */
    public void sendPrivateMessage(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .sendMessage(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<BaseResult>(null) {
                    @Override
                    public void onSuccess(BaseResult o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }

    /**
     * 发送私聊消息
     *
     * @param body
     * @param tag
     */
    public void sendGroupMessage(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .sendGroupMessage(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<BaseResult>(null) {
                    @Override
                    public void onSuccess(BaseResult o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }
}

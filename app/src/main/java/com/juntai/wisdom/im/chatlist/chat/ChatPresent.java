package com.juntai.wisdom.im.chatlist.chat;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;

import com.juntai.disabled.basecomponent.base.BaseObserver;
import com.juntai.disabled.basecomponent.base.BaseResult;
import com.juntai.disabled.basecomponent.bean.BaseMenuBean;
import com.juntai.disabled.basecomponent.mvp.IModel;
import com.juntai.disabled.basecomponent.utils.RxScheduler;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppNetModule;
import com.juntai.wisdom.im.base.BaseAppPresent;
import com.juntai.wisdom.im.bean.GroupBean;
import com.juntai.wisdom.im.bean.GroupListBean;
import com.juntai.wisdom.im.bean.HomePageMenuBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MessageListBean;
import com.juntai.wisdom.im.bean.MyMenuBean;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.utils.UserInfoManager;

import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.Logging;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoDecoderFactory;
import org.webrtc.VideoEncoderFactory;
import org.webrtc.audio.JavaAudioDeviceModule;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-11-07 14:27
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-11-07 14:27
 */
public class ChatPresent extends BaseAppPresent<IModel, MainContract.IBaseView> {

    public final static String MORE_ACTION_PIC = "照片";
    public final static String MORE_ACTION_TAKE_PHOTO = "拍照";
    public final static String MORE_ACTION_VIDEO_CALL = "视频通话";
    public final static String MORE_ACTION_LOCATION = "位置";
    public final static String MORE_ACTION_SECRET_CHAT = "密聊";
    public final static String MORE_ACTION_FILE = "文件";
    public final static String MORE_ACTION_CARD = "名片";
    public final static String EDIT_MSG_DELETE = "删除";
    public final static String EDIT_MSG_FORWARD = "转发";



    /**
     * 获取适配器数据
     *
     * @return
     */
    public List<HomePageMenuBean> getEditChatMsgMenus(MessageBodyBean messageBodyBean) {
        List<HomePageMenuBean> arrays = new ArrayList<>();
        if (messageBodyBean.getMsgType()==0) {
            arrays.add(new HomePageMenuBean(MainContract.COPY_MSG, R.mipmap.edit_msg_copy_icon));
        }
        arrays.add(new HomePageMenuBean(MainContract.FORWARD_MSG, R.mipmap.edit_msg_forward_icon));
        if (messageBodyBean.getMsgType()!=7) {
            //名片不能收藏
            arrays.add(new HomePageMenuBean(MainContract.COLLECTION_MSG, R.mipmap.edit_msg_collection_icon));
        }
        arrays.add(new HomePageMenuBean(MainContract.DELETE_MSG, R.mipmap.edit_msg_delete_icon));
        arrays.add(new HomePageMenuBean(MainContract.SELECT_MORE, R.mipmap.edit_msg_select_icon));
        return arrays;
    }

    /**
     * 获取更多功能菜单
     *
     * @return
     */
    public List<MyMenuBean> getMoreActionMenus(int chatType) {
        List<MyMenuBean> arrays = new ArrayList<>();
        arrays.add(new MyMenuBean(MORE_ACTION_PIC, R.mipmap.more_action_pic));
        arrays.add(new MyMenuBean(MORE_ACTION_TAKE_PHOTO, R.mipmap.more_action_take_photo));
        if (0==chatType) {
            arrays.add(new MyMenuBean(MORE_ACTION_VIDEO_CALL, R.mipmap.more_action_video_call));
        }
        arrays.add(new MyMenuBean(MORE_ACTION_LOCATION, R.mipmap.more_action_location));
        if (0==chatType) {
            arrays.add(new MyMenuBean(MORE_ACTION_SECRET_CHAT, R.mipmap.more_action_secret_chat));
        }
        arrays.add(new MyMenuBean(MORE_ACTION_FILE, R.mipmap.more_action_file));
        arrays.add(new MyMenuBean(MORE_ACTION_CARD, R.mipmap.more_action_card));
        return arrays;
    }

    @Override
    protected IModel createModel() {
        return null;
    }
    public void topChat(RequestBody requestBody, String tag) {
        AppNetModule.createrRetrofit()
                .topChat(requestBody)
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
     * @param body
     * @param tag
     */
    public void getPrivateUnreadMsg(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .getPrivateUnreadMsg(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<MessageListBean>(null) {
                    @Override
                    public void onSuccess(MessageListBean o) {
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
    public void getGroupUnreadMsg(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .getGroupUnreadMsg(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<MessageListBean>(null) {
                    @Override
                    public void onSuccess(MessageListBean o) {
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
    public void requestVideoCall(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .requestVideoCall(body)
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
     * @param body
     * @param tag
     */
    public void deleteFriend(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .deleteFriend(body)
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
     * @param body
     * @param tag
     */
    public void blockFriend(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .blockFriend(body)
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
     * @param body
     * @param tag
     */
    public void accessVideoCall(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .accessVideoCall(body)
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
     * @param body
     * @param tag
     */
    public void rejectVideoCall(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .rejectVideoCall(body)
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
     * @param body
     * @param tag
     */
    public void getGroupQRcode(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .getGroupQRcode(body)
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
     * @param body
     * @param tag
     */
    public void getGroupList(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .getGroupList(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<GroupListBean>(getView()) {
                    @Override
                    public void onSuccess(GroupListBean o) {
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
    public void creatGroup(List<Integer> userList, String tag) {
        AppNetModule.createrRetrofit()
                .creatGroup(UserInfoManager.getUserId(),UserInfoManager.getUserToken(),1,UserInfoManager.getUserNickName(),userList)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<GroupBean>(getView()) {
                    @Override
                    public void onSuccess(GroupBean o) {
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
    public void quitGroup(String groupUuid,List<Integer> userIds, String tag) {
        AppNetModule.createrRetrofit()
                .quitGroup(groupUuid,userIds)
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
     * @param tag
     */
    public void ownerQuitGroup(String groupUuid,int userId, String tag) {
        AppNetModule.createrRetrofit()
                .ownerQuitGroup(groupUuid,userId)
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
     * @param body
     * @param tag
     */
    public void topGroupChat(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .topGroupChat(body)
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
     * @param body
     * @param tag
     */
    public void getGroupPeoples(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .getGroupPeoples(body)
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










    /*====================================================    videocall   ==============================================================*/

    public PeerConnectionFactory createPeerConnectionFactory(EglBase mRootEglBase, Context context) {
        final VideoEncoderFactory encoderFactory;
        final VideoDecoderFactory decoderFactory;
//使用外置麦克风
        JavaAudioDeviceModule.Builder admBuilder = JavaAudioDeviceModule.builder(context);

        admBuilder.setAudioSource(MediaRecorder.AudioSource.MIC);
        encoderFactory = new DefaultVideoEncoderFactory(
                mRootEglBase.getEglBaseContext(),
                false /* enableIntelVp8Encoder */,
                true);
        decoderFactory = new DefaultVideoDecoderFactory(mRootEglBase.getEglBaseContext());

        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions.builder(context)
                .setEnableInternalTracer(true)
                .createInitializationOptions());

        PeerConnectionFactory.Builder builder = PeerConnectionFactory.builder()
                .setAudioDeviceModule(admBuilder.createAudioDeviceModule())
                .setVideoEncoderFactory(encoderFactory)
                .setVideoDecoderFactory(decoderFactory);
        builder.setOptions(null);

        return builder.createPeerConnectionFactory();
    }

    /*
     * Read more about Camera2 here
     * https://developer.android.com/reference/android/hardware/camera2/package-summary.html
     **/
    public VideoCapturer createVideoCapturer(Context context, boolean isFrontCamera) {
        if (Camera2Enumerator.isSupported(context)) {
            return createCameraCapturer(new Camera2Enumerator(context), isFrontCamera);
        } else {
            return createCameraCapturer(new Camera1Enumerator(true), isFrontCamera);
        }
    }

    private VideoCapturer createCameraCapturer(CameraEnumerator enumerator, boolean isFrontCamera) {
        final String[] deviceNames = enumerator.getDeviceNames();

        // First, try to find front facing camera
        Log.d(TAG, "Looking for front facing cameras.");
        if (isFrontCamera) {
            for (String deviceName : deviceNames) {
                if (enumerator.isFrontFacing(deviceName)) {
                    Logging.d(TAG, "Creating front facing camera capturer.");
                    VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                    if (videoCapturer != null) {
                        return videoCapturer;
                    }
                }
            }
        } else {
            // Front facing camera not found, try something else
            Log.d(TAG, "Looking for other cameras.");
            for (String deviceName : deviceNames) {
                if (!enumerator.isFrontFacing(deviceName)) {
                    Logging.d(TAG, "Creating other camera capturer.");
                    VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                    if (videoCapturer != null) {
                        return videoCapturer;
                    }
                }
            }
        }
        return null;
    }



    /**
     * 转发方式
     * @return
     */
    public List<BaseMenuBean>  getForwardTypes() {
        List<BaseMenuBean> calls = new ArrayList<>();
        calls.add(new BaseMenuBean("逐条转发"));
        calls.add(new BaseMenuBean("合并转发"));
        return calls;
    }


    public List<MyMenuBean> getBoottomEditMsgAdapterData() {
        List<MyMenuBean> arrays = new ArrayList<>();
        arrays.add(new MyMenuBean(EDIT_MSG_FORWARD, R.mipmap.forward_black_icon));
        arrays.add(new MyMenuBean(EDIT_MSG_DELETE, R.mipmap.delete_black_icon));
        return arrays;
    }
}

package com.juntai.wisdom.im.base.displayPicVideo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.bean.BaseMenuBean;
import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.basecomponent.widght.BaseBottomDialog;
import com.juntai.disabled.federation.R;
import com.juntai.disabled.video.CustomStandardGSYVideoPlayer;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseAppFragment;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.chatlist.chat.ChatPresent;
import com.juntai.wisdom.im.chatlist.chat.chatInfo.ChatInfoActivity;
import com.juntai.wisdom.im.chatlist.chat.forwardMsg.ForwardMsgActivity;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.utils.UserInfoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;

import java.io.File;
import java.util.Objects;

import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-02-27 11:48
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-02-27 11:48
 */
public class DisplayVideoFragment extends BaseAppFragment<ChatPresent> implements MainContract.IBaseView {


    private static String VIDEOPATH = "VIDEOPATH";
    private static String MSG = "msgStr";
    private CustomStandardGSYVideoPlayer videoPlayer;
    boolean isPlay, isPause;
    private MessageBodyBean messageBodyBean;

    @Override
    protected ChatPresent createPresenter() {
        return new ChatPresent();
    }


    public static DisplayVideoFragment getInstance(String videoPath, MessageBodyBean msgJson) {
        DisplayVideoFragment fragment = new DisplayVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(VIDEOPATH, videoPath);
        bundle.putParcelable(MSG, msgJson);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    protected boolean canCancelLoadingDialog() {
        return false;
    }

    @Override
    protected void lazyLoad() {
        messageBodyBean = getArguments().getParcelable(MSG);
        videoPlayer = (CustomStandardGSYVideoPlayer) getView(R.id.video_player);
        //RTMP播放需切换至exo播放
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);
        String videoPath = getArguments().getString(VIDEOPATH);
        /**如果是本人发送的视频 文件地址路径是
         * 如果是非本人发送的适配 文件地址是getAppVideoPath()+getSavedFileName(messageBodyBean)/文件
         */
        if (messageBodyBean.getFromUserId() == UserInfoManager.getUserId()) {
            // : 2022-03-09  本人发的视频
            if (FileCacheUtils.isFileExists(messageBodyBean.getLocalCatchPath())) {
                videoPlayer.setUp(messageBodyBean.getLocalCatchPath(), false, "");
            } else {
                loadNetVideoFile(videoPath);
            }
        } else {
            loadNetVideoFile(videoPath);
        }


        //增加封面
//        ImageView imageView = new ImageView(getActivity());
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setImageResource(R.drawable.empty_drawable);
//        videoPlayer.setThumbImageView(imageView);
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        //设置旋转
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);
        //设置返回按键功能
        videoPlayer.startPlayLogic();
//        videoPlayer.getStartButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (videoPlayer.getCurrentState()== GSYVideoView.CURRENT_STATE_NORMAL ||videoPlayer.getCurrentState()== GSYVideoView.CURRENT_STATE_ERROR) {
//                    videoPlayer.startPlayLogic();
//                }else{
//                    videoPlayer.onVideoResume();
//                }
//
//            }
//        });
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

        videoPlayer.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                isPlay = true;
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
            }
            // : 2022-02-27 所有的播放回调
        });

        videoPlayer.getDownloadActionIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // : 2022-02-27 将缓存到本地的文件复制到外部存储器上
                downloadVideoFile();

            }
        });
        videoPlayer.getMoreActionIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreActionAdapter actionAdapter = new MoreActionAdapter(R.layout.more_action);

                ((PicVideoDisplayActivity) getActivity()).initBottomDialog(getBaseActivity().getBaseBottomDialogMenus(
                        new BaseMenuBean("转发", R.mipmap.forword_icon)
                        ,new BaseMenuBean("分享", R.mipmap.share_icon)
                        , new BaseMenuBean("收藏", R.mipmap.collect_icon)
                        , new BaseMenuBean("保存到本地", R.mipmap.save_icon)
                        ), actionAdapter
                        , new GridLayoutManager(mContext, 5), new BaseBottomDialog.OnItemClick() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                switch (position) {
                                    case 0:
                                        // : 2022-02-28 转发
                                        startActivityForResult(new Intent(mContext, ForwardMsgActivity.class).putExtra(BASE_PARCELABLE, messageBodyBean), ChatInfoActivity.CHAT_INFO_REQUEST);
                                        ((PicVideoDisplayActivity) getActivity()).releaseBottomSheetDialog();
                                        break;
                                    case 1:
                                        // TODO: 2022-02-28 分享
                                        break;
                                    case 2:
                                        // : 2022-02-28 收藏视频
                                        mPresenter.collectFile(mContext,messageBodyBean,FileCacheUtils.getAppVideoPath(false));

                                        break;
                                    case 3:
                                        // : 2022-02-28 下载视频
                                        downloadVideoFile();

                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
            }
        });

    }

    private void downloadVideoFile() {
        String oldFilePath = null;
        if (messageBodyBean.getFromUserId()== UserInfoManager.getUserId()) {
            //自己发的视频 本地有记录
            oldFilePath = messageBodyBean.getLocalCatchPath();
        }else {
            oldFilePath = FileCacheUtils.isVideoFileExistsInDir(getSavedFileNameWithoutSuffix(messageBodyBean), true);

        }
        if (!TextUtils.isEmpty(oldFilePath)) {
            File file = new File(oldFilePath);
            FileCacheUtils.copyFile((PicVideoDisplayActivity)getActivity(),oldFilePath, FileCacheUtils.getAppVideoPath(false) + getSavedFileNameWithoutSuffix(messageBodyBean)+".mp4",false);
        } else {
            ToastUtils.toast(mContext, "视频缓存完成之后才可保存到本地");
        }
    }

    private void loadNetVideoFile(String videoPath) {
        if (!TextUtils.isEmpty(FileCacheUtils.isVideoFileExistsInDir(getSavedFileNameWithoutSuffix(messageBodyBean), true))) {
//            ToastUtils.toast(getBaseActivity(), "对方发送的   本地已缓存");
            //视频存在
            videoPath = FileCacheUtils.getVideoFileInDir(getSavedFileNameWithoutSuffix(messageBodyBean), true);
            videoPlayer.setUp(videoPath, false, "");
        } else {
//            ToastUtils.toast(getBaseActivity(), "对方发送的   本地没有缓存");
            videoPlayer.setUp(videoPath, true, new File(FileCacheUtils.getAppVideoPath(true) + getSavedFileNameWithoutSuffix(messageBodyBean)), "");
        }
    }




    @Override
    protected void lazyloadGone() {
        super.lazyloadGone();
        if (videoPlayer != null) {
            videoPlayer.onVideoPause();
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.display_video_layout;
    }


    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object o) {
        switch (tag) {
            case AppHttpPath.COLLECT:
                ToastUtils.toast(mContext, "收藏成功");
                ((PicVideoDisplayActivity) getActivity()).releaseBottomSheetDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        if (videoPlayer != null) {
            videoPlayer.onVideoPause();

        }
        super.onPause();
        isPause = true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            videoPlayer.getCurrentPlayer().release();
            videoPlayer.release();
        }
        videoPlayer = null;
    }


}

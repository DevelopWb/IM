package com.juntai.wisdom.im.base.displayPicVideo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.bean.BaseMenuBean;
import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.basecomponent.widght.BaseBottomDialog;
import com.juntai.disabled.federation.R;
import com.juntai.disabled.video.img.PhotoView;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseAppFragment;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.chatlist.chat.ChatPresent;
import com.juntai.wisdom.im.chatlist.chat.chatInfo.ChatInfoActivity;
import com.juntai.wisdom.im.chatlist.chat.forwardMsg.ForwardMsgActivity;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.utils.UserInfoManager;

import java.io.File;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-02-27 11:48
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-02-27 11:48
 */
public class DisplayPhotoFragment extends BaseAppFragment<ChatPresent> implements View.OnClickListener, MainContract.IBaseView {


    private PhotoView mPhotoDisplayPv;
    private ImageView mDisplayPicActionMoreIv;
    private ImageView mDisplayPicActionDownloadIv;
    private static String PICPATH = "picPath";
    private static String MSGSTR = "msgStr";

    private String path;
    private MessageBodyBean messageBodyBean;

    @Override
    protected ChatPresent createPresenter() {
        return new ChatPresent();
    }


    public static DisplayPhotoFragment getInstance(String picPath, MessageBodyBean msgStr) {
        DisplayPhotoFragment fragment = new DisplayPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PICPATH, picPath);
        bundle.putParcelable(MSGSTR, msgStr);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    protected boolean canCancelLoadingDialog() {
        return false;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.display_pic_layout;
    }

    @Override
    protected void initView() {
        path = getArguments().getString(PICPATH);
        messageBodyBean = getArguments().getParcelable(MSGSTR);
        mPhotoDisplayPv = (PhotoView) getView(R.id.photo_display_pv);
        mDisplayPicActionMoreIv = (ImageView) getView(R.id.display_pic_action_more_iv);
        mDisplayPicActionMoreIv.setOnClickListener(this);
        mDisplayPicActionDownloadIv = (ImageView) getView(R.id.display_pic_action_download_iv);
        mDisplayPicActionDownloadIv.setOnClickListener(this);
        mPhotoDisplayPv.enable();
        if (messageBodyBean.getFromUserId() == UserInfoManager.getUserId()) {
            // : 2022-03-09  本人发的视频
            if (FileCacheUtils.isFileExists(messageBodyBean.getLocalCatchPath())) {
                //本地原图片存在
                ImageLoadUtil.loadImageCache(getActivity(), messageBodyBean.getLocalCatchPath(), mPhotoDisplayPv);
            } else {
                //本地原图片被删除
                loadNetImageFile();
            }
        } else {
            loadNetImageFile();
        }


    }

    private void loadNetImageFile() {
        if (!FileCacheUtils.isFileExists(FileCacheUtils.getAppImagePath(true) + getSavedFileName(messageBodyBean))) {
            //本地没有缓存
            ImageLoadUtil.loadImageCache(getActivity(), messageBodyBean.getContent(), mPhotoDisplayPv);
            ImageLoadUtil.setGlideDownloadFileToLocal((PicVideoDisplayActivity) getActivity(), mContext, messageBodyBean.getContent(), true);
//            ToastUtils.toast(getBaseActivity(), "对方发送的   本地没有缓存");

        } else {
//            ToastUtils.toast(getBaseActivity(), "对方发送的   本地已缓存");
            ImageLoadUtil.loadImageCache(getActivity(), FileCacheUtils.getAppImagePath(true) + getSavedFileName(messageBodyBean), mPhotoDisplayPv);

        }
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
    public void onClick(View v) {
        MoreActionAdapter actionAdapter = new MoreActionAdapter(R.layout.more_action);
        int id = v.getId();
        if (id == R.id.display_pic_action_more_iv) {

            ((PicVideoDisplayActivity) getActivity()).initBottomDialog(getBaseActivity().getBaseBottomDialogMenus(
                    new BaseMenuBean("转发", R.mipmap.forword_icon)
                    , new BaseMenuBean("分享", R.mipmap.share_icon)
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
//                                    ShareTool.shareForMob(mContext,
//                                            "",
//                                            messageBodyBean.getContent(),
//                                            "",
//                                            messageBodyBean.getContent(),
//                                            new PlatformActionListener() {
//                                                @Override
//                                                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                                                    //  分享成功后的操作或者提示
//                                                    ToastUtils.success(mContext, "分享成功！");
//                                                }
//
//                                                @Override
//                                                public void onError(Platform platform, int i, Throwable throwable) {
//                                                    //  失败，打印throwable为错误码
//                                                    ToastUtils.warning(mContext, "分享失败！");
//                                                }
//
//                                                @Override
//                                                public void onCancel(Platform platform, int i) {
//                                                    //  分享取消操作
//                                                    ToastUtils.warning(mContext, "分享已取消！");
//                                                }
//                                            });
                                    break;
                                case 2:
                                    // : 2022-02-28 收藏图片

                                    mPresenter.collectFile(mContext,messageBodyBean,FileCacheUtils.getAppImagePath(false));

                                    break;
                                case 3:
                                    // : 2022-02-28 下载图片
                                    downloadImage();
                                    break;
                                default:
                                    break;
                            }
                        }
                    });

        } else if (id == R.id.display_pic_action_download_iv) {
            // : 2022-02-28 图片下载
            downloadImage();
        } else if (id == R.id.photo_display_pv) {
            ((PicVideoDisplayActivity) getActivity()).onBackPressed();
        }
    }


    private void downloadImage() {
        String oldFilePath = null;
        String newFilePath = null;
        if (messageBodyBean.getFromUserId() == UserInfoManager.getUserId()) {
            //自己发的图片 本地有记录
            oldFilePath = messageBodyBean.getLocalCatchPath();
            File oldFile = new File(oldFilePath);
            newFilePath =FileCacheUtils.getAppImagePath(false)+oldFile.getName();
        } else {
            oldFilePath = FileCacheUtils.getAppImagePath(true) + getSavedFileName(messageBodyBean);
            newFilePath =  FileCacheUtils.getAppImagePath(false) + getSavedFileName(messageBodyBean);
        }
        FileCacheUtils.copyFile((PicVideoDisplayActivity) getActivity(), oldFilePath, newFilePath, false);

    }

}

package com.juntai.wisdom.im.base.displayPicVideo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.disabled.basecomponent.base.BaseWebViewActivity;
import com.juntai.disabled.basecomponent.bean.BaseMenuBean;
import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.basecomponent.utils.GsonTools;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.basecomponent.widght.BaseBottomDialog;
import com.juntai.disabled.federation.R;
import com.juntai.disabled.video.img.PhotoView;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseAppFragment;
import com.juntai.wisdom.im.bean.GroupDetailInfoBean;
import com.juntai.wisdom.im.bean.GroupInfoByUuidBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.UserInfoByUUIDBean;
import com.juntai.wisdom.im.chat_module.chat.ChatPresent;
import com.juntai.wisdom.im.chat_module.chat.chatInfo.ChatInfoActivity;
import com.juntai.wisdom.im.chat_module.chat.forwardMsg.ForwardMsgActivity;
import com.juntai.wisdom.im.chat_module.groupchat.GroupChatActivity;
import com.juntai.wisdom.im.contact.ContactorInfoActivity;
import com.juntai.wisdom.im.contact.group.JoinGroupByUuidActivity;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.utils.UserInfoManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * @Author: tobato
 * @Description: ????????????
 * @CreateDate: 2022-02-27 11:48
 * @UpdateUser: ?????????
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
        mPhotoDisplayPv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //?????????????????????  ?????????????????? ??????????????????????????????

                mDisplayPicActionMoreIv.performClick();

                return true;
            }
        });
        if (messageBodyBean.getFromUserId() == UserInfoManager.getUserId()) {
            // : 2022-03-09  ??????????????????
            if (FileCacheUtils.isFileExists(messageBodyBean.getLocalCatchPath())) {
                //?????????????????????
                ImageLoadUtil.loadImageCache(getActivity(), messageBodyBean.getLocalCatchPath(), mPhotoDisplayPv);
            } else {
                //????????????????????????  ?????????????????????????????????
                loadNetImageFile();
            }
        } else {
            loadNetImageFile();
        }


    }

    private void loadNetImageFile() {
        if (!FileCacheUtils.isFileExists(FileCacheUtils.getAppImagePath(true) + getSavedFileName(messageBodyBean))) {
            //??????????????????
            ImageLoadUtil.loadImageCache(getActivity(), messageBodyBean.getContent(), mPhotoDisplayPv);
            ImageLoadUtil.setGlideDownloadFileToLocal((PicVideoDisplayActivity) getActivity(), mContext, messageBodyBean.getContent(), true);
//            ToastUtils.toast(getBaseActivity(), "???????????????   ??????????????????");

        } else {
//            ToastUtils.toast(getBaseActivity(), "???????????????   ???????????????");
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
                ToastUtils.toast(mContext, "????????????");
                ((PicVideoDisplayActivity) getActivity()).releaseBottomSheetDialog();
                break;
            case AppHttpPath.ADD_FRIEND_BY_UUID:
                // : 2022-01-18 ????????????

                UserInfoByUUIDBean userInfoByUUIDBean = (UserInfoByUUIDBean) o;
                if (userInfoByUUIDBean != null && userInfoByUUIDBean.getData() != null) {
                    startActivity(new Intent(mContext, ContactorInfoActivity.class).putExtra(BaseActivity.BASE_ID, userInfoByUUIDBean.getData().getId()));
                }

                break;
            case AppHttpPath.JOIN_GROUP_BY_UUID:
                GroupInfoByUuidBean groupInfoByUuidBean = (GroupInfoByUuidBean) o;
                if (groupInfoByUuidBean != null) {
                    GroupInfoByUuidBean.DataBean dataBean = groupInfoByUuidBean.getData();
                    GroupDetailInfoBean groupBean = GsonTools.modelA2B(dataBean, GroupDetailInfoBean.class);
                    groupBean.setGroupId(dataBean.getId());
                    startActivity(new Intent(mContext, GroupChatActivity.class)
                            .putExtra(BASE_ID, groupBean.getGroupId()));
                }
                break;
            default:
                break;
        }
    }

    /**
     * ?????????????????????Bitmap
     *
     * @param path ?????????????????????
     * @return
     */
    public static Bitmap getImageBitmap(String path) {
        Bitmap bitmap = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onClick(View v) {
        MoreActionAdapter actionAdapter = new MoreActionAdapter(R.layout.more_action);
        int id = v.getId();
        if (id == R.id.display_pic_action_more_iv) {
            List<BaseMenuBean> menuBeans = getBaseActivity().getBaseBottomDialogMenus(
                    new BaseMenuBean(BaseMenuBean.PIC_MENU_FORWARD, R.mipmap.forword_icon)
                    , new BaseMenuBean(BaseMenuBean.PIC_MENU_SHARE, R.mipmap.share_icon)
                    , new BaseMenuBean(BaseMenuBean.PIC_MENU_COLLECT, R.mipmap.collect_icon)
                    , new BaseMenuBean(BaseMenuBean.PIC_MENU_SAVE, R.mipmap.save_icon));
            Bitmap bitmap = null;
            String result = null;
            if (messageBodyBean.getFromUserId() == UserInfoManager.getUserId()) {
                bitmap = getImageBitmap(messageBodyBean.getLocalCatchPath());
            } else {
                bitmap = getImageBitmap(FileCacheUtils.getAppImagePath(true) + getSavedFileName(messageBodyBean));
            }
            //???QRCODE_SCAN_TYPE ????????? DATAMATRIX_SCAN_TYPE???????????????QR???Data Matrix??????
            HmsScanAnalyzerOptions options = new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE, HmsScan.DATAMATRIX_SCAN_TYPE).setPhotoMode(true).create();
            HmsScan[] hmsScans = ScanUtil.decodeWithBitmap(mContext, bitmap, options);
            //??????????????????
            if (hmsScans != null && hmsScans.length > 0) {
                result = hmsScans[0].showResult;
                //??????????????????
                menuBeans.add(new BaseMenuBean(BaseMenuBean.PIC_MENU_SPOT_QRCODE, R.mipmap.create_qrcode_icon));
            }


            String finalResult = result;
            ((PicVideoDisplayActivity) getActivity()).initBottomDialog(menuBeans
                    , actionAdapter
                    , new GridLayoutManager(mContext, 5), new BaseBottomDialog.OnItemClick() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            BaseMenuBean menuBean = (BaseMenuBean) adapter.getItem(position);
                            ((PicVideoDisplayActivity) getActivity()).releaseBottomSheetDialog();
                            switch (menuBean.getName()) {
                                case BaseMenuBean.PIC_MENU_FORWARD:
                                    // : 2022-02-28 ??????
                                    startActivityForResult(new Intent(mContext, ForwardMsgActivity.class).putExtra(BASE_PARCELABLE, messageBodyBean), ChatInfoActivity.CHAT_INFO_REQUEST);
                                    break;
                                case BaseMenuBean.PIC_MENU_SHARE:
                                    // TODO: 2022-02-28 ??????
//                                    ShareTool.shareForMob(mContext,
//                                            "",
//                                            messageBodyBean.getContent(),
//                                            "",
//                                            messageBodyBean.getContent(),
//                                            new PlatformActionListener() {
//                                                @Override
//                                                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                                                    //  ????????????????????????????????????
//                                                    ToastUtils.success(mContext, "???????????????");
//                                                }
//
//                                                @Override
//                                                public void onError(Platform platform, int i, Throwable throwable) {
//                                                    //  ???????????????throwable????????????
//                                                    ToastUtils.warning(mContext, "???????????????");
//                                                }
//
//                                                @Override
//                                                public void onCancel(Platform platform, int i) {
//                                                    //  ??????????????????
//                                                    ToastUtils.warning(mContext, "??????????????????");
//                                                }
//                                            });
                                    break;
                                case BaseMenuBean.PIC_MENU_COLLECT:
                                    // : 2022-02-28 ????????????

                                    mPresenter.collectFile(mContext, messageBodyBean, FileCacheUtils.getAppImagePath(false));

                                    break;
                                case BaseMenuBean.PIC_MENU_SAVE:
                                    // : 2022-02-28 ????????????
                                    downloadImage();
                                    break;

                                case BaseMenuBean.PIC_MENU_SPOT_QRCODE:
                                    // : 2022/4/6 ???????????????
                                    resolveQrcode(finalResult);


                                    break;
                                default:
                                    break;
                            }
                        }
                    });

        } else if (id == R.id.display_pic_action_download_iv) {
            // : 2022-02-28 ????????????
            downloadImage();
        } else if (id == R.id.photo_display_pv) {
            ((PicVideoDisplayActivity) getActivity()).onBackPressed();
        }
    }

    /**
     * ???????????????
     * @param result
     */
    public void resolveQrcode(String result) {
        if (result.contains("juntaikeji")&&result.contains("uuid=")&&result.contains("&type=")) {
            //???????????????
            String uuid = result.substring(result.indexOf("=") + 1, result.indexOf("&"));
            String type = result.substring(result.lastIndexOf("=") + 1, result.length());
            if ("1".equals(type)) {
                //??????
                if (UserInfoManager.getUserUUID().equals(uuid)) {
                    ToastUtils.error(mContext,"???????????????????????????");
                    return;
                }
                mPresenter.addFriendByUuid(getBaseAppActivity().getBaseBuilder().add("uuid", uuid).build(), AppHttpPath.ADD_FRIEND_BY_UUID);
            } else {
                //??????
                // : 2022-01-18 ?????????
                if (!getBaseAppActivity().isGroupExsit(uuid)) {
                    //?????????????????????
                    startActivity(new Intent(mContext, JoinGroupByUuidActivity.class).putExtra(BASE_STRING, uuid));
                } else {
                    //????????????  ??????????????? ??????????????????????????????
                    mPresenter.joinGroupByUuid(getBaseAppActivity().getBaseBuilder().add("uuid", uuid).build(), AppHttpPath.JOIN_GROUP_BY_UUID);
                }
            }
        }else {
            startActivity(new Intent(mContext, BaseWebViewActivity.class).putExtra("url", result));
        }
    }

    private void downloadImage() {
        String oldFilePath = null;
        String newFilePath = null;
        if (messageBodyBean.getFromUserId() == UserInfoManager.getUserId()) {
            //?????????????????? ???????????????
            oldFilePath = messageBodyBean.getLocalCatchPath();
            File oldFile = new File(oldFilePath);
            newFilePath = FileCacheUtils.getAppImagePath(false) + oldFile.getName();
        } else {
            oldFilePath = FileCacheUtils.getAppImagePath(true) + getSavedFileName(messageBodyBean);
            newFilePath = FileCacheUtils.getAppImagePath(false) + getSavedFileName(messageBodyBean);
        }
        FileCacheUtils.copyFile((PicVideoDisplayActivity) getActivity(), oldFilePath, newFilePath, false);

    }


}

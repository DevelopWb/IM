package com.juntai.wisdom.im.base;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.juntai.disabled.basecomponent.base.BaseWebViewActivity;
import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.basecomponent.utils.MD5;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.bdmap.act.LocateShowActivity;
import com.juntai.disabled.bdmap.utils.NagivationUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.selectPics.BaseSelectPicsActivity;
import com.juntai.wisdom.im.base.uploadFile.UploadFileBean;
import com.juntai.wisdom.im.base.uploadFile.UploadUtil;
import com.juntai.wisdom.im.base.uploadFile.listener.OnUploadListener;
import com.juntai.wisdom.im.bean.FinishVideoActivityMsgBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.chat_module.ChatDetailDisplayActivity;
import com.juntai.wisdom.im.chat_module.chat.PrivateChatActivity;
import com.juntai.wisdom.im.chat_module.chat.chatRecord.ChatRecordDetailActivity;
import com.juntai.wisdom.im.chat_module.chat.displayFile.FileDetailActivity;
import com.juntai.wisdom.im.utils.NotificationTool;
import com.juntai.wisdom.im.utils.ObjectBox;
import com.juntai.wisdom.im.utils.StringTools;
import com.juntai.wisdom.im.utils.UserInfoManager;
import com.tencent.smtt.sdk.TbsReaderView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnDetectBigUrlFileListener;
import org.wlf.filedownloader.listener.OnFileDownloadStatusListener;
import org.wlf.filedownloader.listener.simple.OnSimpleFileDownloadStatusListener;

import java.util.List;

import okhttp3.FormBody;

/**
 * @aouther tobato
 * @description ??????
 * @date 2020/4/27 8:48  app?????????
 */
public abstract class BaseAppActivity<P extends BasePresenter> extends BaseSelectPicsActivity<P> {

    public OnFileDownloadStatusListener mOnFileDownloadStatusListener;
    OnFileDownloadStatus onFileDownloadStatus;
    protected TbsReaderView mTbsReaderView;
    public UploadUtil mUploadUtil;

    private OnFileUploadStatus onFileUploadStatus;


    public void setOnFileUploadStatus(OnFileUploadStatus onFileUploadStatus) {
        this.onFileUploadStatus = onFileUploadStatus;
    }

    public void setOnFileDownloadStatus(OnFileDownloadStatus onFileDownloadStatus) {
        this.onFileDownloadStatus = onFileDownloadStatus;
    }

    /**
     * ????????????????????????
     *
     * @param content
     */
    public void copy(String content) {
// ????????????????????????
        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationTool.SHOW_NOTIFICATION = true;
        getToolbar().setBackgroundResource(R.drawable.sp_filled_gray_lighter);
        registFileDownloadListener();
        //?????????????????????
        mUploadUtil = new UploadUtil();
        mUploadUtil.setOnUploadListener(new OnUploadListener() {
            @Override
            public void onAllSuccess() {
                ToastUtils.toast(mContext, "onAllSuccess");
            }

            @Override
            public void onAllFailed() {

            }

            @Override
            public void onThreadProgressChange(UploadFileBean uploadFileBean, int position, int percent) {
                Log.d("onThreadProgressChange", "onThreadProgressChange" + uploadFileBean.getFilePath() + "---------" + percent);

                if (onFileUploadStatus != null) {
                    onFileUploadStatus.onUploadProgressChange(uploadFileBean, percent);
                }
            }

            @Override
            public void onThreadFinish(UploadFileBean uploadFileBean, int position) {
                if (onFileUploadStatus != null) {
                    onFileUploadStatus.onUploadFinish(uploadFileBean);
                }
            }

            @Override
            public void onThreadInterrupted(int position) {

            }
        });
    }


    public interface OnFileUploadStatus {
        void onUploadProgressChange(UploadFileBean uploadFileBean, int percent);

        void onUploadFinish(UploadFileBean uploadFileBean);

    }

    /**
     * ???????????????
     *
     * @return
     */
    public boolean initThirdShareLogic(Intent intent, Context context, Class cls) {
        if (intent != null) {
            String shareTitle = intent.getStringExtra("title");
            String shareUrl = intent.getStringExtra("shareUrl");
            String sharePic = intent.getStringExtra("picPath");
            String shareContent = intent.getStringExtra("content");
            String shareFromApp = intent.getStringExtra("shareFromApp");
            if (!TextUtils.isEmpty(shareUrl) && !TextUtils.isEmpty(shareTitle)) {
                Intent toIntent = new Intent();
                toIntent.putExtra("title", shareTitle);
                toIntent.putExtra("shareUrl", shareUrl);
                toIntent.putExtra("picPath", sharePic);
                toIntent.putExtra("content", shareContent);
                toIntent.putExtra("shareFromApp", shareFromApp);
                toIntent.setClass(context, cls);
                startActivity(toIntent);
                return true;
            }
        }
        return false;
    }

    protected void registFileDownloadListener() {


        mOnFileDownloadStatusListener = new OnSimpleFileDownloadStatusListener() {
            @Override
            public void onFileDownloadStatusRetrying(DownloadFileInfo downloadFileInfo, int retryTimes) {
                // ?????????????????????????????????????????????????????????????????????????????????????????????????????????retryTimes????????????????????????

            }

            @Override
            public void onFileDownloadStatusWaiting(DownloadFileInfo downloadFileInfo) {
                // ??????????????????????????????????????????????????????FileDownloader?????????????????????
            }

            @Override
            public void onFileDownloadStatusPreparing(DownloadFileInfo downloadFileInfo) {
                // ???????????????????????????????????????
            }

            @Override
            public void onFileDownloadStatusPrepared(DownloadFileInfo downloadFileInfo) {
                // ????????????????????????????????????????????????
            }

            @Override
            public void onFileDownloadStatusDownloading(DownloadFileInfo downloadFileInfo, float downloadSpeed, long
                    remainingTime) {
                // ???????????????downloadSpeed??????????????????????????????KB/s???remainingTime????????????????????????????????????
                if (onFileDownloadStatus != null) {
                    onFileDownloadStatus.onFileDownloadProgress(downloadFileInfo, downloadSpeed, remainingTime);
                }
            }

            @Override
            public void onFileDownloadStatusPaused(DownloadFileInfo downloadFileInfo) {
                // ??????????????????
                if (onFileDownloadStatus != null) {
                    onFileDownloadStatus.onFileDownloadPause(downloadFileInfo);
                }
            }

            @Override
            public void onFileDownloadStatusCompleted(DownloadFileInfo downloadFileInfo) {
                // ??????????????????????????????????????????????????????
                if (onFileDownloadStatus != null) {
                    onFileDownloadStatus.onFileDownloadSuccess(downloadFileInfo);
                }
            }

            @Override
            public void onFileDownloadStatusFailed(String url, DownloadFileInfo downloadFileInfo, FileDownloadStatusFailReason failReason) {
                // ??????????????????????????????????????????failReason??????????????????????????????????????????

                String failType = failReason.getType();
                String failUrl = failReason.getUrl();// ??????failUrl = url???url???failReason.getUrl()???????????????

                if (OnDetectBigUrlFileListener.DetectBigUrlFileFailReason.TYPE_HTTP_FILE_NOT_EXIST.equals(failType)) {
                    if (onFileDownloadStatus != null) {
                        onFileDownloadStatus.onFileDownloadErrorOfNoExist(downloadFileInfo);
                    }

                } else {
                    // ????????????....
                    if (onFileDownloadStatus != null) {
                        onFileDownloadStatus.onFileDownloadError(downloadFileInfo);
                    }
                }

                // ????????????????????????
                Throwable failCause = failReason.getCause();// ??????failReason.getOriginalCause()

                // ????????????????????????
                String failMsg = failReason.getMessage();// ??????failReason.getOriginalCause().getMessage()
            }
        };
        FileDownloader.registerDownloadStatusListener(mOnFileDownloadStatusListener);

    }

    /**
     * ??????????????????
     *
     * @param messageBodyBean
     * @return
     */
    public String getSavedFileName(MessageBodyBean messageBodyBean) {
        String content = messageBodyBean.getContent();
        return getSavedFileName(content);
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public String getSavedFileName(String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        if (content.contains("/")) {
            content = content.substring(content.lastIndexOf("/") + 1, content.length());
        }
        return content;
    }

    /**
     * ??????????????????  ??????
     *
     * @param messageBodyBean
     * @return
     */
    public String getSavedFileNameWithoutSuffix(MessageBodyBean messageBodyBean) {
        String content = messageBodyBean.getContent();
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        if (content.contains("/")) {
            content = content.substring(content.lastIndexOf("/") + 1, content.lastIndexOf("."));
        }
        return content;
    }

    /**
     * ???????????????
     *
     * @param downloadFileInfo
     */
    public void initProgressStatus(DownloadFileInfo downloadFileInfo, ProgressBar mFileProgressPb) {
        int totalSize = (int) downloadFileInfo.getFileSizeLong();
        int downloaded = (int) downloadFileInfo.getDownloadedSizeLong();
        double rate = (double) totalSize / Integer.MAX_VALUE;
        if (rate > 1.0) {
            totalSize = Integer.MAX_VALUE;
            downloaded = (int) (downloaded / rate);
        }
        mFileProgressPb.setMax(totalSize);
        mFileProgressPb.setProgress(downloaded);
    }

    public interface OnFileDownloadStatus {

        void onFileDownloadProgress(DownloadFileInfo downloadFileInfo, float downloadSpeed, long
                remainingTime);

        void onFileDownloadSuccess(DownloadFileInfo downloadFileInfo);

        void onFileDownloadErrorOfNoExist(DownloadFileInfo downloadFileInfo);

        void onFileDownloadError(DownloadFileInfo downloadFileInfo);

        void onFileDownloadPause(DownloadFileInfo downloadFileInfo);

    }


    /**
     * @param finishVideoActivityMsgBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //???ui????????????
    public void receiveMessage(FinishVideoActivityMsgBean finishVideoActivityMsgBean) {
        MessageBodyBean messageBodyBean = finishVideoActivityMsgBean.getMessageBodyBean();
        if (messageBodyBean != null) {
            switch (messageBodyBean.getMsgType()) {
                case 4:
                case 5:
                    receiveVideoCallMessageBody(messageBodyBean);
                    break;
                default:
                    break;
            }

        }

    }


    @Override
    protected boolean canCancelLoadingDialog() {
        return true;
    }

    /**
     * ??????????????????????????????
     *
     * @param uuid
     * @return
     */
    public boolean isGroupExsit(String uuid) {
        return ObjectBox.checkGroupIsExist(uuid);
    }

    public void receiveVideoCallMessageBody(MessageBodyBean messageBodyBean) {
        /**
         * ???????????????????????????????????? ????????????????????????
         */
        if (!PrivateChatActivity.ACTIVITY_IS_ON) {
            ObjectBox.addMessage(messageBodyBean);
        }

    }

    @Override
    protected String getUpdateHttpUrl() {
        return AppHttpPath.APP_UPDATE;
    }

    /**
     * ??????
     *
     * @param endLatlng ?????????
     * @param endName   ???????????????
     */
    public void navigationLogic(LatLng endLatlng, String endName) {
        AlertDialog.Builder build = new AlertDialog.Builder(mContext);
        final String item_list[] = {"????????????", "????????????", "????????????"};
        build.setItems(item_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (item_list[which]) {
                    case "????????????":
                        NagivationUtils.getInstant().openTencent(mContext, endLatlng.latitude, endLatlng.longitude,
                                endName);
                        break;
                    case "????????????":
                        NagivationUtils.getInstant().openGaoDeMap(mContext, endLatlng.latitude, endLatlng.longitude,
                                endName);
                        break;
                    case "????????????":
                        NagivationUtils.getInstant().openBaiduMap(mContext, endLatlng.latitude, endLatlng.longitude,
                                endName);
                        break;
                    default:
                        break;
                }
            }
        });
        build.setTitle("?????????????????????");
        AlertDialog alertDialog = build.create();
        alertDialog.show();
    }


    /**
     * ???????????????????????????
     */
    public void broadcasetRefreshHead() {
        Intent intent = new Intent();
        intent.setAction("action.refreshHead");
        sendBroadcast(intent);
    }

    /**
     * ??????builder
     *
     * @return
     */
    public FormBody.Builder getBaseBuilder() {
        FormBody.Builder builder = new FormBody.Builder()
                .add("account", UserInfoManager.getUserAccount())
                .add("token", UserInfoManager.getUserToken())
                .add("type", "1")
                .add("userId", String.valueOf(UserInfoManager.getUserId()));
        return builder;
    }
//    /**
//     * ??????builder
//     *
//     * @return
//     */
//    public FormBody.Builder getBaseBuilderWithoutUserId() {
//        FormBody.Builder builder = new FormBody.Builder();
//        builder.add("account", UserInfoManager.getUserAccount());
//        builder.add("token", UserInfoManager.getUserToken());
//        return builder;
//    }


    //    /**
    //     * ?????????????????????
    //     *
    //     * @return
    //     */
    //    public boolean isInnerAccount() {
    //        return UserInfoManager.isTest();

    //    }
    @Override
    public void singleLogin() {
        //        ToastUtils.toast(mContext, "????????????,???????????????");
        //        Hawk.delete(HawkProperty.USER_INFO);
        //        ActivityManagerTool.getInstance().finishApp();
        //        startActivity(new Intent(mContext, LoginActivity.class));
    }

    @Override
    public void onLocationReceived(BDLocation bdLocation) {

    }

    /**
     * ???list???????????????????????????  ??????????????????
     *
     * @return
     */
    public String getListToString(List<String> arrays) {
        if (arrays == null || arrays.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(arrays.size());
        for (String selectedServicePeople : arrays) {
            sb.append(selectedServicePeople + ",");
        }
        String people = sb.toString();
        if (StringTools.isStringValueOk(people)) {
            people = people.substring(0, people.length() - 1);
        }
        return people;
    }

    /**
     * ??????????????????
     */
    public void copyTelephoneNum(String text) {
        //???????????????
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        //??????ClipData??????
        //???????????????????????????????????????????????????
        //????????????????????????????????????????????????
        ClipData clip = ClipData.newPlainText("simple text", text);
        //??????clipdata??????.
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public boolean requestLocation() {
        return false;
    }


    /**
     * ????????????
     *
     * @param pwd
     * @return
     */
    protected String encryptPwd(String account, String pwd) {
        return MD5.md5(String.format("%s#%s", account, pwd));
    }

    @Override
    protected void selectedPicsAndEmpressed(List<String> icons) {

    }


    @Override
    protected String getDownloadTitleRightName() {
        return null;
    }

    @Override
    protected String getDownLoadPath() {
        return null;
    }


    protected boolean addTbsReaderView(String filePath) {
        //1???????????????
        TbsReaderView.ReaderCallback readerCallback = new TbsReaderView.ReaderCallback() {
            @Override
            public void onCallBackAction(Integer integer, Object o, Object o1) {
                //?????????????????? TbsReaderView.ReaderCallback
            }
        };
        //2?????????TbsReaderView
        mTbsReaderView = new TbsReaderView(this, readerCallback);
        mBaseRootCol.addView(
                mTbsReaderView,
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        //3??????TbsReaderView ?????????RootLayout????????????????????????????????????????????????


        //4?????????????????????
        Bundle bundle = new Bundle();
        bundle.putString(TbsReaderView.KEY_FILE_PATH, filePath);
        bundle.putString(TbsReaderView.KEY_TEMP_PATH, getExternalFilesDir("temp").getAbsolutePath());
        String extensionName = parseFormat(filePath);

        //5?????????preOpen???????????????????????????????????? ??????tbs???????????????????????????false????????????????????????????????????
        boolean result = mTbsReaderView.preOpen(extensionName, false);
        if (result) {
            //6?????????openFile????????????
            mTbsReaderView.openFile(bundle);
            return true;
        }
        return false;
    }

    public String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @Override
    public void onBackPressed() {
        hideKeyboard(mBaseRootCol);
        super.onBackPressed();
    }


    protected boolean closeFile() {
        if (mTbsReaderView != null) {
            mBaseRootCol.removeView(mTbsReaderView);
            mTbsReaderView.onStop();
            mTbsReaderView = null;
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        //7????????????????????????onStop??????
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
        FileDownloader.unregisterDownloadStatusListener(mOnFileDownloadStatusListener);
        super.onDestroy();
    }

    /**
     * ??????????????????
     *  // TODO: 2022/4/18 ??????????????????????????? ????????????????????????
     * @param messageBodyBean
     */
    public void startToMsgDetail(Context mContext, MessageBodyBean messageBodyBean) {
        switch (messageBodyBean.getMsgType()) {
            case 0:
            case 1:
            case 2:
            case 3:
                startActivity(new Intent(mContext, ChatDetailDisplayActivity.class).putExtra(BASE_PARCELABLE, messageBodyBean));
                break;
            case 6:
                //????????????
                LocateShowActivity.startLocateActivity(mContext, Double.parseDouble(messageBodyBean.getLat()), Double.parseDouble(messageBodyBean.getLng()), messageBodyBean.getAddrName(), messageBodyBean.getAddrDes());

                break;
            case 8:
                startActivity(new Intent(mContext, FileDetailActivity.class).putExtra(BASE_PARCELABLE
                        , messageBodyBean));
                break;
            case 9:
                startActivity(new Intent(mContext, ChatRecordDetailActivity.class).putExtra(BASE_STRING, TextUtils.isEmpty(messageBodyBean.getQuoteMsg()) ? messageBodyBean.getContent() : messageBodyBean.getQuoteMsg()));
                break;
            case 11:
                startActivity(new Intent(mContext, BaseWebViewActivity.class).putExtra("url", messageBodyBean.getShareUrl()));
                break;
            default:
                break;
        }


    }

}

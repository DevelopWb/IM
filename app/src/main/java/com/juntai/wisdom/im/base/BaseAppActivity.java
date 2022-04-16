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
 * @description 描述
 * @date 2020/4/27 8:48  app的基类
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
     * 实现文本复制功能
     *
     * @param content
     */
    public void copy(String content) {
// 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationTool.SHOW_NOTIFICATION = true;
        getToolbar().setBackgroundResource(R.drawable.sp_filled_gray_lighter);
        registFileDownloadListener();
        //上传文件工具类
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
     * 第三方分享
     *
     * @return
     */
    public boolean initThirdShareLogic(Intent intent,Context context,Class cls) {
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
                // 正在重试下载（如果你配置了重试次数，当一旦下载失败时会尝试重试下载），retryTimes是当前第几次重试

            }

            @Override
            public void onFileDownloadStatusWaiting(DownloadFileInfo downloadFileInfo) {
                // 等待下载（等待其它任务执行完成，或者FileDownloader在忙别的操作）
            }

            @Override
            public void onFileDownloadStatusPreparing(DownloadFileInfo downloadFileInfo) {
                // 准备中（即，正在连接资源）
            }

            @Override
            public void onFileDownloadStatusPrepared(DownloadFileInfo downloadFileInfo) {
                // 已准备好（即，已经连接到了资源）
            }

            @Override
            public void onFileDownloadStatusDownloading(DownloadFileInfo downloadFileInfo, float downloadSpeed, long
                    remainingTime) {
                // 正在下载，downloadSpeed为当前下载速度，单位KB/s，remainingTime为预估的剩余时间，单位秒
                if (onFileDownloadStatus != null) {
                    onFileDownloadStatus.onFileDownloadProgress(downloadFileInfo, downloadSpeed, remainingTime);
                }
            }

            @Override
            public void onFileDownloadStatusPaused(DownloadFileInfo downloadFileInfo) {
                // 下载已被暂停
                if (onFileDownloadStatus != null) {
                    onFileDownloadStatus.onFileDownloadPause(downloadFileInfo);
                }
            }

            @Override
            public void onFileDownloadStatusCompleted(DownloadFileInfo downloadFileInfo) {
                // 下载完成（整个文件已经全部下载完成）
                if (onFileDownloadStatus != null) {
                    onFileDownloadStatus.onFileDownloadSuccess(downloadFileInfo);
                }
            }

            @Override
            public void onFileDownloadStatusFailed(String url, DownloadFileInfo downloadFileInfo, FileDownloadStatusFailReason failReason) {
                // 下载失败了，详细查看失败原因failReason，有些失败原因你可能必须关心

                String failType = failReason.getType();
                String failUrl = failReason.getUrl();// 或：failUrl = url，url和failReason.getUrl()会是一样的

                if (OnDetectBigUrlFileListener.DetectBigUrlFileFailReason.TYPE_HTTP_FILE_NOT_EXIST.equals(failType)) {
                    if (onFileDownloadStatus != null) {
                        onFileDownloadStatus.onFileDownloadErrorOfNoExist(downloadFileInfo);
                    }

                } else {
                    // 更多错误....
                    if (onFileDownloadStatus != null) {
                        onFileDownloadStatus.onFileDownloadError(downloadFileInfo);
                    }
                }

                // 查看详细异常信息
                Throwable failCause = failReason.getCause();// 或：failReason.getOriginalCause()

                // 查看异常描述信息
                String failMsg = failReason.getMessage();// 或：failReason.getOriginalCause().getMessage()
            }
        };
        FileDownloader.registerDownloadStatusListener(mOnFileDownloadStatusListener);

    }

    /**
     * 获取文件名称
     *
     * @param messageBodyBean
     * @return
     */
    public String getSavedFileName(MessageBodyBean messageBodyBean) {
        String content = messageBodyBean.getContent();
        return getSavedFileName(content);
    }

    /**
     * 获取文件名称
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
     * 获取文件名称  后缀
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
     * 进度条进度
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
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
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
     * 检测该群是否已经存在
     *
     * @param uuid
     * @return
     */
    public boolean isGroupExsit(String uuid) {
        return ObjectBox.checkGroupIsExist(uuid);
    }

    public void receiveVideoCallMessageBody(MessageBodyBean messageBodyBean) {
        /**
         * 如果当前界面不在聊天界面 将记录保存到本地
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
     * 导航
     *
     * @param endLatlng 目的地
     * @param endName   目的地名称
     */
    public void navigationLogic(LatLng endLatlng, String endName) {
        AlertDialog.Builder build = new AlertDialog.Builder(mContext);
        final String item_list[] = {"腾讯地图", "高德地图", "百度地图"};
        build.setItems(item_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (item_list[which]) {
                    case "腾讯地图":
                        NagivationUtils.getInstant().openTencent(mContext, endLatlng.latitude, endLatlng.longitude,
                                endName);
                        break;
                    case "高德地图":
                        NagivationUtils.getInstant().openGaoDeMap(mContext, endLatlng.latitude, endLatlng.longitude,
                                endName);
                        break;
                    case "百度地图":
                        NagivationUtils.getInstant().openBaiduMap(mContext, endLatlng.latitude, endLatlng.longitude,
                                endName);
                        break;
                    default:
                        break;
                }
            }
        });
        build.setTitle("请选择导航方式");
        AlertDialog alertDialog = build.create();
        alertDialog.show();
    }


    /**
     * 发送更新头像的广播
     */
    public void broadcasetRefreshHead() {
        Intent intent = new Intent();
        intent.setAction("action.refreshHead");
        sendBroadcast(intent);
    }

    /**
     * 获取builder
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
//     * 获取builder
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
    //     * 是否是内部账号
    //     *
    //     * @return
    //     */
    //    public boolean isInnerAccount() {
    //        return UserInfoManager.isTest();

    //    }
    @Override
    public void singleLogin() {
        //        ToastUtils.toast(mContext, "登录过期,请重新登录");
        //        Hawk.delete(HawkProperty.USER_INFO);
        //        ActivityManagerTool.getInstance().finishApp();
        //        startActivity(new Intent(mContext, LoginActivity.class));
    }

    @Override
    public void onLocationReceived(BDLocation bdLocation) {

    }

    /**
     * 将list中的数据转成字符串  并以逗号隔开
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
     * 复制电话号码
     */
    public void copyTelephoneNum(String text) {
        //获取剪贴版
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        //创建ClipData对象
        //第一个参数只是一个标记，随便传入。
        //第二个参数是要复制到剪贴版的内容
        ClipData clip = ClipData.newPlainText("simple text", text);
        //传入clipdata对象.
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public boolean requestLocation() {
        return false;
    }


    /**
     * 加密密码
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
        //1、设置回调
        TbsReaderView.ReaderCallback readerCallback = new TbsReaderView.ReaderCallback() {
            @Override
            public void onCallBackAction(Integer integer, Object o, Object o1) {
                //回调结果参考 TbsReaderView.ReaderCallback
            }
        };
        //2、创建TbsReaderView
        mTbsReaderView = new TbsReaderView(this, readerCallback);
        mBaseRootCol.addView(
                mTbsReaderView,
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        //3、将TbsReaderView 添加到RootLayout中（可添加到自定义标题栏的下方）


        //4、传入指定参数
        Bundle bundle = new Bundle();
        bundle.putString(TbsReaderView.KEY_FILE_PATH, filePath);
        bundle.putString(TbsReaderView.KEY_TEMP_PATH, getExternalFilesDir("temp").getAbsolutePath());
        String extensionName = parseFormat(filePath);

        //5、调用preOpen判断是否支持当前文件类型 （若tbs支持的文档类型返回false，则说明内核未加载成功）
        boolean result = mTbsReaderView.preOpen(extensionName, false);
        if (result) {
            //6、调用openFile打开文件
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
        //7、结束时一定调用onStop方法
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
        FileDownloader.unregisterDownloadStatusListener(mOnFileDownloadStatusListener);
        super.onDestroy();
    }

    /**
     * 消息详情展示
     * @param messageBodyBean
     */
    public  void startToMsgDetail(Context mContext,  MessageBodyBean messageBodyBean) {
        switch (messageBodyBean.getMsgType()) {
            case 1:
            case 2:
            case 3:
                startActivity(new Intent(mContext, ChatDetailDisplayActivity.class).putExtra(BASE_PARCELABLE,messageBodyBean));
                break;
            case 6:
                //位置信息
                LocateShowActivity.startLocateActivity(mContext, Double.parseDouble(messageBodyBean.getLat()), Double.parseDouble(messageBodyBean.getLng()), messageBodyBean.getAddrName(), messageBodyBean.getAddrDes());

                break;
            case 8:
                startActivity(new Intent(mContext, FileDetailActivity.class).putExtra(BASE_PARCELABLE
                        , messageBodyBean));
                break;
            case 9:
                startActivity(new Intent(mContext, ChatRecordDetailActivity.class).putExtra(BASE_STRING, TextUtils.isEmpty(messageBodyBean.getQuoteMsg())?messageBodyBean.getContent():messageBodyBean.getQuoteMsg()));
                break;
            case 11:
                startActivity(new Intent(mContext, BaseWebViewActivity.class).putExtra("url", messageBodyBean.getShareUrl()));
                break;
            default:
                break;
        }



    }

}

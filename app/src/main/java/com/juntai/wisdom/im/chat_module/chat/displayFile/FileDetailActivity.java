package com.juntai.wisdom.im.chat_module.chat.displayFile;

import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.chat_module.chat.ChatPresent;
import com.juntai.wisdom.im.utils.MyFileProvider;
import com.juntai.wisdom.im.utils.UrlFormatUtil;
import com.juntai.wisdom.im.utils.UserInfoManager;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnDeleteDownloadFileListener;

/**
 * @aouther tobato
 * @description 描述   文件详情
 * @date 2022-03-04 10:24
 */
public class FileDetailActivity extends BaseAppActivity<ChatPresent> implements BaseAppActivity.OnFileDownloadStatus,
        View.OnClickListener {

    private MessageBodyBean messageBodyBean;
    private LinearLayout mFileBaseInfoLl;
    private ImageView mReceiverFileTagIv;
    /**
     * dfadsfa
     */
    private TextView mReceiverFileNameTv;
    /**
     * 文件大小：
     */
    private TextView mReceiverFileSizeTv;
    private ProgressBar mFileProgressPb;
    private LinearLayout mFileDetailProgressLl;
    /**
     * 用其他应用打开
     */
    private TextView mFileDetailButtonTv;
    private LinearLayout mFileDetailButtonLl;
    private FrameLayout mFileFrameLl;

    @Override
    protected ChatPresent createPresenter() {
        return new ChatPresent();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_file_detail;
    }

    @Override
    public void initView() {
        setTitleName("详情");
        setOnFileDownloadStatus(this);
        messageBodyBean = getIntent().getParcelableExtra(BASE_PARCELABLE);
        mReceiverFileTagIv = (ImageView) findViewById(R.id.receiver_file_tag_iv);
        mReceiverFileNameTv = (TextView) findViewById(R.id.receiver_file_name_tv);
        mReceiverFileSizeTv = (TextView) findViewById(R.id.receiver_file_size_tv);
        mFileProgressPb = (ProgressBar) findViewById(R.id.file_progress_pb);
        mFileDetailProgressLl = (LinearLayout) findViewById(R.id.file_detail_progress_ll);
        mFileDetailButtonTv = (TextView) findViewById(R.id.file_detail_button_tv);
        mFileDetailButtonTv.setOnClickListener(this);
        mFileDetailButtonLl = (LinearLayout) findViewById(R.id.file_detail_button_ll);
        mFileBaseInfoLl = (LinearLayout) findViewById(R.id.file_base_info_ll);
        mFileFrameLl = (FrameLayout) findViewById(R.id.display_file_fl);
        ImageLoadUtil.loadImage(mContext, MyFileProvider.getFileResId(messageBodyBean.getFileName()), mReceiverFileTagIv);
        mReceiverFileNameTv.setText(messageBodyBean.getFileName());
        mReceiverFileSizeTv.append(messageBodyBean.getFileSize());
        setViewVisibleOrGone(false, mFileDetailProgressLl, mFileDetailButtonLl, mFileFrameLl);

        switch (messageBodyBean.getMsgType()) {
            case 1:
                if (messageBodyBean.getFromUserId() == UserInfoManager.getUserId()) {
                    if (TextUtils.isEmpty(messageBodyBean.getLocalCatchPath())) {
                        //文件不存在
                        ToastUtils.toast(mContext, "当前文件已经失效");
                    } else {
                        openFile(messageBodyBean.getLocalCatchPath());
                    }
                } else {
                    if (!FileCacheUtils.isFileExists(FileCacheUtils.getAppImagePath(false) + getSavedFileName(messageBodyBean))) {
                        //文件不存在
                        ToastUtils.toast(mContext, "当前文件已经失效");
                    } else {
                        openFile(FileCacheUtils.getAppImagePath(false) + getSavedFileName(messageBodyBean));

                    }
                }


                break;
            case 2:
                if (messageBodyBean.getFromUserId() == UserInfoManager.getUserId()) {
                    if (TextUtils.isEmpty(messageBodyBean.getLocalCatchPath())) {
                        //文件不存在
                        ToastUtils.toast(mContext, "当前文件已经失效");
                    } else {
                        openFile(messageBodyBean.getLocalCatchPath());
                    }
                } else {
                    if (!FileCacheUtils.isFileExists(FileCacheUtils.getAppVideoPath(false) + getSavedFileName(messageBodyBean))) {
                        //文件不存在
                        ToastUtils.toast(mContext, "当前文件已经失效");
                    } else {
                        openFile(FileCacheUtils.getAppVideoPath(false) + getSavedFileName(messageBodyBean));

                    }
                }
                break;
            case 3:
                break;
            case 8:
                if (messageBodyBean.getFromUserId() == UserInfoManager.getUserId()) {
                    if (TextUtils.isEmpty(messageBodyBean.getLocalCatchPath())) {
                        //文件不存在
                        ToastUtils.toast(mContext, "当前文件已经失效");
                    } else {
                        openFile(messageBodyBean.getLocalCatchPath());
                    }
                } else {
                    if (!FileCacheUtils.isFileExists(FileCacheUtils.getAppFilePath(false) + getSavedFileName(messageBodyBean))) {
                        // 文件不存在
                        setViewVisibleOrGone(true, mFileDetailProgressLl);
                       DownloadFileInfo downloadFileInfo =  FileDownloader.getDownloadFile(messageBodyBean.getContent());
                        if (downloadFileInfo != null) {
                            FileDownloader.delete(messageBodyBean.getContent(), true, new OnDeleteDownloadFileListener() {
                                @Override
                                public void onDeleteDownloadFilePrepared(DownloadFileInfo downloadFileNeedDelete) {

                                }
                                @Override
                                public void onDeleteDownloadFileSuccess(DownloadFileInfo downloadFileDeleted) {
                                    FileDownloader.start(messageBodyBean.getContent());
                                }

                                @Override
                                public void onDeleteDownloadFileFailed(DownloadFileInfo downloadFileInfo, DeleteDownloadFileFailReason failReason) {
                                    ToastUtils.toast(mContext,"onDeleteDownloadFileFaileddwe4512341");
                                }
                            });
                        }else {
                            FileDownloader.start(messageBodyBean.getContent());
                        }

                    } else {
                        openFile(FileCacheUtils.getAppFilePath(false) + getSavedFileName(messageBodyBean));
                    }
                }


                break;
            default:
                break;
        }


    }

    /**
     * 打开文件  消息类型8
     *
     * @param filePath
     */
    private void openFile(String filePath) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction
                = fragmentManager.beginTransaction();
        switch (FileCacheUtils.getFileType(filePath)) {
            case 1:
                setViewVisibleOrGone(false, mFileBaseInfoLl, mFileDetailProgressLl, mFileDetailButtonLl);
                mFileFrameLl.setVisibility(View.VISIBLE);
                transaction.replace(R.id.display_file_fl, DisplayImageFileFragment.getInstance(filePath));
                transaction.commit();
                break;
            case 2:
                setViewVisibleOrGone(false, mFileBaseInfoLl, mFileDetailProgressLl, mFileDetailButtonLl);
                mFileFrameLl.setVisibility(View.VISIBLE);
                transaction.replace(R.id.display_file_fl, DisplayVideoFileFragment.getInstance(filePath));
                transaction.commit();
                break;
            default:
                if (!addTbsReaderView(filePath)) {
                    if (getSavedFileName(messageBodyBean).contains(".apk")) {
                        mPresenter.installApk(mContext,filePath);
                    }else {
                        openUnofficeFile("用其他应用打开");

                    }

                }
                break;
        }
    }

    @Override
    public void initData() {

    }


    @Override
    public void onSuccess(String tag, Object o) {

    }

    @Override
    public void onFileDownloadProgress(DownloadFileInfo downloadFileInfo, float downloadSpeed, long remainingTime) {
// download progress
        initProgressStatus(downloadFileInfo, mFileProgressPb);
    }

    @Override
    public void onFileDownloadSuccess(DownloadFileInfo downloadFileInfo) {

        requestFinish(true, "用其他应用打开");

    }

    @Override
    public void onFileDownloadErrorOfNoExist(DownloadFileInfo downloadFileInfo) {
        requestFinish(false, "文件已失效");
    }

    private void requestFinish(boolean isSuccess, String text) {
        mFileDetailButtonLl.setVisibility(View.VISIBLE);
        mFileDetailProgressLl.setVisibility(View.GONE);
        if (isSuccess) {
            mFileDetailButtonTv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //判断是否是图片或者视频
                    openFile(FileCacheUtils.getAppFilePath(false) + getSavedFileName(messageBodyBean));
                }

            }, 500);
        } else {
            mFileDetailButtonTv.setText(text);
        }

    }

    /**
     * 打开非office文件
     *
     * @param text
     */
    private void openUnofficeFile(String text) {
        mFileFrameLl.setVisibility(View.GONE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction
                = fragmentManager.beginTransaction();
        switch (messageBodyBean.getMsgType()) {
            case 1:
                setViewVisibleOrGone(false, mFileBaseInfoLl, mFileDetailProgressLl, mFileDetailButtonLl);
                mFileFrameLl.setVisibility(View.VISIBLE);
                if (messageBodyBean.getFromUserId() == UserInfoManager.getUserId()) {
                    transaction.replace(R.id.display_file_fl, DisplayImageFileFragment.getInstance(messageBodyBean.getLocalCatchPath()));
                } else {
                    transaction.replace(R.id.display_file_fl, DisplayImageFileFragment.getInstance(FileCacheUtils.getAppImagePath(false) + getSavedFileName(messageBodyBean)));
                }
                transaction.commit();
                break;
            case 2:
                setViewVisibleOrGone(false, mFileBaseInfoLl, mFileDetailProgressLl, mFileDetailButtonLl);
                mFileFrameLl.setVisibility(View.VISIBLE);
                if (messageBodyBean.getFromUserId() == UserInfoManager.getUserId()) {
                    transaction.replace(R.id.display_file_fl, DisplayVideoFileFragment.getInstance(messageBodyBean.getLocalCatchPath()));
                } else {
                    transaction.replace(R.id.display_file_fl, DisplayVideoFileFragment.getInstance(FileCacheUtils.getAppVideoPath(false) + getSavedFileName(messageBodyBean)));
                }
                transaction.commit();
                break;
            case 3:
                setViewVisibleOrGone(false, mFileBaseInfoLl, mFileDetailProgressLl, mFileDetailButtonLl);
                mFileFrameLl.setVisibility(View.VISIBLE);
                transaction.replace(R.id.display_file_fl, DisplayAudioFileFragment.getInstance(FileCacheUtils.getAppFilePath(false) + getSavedFileName(messageBodyBean)));
                transaction.commit();
                break;
            case 8:
                setViewVisibleOrGone(true, mFileBaseInfoLl, mFileDetailButtonLl);
                mFileDetailButtonTv.setText(text);
                break;
            default:
                break;
        }


    }


    @Override
    public void onFileDownloadError(DownloadFileInfo downloadFileInfo) {
        requestFinish(false, "下载失败,点击重试");
    }

    @Override
    public void onFileDownloadPause(DownloadFileInfo downloadFileInfo) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.file_detail_button_tv:
                String text = getTextViewValue(mFileDetailButtonTv);
                switch (text) {
                    case "用其他应用打开":
                        if (!addTbsReaderView(FileCacheUtils.getAppFilePath(false) + getSavedFileName(messageBodyBean))) {
                            closeFile();
                            AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                                    .setMessage("暂不支持打开此类型文件,文件已下载到" + FileCacheUtils.getAppFilePath(false) + getSavedFileName(messageBodyBean))
                                    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                            setAlertDialogHeightWidth(alertDialog, -1, 0);
                        }
                        break;
                    case "文件已失效":
                        break;
                    case "下载失败,点击重试":
                        setViewVisibleOrGone(false, mFileDetailButtonLl);
                        setViewVisibleOrGone(true, mFileDetailProgressLl);
                        FileDownloader.reStart(UrlFormatUtil.getImageOriginalUrl(messageBodyBean.getContent()));
                        break;
                    default:
                        break;
                }

                break;


        }
    }

}
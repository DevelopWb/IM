package com.juntai.disabled.basecomponent.base.update;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.allenliu.versionchecklib.core.http.HttpParams;
import com.allenliu.versionchecklib.core.http.HttpRequestMethod;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener;
import com.juntai.disabled.basecomponent.R;
import com.juntai.disabled.basecomponent.base.BaseDownLoadActivity;
import com.juntai.disabled.basecomponent.bean.UpdateBean;
import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.basecomponent.utils.ActivityManagerTool;
import com.juntai.disabled.basecomponent.utils.AppUtils;
import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.basecomponent.utils.GsonTools;
import com.juntai.disabled.basecomponent.utils.LogUtil;
import com.juntai.disabled.basecomponent.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ma
 * on 2019/12/24
 */
public abstract class UpdateActivity<P extends BasePresenter> extends BaseDownLoadActivity<P> {

    boolean isForceUpdate = false;
    String version;


    /**
     * @param isWarn 是否提醒
     */
    public void update(boolean isWarn) {
        AllenVersionChecker
                .getInstance()
                .requestVersion()
                .setRequestMethod(HttpRequestMethod.POST)
                .setRequestUrl(getUpdateHttpUrl())
                .setRequestParams(getHttpParams())
                .request(new RequestVersionListener() {
                    @Nullable
                    @Override
                    public UIData onRequestVersionSuccess(DownloadBuilder downloadBuilder, String result) {
                        //拿到服务器返回的数据，解析，拿到downloadUrl和一些其他的UI数据
                        LogUtil.d("更新" + result);

                        UpdateBean upgradeBean = GsonTools.changeGsonToBean(result, UpdateBean.class);
                        if (upgradeBean == null||upgradeBean.getData()==null) {
                            if (isWarn) {
                                ToastUtils.toast(mContext, "已是最新版本");
                            }
                            return null;
                        }
                        version = upgradeBean.getData().getVersionsName();
                        isForceUpdate = upgradeBean.getData().isConstraintUpdate();
                        if (AppUtils.getVersionCode(mContext) < upgradeBean.getData().getVersionsCode()) {
                            return UIData.create()
                                    .setTitle(upgradeBean.getData().getFileName())
                                    .setContent(upgradeBean.getData().getUpdateContent())
                                    .setDownloadUrl(upgradeBean.getData().getDownloadLink());
                        } else {
                            if (isWarn) {
                                ToastUtils.toast(mContext, "已是最新版本");
                            }
                            //如果是最新版本直接return null
                            return null;
                        }
                    }

                    @Override
                    public void onRequestVersionFailure(String message) {
                        LogUtil.d("更新" + message);
                    }
                })
                .setDownloadAPKPath(FileCacheUtils.getAppPath(true))//自定义下载路径
                .setApkName(new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()))
                .setCustomVersionDialogListener((context, versionBundle) -> {
                    UpdateDialog updateDialog = new UpdateDialog(context, R.style.BaseDialog, R.layout.update_dialog);
                    //versionBundle 就是UIData，之前开发者传入的，在这里可以拿出UI数据并展示
                    TextView textView = updateDialog.findViewById(R.id.update_content);
                    textView.setText(versionBundle.getContent());
                    //强制更新，隐藏取消按钮
                    if (isForceUpdate) {
                        updateDialog.findViewById(R.id.versionchecklib_version_dialog_cancel).setVisibility(View.GONE);
                    }
                    return updateDialog;
                })
                .setForceUpdateListener(() -> {
                    //强制更新
                    if (isForceUpdate) {
                        ActivityManagerTool.getInstance().finishApp();
                    }
                    cancle();
                })
                .executeMission(mContext.getApplicationContext());
    }

    /**
     * 获取更新的url
     * @return
     */
    protected abstract String getUpdateHttpUrl();

    public static void cancle() {
        AllenVersionChecker.getInstance().cancelAllMission();
    }

    /**
     * 获取参数
     *
     * @return
     */
    private HttpParams getHttpParams() {
        HttpParams httpParams = new HttpParams();
//        httpParams.put("typeId", MyApp.CHECK_UPDATE_TYPE);
        return httpParams;
    }
}

package com.juntai.wisdom.im.base;


import android.text.TextUtils;

import com.juntai.disabled.basecomponent.base.BaseMvpFragment;
import com.juntai.disabled.basecomponent.mvp.IPresenter;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.utils.NotificationTool;

/**
 * @aouther tobato
 * @description 描述  app的fragment的基类
 * @date 2020/7/18 16:43
 */
public abstract class BaseAppFragment<P extends IPresenter> extends BaseMvpFragment<P> {

    @Override
    protected boolean canCancelLoadingDialog() {
        return true;
    }

    @Override
    protected void lazyloadGone() {

    }
    /**
     * 获取文件名称  后缀
     * @param messageBodyBean
     * @return
     */
    public String getSavedFileNameWithoutSuffix(MessageBodyBean messageBodyBean){
        String content = messageBodyBean.getContent();
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        if (content.contains("/")) {
            content = content.substring(content.lastIndexOf("/")+1,content.lastIndexOf("."));
        }
        return content;
    }
    /**
     * 获取文件名称  带后缀   图片的缓存需要
     * @param messageBodyBean
     * @return
     */
    public String getSavedFileName(MessageBodyBean messageBodyBean){
        String content = messageBodyBean.getContent();
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        if (content.contains("/")) {
            content = content.substring(content.lastIndexOf("/")+1,content.length());
        }
        return content;
    }
    @Override
    protected void lazyLoad() {
        NotificationTool.SHOW_NOTIFICATION = true;
    }

    /**
     * 获取activity
     *
     * @return
     */
    public BaseAppActivity getBaseAppActivity() {
        return (BaseAppActivity) getActivity();
    }
}

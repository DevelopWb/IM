package com.juntai.disabled.basecomponent.base;

import android.content.Intent;

import com.google.gson.JsonParseException;
import com.juntai.disabled.basecomponent.app.BaseApplication;
import com.juntai.disabled.basecomponent.bean.BaseAddrBean;
import com.juntai.disabled.basecomponent.mvp.IView;
import com.juntai.disabled.basecomponent.utils.ActionConfig;
import com.juntai.disabled.basecomponent.utils.LogUtil;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

public abstract class BaseObserver<T> extends DisposableObserver<T> {
    protected IView view;
    /**
     * 解析数据失败
     */
    public static final int PARSE_ERROR = 1001;
    /**
     * 网络问题
     */
    public static final int BAD_NETWORK = 1002;
    /**
     * 连接错误
     */
    public static final int CONNECT_ERROR = 1003;
    /**
     * 连接超时
     */
    public static final int CONNECT_TIMEOUT = 1004;


    public BaseObserver(IView view) {
        this.view = view;
    }

    @Override
    protected void onStart() {
        if (view != null) {
            view.showLoading();
        }
    }

    @Override
    public void onNext(T bean) {
        try {
            BaseResult model = (BaseResult) bean;
            if (0 == model.code||model instanceof BaseAddrBean) {
                onSuccess(bean);
            } else if (30003 == model.code||30004 == model.code) {
                //单点登录   被顶后 服务端的success值为false status没有赋值
                if (BaseApplication.isReLoadWarn) {
                    BaseApplication.isReLoadWarn = false;
                    LogUtil.e("resule == false");
                    BaseApplication.app.sendBroadcast(new Intent().setAction(ActionConfig.BROAD_LOGIN));
                }
            }else {
                onError(model.msg);
            }
        } catch (ClassCastException ee) {
            LogUtil.e("数据解析失败" + ee.toString());
            onException(PARSE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("加载失败" + e.toString());
            onError(e.toString());
        }
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e("加载失败" + e.toString());
        if (view != null) {
            view.hideLoading();
        }
        if (e instanceof HttpException) {
            //   HTTP错误
            onException(BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {
            //   连接错误
            onException(CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {
            //  连接超时
            onException(CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //  解析错误
            onException(PARSE_ERROR);
        } else {
            if (e != null) {
                onError(e.toString());
            } else {
                onError("未知错误");
            }
        }

    }

    /**
     * 异常
     *
     * @param unknownError
     */
    private void onException(int unknownError) {
        switch (unknownError) {
            case CONNECT_ERROR:
                onError("服务器访问失败");
                break;
            case CONNECT_TIMEOUT:
                onError("连接超时");
                break;
            case BAD_NETWORK:
                onError("当前网络连接不可用，请检查网络设置");
                break;
            case PARSE_ERROR:
                onError("解析数据失败");
                break;
            default:
                break;
        }
    }


    @Override
    public void onComplete() {
        if (view != null) {
            view.hideLoading();
        }

    }

    public abstract void onSuccess(T o);

    public abstract void onError(String msg);
}

package com.juntai.wisdom.im.entrance;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.juntai.disabled.basecomponent.utils.ActionConfig;
import com.juntai.disabled.basecomponent.utils.EventManager;
import com.juntai.disabled.basecomponent.utils.MD5;
import com.juntai.disabled.basecomponent.utils.PubUtil;
import com.juntai.disabled.basecomponent.utils.RomUtil;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.MyApp;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.base.sendcode.SendCodeModel;
import com.juntai.wisdom.im.bean.UserBean;
import com.juntai.wisdom.im.entrance.main.MainActivity;
import com.juntai.wisdom.im.entrance.regist.RegistActivity;
import com.juntai.wisdom.im.utils.HawkProperty;
import com.juntai.wisdom.im.utils.UserInfoManager;
import com.orhanobut.hawk.Hawk;

import java.lang.ref.WeakReference;

import cn.sharesdk.framework.PlatformDb;

/**
 * @aouther tobato
 * @description 描述  登录
 * @date 2020/3/6 9:12
 */
public class LoginActivity extends BaseAppActivity<EntrancePresent> implements EntranceContract.IEntranceView,
        View.OnClickListener {
    public String otherHeadIcon = "";
    /**
     * 登录
     */
    private TextView mLoginTv;
    /**
     * 账号
     */
    private EditText mPhoneEt;
    /**
     * 密码
     */
    private EditText mPassword;
    String account, password;
    private boolean isHide = true;//默认隐藏

    /**
     * 找回密码
     */
    private TextView mRebackPwdTv;
//    private ImageView mLoginByWchatIv;
//    private ImageView mLoginByQqIv;

    private MyHandler myHandler = new MyHandler(this);
    /**
     * 注册
     */
    private TextView mRegistTv;
    private ImageView mHideShowIv;

    static class MyHandler extends Handler {
        private WeakReference<Activity> mActivity;//弱引用

        MyHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginActivity theActivity = (LoginActivity) mActivity.get();
            switch (msg.what) {
                //此处可以根据what的值处理多条信息
                case 1:
                    theActivity.otherLogin();
                    break;
            }
        }
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_login;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void initData() {
        update(false);
        mLoginTv = (TextView) findViewById(R.id.login_tv);
        mLoginTv.setOnClickListener(this);
        mPhoneEt = (EditText) findViewById(R.id.regist_phone_et);
        mPassword = (EditText) findViewById(R.id.password_et);
        mHideShowIv = (ImageView) findViewById(R.id.hide_show_iv);
        mHideShowIv.setOnClickListener(this);
        getToolbar().setVisibility(View.GONE);
    }

    @Override
    protected EntrancePresent createPresenter() {
        return new EntrancePresent();
    }

    @Override
    public void onSuccess(String tag, Object o) {
        switch (tag) {
            //登录成功
            case EntranceContract.LOGIN_TAG:
                UserBean loginBean = (UserBean) o;
                if (loginBean != null) {
                    if (loginBean.code == 0) {
                        ToastUtils.success(mContext, "登录成功");
                        MyApp.isReLoadWarn = true;
                        Hawk.put(HawkProperty.SP_KEY_USER, loginBean);
                        Hawk.put(HawkProperty.SP_KEY_TOKEN, loginBean.getData().getToken());
                        //ws连接
                        EventManager.sendStringMsg(ActionConfig.BROAD_LOGIN_AFTER);
                        startActivity(new Intent(mContext,MainActivity.class));
                        onBackPressed();
                    } else {
                        ToastUtils.error(mContext, loginBean.msg == null ? PubUtil.ERROR_NOTICE :
                                loginBean.msg);
                    }
                } else {
                    ToastUtils.error(mContext, PubUtil.ERROR_NOTICE);
                }
                break;
            case EntranceContract.OTHER_REGIST:
                mPresenter.login(null, null, UserInfoManager.WECHAT_ID, UserInfoManager.QQ_ID,
                        EntranceContract.LOGIN_TAG);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.login_tv:
                account = mPhoneEt.getText().toString();
                password = mPassword.getText().toString();
                if (account.isEmpty()) {
                    ToastUtils.error(mContext, "账号不可为空");
                    return;
                }
                if (!SendCodeModel.isMobileNO(account)) {
                    ToastUtils.error(mContext, "手机号码格式不正确");
                    return;
                }
                if (password.isEmpty()) {
                    ToastUtils.error(mContext, "登录密码不能为空");
                    return;
                }

                mPresenter.login(account, MD5.md5(String.format("%s#%s", account, password)), RomUtil.getName(),Hawk.get(HawkProperty.DEV_REGID),
                        EntranceContract.LOGIN_TAG);
                break;
            case R.id.forget_pwd_tv:
                startActivity(new Intent(this, ForgetPwdActivity.class));
                break;
//            case R.id.login_by_wchat_iv:
//                loginForQQWeChat(Wechat.NAME);
//                break;
//            case R.id.login_by_qq_iv:
//                loginForQQWeChat(QQ.NAME);
//                break;
            case R.id.regist_tv:
                startActivity(new Intent(this, RegistActivity.class));
                break;
            case R.id.hide_show_iv:
                if (isHide) {
                    isHide = false;
                    //设置EditText的密码为可见的
                    mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mHideShowIv.setImageResource(R.mipmap.show_icon);
                } else {
                    isHide = true;
                    //设置EditText的密码为隐藏
                    mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mHideShowIv.setImageResource(R.mipmap.hide_icon);

                }
                mPassword.setSelection(mPassword.getText().length());
                break;
        }
    }


    @Override
    public void initView() {
        mLoginTv = (TextView) findViewById(R.id.login_tv);
        mLoginTv.setOnClickListener(this);
        mRebackPwdTv = (TextView) findViewById(R.id.forget_pwd_tv);
        mRebackPwdTv.setOnClickListener(this);
//        mLoginByWchatIv = (ImageView) findViewById(R.id.login_by_wchat_iv);
//        mLoginByWchatIv.setOnClickListener(this);
//        mLoginByQqIv = (ImageView) findViewById(R.id.login_by_qq_iv);
//        mLoginByQqIv.setOnClickListener(this);
        mRegistTv = (TextView) findViewById(R.id.regist_tv);
        mRegistTv.setOnClickListener(this);
    }

    PlatformDb platDB;


    /**
     * 第三方登录
     */
    public void otherLogin() {
        mPresenter.login(null, null, UserInfoManager.WECHAT_ID, UserInfoManager.QQ_ID, EntranceContract.LOGIN_TAG);
    }

    @Override
    protected void onDestroy() {
        myHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}

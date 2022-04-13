package com.juntai.wisdom.im.entrance;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.entrance.main.MainActivity;
import com.juntai.wisdom.im.socket.SocketManager;
import com.juntai.wisdom.im.utils.UserInfoManager;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * @aouther Ma
 * @date 2019/3/13
 */
public class SplashActivity extends BaseAppActivity {
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutView() {
        return 0;
    }

    @Override
    public void initView() {
        initToolbarAndStatusBar(false);
        hideBottomVirtureBar();
        new RxPermissions(this)
                .request(permissions)
                .delay(1, TimeUnit.SECONDS)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //所有权限通过
                        } else {
                            //有一个权限没通过
                        }
                        // 连接socket:登录
                        if (UserInfoManager.isLogin()) {
                            SocketManager.getInstance().connect(AppHttpPath.BASE_SOCKET + UserInfoManager.getUserUUID() + "/" + UserInfoManager.getUserId());
                            if (!initThirdShareLogic(getIntent(),SplashActivity.this,MainActivity.class)) {
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            }

                        } else {
                            if (!initThirdShareLogic(getIntent(),SplashActivity.this,LoginActivity.class)) {
                                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            }

                        }
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
    }



    @Override
    public void initData() {

    }


    /**
     * 隐藏pad底部虚拟键
     */
    protected void hideBottomVirtureBar() {
        Window window;
        window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(params);
    }

    @Override
    public void onSuccess(String tag, Object o) {

    }
}

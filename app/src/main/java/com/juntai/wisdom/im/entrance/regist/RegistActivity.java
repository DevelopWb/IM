package com.juntai.wisdom.im.entrance.regist;

import android.content.Intent;
import android.view.View;

import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseWithSmsActivity;
import com.juntai.wisdom.im.base.sendcode.SendCodeContract;
import com.juntai.wisdom.im.entrance.LoginActivity;

import java.util.List;

/**
 * @aouther tobato
 * @description 描述  注册
 * @date 2020/3/8 14:27
 */
public class RegistActivity extends BaseWithSmsActivity implements View.OnClickListener {




    @Override
    protected String getPwdHint() {
        return "请输入你的密码";
    }

    @Override
    protected String getCommitTextName() {
        return "注册";
    }

    @Override
    protected String getTitleName() {
        return "注册";
    }


    @Override
    public void initData() {
    }


    @Override
    public void onSuccess(String tag, Object o) {
        super.onSuccess(tag, o);
        switch (tag) {
            case SendCodeContract.REGIST:
                ToastUtils.success(mContext, "注册成功");
                startActivity(new Intent(this, LoginActivity.class).putExtra(REGIST_PHONE,
                        getTextViewValue(mRegistPhoneEt)));
                break;
            case AppHttpPath.UPLOAD_FILES:
                List<String> picPaths = (List<String>) o;
                if (picPaths == null || picPaths.isEmpty()) {
                    ToastUtils.toast(mContext, "头像上传失败 点击重试");
                    return;
                }
                builder.add("headPortrait", picPaths.get(0));
                mPresenter.regist(SendCodeContract.REGIST, builder.build());

                break;
            default:
                break;
        }
    }


    @Override
    protected void commit() {
        //上传头像
        mPresenter.uploadFile(AppHttpPath.UPLOAD_FILES, headPicPath);
    }





}

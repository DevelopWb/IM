package com.juntai.wisdom.im.entrance;

import com.juntai.disabled.basecomponent.utils.MD5;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseWithSmsActivity;
import com.juntai.wisdom.im.utils.UserInfoManager;

/**
 * @aouther tobato
 * @description 描述  忘记密码
 * @date 2021-11-13 9:17
 */
public class ForgetPwdActivity extends BaseWithSmsActivity {

    @Override
    protected String getPwdHint() {
        return "请输入新密码";
    }

    @Override
    protected String getCommitTextName() {
        return "提交";
    }

    @Override
    protected String getTitleName() {
        return "忘记密码";
    }

    @Override
    protected void commit() {
        mPresenter.modifyPwd(UserInfoManager.getUserId(),getTextViewValue(mRegistPhoneEt), MD5.md5(String.format("%s#%s", getTextViewValue(mRegistPhoneEt), getTextViewValue(mPasswordEt)))
                ,getTextViewValue(mRegistCheckCodeEt), AppHttpPath.MODIFY_PWD);
    }


}

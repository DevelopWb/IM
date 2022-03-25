package com.juntai.wisdom.im.mine;

import com.juntai.disabled.basecomponent.utils.MD5;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseWithSmsActivity;
import com.juntai.wisdom.im.utils.UserInfoManager;

/**
 * @aouther tobato
 * @description 描述  修改密码
 * @date 2021-10-13 15:09
 */
public class ModifyPwdActivity extends BaseWithSmsActivity {

    @Override
    protected String getPwdHint() {
        return "请输入新密码";
    }

    @Override
    protected String getCommitTextName() {
        return "修改密码";
    }

    @Override
    protected String getTitleName() {
        return "修改密码";
    }

    @Override
    protected void commit() {
        mPresenter.modifyPwd(UserInfoManager.getUserId(),getTextViewValue(mRegistPhoneEt), MD5.md5(String.format("%s#%s", getTextViewValue(mRegistPhoneEt), getTextViewValue(mPasswordEt)))
                ,getTextViewValue(mRegistCheckCodeEt), AppHttpPath.MODIFY_PWD);
    }

}

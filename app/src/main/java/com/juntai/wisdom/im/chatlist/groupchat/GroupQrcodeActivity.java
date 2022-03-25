package com.juntai.wisdom.im.chatlist.groupchat;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juntai.disabled.basecomponent.base.BaseResult;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.bean.GroupListBean;
import com.juntai.wisdom.im.chatlist.chat.ChatPresent;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.utils.UrlFormatUtil;

/**
 * @aouther tobato
 * @description 描述  群聊二维码
 * @date 2022-01-15 16:14
 */
public class GroupQrcodeActivity extends BaseAppActivity<ChatPresent> implements MainContract.IBaseView {

    private ImageView mContactorIconIv;
    /**
     * 昵称
     */
    private TextView mContactorNameTv;
    private ImageView mQrcodeIv;
    private String qrcodeUrl;
    private GroupListBean.DataBean groupBean;

    @Override
    protected ChatPresent createPresenter() {
        return new ChatPresent();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_group_qrcode;
    }

    @Override
    public void initView() {
        setTitleName("群二维码");
        mContactorIconIv = (ImageView) findViewById(R.id.contactor_icon_iv);
        mContactorNameTv = (TextView) findViewById(R.id.contactor_name_tv);
        if (getIntent() != null) {
            groupBean = getIntent().getParcelableExtra(BASE_PARCELABLE);
            ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageOriginalUrl(groupBean.getGroupPicture()), mContactorIconIv);
            mContactorNameTv.setText(groupBean.getGroupName());
        }


        mQrcodeIv = (ImageView) findViewById(R.id.qrcode_iv);
        mQrcodeIv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                initBottomDialog(getBaseBottomDialogMenus("保存图片"), UrlFormatUtil.getImageOriginalUrl(qrcodeUrl));
                return true;
            }
        });
    }

    @Override
    public void initData() {
        mPresenter.getGroupQRcode(getBaseBuilder().add("groupId",String.valueOf(groupBean.getGroupId())).build(), AppHttpPath.GET_GROUP_QRCODE);
    }


    @Override
    public void onSuccess(String tag, Object o) {
        switch (tag) {
            case AppHttpPath.GET_GROUP_QRCODE:
                BaseResult baseResult = (BaseResult) o;
                qrcodeUrl = baseResult.getMessage();
                if (!TextUtils.isEmpty(qrcodeUrl)) {
                    ImageLoadUtil.loadImage(mContext, UrlFormatUtil.getImageOriginalUrl(qrcodeUrl), mQrcodeIv);
                }

                break;
            default:
                break;
        }
    }

}

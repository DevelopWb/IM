package com.juntai.wisdom.im.chatlist.groupchat;

import android.widget.ImageView;
import android.widget.TextView;

import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.basecomponent.utils.ScreenUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.bean.GroupDetailInfoBean;
import com.juntai.wisdom.im.chatlist.chat.ChatPresent;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.utils.UrlFormatUtil;
import com.king.zxing.util.CodeUtils;

import static android.graphics.BitmapFactory.decodeFile;

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
    private GroupDetailInfoBean groupBean;

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
    }

    @Override
    public void initData() {
        mQrcodeIv.setImageBitmap(CodeUtils.createQRCode(String.format("https://www.baidu.com?uuid=%s&type=2",
                groupBean.getUuid()), ScreenUtils.getInstance(mContext).getScreenWidth(), decodeFile(FileCacheUtils.getAppImagePath(true)+getSavedFileName(groupBean.getGroupPicture())))
        );
    }


    @Override
    public void onSuccess(String tag, Object o) {
    }

}

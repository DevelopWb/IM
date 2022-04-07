package com.juntai.wisdom.im.mine.myCollect;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseRecyclerviewActivity;
import com.juntai.wisdom.im.bean.CollectMessagesBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.chatlist.chat.displayFile.FileDetailActivity;
import com.juntai.wisdom.im.mine.MyCenterContract;
import com.juntai.wisdom.im.mine.MyCenterPresent;

import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  我的收藏
 * @date 2022-03-01 9:17
 */
public class MyCollectActivity extends BaseRecyclerviewActivity<MyCenterPresent> implements MyCenterContract.ICenterView, View.OnClickListener {

    /**
     * 搜索
     */
    private TextView mSearchTv;
    private int currentPosition;

    @Override
    protected MyCenterPresent createPresenter() {
        return new MyCenterPresent();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_my_collect;
    }

    @Override
    protected LinearLayoutManager getBaseAdapterManager() {
        return null;
    }

    @Override
    public void initData() {
        super.initData();
        setTitleName("我的收藏");
        mSearchTv = (TextView) findViewById(R.id.search_tv);
        mSearchTv.setOnClickListener(this);
        baseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultipleItem multipleItem = (MultipleItem) adapter.getItem(position);
                MessageBodyBean messageBodyBean = (MessageBodyBean) multipleItem.getObject();
// TODO: 2022/3/18 收藏的文件都已经保存到本地 

                startActivity(new Intent(mContext, FileDetailActivity.class).putExtra(BASE_PARCELABLE, messageBodyBean));

            }
        });
        baseQuickAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                currentPosition = position;
                MultipleItem multipleItem = (MultipleItem) adapter.getItem(position);
                MessageBodyBean messageBodyBean = (MessageBodyBean) multipleItem.getObject();
                long id = messageBodyBean.getId();
                showAlertDialog(String.format("确定删除当前收藏？"), "确定", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.deleteCollection(getBaseBuilder().add("id", String.valueOf(id)).build(), AppHttpPath.DELETE_COLLECT);
                    }
                });


                return false;
            }
        });
    }


    @Override
    protected void getRvAdapterData() {
        mPresenter.getAllCollection(getBaseBuilder().add("page", String.valueOf(page)).add("limit", String.valueOf(limit)).build(), AppHttpPath.ALL_COLLECTS);

    }


    @Override
    protected boolean enableRefresh() {
        return true;
    }

    @Override
    protected boolean enableLoadMore() {
        return true;
    }

    @Override
    protected BaseQuickAdapter getBaseQuickAdapter() {
        return new MyCollectAdapter(null);
    }


    @Override
    public void onSuccess(String tag, Object o) {
        super.onSuccess(tag, o);
        switch (tag) {
            case AppHttpPath.ALL_COLLECTS:
                CollectMessagesBean collectMessagesBean = (CollectMessagesBean) o;
                CollectMessagesBean.DataBean dataBean = collectMessagesBean.getData();
                if (dataBean != null) {
                    List<MessageBodyBean> messageBodyBeans = dataBean.getList();
                    List<MultipleItem> data = new ArrayList<>();
                    if (messageBodyBeans != null) {
                        for (MessageBodyBean messageBodyBean : messageBodyBeans) {
                            //收藏消息类型 普通文本  图片 视频  音频  8文件 6位置
                            switch (messageBodyBean.getMsgType()) {
                                case 0:
                                    data.add(new MultipleItem(MultipleItem.ITEM_COLLECTION_TEXT, messageBodyBean));
                                    break;
                                case 1:
                                    data.add(new MultipleItem(MultipleItem.ITEM_COLLECTION_PIC, messageBodyBean));
                                    break;
                                case 2:
                                    data.add(new MultipleItem(MultipleItem.ITEM_COLLECTION_VIDEO, messageBodyBean));
                                    break;
                                case 3:
                                    data.add(new MultipleItem(MultipleItem.ITEM_COLLECTION_AUDIO, messageBodyBean));
                                    break;
                                case 6:
                                    data.add(new MultipleItem(MultipleItem.ITEM_COLLECTION_LOCATE, messageBodyBean));
                                    break;
                                case 8:
                                    data.add(new MultipleItem(MultipleItem.ITEM_COLLECTION_FILE, messageBodyBean));
                                    break;
                                default:
                                    break;
                            }
                        }
                        setData(data, dataBean.getCurrPage() == dataBean.getTotalPage());
                    }
                }
                break;
            case AppHttpPath.DELETE_COLLECT:
                baseQuickAdapter.remove(currentPosition);
                ToastUtils.toast(mContext, "删除成功");
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
            case R.id.search_tv:
                startActivity(new Intent(mContext, SearchCollectActivity.class));
                break;
        }
    }
}

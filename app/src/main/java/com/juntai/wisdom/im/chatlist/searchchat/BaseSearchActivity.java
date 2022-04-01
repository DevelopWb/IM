package com.juntai.wisdom.im.chatlist.searchchat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.bean.CollectMessagesBean;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.GroupDetailBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.chatlist.chat.ChatPresent;
import com.juntai.wisdom.im.entrance.main.MainContract;

import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  搜索聊天记录
 * @date 2022-01-25 9:32
 */
public abstract class BaseSearchActivity extends BaseAppActivity<ChatPresent> implements MainContract.IBaseView, View.OnClickListener {

    private SearchView mSearchContentSv;
    /**
     * 取消
     */
    private TextView mBackTv;
    private RecyclerView mRecyclerview;
    protected ContactBean contactBean;
    protected BaseQuickAdapter adapter;
    protected GroupDetailBean.DataBean groupBean;

    @Override
    protected ChatPresent createPresenter() {
        return new ChatPresent();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_search_chat_record;
    }

    @Override
    public void initView() {

        initToolbarAndStatusBar(false);
        mImmersionBar.statusBarDarkFont(true).init();
        mSearchContentSv = (SearchView) findViewById(R.id.search_content_sv);
        mBackTv = (TextView) findViewById(R.id.cancel_tv);
        mBackTv.setOnClickListener(this);
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        adapter = getAdapter();
        initRecyclerview(mRecyclerview, adapter, LinearLayoutManager.VERTICAL);
        SearchView.SearchAutoComplete textView = (SearchView.SearchAutoComplete) mSearchContentSv.findViewById(R.id.search_src_text);
        textView.setTextSize(14);
        mSearchContentSv.requestFocus();
        mSearchContentSv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (TextUtils.isEmpty(s)) {
                    return true;
                }
                commitSearch(s);
                getViewFocus(mRecyclerview);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    return true;
                }
                commitSearch(s);
                return false;
            }
        });


    }

    /**
     * 提交搜索
     * @param s
     * @return
     */
    protected abstract boolean commitSearch(String s);



    protected abstract BaseQuickAdapter getAdapter();

    /**
     * chatType  聊天类型（1：搜索私聊信息；2搜索公聊信息； 3搜索联系人  4首页搜索  搜索各种类型数据  5消息转发的时候跳转的界面  里面包含群组和通讯录  6 收藏的内容
     *
     * @return
     */
    protected abstract int getSearchType();

    @Override
    public void initData() {
        switch (getSearchType()) {
            case 1:
                if (getIntent() != null) {
                    contactBean = getIntent().getParcelableExtra(BASE_PARCELABLE);
                }
                break;
            case 2:
                // : 2022-01-25 群聊记录相关逻辑
                if (getIntent() != null) {
                    groupBean = getIntent().getParcelableExtra(BASE_PARCELABLE);
                }
                break;
            case 3:
                break;
            default:
                break;
        }
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                onAdapterItemClick(adapter,position);
            }
        });
    }

    protected abstract void onAdapterItemClick(BaseQuickAdapter adapter, int position);


    @Override
    public void onSuccess(String tag, Object o) {
        switch (tag) {
            case AppHttpPath.ALL_COLLECTS:
                CollectMessagesBean collectMessagesBean = (CollectMessagesBean) o;
                CollectMessagesBean.DataBean dataBean = collectMessagesBean.getData();
                if (dataBean != null) {
                    List<MessageBodyBean> messageBodyBeans = dataBean.getList();
                    List<MultipleItem> data = new ArrayList<>();
                    if (messageBodyBeans != null) {
                        if (4 == getSearchType()) {
                            if (messageBodyBeans.size() > 0) {
                                data.add(new MultipleItem(MultipleItem.ITEM_TITLE, "我的收藏"));
                            }
                        }
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
                        if (4 == getSearchType()) {
                            adapter.addData(data);
                        } else {
                            adapter.setNewData(data);
                        }
                    }
                }
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
            case R.id.cancel_tv:
                finish();
                break;
        }
    }

}

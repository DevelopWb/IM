package com.juntai.wisdom.im.base;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.mvp.IPresenter;
import com.juntai.disabled.federation.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * @Author: tobato
 * @Description: 作用描述  只有一个recyclerview的fragment
 * @CreateDate: 2021/4/29 16:16
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/29 16:16
 */
public abstract class BaseRecyclerviewFragment<P extends IPresenter> extends BaseAppFragment<P> {
    private RecyclerView mRecyclerview;
    protected SmartRefreshLayout mSmartrefreshlayout;
    protected BaseQuickAdapter adapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.recycleview_layout;
    }

    @Override
    protected void initView() {
        if (0==getLayoutRes()) {
            return;
        }
        mRecyclerview = (RecyclerView) getView(R.id.recyclerview);
        mSmartrefreshlayout = (SmartRefreshLayout) getView(R.id.smartrefreshlayout);
        adapter = getAdapter();
        if (adapter != null) {
            getBaseActivity().initRecyclerview(mRecyclerview,adapter, LinearLayoutManager.VERTICAL);
            mSmartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshLayout) {
                    freshlayoutOnRefresh();
                }
            });
            mSmartrefreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(RefreshLayout refreshLayout) {
                    freshlayoutOnLoadMore();
                }
            });
        }
    }

    protected abstract void freshlayoutOnLoadMore();

    protected abstract void freshlayoutOnRefresh();

    protected abstract BaseQuickAdapter getAdapter();

    @Override
    public void onSuccess(String tag, Object o) {
        mSmartrefreshlayout.finishLoadMore();
        mSmartrefreshlayout.finishRefresh();
        getBaseActivity().getViewFocus(mRecyclerview);
    }
}

package com.readeveryday.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.readeveryday.R;
import com.readeveryday.ui.base.BaseFragment;
import com.readeveryday.ui.presenter.ZhiHuFgPreshenter;
import com.readeveryday.ui.view.ZhiHuFgView;

import butterknife.BindView;

/**
 * Created by XuYanping on 2017/3/15.
 */

public class ZhiHuFragment extends BaseFragment<ZhiHuFgView, ZhiHuFgPreshenter> implements ZhiHuFgView {
    @BindView(R.id.content_list)
    RecyclerView mContentList;

    private LinearLayoutManager mLayoutManager;

    @Override
    protected ZhiHuFgPreshenter createPresenter() {
        return new ZhiHuFgPreshenter(getContext());
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_zhihu;
    }

    @Override
    public void setDataRefresh(Boolean refresh) {
        setRefresh(refresh);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mContentList;
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    @Override
    protected void initView(View rootView) {
        mLayoutManager = new LinearLayoutManager(getContext());
        mContentList.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDataRefresh(true);
        mPresenter.getLatesNews();
        mPresenter.scrollRecycleView();
    }

    @Override
    public void requestDataRefresh() {
        super.requestDataRefresh();
        setDataRefresh(true);
        mPresenter.getLatesNews();
    }
}

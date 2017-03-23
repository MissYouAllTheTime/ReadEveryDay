package com.readeveryday.ui.presenter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.AndroidCharacter;
import android.util.Log;

import com.readeveryday.api.ZhiHuApi;
import com.readeveryday.bean.zhihu.NewsTimeLine;
import com.readeveryday.ui.adapter.ZhiHuAdapter;
import com.readeveryday.ui.base.BasePresenter;
import com.readeveryday.ui.view.ZhiHuFgView;
import com.readeveryday.utils.PromptUtil;

import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.SafeSubscriber;
import rx.schedulers.Schedulers;

/**
 * Created by XuYanping on 2017/3/15.
 */

public class ZhiHuFgPresenter extends BasePresenter<ZhiHuFgView> {

    private Context mContext;
    private ZhiHuFgView mZhiHuFgView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private NewsTimeLine mTimeLine;
    private ZhiHuAdapter mAdapter;
    private int lastVisibleItem;
    private boolean isLoadMore = false; //是否加载更多

    public ZhiHuFgPresenter(Context context) {
        mContext = context;
    }

    public void getLatesNews() {
        mZhiHuFgView = getView();
        if (mZhiHuFgView != null) {
            mRecyclerView = mZhiHuFgView.getRecyclerView();
            mLayoutManager = mZhiHuFgView.getLayoutManager();
            zhihuApi.getLatestNews()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<NewsTimeLine>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Log.d("e", e.toString());
                    PromptUtil.toastShowShort(mContext, "网络不见了~");
                }

                @Override
                public void onNext(NewsTimeLine line) {
                    setData(mContext, line, mZhiHuFgView, mRecyclerView);
                }
            });
        }
    }

    String time;

    private void setData(Context context, NewsTimeLine line, ZhiHuFgView view, RecyclerView recyclerView) {

        if (isLoadMore) {
            if (time == null) {
                mAdapter.updateLoadStatus(mAdapter.LOAD_NONE);
                view.setDataRefresh(false);
                return;
            } else {
                mTimeLine.getStories().addAll(line.getStories());
            }
            mAdapter.notifyDataSetChanged();
        } else {
            mTimeLine = line;
            mAdapter = new ZhiHuAdapter(context, mTimeLine);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        view.setDataRefresh(false);
        time = line.getDate();
    }

    public void scrollRecycleView() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                    if (mLayoutManager.getItemCount() == 1) {
                        mAdapter.updateLoadStatus(mAdapter.LOAD_NONE);
                        return;
                    }
                    if (lastVisibleItem + 1 == mLayoutManager.getItemCount()) {
                        mAdapter.updateLoadStatus(mAdapter.LOAD_PULL_TO);
                        isLoadMore = true;
                        mAdapter.updateLoadStatus(mAdapter.LOAD_MORE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getBeforeNews(time);
                            }
                        }, 1000);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void getBeforeNews(String time) {
        mZhiHuFgView = getView();
        if (mZhiHuFgView != null) {
            mRecyclerView = mZhiHuFgView.getRecyclerView();
            mLayoutManager = mZhiHuFgView.getLayoutManager();

            zhihuApi.getBeforetNews(time).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<NewsTimeLine>() {
                @Override
                public void onCompleted() {
                    Log.d("e", "completed");
                }

                @Override
                public void onError(Throwable e) {
                    PromptUtil.toastShowShort(mContext, "网络不见了~");
                }

                @Override
                public void onNext(NewsTimeLine line) {
                    setData(mContext, line, mZhiHuFgView, mRecyclerView);
                }
            });
        }
    }
}
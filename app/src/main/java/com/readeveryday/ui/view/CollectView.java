package com.readeveryday.ui.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by XuYanping on 2017/3/24.
 */

public interface CollectView {

    LinearLayout getParentLayout();

    RecyclerView getRecyclerView();

    LinearLayoutManager getLayoutManager();

    RelativeLayout getNoData();

}

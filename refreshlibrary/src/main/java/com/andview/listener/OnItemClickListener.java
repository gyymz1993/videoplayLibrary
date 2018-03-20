package com.andview.listener;

import android.view.View;

import com.andview.adapter.BaseRecyclerHolder;


public interface OnItemClickListener<T> {
    /**
     * @param childView     点击选中的子视图
     * @param position      点击视图的索引
     * @param item      点击视图的类型
     */
    void onItemClick(BaseRecyclerHolder baseRecyclerHolder,int position, T item);
}

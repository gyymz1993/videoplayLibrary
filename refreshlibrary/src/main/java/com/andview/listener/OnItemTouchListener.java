package com.andview.listener;

import android.view.MotionEvent;
import android.view.View;

import com.andview.myrvview.ViewHolder;

/**
 * CSDN_LQR
 * item的触摸回调
 */
public interface OnItemTouchListener {
    boolean onItemTouch(ViewHolder helper, View childView, MotionEvent event, int position);
}

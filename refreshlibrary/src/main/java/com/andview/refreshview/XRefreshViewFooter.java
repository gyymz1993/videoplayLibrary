package com.andview.refreshview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.callback.IFooterCallBack;

public class XRefreshViewFooter extends LinearLayout implements IFooterCallBack {
    private Context mContext;

    private View mContentView;
    private View mProgressBar;
    private TextView mHintView;
    private TextView mClickView;
    private boolean showing = true;

    public XRefreshViewFooter(Context context) {
        super(context);
        initView(context);
    }

    public XRefreshViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @Override
    public void callWhenNotAutoLoadMore(final XRefreshView xRefreshView) {
        mClickView.setText(R.string.xrefreshview_footer_hint_click);
        mClickView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                xRefreshView.notifyLoadMore();
            }
        });
        Log.e("XRefreshViewFooter", "onReleaseToLoadMore mHintView " + mHintView.getText().toString());
    }

    @Override
    public void onStateReady(boolean mHasLoadComplete) {
        Log.e("XRefreshViewFooter", "onStateReady mHasLoadComplete " +mHasLoadComplete);
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        if (!mHasLoadComplete) {
            mClickView.setText(R.string.xrefreshview_footer_hint_click);
            mClickView.setVisibility(View.VISIBLE);
            Log.e("XRefreshViewFooter", "onStateReady mHintView " + mClickView.getText().toString());
        } else {
            mHintView.setText(R.string.xrefreshview_footer_hint_complete);
            mHintView.setVisibility(View.VISIBLE);
            Log.e("XRefreshViewFooter", "onStateReady mHintView " + mHintView.getText().toString());
        }

//        show(true);
    }

    @Override
    public void onStateRefreshing() {
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mClickView.setVisibility(View.GONE);
        show(true);
    }

    @Override
    public void onReleaseToLoadMore() {
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mClickView.setText(R.string.xrefreshview_footer_hint_release);
        mClickView.setVisibility(View.VISIBLE);
        Log.e("XRefreshViewFooter", "onReleaseToLoadMore mHintView " + mHintView.getText().toString());
    }

    @Override
    public void onStateFinish(boolean hideFooter) {
        mHintView.setText(R.string.xrefreshview_footer_hint_normal);
//        if (hideFooter) {
//
//        } else {
//            //处理数据加载失败时ui显示的逻辑，也可以不处理，看自己的需求
//            mHintView.setText(R.string.xrefreshview_footer_hint_fail);
//        }
        mHintView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mClickView.setVisibility(View.GONE);

        Log.e("XRefreshViewFooter", "onStateFinish mHintView " + mHintView.getText().toString());
    }

    @Override
    public void onStateComplete() {
        mHintView.setText(R.string.xrefreshview_footer_hint_complete);

        mHintView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mClickView.setVisibility(View.GONE);

        Log.e("XRefreshViewFooter", "onStateComplete mHintView " + mHintView.getText().toString());
        // mHintView.setVisibility(View.VISIBLE);
    }

    @Override
    public void show(final boolean show) {
        if (show == showing) {
            return;
        }
        showing = show;
        LayoutParams lp = (LayoutParams) mContentView
                .getLayoutParams();
        lp.height = show ? LayoutParams.WRAP_CONTENT : 0;
        mContentView.setLayoutParams(lp);
//        setVisibility(show?VISIBLE:GONE);

    }

    @Override
    public boolean isShowing() {
        return showing;
    }

    private void initView(Context context) {
        mContext = context;
        ViewGroup moreView = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.xrefresh_view_xrefreshview_footer, this);
        moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mContentView = moreView.findViewById(R.id.xrefreshview_footer_content);
        mProgressBar = moreView
                .findViewById(R.id.xrefreshview_footer_progressbar);
        mHintView = (TextView) moreView
                .findViewById(R.id.xrefreshview_footer_hint_textview);
        mClickView = (TextView) moreView
                .findViewById(R.id.xrefreshview_footer_click_textview);
    }

    @Override
    public int getFooterHeight() {
        return getMeasuredHeight();
    }
}

package com.xiao.nicevideoplayer;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by XiaoJianjun on 2017/4/28.
 * 播放器
 */
public class EditNiceVideoPlayer extends NiceVideoPlayer {

    public EditNiceVideoPlayer(Context context) {
        super(context);
    }


    public EditNiceVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);

    }


    @Override
    public void seekTo(long pos) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(pos);
        }
    }


    public void stopseekTo(long pos) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setVolume(0, 0);
            mMediaPlayer.seekTo(pos);
            mMediaPlayer.pause();
        }
    }


    private long mEndTime = 0;
    private long mStartTime = 0;

    public void setchageUrlUp(String url, final Long startTime, final long endTime, final long lenght) {
        if (mController instanceof EditTxVideoPlayerController) {
            ((EditTxVideoPlayerController) mController).setOnSeekBarChangeListener(new EditTxVideoPlayerController.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(long progress) {
                    if (progress > lenght) {
                        mMediaPlayer.seekTo(startTime);
                    }
                }
            });
            mController.setLenght(lenght);
        }
        mUrl = url;
        mStartTime = startTime;
        mEndTime = endTime ;
        mMaxLenght = lenght;
        mMediaPlayer.seekTo(startTime);
        // chageUrlUp(url);
    }


    public void chageUrlUp(String url) {
        mUrl = url;
        if (!isInitMediaPlayer) {
            openMediaPlayer();
        } else {
//            releasePlayer();
//            openMediaPlayer();
            mMediaPlayer.pause();
            mMediaPlayer.reset();
            openMediaPlayer();
        }
        setUp(url, null);
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        if (mSurfaceTexture == null) {
            mSurfaceTexture = surfaceTexture;
            openMediaPlayer();
        } else {
            mTextureView.setSurfaceTexture(mSurfaceTexture);
        }
    }


    protected long mMaxLenght = 0;
    protected long mCurrentPosition;

    @Override
    public long getDuration() {
        if (Integer.valueOf(String.valueOf(mMaxLenght)) != 0) {
            return mMaxLenght;
        } else {
            return mMediaPlayer != null ? mMediaPlayer.getDuration() : 0;
        }

    }


    @Override
    public void start() {
        /**
           本地播放 无需状态判断
         */
        mCurrentState = STATE_IDLE;
        if (mCurrentState == STATE_IDLE) {
            //  NiceVideoPlayerManager.instance().setCurrentNiceVideoPlayer(this);
            LogUtil.d("NiceVideoPlayer只有在mCurrentState ");
            initAudioManager();
            initMediaPlayer();
            initTextureView();
            addTextureView();
            isInitVideoManager = true;
        } else {
            LogUtil.d("NiceVideoPlayer只有在mCurrentState == STATE_IDLE时才能调用start方法.");
        }
    }


    public void setmDuration(long maxLenght) {
        this.mMaxLenght = maxLenght;
    }

    public long getmMaxLenght() {
        return mMaxLenght;
    }

    public long getmEndTime() {
        return mEndTime;
    }

    public long getmStartTime() {
        return mStartTime;
    }

    @Override
    public long getCurrentPosition() {
        long getCurrentPosition = mMediaPlayer != null ? mMediaPlayer.getCurrentPosition() : 0;
        //真正的   进度
        // 0    ----    16;
        //3  -  8
        // 30000---8000
        if (mStartTime != 0) {

            getCurrentPosition -= mStartTime;
            return getCurrentPosition;
        } else {
            return getCurrentPosition;
        }


        //  return mMediaPlayer != null ? mMediaPlayer.getCurrentPosition() : 0;
    }

    public void setmCurrentPosition(long mCurrentPosition) {
        this.mCurrentPosition = mCurrentPosition;
    }


    protected IMediaPlayer.OnCompletionListener mOnCompletionListener
            = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(final IMediaPlayer mp) {
//            mCurrentState = STATE_COMPLETED;
//            mController.onPlayStateChanged(mCurrentState);
//            LogUtil.d("onCompletion ——> STATE_COMPLETED");
//            // 清除屏幕常亮
//            mContainer.setKeepScreenOn(false);

            //重播i
            mCurrentState = STATE_PREPARED;
            mController.onPlayStateChanged(mCurrentState);
            LogUtil.d("onPrepared ——> STATE_PREPARED");
            if (mStartTime != 0) {
                //  mp.seekTo(mEndTime);
                mp.seekTo(mStartTime);
                // mp.start();
            }
            mp.start();
        }
    };


    protected void openMediaPlayer() {
        // 屏幕常亮
        mContainer.setKeepScreenOn(true);
        // 设置监听
        mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
        mMediaPlayer.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
        mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
        mMediaPlayer.setOnErrorListener(mOnErrorListener);
        mMediaPlayer.setOnInfoListener(mOnInfoListener);
        mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        // 设置dataSource
        try {
            LogUtil.i("mUrl   openMediaPlayer--------------" + mUrl);
            mMediaPlayer.setDataSource(mContext.getApplicationContext(), Uri.parse(mUrl), mHeaders);
            LogUtil.d("STATE_PREPARING");
        } catch (Exception e) {
           // e.printStackTrace();
            LogUtil.e("打开播放器发生错误", e);
        }

        if (mSurface == null) {
            mSurface = new Surface(mSurfaceTexture);
        }
        mMediaPlayer.setSurface(mSurface);
        mMediaPlayer.prepareAsync();
        mCurrentState = STATE_PREPARING;
        mController.onPlayStateChanged(mCurrentState);
        isInitMediaPlayer = true;
    }


}

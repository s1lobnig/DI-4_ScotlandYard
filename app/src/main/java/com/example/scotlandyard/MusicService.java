package com.example.scotlandyard;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class MusicService extends Service implements MediaPlayer.OnErrorListener {

    private static final String TAG = "MusicService";

    private MediaPlayer mPlayer;
    private int length = 0;

    private final IBinder binder = new LocalBinder();

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
        return true;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = MediaPlayer.create(this, R.raw.scotland_yard_bg_music);
        mPlayer.setOnErrorListener(this);
        if (mPlayer != null) {
            mPlayer.setLooping(true);
            mPlayer.setVolume(100, 100);
            mPlayer.setOnErrorListener(this);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPlayer.start();
        return START_STICKY;
    }

    public void pauseMusic() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            length = mPlayer.getCurrentPosition();
            Log.d(TAG, "Pause at " + length);
        }
    }

    public void resumeMusic() {
        if (mPlayer != null && !mPlayer.isPlaying()) {
            Log.d(TAG, "Resume from " + length);
            mPlayer.seekTo(length);
            mPlayer.start();
        }
    }

    public void stopMusic() {
        try {
            mPlayer.stop();
            mPlayer.reset();
            mPlayer.release();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            mPlayer = null;
        }
        Log.d(TAG, "Stop music");
    }

    @Override
    public boolean stopService(Intent name) {
        stopMusic();
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        stopMusic();
        super.onDestroy();
    }
}
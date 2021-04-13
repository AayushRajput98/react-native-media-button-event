package com.mediabuttonhandling.reactnative;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;
import androidx.media.session.MediaButtonReceiver;

import java.util.List;

public class MediaService extends  MediaBrowserServiceCompat{
    private MediaSessionCompat session; //Session instance
    AudioTrack track;
    private final MediaSessionCompat.Callback callback = new MediaSessionCompat.Callback() {   //Media Session callback instance
        @Override
        public boolean onMediaButtonEvent(final Intent mediaButtonIntent) {
            String action = mediaButtonIntent.getAction();
            Log.d("MediaSession", "Intent Action: " + action);
            KeyEvent event = (KeyEvent) mediaButtonIntent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            Log.d("MediaSession", "KeyCode: " + event.getKeyCode());
            RNMediaButtonHandlingModule.getInstance().onMediaButtonEvent(event.getKeyCode(), event); //Emits the event to JS
            return super.onMediaButtonEvent(mediaButtonIntent);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MediaService", "onCreate");
        session = new MediaSessionCompat(this, "Session");
        Log.d("SessionToken", ""+session.getSessionToken());
        Log.d("MediaSession", "" + session);
        session.setCallback(callback);
        PlaybackStateCompat.Builder state = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_STOP)
                .setState(PlaybackStateCompat.STATE_CONNECTING, 0, 0);
        session.setPlaybackState(state.build());
        session.setActive(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            track = new AudioTrack.Builder()
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build())
                    .setAudioFormat(new AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(48000)
                            .setChannelIndexMask(AudioFormat.CHANNEL_OUT_STEREO)
                            .build())
                    .setBufferSizeInBytes(AudioTrack.getMinBufferSize(48000, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT))
                    .build();
        }
        //track = new AudioTrack(AudioManager.STREAM_MUSIC, 48000, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, AudioTrack.getMinBufferSize(48000, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT), AudioTrack.MODE_STREAM);
        track.play();
        setSessionToken(session.getSessionToken());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MediaButtonReceiver.handleIntent(session, intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return new BrowserRoot(getString(R.string.app_name), null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        result.sendResult(null);
    }

    @Override
    public void onDestroy() {
        session.release();
        track.stop();
        track.release();
        super.onDestroy();
    }
}


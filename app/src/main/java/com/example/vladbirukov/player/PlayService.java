package com.example.vladbirukov.player;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class PlayService extends Service
{
                                                                                                                                                                        int music_compostion [][] = {{R.raw.jazz_1,R.raw.jazz_2, R.raw.jazz_4}, {R.raw.pop_1,R.raw.pop_3,R.raw.pop_4}, {R.raw.rock_2,R.raw.rock_3,R.raw.rock_4}, {R.raw.metal_1,R.raw.metal_2,R.raw.metal_3}, {R.raw.national_1, R.raw.national_2,R.raw.national_4}};
    MediaPlayer mPlayer;
    MyBinder binder = new MyBinder();
    int i;
    int j;
    NotificationManager notificationManager;
                                                                                                                                                                         String name = "Bebe Rexha - I got you";
    @Override
    public void onCreate()
    {
        super.onCreate();


    }


    public int onStartCommand(Intent intent, int flags, int startId) {

        sendNotif();
        return super.onStartCommand(intent, flags, startId);
    }


    void sendNotif() {
         notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, ActiviteTwo.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification builder = new Notification.Builder(this)
                .setTicker("Player!")
                .setContentTitle("Run")
                .setContentText(
                        "Name:"+ name)
                .setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pIntent)
                .addAction(R.mipmap.ic_launcher, "Перейти", pIntent)
                .build();

        builder.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, builder);
    }

    @Override
    public void onStart(Intent intent, int startid)
    {

        if (mPlayer !=null){
            mPlayer.stop();
            create_player();
        }else{
            create_player();
        }
    }

    public  void create_player(){
        mPlayer = MediaPlayer.create(this, music_compostion[i][j]);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mPlayer.setLooping(true);

        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                mediaPlayer.start();
            }
        });

    }

    public void get_music_composition(int compos_id, int music_id){
        i = compos_id;
        j = music_id;
    }


    public boolean is_Ready(){
        return mPlayer.isPlaying();

    }

    public void stop(){
        mPlayer.stop();
    }

    @Override
    public void onDestroy()
    {
        if(mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        super.onDestroy();


    }
    public IBinder onBind(Intent arg0) {
        return binder;
    }

    class MyBinder extends Binder {
        PlayService getService() {
            return PlayService.this;
        }
    }

}

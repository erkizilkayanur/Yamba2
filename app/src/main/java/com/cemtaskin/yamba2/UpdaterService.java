package com.cemtaskin.yamba2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cemtaskin.yamba2.Model.Timeline;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterBase;
import twitter4j.TwitterException;

/**
 * Created by ctaskin on 02/12/15.
 */
public class UpdaterService extends Service {


    private static final String TAG= "YambaService";

    public static final  String NEW_STATUS_INTENT="statusUpdated";

    private static final int DELAY=60000;
    private boolean runFlag = false;
    private Updater updater;

    private YambaApplication yambaApplication;






    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.updater = new Updater();
        this.yambaApplication=(YambaApplication)getApplication();

        Log.d(TAG,"Service OnCreate");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"Service OnStart");

        runFlag=true;
        this.updater.start();
        this.yambaApplication.setServiceRunning(true);

        return super.onStartCommand(intent, flags, startId);



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.runFlag=false;
        this.updater.interrupt();
        this.yambaApplication.setServiceRunning(false);
        Log.d(TAG, "Service OnDestroy");
    }


    private class Updater extends  Thread{
        List<Status> timeline;


        @Override
        public void run() {
            super.run();
            UpdaterService service = UpdaterService.this;
            while (service.runFlag){
                Log.d(TAG,"Updater is running");

                try {
                    timeline=yambaApplication.getTwitter().getHomeTimeline();
                } catch (TwitterException e) {
                    e.printStackTrace();
                    Log.d(TAG,e.toString());
                }

                Boolean newAdded=false;

                if (timeline!=null){
                    for(Status s:timeline){
                        //Log.d(TAG,s.getText());

                        Timeline t=new Timeline(s.getCreatedAt().getTime(),s.getSource(),s.getText(), s.getUser().getName(),s.getId());
                        if (!Timeline.IsExists(s.getId())){
                            t.save();
                            newAdded=true;

                        }


                    }
                }

                if (newAdded){
                    service.sendBroadcast(new Intent(NEW_STATUS_INTENT));
                }



                try{
                    Thread.sleep(DELAY);
                }catch (InterruptedException ex){
                    service.runFlag=false;
                    Log.d(TAG,ex.toString());
                }
            }

        }
    }
}

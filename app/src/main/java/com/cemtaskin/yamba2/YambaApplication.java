package com.cemtaskin.yamba2;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.activeandroid.ActiveAndroid;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Created by ctaskin on 02/12/15.
 */
public class YambaApplication extends com.activeandroid.app.Application
        implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG=YambaApplication.class.getSimpleName();

    public SharedPreferences prefs;
    public Twitter twitter;
    private boolean serviceRunning;




    @Override
    public void onCreate() {

        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

        super.onCreate();
        Log.d(TAG,"Application created");
    }


    public synchronized Twitter getTwitter(){
        if (twitter==null){
            String consumerKey = prefs.getString("consumerKey","");
            String consumerSecret = prefs.getString("consumerSecret","");
            String accessTokenKey = prefs.getString("accessTokenKey","");
            String accessTokenSecret = prefs.getString("accessTokenSecret","");


            try{
                twitter = new TwitterFactory().getInstance();

                AccessToken ac = new AccessToken(accessTokenKey, accessTokenSecret);
                twitter.setOAuthConsumer(consumerKey, consumerSecret);
                twitter.setOAuthAccessToken(ac);
            }catch (Exception ex){

            }


        }

        return twitter;
    }

    @Override
    public void onTerminate() {
        Log.i(TAG, "Application terminated");
        super.onTerminate();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        this.twitter=null;
    }

    public boolean isServiceRunning() {
        return serviceRunning;
    }

    public void setServiceRunning(boolean serviceRunning) {
        this.serviceRunning = serviceRunning;
    }
}

package com.cemtaskin.yamba2;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.cemtaskin.yamba2.Adapters.TimeLineAdapter;
import com.cemtaskin.yamba2.Model.Timeline;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

public class TimeLineActivity extends BaseActivity {

    ProgressDialog dialog;
    Context context;

    @Bind(R.id.grdList) RecyclerView grdList;
    TimeLineAdapter adapter;

    YambaApplication app;
    TimeLineReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        ButterKnife.bind(this);

        app=(YambaApplication)getApplication();


        context=this;

        receiver=new TimeLineReceiver();
        LinearLayoutManager ll=new LinearLayoutManager(this);
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        grdList.setLayoutManager(ll);




        List<Timeline> data = Timeline.getTimelines();


        adapter=new TimeLineAdapter(data);
        grdList.setAdapter(adapter);


        //new AsynGetTweets().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @OnClick(R.id.btnSendBroadCast)
    public  void send(){
        Intent intent=new Intent("statusUpdated");
        sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver,new IntentFilter(UpdaterService.NEW_STATUS_INTENT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private class AsynGetTweets extends AsyncTask<Void,Void,List<Timeline>>{

        List<Timeline> result=new ArrayList<Timeline>();


        @Override
        protected List<Timeline> doInBackground(Void... params) {

            try {
                ResponseList<twitter4j.Status> timeline=app.getTwitter().getHomeTimeline();
                for(twitter4j.Status s: timeline){
                    Timeline t=new Timeline();
                    t.txt=s.getText();
                    t.user=s.getUser().getName();
                    t.created_at=s.getCreatedAt().getTime();
                    t.source=s.getSource();

                    result.add(t);

                }
            } catch (TwitterException e) {
                e.printStackTrace();

            }

            return result;

        }

        @Override
        protected void onPostExecute(List<Timeline> timelines) {
            super.onPostExecute(timelines);

            grdList.setAdapter(new TimeLineAdapter(timelines));

            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog= ProgressDialog.show(context, "Lütfen bekleyiniz",
                    "Twitter ile haberleşiyor...,", true);
        }
    }

    class TimeLineReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            adapter=new TimeLineAdapter(Timeline.getTimelines());
            adapter.notifyDataSetChanged();
            grdList.setAdapter(adapter);
        }
    }
}

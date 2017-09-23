package com.cemtaskin.yamba2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cemtaskin.yamba2.Model.Timeline;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;


public class StatusActivity extends BaseActivity {

    private String message;
    SharedPreferences prefs;



    @Bind(R.id.txtStatusText) EditText txtStatusText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_status);

        ButterKnife.bind(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

    }






    @OnClick(R.id.btnUpdateStatus)
    public void updateStatusClicked(View v){
       if (txtStatusText.getText().toString().equals("")){
           Toast.makeText(getApplicationContext(),"Lütfen geçerli bir durum giriniz",Toast.LENGTH_SHORT).show();
           return;
       }




       new AsynTwitterPost().execute(new String[]{txtStatusText.getText().toString()});
    }

    public String getMessage() {
        return txtStatusText.getText().toString();
    }

    public void setMessage(String message) {
        this.message = message;
    }


    private class AsynTwitterPost extends AsyncTask<String,Integer,Integer>{
        int successFullCount=0;

        @Override
        protected Integer doInBackground(String[] params) {


            YambaApplication application = (YambaApplication)getApplication();

            int index=0;

            for (String s : params){
                index++;

                try {
                    application.getTwitter().updateStatus(s);
                    publishProgress(index);
                    successFullCount++;
                } catch (TwitterException e) {
                    System.err.println("Error occurred while updating the status!");
                    publishProgress(index*-1);
                }

            }


            return successFullCount;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(values[0]<0){
                Toast.makeText(getApplicationContext(),String.format("%d. status can not be updated",values[0]*-1),Toast.LENGTH_SHORT).show();
            }else if (values[0]>0)
            {
                Toast.makeText(getApplicationContext(),String.format("%d. status can be updated",values[0]),Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Toast.makeText(getApplicationContext(), String.format("%d. status can  be updated",successFullCount), Toast.LENGTH_SHORT).show();
        }
    }


}

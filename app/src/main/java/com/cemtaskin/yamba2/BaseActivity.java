package com.cemtaskin.yamba2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.cemtaskin.yamba2.Model.Timeline;

/**
 * Created by ctaskin on 19/12/15.
 */
public class BaseActivity extends AppCompatActivity{
    YambaApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app=(YambaApplication)getApplication();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.mItemPrefs:

                Intent intent = new Intent(BaseActivity.this,PreferencesActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

                break;

            case R.id.mItemToogleService:

                if (app.isServiceRunning()){
                    Intent intentStartService = new Intent(BaseActivity.this,UpdaterService.class);
                    stopService(intentStartService);
                }else{
                    Intent intentStartService = new Intent(BaseActivity.this,UpdaterService.class);
                    startService(intentStartService);
                }



                break;

            case R.id.mItemStatusActivity:
                Intent i=new Intent(BaseActivity.this,StatusActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i);
                break;

            case R.id.mItemTimeLActivityActivity:
                Intent i2=new Intent(BaseActivity.this,TimeLineActivity.class);
                i2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(i2);
                break;
        }

        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {



        if (menu!=null){
            MenuItem toggleItem = menu.findItem(R.id.mItemToogleService);
            if (app.isServiceRunning()){
                toggleItem.setTitle("Stop Service");
            }else
            {
                toggleItem.setTitle("Start Service");
            }
        }


        return true;
    }
}

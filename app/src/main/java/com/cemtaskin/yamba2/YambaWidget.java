package com.cemtaskin.yamba2;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;

import static android.text.format.DateUtils.getRelativeTimeSpanString;

/**
 * Created by ctaskin on 30/12/15.
 */
public class YambaWidget extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Cursor c = context.getContentResolver().query(StatusProvider.CONTENT_URI,null,null,null,null);

        try {


           while (c.moveToNext()) {String user = c.getString(4);
              long created_at = c.getLong(1);
              String txt = c.getString(3);


                for (int appWidgetID : appWidgetIds) {
                    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.yamba_widget);
                   remoteViews.setTextViewText(R.id.txtUser2, user);
                    remoteViews.setTextViewText(R.id.txt2, txt);
                    String ago = (String) getRelativeTimeSpanString(created_at, Calendar.getInstance().getTimeInMillis(), DateUtils.SECOND_IN_MILLIS);
                    remoteViews.setTextViewText(R.id.txtTimeAgo2, ago);

                   remoteViews.setOnClickPendingIntent(R.id.yamba_icon, PendingIntent.getActivity(context, 0, new Intent(context, TimeLineActivity.class), 0));
                    appWidgetManager.updateAppWidget(appWidgetID,remoteViews);

                }
           }
        }finally {
            c.close();
        }




    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(UpdaterService.NEW_STATUS_INTENT)){
            Log.d("YWidget", "New status updated");

            AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
            this.onUpdate(context,appWidgetManager,appWidgetManager.getAppWidgetIds(new ComponentName(context,YambaWidget.class)));
        }
    }

}

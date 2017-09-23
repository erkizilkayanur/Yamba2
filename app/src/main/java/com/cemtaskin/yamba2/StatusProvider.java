package com.cemtaskin.yamba2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.activeandroid.Cache;
import com.activeandroid.content.ContentProvider;
import com.activeandroid.query.Select;
import com.cemtaskin.yamba2.Model.Timeline;

/**
 * Created by ctaskin on 30/12/15.
 */
public class StatusProvider extends ContentProvider {

    public static  final Uri CONTENT_URI = Uri.parse("content://com.cemtaskin.yamba2.StatusProvider");
    public static final String SINGLE_RECORD_MIME_TYPE="vdn.android.cursor.item/vdn.cemtaskin.yamba.status";
    public static final String MULTIPLE_RECORD_MIME_TYPE="vdn.android.cursor.item/vdn.cemtaskin.yamba.mstatus";



    @Override
    public String getType(Uri uri) {

        long id=getID(uri);
        if (id<0){
            return MULTIPLE_RECORD_MIME_TYPE;
        }else{
            return SINGLE_RECORD_MIME_TYPE;
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long created_at= values.getAsLong("created_at");
        long remoteID = values.getAsLong("remoteID");


        String user = values.getAsString("user");
        String txt = values.getAsString("txt");
        String source= values.getAsString("source");
        Timeline t=new Timeline(created_at,source,txt,user,remoteID);

        if (!Timeline.IsExists(t.remoteID)) {
            t.save();
        }

        return ContentUris.withAppendedId(uri, t.getId());
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {


        long id = getID(uri);

        long created_at= values.getAsLong("created_at");
        long remoteID = values.getAsLong("remoteID");


        String user = values.getAsString("user");
        String txt = values.getAsString("txt");
        String source= values.getAsString("source");

        Timeline t=Timeline.getTimelineByID(id);
        t.source=source;
        t.created_at=created_at;
        t.user=user;
        t.txt=txt;
        t.save();

        return (int)id;


    }

    public long getID(Uri uri){
        String lastSegment=uri.getLastPathSegment();
        if (lastSegment!=null){
            try{
                return Long.parseLong(lastSegment);
            }catch (Exception ex){}
        }
        return -1;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        long id = getID(uri);

        Timeline t=Timeline.getTimelineByID(id);
        t.delete();
        return (int)id;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {


        long id=getID(uri);

        if (id<0){
            String tableName = Cache.getTableInfo(Timeline.class).getTableName();
            // Query all items without any conditions
            String resultRecords = new Select(tableName + ".*, " + tableName + ".Id as _id").
                    from(Timeline.class).toSql();
            // Execute query on the underlying ActiveAndroid SQLite database
            Cursor resultCursor = Cache.openDatabase().rawQuery(resultRecords, null);
            return resultCursor;
        }else
        {
            String tableName = Cache.getTableInfo(Timeline.class).getTableName();
            // Query all items without any conditions
            String resultRecords = new Select(tableName + ".*, " + tableName + ".Id as _id").
                    from(Timeline.class).where("Id = ?",id).toSql();
            // Execute query on the underlying ActiveAndroid SQLite database
            Cursor resultCursor = Cache.openDatabase().rawQuery(resultRecords, null);
            return resultCursor;
        }




    }
}





















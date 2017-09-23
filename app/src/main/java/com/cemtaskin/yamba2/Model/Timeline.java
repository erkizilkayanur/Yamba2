package com.cemtaskin.yamba2.Model;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by ctaskin on 09/12/15.
 */
@Table(name = "timeLine")
public class Timeline extends Model {
    @Column(name="created_at")
    public long created_at;


    @Column(name="source")
    public String source;


    @Column(name="txt")
    public String txt;

    @Column(name="user")
    public String user;

    @Column(name="remoteID")
    public long remoteID;


    public Timeline() {
        super();
    }

    public Timeline(long created_at,String source, String txt,String user,long remoteID) {
        super();
        this.created_at = created_at;
        this.source=source;
        this.txt=txt;
        this.user=user;
        this.remoteID=remoteID;
    }



    public static Boolean IsExists(long remoteID){
        Timeline control = new Select()
                                .from(Timeline.class).
                                where("remoteID = ?",remoteID).executeSingle();

        if (control==null){
            return false;
        }else
        {
            return true;
        }
    }

    public static int getRowCount(){
      return new Select()
                .from(Timeline.class).count();



    }

    public static List<Timeline> getTimelines(){
        return new Select()
                .from(Timeline.class).orderBy("created_at DESC").execute();



    }

    public static Timeline getTimelineByID(long ID){
        return new Select()
                .from(Timeline.class).where("Id = ?",ID).executeSingle();
    }

}

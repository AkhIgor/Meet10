package com.example.meet10;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

/**
 * Created by Игорь on 05.07.2018.
 */

@Entity
public class Note {

    @PrimaryKey(autoGenerate = true) long ID;
    String Name;
    String Date;
    String Info;

    @Ignore private DbHelper dbHelper;
    @Ignore Context context;

    public Note() {
    }

    public Note(String name, String date, String info) {
        this.Name = name;
        this.Date = date;
        this.Info = info;
    }

    public Note(long id, String name, String date, String info) {
        this.ID = id;
        this.Name = name;
        this.Date = date;
        this.Info = info;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }
}

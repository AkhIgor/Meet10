package com.example.meet10;

import android.app.Application;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by Игорь on 08.07.2018.
 */

public class MyRoom extends Application {
    public static MyRoom instance;
    private DataBase dataBase;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        dataBase = Room.databaseBuilder(getApplicationContext(), DataBase.class, "database").build();
    }

    public static MyRoom getInstance() {
        return instance;
    }

    public DataBase getDatabase() {
        return dataBase;
    }
}

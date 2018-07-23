package com.example.meet10;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by Игорь on 08.07.2018.
 */

@Database(entities = {Note.class}, version = 1)
public abstract class DataBase extends RoomDatabase {
    public abstract NoteDao getNoteDAO();
}

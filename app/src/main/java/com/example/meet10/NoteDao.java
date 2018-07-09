package com.example.meet10;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Игорь on 08.07.2018.
 */

@Dao
public interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addItem(Note note);

    @Delete
    void deleteItem(Note note);

    @Update
    void updateItem(Note note);

    @Query("SELECT * FROM Note")
    List<Note> getNotes();
}

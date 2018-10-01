package uk.co.imagitech.simplecursoradapterloaderfreezerepro;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    Cursor getAll();

    @Query("SELECT count(*) FROM user")
    int getCount();

    @Insert
    void insertAll(List<User> users);
}
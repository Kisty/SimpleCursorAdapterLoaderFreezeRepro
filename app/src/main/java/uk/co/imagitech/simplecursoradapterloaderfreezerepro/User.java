package uk.co.imagitech.simplecursoradapterloaderfreezerepro;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public long _id;

    @ColumnInfo(name = "first_name")
    public final String firstName;

    @ColumnInfo(name = "last_name")
    public final String lastName;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
package com.lbs.patpat.ui.login_register;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {LoginedUser.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "USER";

    private static UserDatabase databaseInstance;

    public static synchronized UserDatabase getInstance(Context context)
    {
        if(databaseInstance == null)
        {
            databaseInstance = Room
                    .databaseBuilder(context.getApplicationContext(), UserDatabase.class, DATABASE_NAME)
                    .build();
        }
        return databaseInstance;
    }
    public abstract UserDao userDao();
}

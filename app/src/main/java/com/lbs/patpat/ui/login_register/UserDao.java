package com.lbs.patpat.ui.login_register;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    public void insertUser(LoginedUser user);

    @Query("DELETE FROM logineduser")
    public void deleteUser();

    @Query("SELECT * FROM logineduser")
    LiveData<List<LoginedUser>> getUser();

    @Query("SELECT COUNT(*) FROM logineduser")
    int getCount();

    @Query("SELECT uid FROM logineduser")
    int[] getUID();

    @Query("SELECT intro FROM logineduser")
    String[] getIntro();

    @Query("SELECT token FROM logineduser")
    String[] getToken();

    @Update
    public void addFan(LoginedUser... user);



}

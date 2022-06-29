package com.lbs.patpat.ui.login_register;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao    //数据访问对象
public interface UserDao {
    @Insert //插入用户信息
    public void insertUser(LoginedUser user);

    @Query("DELETE FROM logineduser")   // 删除用户
    public void deleteUser();

    @Query("SELECT * FROM logineduser") //获取用户列表作为LiveData对象
    LiveData<List<LoginedUser>> getUser();

    @Query("SELECT * FROM logineduser")
    List<LoginedUser> getLoginUser();


    @Query("SELECT COUNT(*) FROM logineduser")  //获取行数
    int getCount();

    @Query("SELECT uid FROM logineduser")   //获取UID
    String[] getUID();

    @Query("SELECT intro FROM logineduser")     //获取介绍
    String[] getIntro();

    @Query("SELECT token FROM logineduser")     //获取Token
    String[] getToken();

    @Update     //更新用户信息
    public void updateUser(LoginedUser... user);



}

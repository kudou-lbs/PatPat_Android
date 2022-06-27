package com.lbs.patpat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.navigation.NavigationView;
import com.lbs.patpat.databinding.ActivityMainBinding;
import com.lbs.patpat.global.MyActivity;
import com.lbs.patpat.global.MyApplication;
import com.lbs.patpat.ui.login_register.LoginActivity;
import com.lbs.patpat.ui.login_register.LoginedUser;
import com.lbs.patpat.ui.login_register.UserDao;
import com.lbs.patpat.viewmodel.UserViewModel;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class MainActivity extends MyActivity {

    private ActivityMainBinding binding;
    private ImageView icon;
    private TextView intro, nickName;
    private UserDao userDao;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userDao = MyApplication.getUserDatabase().userDao();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        View headerView = binding.navDrawerMenu.getHeaderView(0);
        icon = headerView.findViewById(R.id.header_avatar);
        intro = headerView.findViewById(R.id.header_follow_and_fans);
        nickName = headerView.findViewById(R.id.header_nickname);

        initBottomNav();
        initNavDrawerMenu();
        checkLoginState();
        //登录用户ViewModel
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getLoginedUser().observe(this, new Observer<List<LoginedUser>>() {
            @Override
            public void onChanged(List<LoginedUser> loginedUsers) {
                Log.d("TEST", "用户数为：" + String.valueOf(loginedUsers.size()));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (loginedUsers.size() == 0) {
                                    Toast.makeText(getApplicationContext(), "用户未登录", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (loginedUsers.get(0).intro.isEmpty())
                                        intro.setText("这个人很懒，什么都没有写");
                                    else
                                        intro.setText(loginedUsers.get(0).intro);
                                    if (loginedUsers.get(0).nickname.isEmpty())
                                        nickName.setText("暂无昵称");
                                    else
                                        nickName.setText(loginedUsers.get(0).nickname);

                                    if (loginedUsers.get(0).avatar.isEmpty()) {
                                        icon.setImageDrawable(getDrawable(R.drawable.icon_default));
                                    } else
                                        Glide.with(MainActivity.this)
                                                .load(getString(R.string.server_ip) + loginedUsers.get(0).avatar)
                                                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                                .into(icon);
                                }

                            }
                        });
                    }
                }).start();
            }

        });

        //适配问题
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainDrawerLayout, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                binding.container.setPadding(0, insets.getSystemWindowInsetTop(), 0, 0);
                return insets;
            }
        });
    }


    //初始化底部栏
    private void initBottomNav() {
        binding.navView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        //修复图片不显示原图
        binding.navView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_discovery, R.id.navigation_dynamic, R.id.navigation_messages)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    //初始化抽屉菜单
    private void initNavDrawerMenu() {
        //修复图片不显示原图

        icon.setBackground(getDrawable(R.drawable.icon_default));
        binding.navDrawerMenu.setItemIconTintList(null);
        binding.navDrawerMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //头部点击事件

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity.this,PersonalActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
    }

    //检验登录状态
    private void checkLoginState() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                //Log.d(getString(R.string.log_tag), "行数为：" + String.valueOf(userDao.getCount()));
                if (userDao.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), "用户未登录", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    Log.d("TEST", "用户未登录");
                } else {
                    try {
                        String cachedToken = userDao.getToken()[0];
                        String testTokenUrl = getString(R.string.server_ip)+"/user/test";
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .addHeader("token", cachedToken)
                                .post(new RequestBody() {
                                    @Override
                                    public MediaType contentType() {
                                        return null;
                                    }

                                    @Override
                                    public void writeTo(BufferedSink sink) throws IOException {

                                    }
                                })
                                .url(testTokenUrl)
                                .build();

                        Response response = null;
                        response = client.newCall(request).execute();
                        String responseBody = response.body().string();
                        Log.d("TEST","\ttestToken Body:"+responseBody );
                        if (responseBody.equals("请求成功")) {

                            Toast.makeText(getApplicationContext(), "用户已登录", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                            Log.d("TEST", "用户已登录");

                        } else {
                            userDao.deleteUser();
                            Toast.makeText(getApplicationContext(), "登录已过期", Toast.LENGTH_SHORT).show();

                            Log.d("TEST", "登录已过期");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("TEST", "检验出错");
                    }
                }
            }
        }).start();

    }

    public ActivityMainBinding getBinding() {
        return binding;
    }
}
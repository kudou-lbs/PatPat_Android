package com.lbs.patpat;

import android.content.Intent;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.navigation.NavigationView;
import com.lbs.patpat.databinding.ActivityMainBinding;
import com.lbs.patpat.global.MyActivity;
import com.lbs.patpat.ui.login_register.LoginActivity;
import com.lbs.patpat.ui.login_register.LoginedUser;
import com.lbs.patpat.viewmodel.UserViewModel;

import java.util.List;

public class MainActivity extends MyActivity {

    private ActivityMainBinding binding;
    private ImageView icon;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        initBottomNav();
        initNavDrawerMenu();
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getLoginedUser().observe(this, new Observer<List<LoginedUser>>() {
            @Override
            public void onChanged(List<LoginedUser> loginedUsers) {

            }
        });

        //适配问题
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainDrawerLayout, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                binding.container.setPadding(0,insets.getSystemWindowInsetTop(),0,0);
                return insets;
            }
        });
    }

    //初始化底部栏
    private void initBottomNav(){
        binding.navView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        //修复图片不显示原图
        binding.navView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_discovery, R.id.navigation_dynamic,R.id.navigation_messages)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    //初始化抽屉菜单
    private void initNavDrawerMenu(){
        //修复图片不显示原图
        View headerView=binding.navDrawerMenu.getHeaderView(0);
        ImageView icon=headerView.findViewById(R.id.header_avatar);
        icon.setBackground(getDrawable(R.drawable.icon_default));
        binding.navDrawerMenu.setItemIconTintList(null);
        binding.navDrawerMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Toast.makeText(MainActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //头部点击事件

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity.this,PersonalActivity.class);
//                startActivity(intent);
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public ActivityMainBinding getBinding() {
        return binding;
    }
}
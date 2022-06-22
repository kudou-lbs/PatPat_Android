package com.lbs.patpat;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import androidx.core.view.ViewCompat;

import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.jaeger.library.StatusBarUtil;
import com.lbs.patpat.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initBottomNav();
        initNavDrawerMenu();

        //状态栏字体颜色为黑
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

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
        binding.navDrawerMenu.setItemIconTintList(null);

    }
    
    public ActivityMainBinding getBinding() {
        return binding;
    }
}
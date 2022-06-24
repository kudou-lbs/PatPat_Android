package com.lbs.patpat.global;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MyActivity extends AppCompatActivity implements BackHandledInterface{
    public BackHandledFragment currentFragment=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //状态栏字体颜色为黑
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void setSelectedFragment(BackHandledFragment backHandledFragment) {
        currentFragment=backHandledFragment;
    }

    @Override
    public void onBackPressed() {
        if(currentFragment==null||!currentFragment.onBackPressed()){
            super.onBackPressed();
        }else{
            getSupportFragmentManager().popBackStack();
        }
    }
}

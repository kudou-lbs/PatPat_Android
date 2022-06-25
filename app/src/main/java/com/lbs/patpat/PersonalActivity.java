package com.lbs.patpat;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lbs.patpat.databinding.ActivityPersonalBinding;
import com.lbs.patpat.global.MyActivity;

public class PersonalActivity extends MyActivity {

    private ActivityPersonalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPersonalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initClick();
    }

    private void initClick(){

    }
}
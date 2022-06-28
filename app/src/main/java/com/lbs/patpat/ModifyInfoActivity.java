package com.lbs.patpat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lbs.patpat.databinding.ActivityModifyInfoBinding;

public class ModifyInfoActivity extends AppCompatActivity {

    private ActivityModifyInfoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModifyInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
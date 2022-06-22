package com.lbs.patpat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lbs.patpat.global.MyActivity;

public class PersonalActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
    }
}
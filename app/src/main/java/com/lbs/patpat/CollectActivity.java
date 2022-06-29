package com.lbs.patpat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.lbs.patpat.databinding.ActivityCollectBinding;
import com.lbs.patpat.fragment.WebViewFragment.WebViewFragment;
import com.lbs.patpat.global.MyActivity;

public class CollectActivity extends MyActivity {

    ActivityCollectBinding binding;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCollectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        uid=getIntent().getStringExtra("uid");
        init();
    }

    private void init(){
        binding.toolbarCollect.discoverGameType.setText("收藏");
        binding.toolbarCollect.discoverGameType.setTextColor(getColor(R.color.black));
        binding.toolbarCollect.discoverReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //绑定fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_collct, WebViewFragment.newInstance(WebViewFragment.USER_COLLECT_POST,uid))
                .commit();
    }
}
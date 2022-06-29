package com.lbs.patpat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayoutMediator;
import com.lbs.patpat.R;
import com.lbs.patpat.databinding.ActivityFollowAndFansBinding;
import com.lbs.patpat.fragment.ListFragment;
import com.lbs.patpat.viewmodel.FollowAndFanViewModel;

/**
 * 打开该活动前，务必传入uid，根据当前用户id展示关注数及粉丝数
 * intent.putExtra("uid", uid);
 * 该活动也是唯一一个能使用FollowAndFanViewModel的活动，因为ViewModel的特殊性，暂时无法做到完全解耦
* */
public class FollowAndFansActivity extends AppCompatActivity {

    ActivityFollowAndFansBinding binding;
    String[] tabs;
    public static String uid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFollowAndFansBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=getIntent();
        uid=intent.getStringExtra("uid");
        tabs=new String[]{"关注","粉丝"};
        //viewModel=
        initTabAndPager();
    }

    private void initTabAndPager() {
        binding.followAndFansPager.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(),getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position){
                    case 0:
                        return ListFragment.newInstance(ListFragment.PERSONAL_FOLLOW);
                    case 1:
                        return ListFragment.newInstance(ListFragment.PERSONAL_FAN);
                    default:
                        return null;
                }
            }

            @Override
            public int getItemCount() {
                return tabs.length;
            }
        });

        new TabLayoutMediator(binding.followAndFansTab,binding.followAndFansPager,((tab, position) -> {
            tab.setText(tabs[position]);
        })).attach();
    }
}
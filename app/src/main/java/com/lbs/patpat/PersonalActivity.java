package com.lbs.patpat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayoutMediator;
import com.lbs.patpat.databinding.ActivityPersonalBinding;
import com.lbs.patpat.fragment.PersonalAboutFragment;
import com.lbs.patpat.fragment.PersonalPublishFragment.PersonalPublishFragment;
import com.lbs.patpat.global.MyActivity;
import com.lbs.patpat.ui.login_register.LoginedUser;
import com.lbs.patpat.viewmodel.UserViewModel;

import java.util.List;

public class PersonalActivity extends MyActivity implements View.OnClickListener{

    private ActivityPersonalBinding binding;
    private String[] tabAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPersonalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        displayInfo();
        initTabAndPager();
        initClick();
        //登录用户ViewModel
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getLoginedUser().observe(this, new Observer<List<LoginedUser>>() {
            @Override
            public void onChanged(List<LoginedUser> loginedUsers) {

            }

        });

    }

    private void displayInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

    private void initTabAndPager(){
        tabAll=new String[]{"发布","关于"};
        binding.personalPager.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(),getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position){
                    case 0:
                        return new PersonalPublishFragment();
                    case 1:
                        return new PersonalAboutFragment();
                    default:
                        break;
                }
                return null;
            }

            @Override
            public int getItemCount() {
                return tabAll.length;
            }
        });

        new TabLayoutMediator(binding.personalTabAll,binding.personalPager,((tab, position) -> {
            tab.setText(tabAll[position]);
        })).attach();
    }

    private void initClick(){binding.personalReturn.setOnClickListener(this);}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.personal_return:
                finish();
                break;
            default:
                break;
        }
    }
}
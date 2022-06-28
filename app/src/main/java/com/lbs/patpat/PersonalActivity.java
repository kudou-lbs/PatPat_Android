package com.lbs.patpat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lbs.patpat.databinding.ActivityPersonalBinding;
import com.lbs.patpat.fragment.PersonalAboutFragment;
import com.lbs.patpat.fragment.PersonalPublishFragment.PersonalPublishFragment;
import com.lbs.patpat.global.MyActivity;
import com.lbs.patpat.ui.login_register.LoginedUser;
import com.lbs.patpat.viewmodel.FollowAndFanViewModel;
import com.lbs.patpat.viewmodel.UserViewModel;

import java.io.InputStream;
import java.util.List;

public class PersonalActivity extends MyActivity implements View.OnClickListener {

    private ActivityPersonalBinding binding;
    private String[] tabAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initTabAndPager();
        initClick();
        //登录用户ViewModel
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getLoginedUser().observe(this, new Observer<List<LoginedUser>>() {
            @Override
            public void onChanged(List<LoginedUser> loginedUsers) {

                if (loginedUsers.get(0).avatar.equals("null")) {
                    binding.personalIcon.setImageDrawable(getDrawable(R.drawable.icon_default));
                } else
                    Glide.with(PersonalActivity.this)
                            .load(getString(R.string.server_ip) + loginedUsers.get(0).avatar)
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(binding.personalIcon);

                if (loginedUsers.get(0).background.equals("null")) {
                    binding.personalBaseInfo.setBackground(getDrawable(R.drawable.drawer_background_custom));
                    binding.personalBaseInfo.getBackground().setAlpha(230);
                } else
                    Glide.with(PersonalActivity.this)
                            .load(getString(R.string.server_ip) + loginedUsers.get(0).background)
                            //.override(binding.personalBaseInfo.getWidth(),binding.personalBaseInfo.getHeight())
                            //.optionalCenterCrop()
                            .into(new AdaptiveBackground(binding.personalBaseInfo));

                if (loginedUsers.get(0).nickname.equals("null"))
                    binding.personalNickname.setText("暂无昵称");
                else
                    binding.personalNickname.setText(loginedUsers.get(0).nickname);
                binding.personalFollowNum.setText("关注" + String.valueOf(loginedUsers.get(0).followNum));
                binding.personalFanNum.setText("粉丝" + String.valueOf(loginedUsers.get(0).fansNum));
                if (loginedUsers.get(0).intro.equals("null"))
                    binding.personalIntro.setText("这个人很懒，什么都没有写");
                else
                    binding.personalIntro.setText(loginedUsers.get(0).intro);

            }

        });

    }

    private void initTabAndPager() {
        tabAll = new String[]{"发布", "关于"};
        binding.personalPager.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
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

        new TabLayoutMediator(binding.personalTabAll, binding.personalPager, ((tab, position) -> {
            tab.setText(tabAll[position]);
        })).attach();
    }

    private void initClick() {
        binding.personalReturn.setOnClickListener(this);
        binding.personalFollowNum.setOnClickListener(this);
        binding.personalFanNum.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal_return:
                finish();
                break;
            case R.id.personal_follow_num:
            case R.id.personal_fan_num:
                Intent intent=new Intent(PersonalActivity.this, FollowAndFansActivity.class);
                //这里要放当前用户id
                intent.putExtra("uid",MainActivity.getUid());
                startActivity(intent);
                break;
            default:
                break;
        }
    }


}
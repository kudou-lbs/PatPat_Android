package com.lbs.patpat.ui.home;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lbs.patpat.MainActivity;
import com.lbs.patpat.R;
import com.lbs.patpat.SearchActivity;
import com.lbs.patpat.databinding.FragmentHomeBinding;
import com.lbs.patpat.fragment.WebViewFragment.WebViewFragment;
import com.lbs.patpat.global.MyApplication;
import com.lbs.patpat.ui.login_register.LoginedUser;
import com.lbs.patpat.viewmodel.UserViewModel;

import java.util.List;

public class HomeFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener,View.OnClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private String[] tabs;
    private ImageView avatar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        avatar = binding.tlExpend.toolbarPersonalHome;
        tabs=new String[2];
        tabs[0]=new String(getString(R.string.home_recommend));
        tabs[1]=new String(getString(R.string.home_leaderboard));

        //LiveData更新头像
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getLoginedUser().observe(requireActivity(), new Observer<List<LoginedUser>>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onChanged(List<LoginedUser> loginedUsers) {
                if (loginedUsers.size()==1){
                    if(loginedUsers.get(0).avatar.equals("null")) {
                        avatar.setImageDrawable(requireActivity().getDrawable(R.drawable.icon_default));
                    }
                    else
                        Glide.with(MyApplication.getContext())
                                .load(requireActivity().getString(R.string.server_ip) + loginedUsers.get(0).avatar)
                                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                .into(avatar);

                }
                else
                    avatar.setImageDrawable(getContext().getDrawable(R.drawable.icon_default));
            }
        });
        //设置适配器
        binding.homePager.setAdapter(new FragmentStateAdapter(getActivity().getSupportFragmentManager(),getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return WebViewFragment.newInstance(position);
            }

            @Override
            public int getItemCount() {
                return tabs.length;
            }
        });

        binding.tlExpend.toolbarSearchHome.setOnClickListener(this);
        binding.tlExpend.toolbarPersonalHome.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //tabLayout和viewPager2绑定
        new TabLayoutMediator(binding.homeTab,binding.homePager,((tab1, position) -> tab1.setText(tabs[position]))).attach();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TEST", "onResume");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_search_home:
                Intent intent=new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.toolbar_personal_home:
                ((MainActivity)getActivity()).getBinding().mainDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
    }
}
package com.lbs.patpat.ui.home;


import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lbs.patpat.MainActivity;
import com.lbs.patpat.R;
import com.lbs.patpat.SearchActivity;
import com.lbs.patpat.databinding.FragmentHomeBinding;
import com.lbs.patpat.global.CircleImageDrawable;
import com.lbs.patpat.ui.webviewFragment.webViewFragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener,View.OnClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private String[] tabs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        tabs=new String[2];
        tabs[0]=new String(getString(R.string.home_recommend));
        tabs[1]=new String(getString(R.string.home_leaderboard));

        //设置适配器
        binding.homePager.setAdapter(new FragmentStateAdapter(getActivity().getSupportFragmentManager(),getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return webViewFragment.newInstance(position);
            }

            @Override
            public int getItemCount() {
                return tabs.length;
            }
        });

        //设置未登录时头像
        binding.tlExpend.toolbarPersonal.setImageDrawable(
                new CircleImageDrawable(CircleImageDrawable.drawableToBitmap(
                                getResources().getDrawable(R.drawable.ic_launcher_background,null))));
        binding.tlExpend.toolbarSearch.setOnClickListener(this);
        binding.tlExpend.toolbarPersonal.setOnClickListener(this);

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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_search:
                Intent intent=new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.toolbar_personal:
                ((MainActivity)getActivity()).getBinding().mainDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
    }
}
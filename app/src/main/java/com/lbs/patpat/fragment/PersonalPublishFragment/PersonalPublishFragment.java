package com.lbs.patpat.fragment.PersonalPublishFragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayoutMediator;
import com.lbs.patpat.MainActivity;
import com.lbs.patpat.R;
import com.lbs.patpat.databinding.FragmentPersonalPublishBinding;
import com.lbs.patpat.fragment.WebViewFragment.WebViewFragment;

public class PersonalPublishFragment extends Fragment {

    private PersonalPublishViewModel mViewModel;
    private FragmentPersonalPublishBinding binding;
    private String uid;
    private String[] publishTab=new String[]{"帖子","评论","喜欢"};

    public PersonalPublishFragment(String uid) {
        super();
        this.uid = uid;
        Log.d("当前用户与登录用户：",this.uid+" "+MainActivity.getUid());
    }

    public static PersonalPublishFragment newInstance(String uid) {
        return new PersonalPublishFragment(uid);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding=FragmentPersonalPublishBinding.inflate(inflater,container,false);
        mViewModel = new ViewModelProvider(this).get(PersonalPublishViewModel.class);
        binding.personalPublishPager.setAdapter(new FragmentStateAdapter(getActivity().getSupportFragmentManager(),getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position){
                    case 0:
                        return WebViewFragment.newInstance(WebViewFragment.USER_POST, uid);
                    case 1:
                        return WebViewFragment.newInstance(WebViewFragment.USER_POST_REPLY,uid);
                    case 2:
                        return WebViewFragment.newInstance(WebViewFragment.USER_LIKE_POST,uid);
                    default:
                        return WebViewFragment.newInstance(WebViewFragment.DEFAULT);
                }
            }

            @Override
            public int getItemCount() {
                return publishTab.length;
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new TabLayoutMediator(binding.personalPublishTab,binding.personalPublishPager,((tab, position) -> {
            tab.setText(publishTab[position]);
        })).attach();
    }
}
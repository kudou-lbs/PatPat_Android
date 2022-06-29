package com.lbs.patpat.fragment.PersonalPublishFragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

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
    private String[] publishTab=new String[]{"帖子","评论","喜欢"};

    public static PersonalPublishFragment newInstance() {
        return new PersonalPublishFragment();
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
                        return WebViewFragment.newInstance(WebViewFragment.USER_POST, MainActivity.getUid());
                    default:
                        return WebViewFragment.newInstance(position);
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
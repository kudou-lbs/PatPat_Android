package com.lbs.patpat.ui.message;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;
import com.lbs.patpat.databinding.FragmentMessageBinding;
import com.lbs.patpat.fragment.WebViewFragment.WebViewFragment;


public class MessageFragment extends Fragment {

    private FragmentMessageBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initTabAndPager();

        return root;
    }

    private void initTabAndPager(){
        String[] tabs=new String[]{"回复","点赞"};
        binding.messagePager.setAdapter(new FragmentStateAdapter(getActivity().getSupportFragmentManager(),getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position){
                    case 0:
                        return WebViewFragment.newInstance(WebViewFragment.USER_MESSAGE_REPLY);
                    case 1:
                        return WebViewFragment.newInstance(WebViewFragment.USER_MESSAGE_LIKE);
                    default:
                        return WebViewFragment.newInstance(WebViewFragment.DEFAULT);
                }
            }

            @Override
            public int getItemCount() {
                return tabs.length;
            }
        });
        new TabLayoutMediator(binding.tlMessage.toolbarTabMessage, binding.messagePager, ((tab, position) -> {
            tab.setText(tabs[position]);
        })).attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
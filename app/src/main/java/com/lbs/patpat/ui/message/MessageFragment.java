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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.lbs.patpat.R;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lbs.patpat.databinding.FragmentMessageBinding;
import com.lbs.patpat.global.MyApplication;
import com.lbs.patpat.ui.login_register.LoginedUser;
import com.lbs.patpat.viewmodel.UserViewModel;

import java.util.List;
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

        //LiveData更新头像，详见HomeFragment
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getLoginedUser().observe(getViewLifecycleOwner(), new Observer<List<LoginedUser>>() {
            @Override
            public void onChanged(List<LoginedUser> loginedUsers) {
                if (loginedUsers.size() == 1) {
                    if (loginedUsers.get(0).avatar.equals("null"))
                        binding.tlMessage.imageView3.setImageDrawable(MyApplication.getContext().getDrawable(R.drawable.icon_default));
                    else
                        Glide.with(MyApplication.getContext())
                                .load(MyApplication.getContext().getString(R.string.server_ip) + loginedUsers.get(0).avatar)
                                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                .into(binding.tlMessage.imageView3);

                } else
                    binding.tlMessage.imageView3.setImageDrawable(MyApplication.getContext().getDrawable(R.drawable.icon_default));
            }
        });
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
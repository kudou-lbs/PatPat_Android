package com.lbs.patpat.ui.dynamic;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.material.tabs.TabLayoutMediator;
import com.lbs.patpat.MainActivity;
import com.lbs.patpat.R;
import com.lbs.patpat.SearchActivity;
import com.lbs.patpat.databinding.FragmentDynamicBinding;
import com.lbs.patpat.fragment.ListFragment;
import com.lbs.patpat.fragment.WebViewFragment.WebViewFragment;
import com.lbs.patpat.global.MyApplication;
import com.lbs.patpat.ui.login_register.LoginedUser;
import com.lbs.patpat.viewmodel.UserViewModel;

import java.util.List;
import java.util.Objects;

public class DynamicFragment extends Fragment implements View.OnClickListener{

    private DynamicViewModel dynamicViewModel;
    private FragmentDynamicBinding binding;
    private ImageView avatar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dynamicViewModel = new ViewModelProvider(this).get(DynamicViewModel.class);

        binding = FragmentDynamicBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.dynamicToolbar.imageView2.setOnClickListener(this);
        binding.dynamicToolbar.imageView3.setOnClickListener(this);
        avatar = binding.dynamicToolbar.imageView3;
        //LiveData更新头像
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getLoginedUser().observe(getViewLifecycleOwner(), new Observer<List<LoginedUser>>() {
            @Override
            public void onChanged(List<LoginedUser> loginedUsers) {
                if (loginedUsers.size()==1){
                    if(loginedUsers.get(0).avatar.equals("null"))
                        avatar.setImageDrawable(requireActivity().getDrawable(R.drawable.icon_default));
                    else
                        Glide.with(MyApplication.getContext())
                                .load(requireActivity().getString(R.string.server_ip) + loginedUsers.get(0).avatar)
                                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                .into(avatar);

                }
                else
                    avatar.setImageDrawable(requireActivity().getDrawable(R.drawable.icon_default));
            }
        });

        //tabLayout与viewPager适配器
        binding.dynamicPager.setAdapter(new FragmentStateAdapter(getActivity().getSupportFragmentManager(),getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                int webViewType= WebViewFragment.DEFAULT;
                switch (position){
                    //webView：关注和推荐
                    case 0:
                        webViewType= WebViewFragment.DYNAMIC_FOLLOW;
                        break;
                    case 1:
                        webViewType= WebViewFragment.DYNAMIC_RECOMMEND;
                        break;
                    //ListView：论坛
                    case 2:
                        return ListFragment.newInstance(WebViewFragment.DYNAMIC_FORUM);
                }

                return WebViewFragment.newInstance(webViewType);
            }

            @Override
            public int getItemCount() {
                return Objects.requireNonNull(dynamicViewModel.getTabItems().getValue()).length;
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //绑定viewpager&tab
        new TabLayoutMediator(binding.dynamicToolbar.toolbarTabDynamic, binding.dynamicPager,
                ((tab, position) -> tab.setText(Objects.requireNonNull(dynamicViewModel.getTabItems().getValue())[position])))
                .attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView2:
                Intent intent=new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.imageView3:
                ((MainActivity)getActivity()).getBinding().mainDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
    }
}
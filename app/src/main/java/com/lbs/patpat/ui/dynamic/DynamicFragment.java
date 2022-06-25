package com.lbs.patpat.ui.dynamic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;
import com.lbs.patpat.databinding.FragmentDynamicBinding;
import com.lbs.patpat.fragment.ListFragment;
import com.lbs.patpat.fragment.WebViewFragment.WebViewFragment;

import java.util.Objects;

public class DynamicFragment extends Fragment {

    private DynamicViewModel dynamicViewModel;
    private FragmentDynamicBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dynamicViewModel = new ViewModelProvider(this).get(DynamicViewModel.class);

        binding = FragmentDynamicBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
}
package com.lbs.patpat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayoutMediator;
import com.lbs.patpat.adapter.ForumListAdapter;
import com.lbs.patpat.databinding.ActivityForumBinding;
import com.lbs.patpat.fragment.WebViewFragment.WebViewFragment;
import com.lbs.patpat.global.MyActivity;

public class ForumActivity extends MyActivity {

    private ActivityForumBinding binding;
    private String[] forumTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityForumBinding.inflate(getLayoutInflater());
        forumTab=new String[]{"全部","官方","精华"};
        initTabAndPager();
        setContentView(binding.getRoot());
    }
    private void initTabAndPager(){
        //关闭预加载
        binding.forumPager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        ((RecyclerView)binding.forumPager.getChildAt(0)).getLayoutManager().setItemPrefetchEnabled(false);
        binding.forumPager.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(),getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return WebViewFragment.newInstance(WebViewFragment.DYNAMIC_RECOMMEND);
            }

            @Override
            public int getItemCount() {
                return forumTab.length;
            }
        });
        new TabLayoutMediator(binding.forumTab,binding.forumPager,((tab, position) -> {
            tab.setText(forumTab[position]);
        })).attach();
    }
}
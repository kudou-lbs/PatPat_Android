package com.lbs.patpat;

import static com.lbs.patpat.global.MyApplication.urlPrefix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lbs.patpat.adapter.ForumListAdapter;
import com.lbs.patpat.databinding.ActivityForumBinding;
import com.lbs.patpat.fragment.WebViewFragment.WebViewFragment;
import com.lbs.patpat.global.MyActivity;
import com.lbs.patpat.model.ForumDetailModel;
import com.lbs.patpat.viewmodel.ForumViewModel;

public class ForumActivity extends MyActivity {

    private ActivityForumBinding binding;
    private String[] forumTab;
    private ForumViewModel forumViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityForumBinding.inflate(getLayoutInflater());
        forumTab=new String[]{"全部","官方","精华"};
        initTabAndPager();
        initData();
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
    private void initData(){
        Intent intent=getIntent();
        Log.d("lbsss",intent.getStringExtra("fid"));
        forumViewModel=new ForumViewModel(intent.getStringExtra("fid"));

        forumViewModel.getForumDetailModelMutableLiveData().observe(this, new Observer<ForumDetailModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(ForumDetailModel forumDetailModel) {
                Glide.with(binding.getRoot())
                        .load(urlPrefix+forumDetailModel.getIcon())
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                        .into(binding.itemForumIcon);
                binding.itemForumName.setText(forumDetailModel.getName());

                binding.itemForumIntro.setText(forumDetailModel.getFollowNum()+"粉丝    "+forumDetailModel.getPostNum()+"帖子");
            }
        });
    }
}
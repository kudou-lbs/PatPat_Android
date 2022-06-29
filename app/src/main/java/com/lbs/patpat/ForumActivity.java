package com.lbs.patpat;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lbs.patpat.databinding.ActivityForumBinding;
import com.lbs.patpat.fragment.WebViewFragment.WebViewFragment;
import com.lbs.patpat.global.MyActivity;
import com.lbs.patpat.global.MyApplication;
import com.lbs.patpat.model.ForumDetailModel;
import com.lbs.patpat.viewmodel.ForumViewModel;

public class ForumActivity extends MyActivity implements View.OnClickListener{

    private ActivityForumBinding binding;
    private String[] forumTab;
    private ForumViewModel forumViewModel;
    private String fid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityForumBinding.inflate(getLayoutInflater());
        forumTab=new String[]{"全部","官方","精华"};
        initTabAndPager();
        initData();
        initClick();
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
                return WebViewFragment.newInstance(WebViewFragment.FORUM_POST,fid);
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
        fid=intent.getStringExtra("fid");
        forumViewModel=new ForumViewModel(fid);

        forumViewModel.getForumDetailModelMutableLiveData().observe(this, new Observer<ForumDetailModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(ForumDetailModel forumDetailModel) {
                Glide.with(binding.getRoot())
                        .load(MyApplication.getContext().getString(R.string.server_ip)+"/" +forumDetailModel.getIcon())
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                        .into(binding.itemForumIcon);
                binding.itemForumName.setText(forumDetailModel.getName());

                binding.itemForumIntro.setText(forumDetailModel.getFollowNum()+"粉丝    "+forumDetailModel.getPostNum()+"帖子");
            }
        });
        forumViewModel.getFollowed().observe(this, new Observer<Boolean>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean) {
                    binding.itemForumFollowed.setText("关注");
                    binding.itemForumFollowed.setTextColor(getColor(R.color.black));
                    binding.itemForumFollowed.setBackground(getDrawable(R.drawable.shape_botton_unfollow));
                }else{
                    binding.itemForumFollowed.setText("已关注");
                    binding.itemForumFollowed.setTextColor(getColor(R.color.grey_background));
                    binding.itemForumFollowed.setBackground(getDrawable(R.drawable.shape_botton_follow));
                }
            }
        });
    }
    private void initClick() {
        binding.forumSearchReturn.setOnClickListener(this);
        binding.floatingButtonNewPost.setOnClickListener(this);
        binding.itemForumFollowed.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forum_search_return:
                finish();
                break;
            case R.id.floating_button_new_post:
                Intent intent=new Intent(this,PostWriteActivity.class);
                intent.putExtra("fid",fid);
                startActivity(intent);
                break;
            case R.id.item_forum_followed:
                forumViewModel.onClickFollow();
                break;
            default:
                break;
        }
    }
}
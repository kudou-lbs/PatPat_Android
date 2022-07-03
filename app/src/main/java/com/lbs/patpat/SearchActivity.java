package com.lbs.patpat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayoutMediator;
import com.lbs.patpat.databinding.ActivitySearchBinding;
import com.lbs.patpat.global.MyActivity;
import com.lbs.patpat.viewmodel.SearchViewModel;
import com.lbs.patpat.fragment.ListFragment;
import com.lbs.patpat.fragment.WebViewFragment.WebViewFragment;

import java.util.Objects;

public class SearchActivity extends MyActivity {

    private ActivitySearchBinding binding;
    private SearchViewModel searchViewModel;
    private boolean initTabAndPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySearchBinding.inflate(getLayoutInflater());
        View root=binding.getRoot();
        setContentView(root);

        searchViewModel=new SearchViewModel();
        initToolBar();
        initSearchPage();
    }

    //初始化标题栏
    private void initToolBar(){
        //返回
        binding.toolbarSearchSearch.searchReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolbarSearchSearch.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //搜索结果
                String key=binding.toolbarSearchSearch.searchEdit.getText().toString().trim();
                if(key.equals("")){
                    Toast.makeText(SearchActivity.this,"请输入有效搜索词",Toast.LENGTH_SHORT).show();
                    return;
                }

                //未初始化则先初始化，也就是绑定
                if(!initTabAndPager){
                    initResultPage(key);
                    initTabAndPager=true;
                }else{
                    renewPagerView(key);
                }
                binding.searchTab.setVisibility(View.VISIBLE);
                binding.hotSearch.setVisibility(View.GONE);
                binding.searchResultPage.setVisibility(View.VISIBLE);
            }
        });
    }

    //初始化搜索前界面
    private void initSearchPage(){
        searchViewModel.getHotSearch().observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(String[] strings) {
                binding.hot1.hottestNum.setText("1");
                binding.hot1.hottestName.setText(strings[0]);
                binding.hot2.hottestNum.setText("2");
                binding.hot2.hottestName.setText(strings[1]);
                binding.hot3.hottestNum.setText("3");
                binding.hot3.hottestName.setText(strings[2]);
                binding.hot4.hottestNum.setText("4");
                binding.hot4.hottestName.setText(strings[3]);
                binding.hot5.hottestNum.setText("5");
                binding.hot5.hottestName.setText(strings[4]);
                binding.hot6.hottestNum.setText("6");
                binding.hot6.hottestName.setText(strings[5]);
            }
        });
        searchViewModel.makeRenewHotApiCall();
    }

    //初始化搜索结果界面
    private void initResultPage(String key){
        renewPagerView(key);
        new TabLayoutMediator(binding.searchTab,binding.searchPager,
                ((tab, position) -> tab.setText(Objects.requireNonNull(searchViewModel.getResultClassify().getValue())[position])))
                .attach();
    }

    //更新搜索结果
    private void renewPagerView(String key){
        Log.d("lbs","开始搜索");
        binding.searchPager.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(),getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position){
                    //case 0与默认一致，返回游戏列表
                    case 0:
                        return WebViewFragment.newInstance(WebViewFragment.SEARCH_POST,key);
                    case 1:
                        return WebViewFragment.newInstance(WebViewFragment.SEARCH_GAMES,key);
                    case 2:
                        return ListFragment.newSearchInstance(WebViewFragment.SEARCH_FORUM,key);
                    case 3:
                        return ListFragment.newSearchInstance(WebViewFragment.SEARCH_USER,key);
                    default:
                        //默认返回游戏列表
                        return WebViewFragment.newInstance(WebViewFragment.DEFAULT);
                }
            }

            @Override
            public int getItemCount() {
                return Objects.requireNonNull(searchViewModel.getResultClassify().getValue()).length;
            }
        });
    }
}
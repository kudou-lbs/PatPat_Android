package com.lbs.patpat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayoutMediator;
import com.lbs.patpat.databinding.ActivitySearchBinding;
import com.lbs.patpat.viewmodel.SearchViewModel;
import com.lbs.patpat.fragment.ListFragment;
import com.lbs.patpat.ui.webviewFragment.webViewFragment;

import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;
    private SearchViewModel searchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySearchBinding.inflate(getLayoutInflater());
        View root=binding.getRoot();
        setContentView(root);

        searchViewModel=new SearchViewModel();
        initToolBar();
        initSearchPage();
        initResultPage();

        //状态栏字体颜色为黑
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
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
    }

    //初始化搜索结果界面
    private void initResultPage(){
        binding.searchPager.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(),getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position){
                    //case 0与默认一致，返回游戏列表
                    case 0:
                        return webViewFragment.newInstance(webViewFragment.SEARCH_GAMES);
                    case 1:
                        return ListFragment.newInstance(webViewFragment.SEARCH_FORUM);
                    case 2:
                        return ListFragment.newInstance(webViewFragment.SEARCH_USER);
                    default:
                        //默认返回游戏列表
                        return webViewFragment.newInstance(webViewFragment.DEFAULT);

                }

            }

            @Override
            public int getItemCount() {
                return Objects.requireNonNull(searchViewModel.getResultClassify().getValue()).length;
            }
        });
        new TabLayoutMediator(binding.searchTab,binding.searchPager,
                ((tab, position) -> tab.setText(Objects.requireNonNull(searchViewModel.getResultClassify().getValue())[position])))
                .attach();
    }
}
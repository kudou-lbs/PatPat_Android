package com.lbs.patpat.fragment.webviewFragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import com.lbs.patpat.R;
import com.lbs.patpat.databinding.FragmentWebViewBinding;
import com.lbs.patpat.global.MyApplication;

public class webViewFragment extends Fragment {

    public final static int DEFAULT=-1;
    public final static int RECOMMEND_PAGE=0;
    public final static int LEADER_PAGE=1;
    public final static int DYNAMIC_FOLLOW=2;
    public final static int DYNAMIC_RECOMMEND=3;
    public final static int DYNAMIC_FORUM=4;
    public final static int SEARCH_GAMES=5;
    //后面两个适应ListFragment
    public final static int SEARCH_FORUM=6;
    public final static int SEARCH_USER=7;

    private WebViewViewModel mViewModel;
    //请求webView地址
    private String url1="http://www.baidu.com";
    private int requestPage;
    private FragmentWebViewBinding binding;

    //这里添加根据pos修改url1
    private webViewFragment(int requestPage){
        super();
        this.requestPage=requestPage;
    }

    //根据position改变行为
    public static webViewFragment newInstance(int position) {
        webViewFragment fragment=new webViewFragment(position);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding =FragmentWebViewBinding.inflate(inflater, container, false);
        //根据请求页面设置url
        String viewSelect=null;
        switch (requestPage){
            //首页推荐
            case RECOMMEND_PAGE:
                viewSelect=MyApplication.getInstance().getString(R.string.url_recommend);
                break;
            //首页排行榜
            case LEADER_PAGE:
                viewSelect=MyApplication.getInstance().getString(R.string.url_leaderboard);
                break;
            //动态关注&推荐
            case DYNAMIC_FOLLOW:
            case DYNAMIC_RECOMMEND:
                viewSelect=MyApplication.getInstance().getString(R.string.url_dynamic_follow_or_recommend);
                break;
            //搜索游戏列表
            case SEARCH_GAMES:
                viewSelect=MyApplication.getInstance().getString(R.string.url_search_game);
                break;
            default:
                break;
        }
        if(viewSelect!=null) {
            url1= MyApplication.getInstance().getString(R.string.url_prefix)+ MyApplication.getInstance().getString(R.string.url_suffix)+viewSelect;
        }

        //允许webView中的js脚本调用安卓方法。
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.setWebViewClient(new WebViewClient());
        binding.webview.loadUrl(url1);

        //测试返回，不起作用，大概是key已经被Main占了
        binding.webview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_BACK&&binding.webview.canGoBack()){
                    binding.webview.goBack();
                    return true;
                }
                return false;
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WebViewViewModel.class);
        // TODO: Use the ViewModel
    }
}
package com.lbs.patpat.ui.webviewFragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import com.lbs.patpat.R;
import com.lbs.patpat.databinding.FragmentWebViewBinding;
import com.lbs.patpat.global.MyApplication;

import java.security.PublicKey;

public class webViewFragment extends Fragment {

    public final static int RECOMMEND_PAGE=0;
    public final static int LEADER_PAGE=1;
    public final static int DYNAMIC_FOLLOW=2;
    public final static int DYNAMIC_RECOMMEND=3;
    public final static int DYNAMIC_FORUM=4;
    public final static int SEARCH_GAMES=5;
    //后面两个得用其他fragment
    public final static int SEARCH_FORUM=6;
    public final static int SEARCH_USER=7;

    private WebViewViewModel mViewModel;
    //请求webView地址
    private String url1="http://www.baidu.com";
    private int requestPage;
    private FragmentWebViewBinding binding;

    //这里添加根据pos修改url1
    public webViewFragment(int requestPage){
        super();
        this.requestPage=requestPage;
    }

    //根据position改变行为
    public static webViewFragment newInstance(int position) {
        webViewFragment fragment=new webViewFragment(position);
        switch (position){
            case RECOMMEND_PAGE:
                fragment.url1= MyApplication.getInstance().getString(R.string.url_prefix)+
                        MyApplication.getInstance().getString(R.string.url_recommend)+
                        MyApplication.getInstance().getString(R.string.url_suffix);
                return fragment;
            case LEADER_PAGE:
                fragment.url1= MyApplication.getInstance().getString(R.string.url_prefix)+
                        MyApplication.getInstance().getString(R.string.url_leaderboard)+
                        MyApplication.getInstance().getString(R.string.url_suffix);
                return fragment;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding =FragmentWebViewBinding.inflate(inflater, container, false);
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.setWebViewClient(new WebViewClient());
        binding.webview.loadUrl(url1);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WebViewViewModel.class);
        // TODO: Use the ViewModel
    }

}
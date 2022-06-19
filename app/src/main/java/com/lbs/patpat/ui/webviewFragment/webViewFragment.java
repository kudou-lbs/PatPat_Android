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

    private WebViewViewModel mViewModel;
    /*public String url1="file:///android_asset/rcmd/index.html";*/
    //默认
    private String url1="http://www.baidu.com";
    private FragmentWebViewBinding binding;

    //这里添加根据pos修改url1
//    public webViewFragment(int pos)

    //根据position改变行为
    public static webViewFragment newInstance(int position) {
        webViewFragment fragment=new webViewFragment();
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
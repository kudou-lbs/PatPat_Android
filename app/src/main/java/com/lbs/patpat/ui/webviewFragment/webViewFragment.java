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

import com.lbs.patpat.databinding.FragmentWebViewBinding;

public class webViewFragment extends Fragment {

    private WebViewViewModel mViewModel;
    private String url1="file:///android_asset/rcmdpage/index.html";
    private FragmentWebViewBinding binding;

    //这里添加根据pos修改url1
//    public webViewFragment(int pos)

    //根据position改变行为
    public static webViewFragment newInstance(int position) {
        webViewFragment webViewFragment=new webViewFragment();

        return webViewFragment;
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
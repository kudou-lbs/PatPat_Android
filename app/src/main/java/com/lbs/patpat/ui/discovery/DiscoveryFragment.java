package com.lbs.patpat.ui.discovery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.lbs.patpat.R;
import com.lbs.patpat.adapter.JSGameTypeAdapter;
import com.lbs.patpat.databinding.FragmentDiscoveryBinding;

public class DiscoveryFragment extends Fragment implements JSGameTypeAdapter {

    private DiscoveryViewModel discoveryViewModel;
    private FragmentDiscoveryBinding binding;
    private final String url=getString(R.string.url_prefix)+getString(R.string.url_suffix)+getString(R.string.url_discovery);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        discoveryViewModel = new ViewModelProvider(this).get(DiscoveryViewModel.class);

        binding = FragmentDiscoveryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //加载webView
        onCreateWebView();

        return root;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void onCreateWebView(){
        binding.wvDiscovery.getSettings().setJavaScriptEnabled(true);
        binding.wvDiscovery.setWebViewClient(new WebViewClient());
        binding.wvDiscovery.loadUrl(url);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //打开游戏详情
    @Override
    public void goToUrl(String url) {

    }

    @Override
    public void finishCurrentActivity() {
        getActivity().finish();
    }

    @Override
    public void goToGameList(String type) {

    }

    @Override
    public void backToGameList() {

    }
}
package com.lbs.patpat.ui.discovery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.lbs.patpat.R;
import com.lbs.patpat.adapter.JSGameTypeAdapter;
import com.lbs.patpat.databinding.FragmentDiscoveryBinding;

public class DiscoveryFragment extends Fragment implements JSGameTypeAdapter {

    private DiscoveryViewModel dashboardViewModel;
    private FragmentDiscoveryBinding binding;
    private String url;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DiscoveryViewModel.class);

        binding = FragmentDiscoveryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //加载webView
        url=getString(R.string.url_prefix)+getString(R.string.url_suffix)+getString(R.string.url_discovery);
        onCreateWebView();

        return root;
    }

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

    @Override
    public void goToUrl(String url) {

    }

    @Override
    public void goToPost(int pid) {

    }

    @Override
    public void goToGameList(String type) {

    }

    @Override
    public void backToGameList() {

    }
}
package com.lbs.patpat.ui.discovery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.lbs.patpat.R;
import com.lbs.patpat.adapter.JSGamType;
import com.lbs.patpat.databinding.FragmentDiscoveryBinding;
import com.lbs.patpat.global.BackHandledFragment;

public class DiscoveryFragment extends BackHandledFragment{

    private DiscoveryViewModel discoveryViewModel;
    private FragmentDiscoveryBinding binding;
    private String url;
    private Handler handler;
    private String type;

    public DiscoveryFragment() {
        super();
        handler=new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 0:
                        goToGameList();
                        break;
                    case 1:
                        backToGameList();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        discoveryViewModel = new ViewModelProvider(this).get(DiscoveryViewModel.class);

        binding = FragmentDiscoveryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //加载webView
        onCreateWebView();

        return root;
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void onCreateWebView(){
        url=getString(R.string.url_prefix)+getString(R.string.url_suffix)+getString(R.string.url_discovery);

        binding.wvDiscovery.getSettings().setJavaScriptEnabled(true);
        binding.wvDiscovery.setWebViewClient(new WebViewClient());
        binding.wvDiscovery.loadUrl(url);

        binding.wvDiscovery.addJavascriptInterface(new JSGamType(DiscoveryFragment.this),"jsAdapter");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onBackPressed() {
        if(binding.wvDiscovery.canGoBack()){
            binding.wvDiscovery.goBack();
            return true;
        }
        return false;
    }

    public FragmentDiscoveryBinding getBinding() {
        return binding;
    }

    public Handler getHandler() {
        return handler;
    }

    private void goToGameList(){
        binding.toolbarDiscoverBoth.toolbarDiscoveryDetailInclude.discoverGameType.setText(type);
        binding.toolbarDiscoverBoth.toolbarDiscoveryAll.setVisibility(View.GONE);
        binding.toolbarDiscoverBoth.toolbarDiscoveryDetail.setVisibility(View.VISIBLE);
    }
    private void backToGameList(){
        binding.toolbarDiscoverBoth.toolbarDiscoveryDetailInclude.discoverGameType.setText("Game Type");
        binding.toolbarDiscoverBoth.toolbarDiscoveryDetail.setVisibility(View.GONE);
        binding.toolbarDiscoverBoth.toolbarDiscoveryAll.setVisibility(View.VISIBLE);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
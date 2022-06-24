package com.lbs.patpat.ui.discovery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.lbs.patpat.R;
import com.lbs.patpat.adapter.JSGameTypeAdapter;
import com.lbs.patpat.databinding.FragmentDiscoveryBinding;
import com.lbs.patpat.global.BackHandledFragment;
import com.lbs.patpat.webViewActivity;

public class DiscoveryFragment extends BackHandledFragment implements JSGameTypeAdapter {

    private DiscoveryViewModel discoveryViewModel;
    private FragmentDiscoveryBinding binding;
    private String url;


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
        url=getString(R.string.url_prefix)+getString(R.string.url_suffix)+getString(R.string.url_discovery);

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
    public void goToUrl(String url1) {
        Intent intent=new Intent(getActivity(), webViewActivity.class);
        intent.putExtra(getString(R.string.intent_url_name),url1);
        getActivity().startActivity(intent);
    }

    @Override
    public void finishCurrentActivity() {
        getActivity().finish();
    }

    /**
     * 展示对应类型游戏列表，在数据展示之前就要调用
     * */
    @Override
    public void goToGameList(String type) {
        binding.toolbarDiscoverBoth.toolbarDiscoveryDetailInclude.discoverGameType.setText(type);
        binding.toolbarDiscoverBoth.toolbarDiscoveryAll.setVisibility(View.GONE);
        binding.toolbarDiscoverBoth.toolbarDiscoveryDetail.setVisibility(View.VISIBLE);
    }

    /**
     * 返回分类界面，在游戏信息销毁之后调用
     * */
    @Override
    public void backToGameList() {
        binding.toolbarDiscoverBoth.toolbarDiscoveryDetailInclude.discoverGameType.setText("Game Type");
        binding.toolbarDiscoverBoth.toolbarDiscoveryDetail.setVisibility(View.GONE);
        binding.toolbarDiscoverBoth.toolbarDiscoveryAll.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onBackPressed() {
        if(binding.wvDiscovery.canGoBack()){
            binding.wvDiscovery.goBack();
            return true;
        }
        return false;
    }
}
package com.lbs.patpat.ui.discovery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.lbs.patpat.MainActivity;
import com.lbs.patpat.R;
import com.lbs.patpat.SearchActivity;
import com.lbs.patpat.adapter.JSGameType;
import com.lbs.patpat.databinding.FragmentDiscoveryBinding;
import com.lbs.patpat.global.BackHandledFragment;
import com.lbs.patpat.global.MyApplication;
import com.lbs.patpat.ui.login_register.LoginedUser;
import com.lbs.patpat.viewmodel.UserViewModel;

import java.util.List;

public class DiscoveryFragment extends BackHandledFragment implements View.OnClickListener{

    private DiscoveryViewModel discoveryViewModel;
    private FragmentDiscoveryBinding binding;
    private String url;
    private Handler handler;
    private String type;
    private ImageView avatar;

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

        binding.toolbarDiscoverBoth.toolbarDiscoveryAllInclude.toolbarSearchHome.setOnClickListener(this);
        binding.toolbarDiscoverBoth.toolbarDiscoveryAllInclude.toolbarPersonalHome.setOnClickListener(this);
        binding.toolbarDiscoverBoth.toolbarDiscoveryDetailInclude.discoverReturn.setOnClickListener(this);
        avatar = binding.toolbarDiscoverBoth.toolbarDiscoveryAllInclude.toolbarPersonalHome;
        //LiveData更新头像，详见HomeFragment
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getLoginedUser().observe(getViewLifecycleOwner(), new Observer<List<LoginedUser>>() {
            @Override
            public void onChanged(List<LoginedUser> loginedUsers) {
                if (loginedUsers.size()==1){
                    if(loginedUsers.get(0).avatar.equals("null"))
                        avatar.setImageDrawable(MyApplication.getContext().getDrawable(R.drawable.icon_default));
                    else
                    Glide.with(MyApplication.getContext())
                            .load(MyApplication.getContext().getString(R.string.server_ip) + loginedUsers.get(0).avatar)
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(avatar);

                }
                else
                    avatar.setImageDrawable(MyApplication.getContext().getDrawable(R.drawable.icon_default));
            }
        });

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

        binding.wvDiscovery.addJavascriptInterface(new JSGameType(DiscoveryFragment.this),"jsAdapter");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_search_home:
                Intent intent=new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.toolbar_personal_home:
                ((MainActivity)getActivity()).getBinding().mainDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.discover_return:
                binding.wvDiscovery.goBack();
                break;
            default:
                break;
        }
    }
}
package com.lbs.patpat.fragment.WebViewFragment;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
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
import com.lbs.patpat.adapter.JSBasic;
import com.lbs.patpat.adapter.JSPostList;
import com.lbs.patpat.databinding.FragmentWebViewBinding;
import com.lbs.patpat.global.MyApplication;
import com.lbs.patpat.ui.login_register.UserDao;

public class WebViewFragment extends Fragment {

    public final static int DEFAULT=-1;
    public final static int RECOMMEND_PAGE=0;
    public final static int LEADER_PAGE=1;
    public final static int DYNAMIC_FOLLOW=2;
    public final static int DYNAMIC_RECOMMEND=3;
    //ListFragment
    public final static int DYNAMIC_FORUM=4;
    public final static int SEARCH_GAMES=5;
    //后面两个使用ListFragment
    public final static int SEARCH_FORUM=6;
    public final static int SEARCH_USER=7;

    public final static int FORUM_POST=8;
    public final static int USER_POST=9;

    private WebViewViewModel mViewModel;
    //请求webView地址
    private String url1="http://www.baidu.com";
    private int requestPage;
    private String postId;
    private FragmentWebViewBinding binding;

    //这里添加根据pos修改url1
    private WebViewFragment(int requestPage){
        super();
        this.requestPage=requestPage;
    }
    private WebViewFragment(int requestPage, String postId){
        this.requestPage=requestPage;
        this.postId=postId;
    }

    //根据position改变行为
    public static WebViewFragment newInstance(int position) {
        WebViewFragment fragment=new WebViewFragment(position);
        return fragment;
    }

    @SuppressLint("SetJavaScriptEnabled")
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
                binding.webview.addJavascriptInterface(new JSBasic(WebViewFragment.this),"jsAdapter");
                break;
            //首页排行榜
            case LEADER_PAGE:
                viewSelect=MyApplication.getInstance().getString(R.string.url_leaderboard);
                binding.webview.addJavascriptInterface(new JSBasic(WebViewFragment.this),"jsAdapter");
                break;
            //动态：关注&推荐
            case DYNAMIC_FOLLOW:
                if(postId!=null){
                    viewSelect="related/:" +postId+"/"
                            +MyApplication.getInstance().getString(R.string.url_dynamic_follow_or_recommend);
                    binding.webview.addJavascriptInterface(new JSPostList(WebViewFragment.this),"jsAdapter");
                    break;
                }
                //未登录则关注页展示随机页面
            case DYNAMIC_RECOMMEND:
                viewSelect=MyApplication.getInstance().getString(R.string.url_dynamic_follow_or_recommend);
                binding.webview.addJavascriptInterface(new JSPostList(WebViewFragment.this),"jsAdapter");
                break;
            //搜索游戏列表
            case SEARCH_GAMES:
                viewSelect=MyApplication.getInstance().getString(R.string.url_search_game);
                binding.webview.addJavascriptInterface(new JSBasic(WebViewFragment.this),"jsAdapter");
                break;
            //论坛内帖子
            case FORUM_POST:
                viewSelect="forum/:" +postId+"/"
                        +MyApplication.getInstance().getString(R.string.url_dynamic_follow_or_recommend);
                binding.webview.addJavascriptInterface(new JSPostList(WebViewFragment.this),"jsAdapter");
                break;
            //用户帖子
            case USER_POST:
                viewSelect="user/:" +postId+"/"
                        +MyApplication.getInstance().getString(R.string.url_dynamic_follow_or_recommend);
                binding.webview.addJavascriptInterface(new JSPostList(WebViewFragment.this),"jsAdapter");
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

        //测试返回，不起作用，大概是key已经被Main占了，已修复
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
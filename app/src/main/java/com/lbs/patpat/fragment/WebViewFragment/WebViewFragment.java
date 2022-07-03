package com.lbs.patpat.fragment.WebViewFragment;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import com.lbs.patpat.MainActivity;
import com.lbs.patpat.R;
import com.lbs.patpat.adapter.JSBasic;
import com.lbs.patpat.adapter.JSPostList;
import com.lbs.patpat.databinding.FragmentWebViewBinding;
import com.lbs.patpat.global.MyApplication;

public class WebViewFragment extends Fragment {

    public final static int DEFAULT=-1;
    //首页：游戏推荐和排行榜
    public final static int RECOMMEND_PAGE=0;
    public final static int LEADER_PAGE=1;
    //动态：关注和推荐帖子
    public final static int DYNAMIC_FOLLOW=2;
    public final static int DYNAMIC_RECOMMEND=3;
    //动态推荐论坛（ListFragment
    public final static int DYNAMIC_FORUM=4;
    //搜索游戏
    public final static int SEARCH_GAMES=5;
    public final static int SEARCH_POST=10;
    //搜索论坛和用户（ListFragment
    public final static int SEARCH_FORUM=6;
    public final static int SEARCH_USER=7;
    //论坛内帖子
    public final static int FORUM_POST=8;
    //用户发的帖子
    public final static int USER_POST=9;
    //用户点赞的帖子
    public final static int USER_LIKE_POST=11;
    //用户收藏的帖子
    public final static int USER_COLLECT_POST=12;
    //用户的评论
    public final static int USER_POST_REPLY=13;
    //回复消息
    public final static int USER_MESSAGE_REPLY=14;
    //点赞消息
    public final static int USER_MESSAGE_LIKE=15;


    private WebViewViewModel mViewModel;
    //请求webView地址
    private String url1="http://www.baidu.com";
    private int requestPage;
    private String idOrKey;
    private FragmentWebViewBinding binding;

    public WebViewFragment(){
        super();
    }
    //这里添加根据pos修改url1
    private WebViewFragment(int requestPage){
        super();
        this.requestPage=requestPage;
    }
    private WebViewFragment(int requestPage, String idOrKey){
        this.requestPage=requestPage;
        this.idOrKey = idOrKey;
    }

    //根据position改变行为
    public static WebViewFragment newInstance(int position) {
        WebViewFragment fragment=new WebViewFragment(position);
        return fragment;
    }
    public static WebViewFragment newInstance(int position,String postId) {
        WebViewFragment fragment=new WebViewFragment(position,postId);
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
                if(idOrKey !=null){
                    viewSelect="related/" + idOrKey +"/"
                            +MyApplication.getInstance().getString(R.string.url_post);
                    binding.webview.addJavascriptInterface(new JSPostList(WebViewFragment.this),"jsAdapter");
                    break;
                }
            //未登录则关注页展示随机页面
            case DYNAMIC_RECOMMEND:
                viewSelect=MyApplication.getInstance().getString(R.string.url_post);
                binding.webview.addJavascriptInterface(new JSPostList(WebViewFragment.this),"jsAdapter");
                break;
            //搜索游戏列表    lbsDebug：629_15:13等前端爷接口
            case SEARCH_GAMES:
                viewSelect=MyApplication.getInstance().getString(R.string.url_search)+"/"+idOrKey+"/game";
                binding.webview.addJavascriptInterface(new JSBasic(WebViewFragment.this),"jsAdapter");
                break;
            //搜索帖子列表
            case SEARCH_POST:
                viewSelect=MyApplication.getInstance().getString(R.string.url_search)+"/"+idOrKey+"/post";
                binding.webview.addJavascriptInterface(new JSBasic(WebViewFragment.this),"jsAdapter");
                break;
            //论坛内帖子
            case FORUM_POST:
                viewSelect="forum/" + idOrKey +"/"
                        +MyApplication.getInstance().getString(R.string.url_post);
                binding.webview.addJavascriptInterface(new JSPostList(WebViewFragment.this),"jsAdapter");
                break;
            //用户帖子
            case USER_POST:
                Log.d("lmw", idOrKey);
                Log.d("当前用户与登录用户：",idOrKey+" "+ MainActivity.getUid());
                viewSelect="user/" + idOrKey +"/"
                        +MyApplication.getInstance().getString(R.string.url_post);
                binding.webview.addJavascriptInterface(new JSPostList(WebViewFragment.this),"jsAdapter");
                break;
            //用户喜欢（点赞的帖子
            case USER_LIKE_POST:
                viewSelect="like/" + idOrKey +"/"
                        +MyApplication.getInstance().getString(R.string.url_post);
                binding.webview.addJavascriptInterface(new JSPostList(WebViewFragment.this),"jsAdapter");
                break;
            //用户收藏的帖子
            case USER_COLLECT_POST:
                viewSelect="collect/"+idOrKey+"/"
                        +MyApplication.getInstance().getString(R.string.url_post);
                binding.webview.addJavascriptInterface(new JSPostList(WebViewFragment.this),"jsAdapter");
                break;
            //用户评论
            case USER_POST_REPLY:
                viewSelect="user/"+idOrKey+"/reply";
                binding.webview.addJavascriptInterface(new JSPostList(WebViewFragment.this),"jsAdapter");
                break;
            //回复消息
            case USER_MESSAGE_REPLY:
                viewSelect="message/reply";
                binding.webview.addJavascriptInterface(new JSPostList(WebViewFragment.this),"jsAdapter");
                break;
            //点赞消息
            case USER_MESSAGE_LIKE:
                viewSelect="message/like";
                binding.webview.addJavascriptInterface(new JSPostList(WebViewFragment.this),"jsAdapter");
                break;
            default:
                break;
        }
        if(viewSelect!=null) {
            url1= MyApplication.getInstance().getString(R.string.url_prefix)+ MyApplication.getInstance().getString(R.string.url_suffix)+viewSelect;
            Log.d("当前url:",url1);
            Log.d("lmw",url1);
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
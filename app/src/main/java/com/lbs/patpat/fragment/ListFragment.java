package com.lbs.patpat.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lbs.patpat.ForumActivity;
import com.lbs.patpat.MainActivity;
import com.lbs.patpat.R;
import com.lbs.patpat.adapter.ForumListAdapter;
import com.lbs.patpat.adapter.UserListAdapter;
import com.lbs.patpat.model.ForumModel;
import com.lbs.patpat.fragment.WebViewFragment.WebViewFragment;
import com.lbs.patpat.model.UserModel;
import com.lbs.patpat.viewmodel.FollowAndFanViewModel;
import com.lbs.patpat.viewmodel.FollowAndFanViewModelFactory;
import com.lbs.patpat.viewmodel.ListViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements ForumListAdapter.OnItemClickListener{

    private View root;
    private RecyclerView recyclerView;

    private int requestPage;
    private ListViewModel mViewModel;
    private String searchKey;
    //搜索社区
    private ForumListAdapter forumListAdapter;
    //搜索用户
    private UserListAdapter userListAdapter;
    //关注和粉丝
    private FollowAndFanViewModel followAndFanViewModel;

    public final static int PERSONAL_FOLLOW=0;
    public final static int PERSONAL_FAN=1;

    public ListFragment(int position){
        super();
        //根据不同情形选择不同的viewModel，adapter
        requestPage=position;
    }
    public ListFragment(int position,String searchKey){
        super();
        //根据不同情形选择不同的viewModel，adapter
        requestPage=position;
        this.searchKey=searchKey;
    }

    public static ListFragment newInstance(int position) {
        switch (position){
            //搜索结果
            case WebViewFragment.DYNAMIC_FORUM:
                return new ListFragment(WebViewFragment.DYNAMIC_FORUM);
            case ListFragment.PERSONAL_FOLLOW:
                return new ListFragment(PERSONAL_FOLLOW);
            case ListFragment.PERSONAL_FAN:
                return new ListFragment(PERSONAL_FAN);
            default:
                break;
        }
        return new ListFragment(WebViewFragment.DEFAULT);
    }

    public static ListFragment newSearchInstance(int position,String key){
        switch (position){
            case WebViewFragment.SEARCH_FORUM:
                return new ListFragment(WebViewFragment.SEARCH_FORUM,key);
            case WebViewFragment.SEARCH_USER:
                return new ListFragment(WebViewFragment.SEARCH_USER,key);
            default:
                break;
        }
        return new ListFragment(WebViewFragment.DEFAULT);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.fragment_list, container, false);

        //大问题，在这里写每次重建时view时都会插入重复的数据，已修复
        initRecyclerView();

        return root;
    }

    private void initRecyclerView() {
        recyclerView=root.findViewById(R.id.recycler_view_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //adapter里定义了数据的展示方式
        switch (requestPage){
            case WebViewFragment.SEARCH_FORUM:
            case WebViewFragment.DYNAMIC_FORUM:
                forumListAdapter =new ForumListAdapter(getActivity(),null,this);
                recyclerView.setAdapter(forumListAdapter);
                bindForumViewModel();
                break;
            case WebViewFragment.SEARCH_USER:
            case ListFragment.PERSONAL_FOLLOW:
            case ListFragment.PERSONAL_FAN:
                userListAdapter =new UserListAdapter(getActivity(),null);
                recyclerView.setAdapter(userListAdapter);
                switch (requestPage){
                    case WebViewFragment.SEARCH_USER:
                        bindSearchUserViewModel();
                        break;
                    case ListFragment.PERSONAL_FOLLOW:
                        bindFollowUserViewModel();
                        break;
                    case ListFragment.PERSONAL_FAN:
                        bindFanUserViewModel();
                        break;
                }
                break;
        }
        //到达底部时请求数据
        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(recyclerView.canScrollVertically(-1)){
                    requestMoreInfo();
                }
            }
        });
    }

    //论坛相关，信息请求见requestMoreInfo
    public void bindForumViewModel() {
        // 专：mViewModel = new ViewModelProvider(this).get(ListViewModel.class);
        // 通过这种方式获取到的viewModel是activity共用的，下面是共：
        mViewModel= new ViewModelProvider(requireActivity()).get(ListViewModel.class);
        mViewModel.getForumsList().setValue(new ArrayList<>());
        mViewModel.getForumsList().observe(requireActivity(), new Observer<List<ForumModel>>() {
            @Override
            public void onChanged(List<ForumModel> forumModels) {
                if(forumModels!=null){
                    forumListAdapter.setForumModelList(forumModels);
                }
            }
        });
        //调用api获取数据
        requestMoreInfo();
    }
    public void bindSearchUserViewModel(){
        mViewModel= new ViewModelProvider(requireActivity()).get(ListViewModel.class);
        mViewModel.getUserList().setValue(new ArrayList<>());
        mViewModel.getUserList().observe(requireActivity(), new Observer<List<UserModel>>() {
            @Override
            public void onChanged(List<UserModel> userModels) {
                userListAdapter.setUserModelList(userModels);
            }
        });
        requestMoreInfo();
    }
    //关注和粉丝
    public void bindFollowUserViewModel(){
        //followAndFanViewModel= new ViewModelProvider(this,new FollowAndFanViewModelFactory(MainActivity.getUid())).get(FollowAndFanViewModel.class);
        followAndFanViewModel=new ViewModelProvider(requireActivity()).get(FollowAndFanViewModel.class);
        followAndFanViewModel.getFollowUsers().observe(requireActivity(), new Observer<List<UserModel>>() {
            @Override
            public void onChanged(List<UserModel> userModels) {
                userListAdapter.setUserModelList(userModels);
            }
        });
        requestMoreInfo();
    }
    public void bindFanUserViewModel(){
        followAndFanViewModel=new ViewModelProvider(requireActivity()).get(FollowAndFanViewModel.class);
        followAndFanViewModel.getFanUsers().observe(requireActivity(), new Observer<List<UserModel>>() {
            @Override
            public void onChanged(List<UserModel> userModels) {
                userListAdapter.setUserModelList(userModels);
            }
        });
        requestMoreInfo();
    }


    private void requestMoreInfo(){
        switch (requestPage){
            case WebViewFragment.DYNAMIC_FORUM:
                mViewModel.makeFollowForumApiCall();
                break;
            case WebViewFragment.SEARCH_FORUM:
                mViewModel.makeForumApiCall(searchKey);
                break;
            case WebViewFragment.SEARCH_USER:
                mViewModel.makeUserApiCall(searchKey);
                break;
            case ListFragment.PERSONAL_FOLLOW:
                followAndFanViewModel.makeFollowUserCall();
                break;
            case ListFragment.PERSONAL_FAN:
                followAndFanViewModel.makeFanUserCall();
                break;
            default:
                break;
        }
    }

    //社区项点击事件，用户点击事件未做
    @Override
    public void onItemClick(ForumModel forumModel) {
        //Toast.makeText(getActivity(),forumModel.getForumName(),Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getActivity(), ForumActivity.class);
        intent.putExtra("fid",forumModel.getForumFid());
        getActivity().startActivity(intent);
    }
}
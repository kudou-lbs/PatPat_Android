package com.lbs.patpat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lbs.patpat.R;
import com.lbs.patpat.adapter.ForumListAdapter;
import com.lbs.patpat.adapter.UserListAdapter;
import com.lbs.patpat.model.ForumModel;
import com.lbs.patpat.fragment.webviewFragment.webViewFragment;
import com.lbs.patpat.model.UserModel;
import com.lbs.patpat.viewmodel.ListViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements ForumListAdapter.OnItemClickListener{

    private View root;
    private RecyclerView recyclerView;

    private int requestPage;
    private ListViewModel mViewModel;
    private List<ForumModel> forumModelList;
    private ForumListAdapter forumListAdapter;
    private List<UserModel> userModelList;
    private UserListAdapter userListAdapter;

    public ListFragment(int position){
        super();
        //根据不同情形选择不同的viewModel，adapter
        requestPage=position;
    }

    public static ListFragment newInstance(int position) {
        switch (position){
            //搜索结果
            case webViewFragment.SEARCH_FORUM:
                return new ListFragment(webViewFragment.SEARCH_FORUM);
            case webViewFragment.SEARCH_USER:
                return new ListFragment(webViewFragment.SEARCH_USER);
            case webViewFragment.DYNAMIC_FORUM:
                return new ListFragment(webViewFragment.DYNAMIC_FORUM);
        }
        return new ListFragment(webViewFragment.DEFAULT);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.fragment_list, container, false);

        forumModelList=new ArrayList<>();
        //大问题，在这里写每次重建时view时都会插入重复的数据
        initRecyclerView();

        userModelList=new ArrayList<>();

        // 一般来说数据获取在onActivityCreated()中进行，但此方法已弃用，且该fragment出现时activity一定已创建，
        // 因此在这里获取数据影响不大，可能视图加载稍慢
        return root;
    }

    private void initRecyclerView() {
        recyclerView=root.findViewById(R.id.recycler_view_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //adapter里定义了数据的展示方式
        if(requestPage==webViewFragment.SEARCH_USER){
            userListAdapter =new UserListAdapter(getActivity(),userModelList);
            recyclerView.setAdapter(userListAdapter);
            bindUserViewModel();
        }else{
            forumListAdapter =new ForumListAdapter(getActivity(),forumModelList,this);
            recyclerView.setAdapter(forumListAdapter);
            bindForumViewModel();
        }
    }
    //观察forumList变化
    public void bindForumViewModel() {
        // 专：mViewModel = new ViewModelProvider(this).get(ListViewModel.class);
        // 通过这种方式获取到的viewModel是activity共用的，下面是共：
        mViewModel= new ViewModelProvider(requireActivity()).get(ListViewModel.class);
        mViewModel.getForumsList().observe(requireActivity(), new Observer<List<ForumModel>>() {
            @Override
            public void onChanged(List<ForumModel> forumModels) {
                if(forumModels!=null){
                    forumModelList=forumModels;
                    forumListAdapter.setForumModelList(forumModels);
                }
            }
        });
        //调用api获取数据
        mViewModel.makeForumApiCall();
    }
    //观察userList变化
    public void bindUserViewModel(){
        mViewModel= new ViewModelProvider(requireActivity()).get(ListViewModel.class);
        mViewModel.getUserList().observe(requireActivity(), new Observer<List<UserModel>>() {
            @Override
            public void onChanged(List<UserModel> userModels) {
                userModelList=userModels;
                userListAdapter.setUserModelList(userModels);
            }
        });
        mViewModel.makeUserApiCall();
    }

    //社区项点击事件
    @Override
    public void onItemClick(ForumModel forumModel) {
        Toast.makeText(getActivity(),forumModel.getForumName(),Toast.LENGTH_SHORT).show();
    }



}
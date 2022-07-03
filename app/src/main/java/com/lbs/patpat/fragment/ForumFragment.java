package com.lbs.patpat.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lbs.patpat.ForumActivity;
import com.lbs.patpat.R;
import com.lbs.patpat.adapter.ForumListAdapter;
import com.lbs.patpat.adapter.UserListAdapter;
import com.lbs.patpat.databinding.FragmentForumBinding;
import com.lbs.patpat.fragment.WebViewFragment.WebViewFragment;
import com.lbs.patpat.model.ForumModel;
import com.lbs.patpat.viewmodel.ForumListViewModel;

import java.util.List;

public class ForumFragment extends Fragment implements ForumListAdapter.OnItemClickListener{

    private FragmentForumBinding binding;
    ForumListAdapter followListAdapter;
    ForumListAdapter allListAdapter;
    ForumListViewModel viewModel;

    public ForumFragment() {
        super();
    }

    public static ForumFragment newInstance(){
        return new ForumFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentForumBinding.inflate(inflater,container,false);
        initBinding();
        initObserve();
        return binding.getRoot();
    }

    private void initBinding() {
        binding.dynamicForumFollow.setLayoutManager(new LinearLayoutManager(getContext()));
        followListAdapter=new ForumListAdapter(getContext(),null,this);
        binding.dynamicForumFollow.setAdapter(followListAdapter);

        binding.dynamicForumAll.setLayoutManager(new LinearLayoutManager(getContext()));
        allListAdapter=new ForumListAdapter(getContext(),null,this);
        binding.dynamicForumAll.setAdapter(allListAdapter);
    }

    private void initObserve() {
        viewModel=new ViewModelProvider(requireActivity()).get(ForumListViewModel.class);
        viewModel.getFollowForumList().observe(requireActivity(), new Observer<List<ForumModel>>() {
            @Override
            public void onChanged(List<ForumModel> forumModelList) {
                followListAdapter.setForumModelList(forumModelList);
            }
        });
        viewModel.getAllForumList().observe(requireActivity(), new Observer<List<ForumModel>>() {
            @Override
            public void onChanged(List<ForumModel> forumModelList) {
                allListAdapter.setForumModelList(forumModelList);
            }
        });
        viewModel.makeAllApiCall();
        viewModel.makeFollowApiCall();
        binding.dynamicForumFollow.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(binding.dynamicForumFollow.canScrollVertically(-1)){
                    viewModel.makeFollowApiCall();
                }
            }
        });
        binding.dynamicForumAll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(binding.dynamicForumAll.canScrollVertically(-1)){
                    viewModel.makeAllApiCall();
                }
            }
        });
    }

    @Override
    public void onItemClick(ForumModel forumModel) {
        //Toast.makeText(getActivity(),forumModel.getForumFid(),Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getActivity(), ForumActivity.class);
        intent.putExtra("fid",forumModel.getForumFid());
        getActivity().startActivity(intent);
    }
}
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
import com.lbs.patpat.databinding.FragmentListBinding;
import com.lbs.patpat.model.ForumModel;
import com.lbs.patpat.ui.webviewFragment.webViewFragment;
import com.lbs.patpat.viewmodel.ListViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements ForumListAdapter.OnItemClickListener{

    private View root;
    private RecyclerView recyclerView;

    private ListViewModel mViewModel;
    private List<ForumModel> forumModelList;
    private ForumListAdapter adapter;

    public ListFragment(int position){
        super();
        //根据不同情形选择不同的viewModel，adapter
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
        initRecyclerView();
        return root;
    }

    private void initRecyclerView() {
        recyclerView=root.findViewById(R.id.recycler_view_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new ForumListAdapter(getActivity(),forumModelList,this);
        recyclerView.setAdapter(adapter);

        forumModelList=new ArrayList<>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListViewModel.class);
        // TODO: Use the ViewModel
        mViewModel= new ViewModelProvider(requireActivity()).get(ListViewModel.class);
        mViewModel.getForumsList().observe(requireActivity(), new Observer<List<ForumModel>>() {
            @Override
            public void onChanged(List<ForumModel> forumModels) {
                if(forumModels!=null){
                    forumModelList=forumModels;
                    adapter.setForumModelList(forumModels);
                }
            }
        });
        mViewModel.makeApiCall();
    }

    @Override
    public void onItemClick(ForumModel forumModel) {
        Toast.makeText(getActivity(),forumModel.getForumName(),Toast.LENGTH_SHORT).show();
    }
}
package com.lbs.patpat.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lbs.patpat.R;
import com.lbs.patpat.databinding.FragmentForumBinding;
import com.lbs.patpat.fragment.WebViewFragment.WebViewFragment;

public class ForumFragment extends Fragment {

    private FragmentForumBinding binding;

    public ForumFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentForumBinding.inflate(inflater,container,false);
        initBinding();
        return binding.getRoot();
    }

    private void initBinding() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_forum_follow_container, ListFragment.newInstance(WebViewFragment.SEARCH_FORUM))
                .commit();
    }
}
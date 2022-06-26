package com.lbs.patpat.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lbs.patpat.R;
import com.lbs.patpat.adapter.PersonalAboutAdapter;
import com.lbs.patpat.databinding.FragmentPersonalAboutBinding;
import com.lbs.patpat.model.PersonalModel;
import com.lbs.patpat.viewmodel.PersonalAboutViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalAboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalAboutFragment extends Fragment {

    private FragmentPersonalAboutBinding binding;
    private List<PersonalModel> personalModelList;
    private PersonalAboutAdapter adapter;
    private PersonalAboutViewModel viewModel;

    public PersonalAboutFragment() {
        super();
    }

    public static PersonalAboutFragment newInstance(String param1, String param2) {
        return new PersonalAboutFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personalModelList=new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentPersonalAboutBinding.inflate(inflater,container,false);
        initRecyclerView();
        return binding.getRoot();
    }
    private void initRecyclerView(){
        binding.personalAboutList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new PersonalAboutAdapter(getActivity(),personalModelList);
        binding.personalAboutList.setAdapter(adapter);
        viewModel=new ViewModelProvider(requireActivity()).get(PersonalAboutViewModel.class);
        viewModel.getPersonalModelList().observe(requireActivity(), new Observer<List<PersonalModel>>() {
            @Override
            public void onChanged(List<PersonalModel> personalModels) {
                personalModelList=personalModels;
                adapter.setPersonalInfoItemList(personalModels);
            }
        });
        viewModel.makePersonalApiCall();
    }
}
package com.lbs.patpat.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lbs.patpat.R;
import com.lbs.patpat.databinding.FragmentPersonalAboutBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalAboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalAboutFragment extends Fragment {

    public PersonalAboutFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static PersonalAboutFragment newInstance(String param1, String param2) {
        PersonalAboutFragment fragment = new PersonalAboutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentPersonalAboutBinding binding=FragmentPersonalAboutBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }
}
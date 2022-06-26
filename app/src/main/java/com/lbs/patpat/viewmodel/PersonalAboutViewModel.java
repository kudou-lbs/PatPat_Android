package com.lbs.patpat.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lbs.patpat.model.PersonalModel;

import java.util.ArrayList;
import java.util.List;

public class PersonalAboutViewModel extends ViewModel {
    private MutableLiveData<List<PersonalModel>> personalModelList;

    public PersonalAboutViewModel() {
        personalModelList=new MutableLiveData<>();
        personalModelList.setValue(new ArrayList<>());
    }

    public MutableLiveData<List<PersonalModel>> getPersonalModelList() {
        return personalModelList;
    }
    public void makePersonalApiCall(){
        for(int i=0;i<10;++i){
            personalModelList.getValue().add(new PersonalModel("年龄","19"));
        }
    }
}

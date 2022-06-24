package com.lbs.patpat.global;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BackHandledFragment extends Fragment {
    protected BackHandledInterface backHandledInterface;
    /**
     * 处理回退事件
     * */
    public abstract boolean onBackPressed();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!(getActivity() instanceof MyActivity)){
            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        }else{
            this.backHandledInterface=(BackHandledInterface) getActivity();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        backHandledInterface.setSelectedFragment(this);
    }
}

package com.prapa.seproject.pra_pa.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.prapa.seproject.pra_pa.EditMeterReading;
import com.prapa.seproject.pra_pa.R;

public class HomeFragment extends Fragment {

    FirebaseAuth _mAth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _mAth.signInAnonymously();
        Log.d("HOME", "sign in with - "+_mAth.getCurrentUser());
        initResidentBtn();
        initStaffBtn();
        initLegalPersonBtn();
    }

    private void initResidentBtn(){
        Log.d("HOME", "click resident btn");
        Button _residentBtn = getView().findViewById(R.id.resident_btn_home);
        _residentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new ResidentFragment())
                        .addToBackStack(null).commit();
            }
        });
    }

    private void initStaffBtn(){
        Log.d("HOME", "click staff btn");
        Button _staffBtn = getView().findViewById(R.id.staff_btn_home);
        _staffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new ChoosePlanFragment())
                        .addToBackStack(null).commit();
                Log.d("HOME", "Go to Record");
            }
        });
    }

    private void initLegalPersonBtn(){
        Log.d("HOME", "click legal person btn");
        Button _residentBtn = getView().findViewById(R.id.legal_person_btn_home);
        _residentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new EditMeterReading())
                        .addToBackStack(null).commit();
            }
        });
    }

}

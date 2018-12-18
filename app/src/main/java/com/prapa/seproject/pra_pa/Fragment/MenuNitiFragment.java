package com.prapa.seproject.pra_pa.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.prapa.seproject.pra_pa.Payment.PaymentFragment;
import com.prapa.seproject.pra_pa.R;

public class MenuNitiFragment extends Fragment {

    SharedPreferences _spfr;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu_niti, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _spfr = getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
        initLogout();
        backBtn();
        initEditBtn();
        initVerifyPaymentBtn();
        initViewResidentBtn();
    }

    private void initEditBtn(){
        ImageButton _editBtn = getView().findViewById(R.id.edit_unit_btn);
        _editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new EditUnitFragment())
                        .addToBackStack(null).commit();
                Log.d("MENU_NITI", "Menu niti --> Edit unit");
            }
        });
    }

    private void initViewResidentBtn(){
        ImageButton _editBtn = getView().findViewById(R.id.view_resident_btn);
        _editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new SearchUsernameFragment())
                        .addToBackStack(null).commit();
                Log.d("MENU_NITI", "Menu niti --> View Resident");
            }
        });
    }

    private void initVerifyPaymentBtn(){
        ImageButton _editBtn = getView().findViewById(R.id.status_varify_btn);
        _editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new PaymentFragment())
                        .addToBackStack(null).commit();
                Log.d("MENU_NITI", "Menu niti --> Verify payment");
            }
        });
    }

    private void initLogout(){
        ImageView _logout = getView().findViewById(R.id.logout_menu_niti);
        _logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = _spfr.edit();
                editor.clear();
                editor.commit();
                Log.d("MENU_NITI", _spfr.getString("role", "not found"));
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new HomeFragment())
                        .addToBackStack(null).commit();
                Log.d("MENU_NITI", "Logout --> Home");
            }
        });
    }

    private void backBtn(){
        ImageView _backBtn = getView().findViewById(R.id.back_btn_menu_niti);
        _backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .popBackStack();
            }
        });
    }
}

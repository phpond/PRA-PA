package com.prapa.seproject.pra_pa;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditMeterReading extends Fragment {

    private String new_reading;
    private String new_month;
    private String new_date;


    FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_water_bill, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //initSubmitBtn();
    }


    public EditMeterReading(){

    }

    /*public EditMeterReading(String new_reading, String new_month, String new_date){
        this.new_reading = new_reading;
        this.new_month = new_month;
        this.new_date = new_date;
    }*/

    //get post value from firebase
    //update new value






}

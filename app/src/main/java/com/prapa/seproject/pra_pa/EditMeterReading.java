package com.prapa.seproject.pra_pa;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditMeterReading extends Fragment {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth c_auth = FirebaseAuth.getInstance();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_water_bill, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //get current room (not yet)
        c_auth.getCurrentUser().getUid();
        Log.d("room", "GET DATA OF "+c_auth.getCurrentUser().getUid());
                final int room_num = 23;
        getDataFromFirebase(room_num);
        EditSubmitBtn();
    }

    void getDataFromFirebase(final int room_num){



    }



    //edit_submit button on click
    private void EditSubmitBtn(final int room_num){
        Button edit_submit = getView().findViewById(R.id.submit_btn_meter_edit_water_bill);
        edit_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get string
                String meter = ((EditText)(getView().findViewById(R.id.water_meter_edit_water_bill))).getText().toString();
                String month = ((EditText)(getView().findViewById(R.id.month_meter_edit_water_bill))).getText().toString();
                String date = ((EditText)(getView().findViewById(R.id.date_meter_edit_water_bill))).getText().toString();

                //pack new string in a new bill
                final Bill bill = new Bill(room_num, meter, month, date);
                updatetoFireBase();
            }
        });
    }

    // update to firebase
    private void updatetoFireBase() {
        //replace previous bill by new bill(not yet)
        //check room then set new value(?)
        //commit to firebase
    }












    








}

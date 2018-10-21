package com.prapa.seproject.pra_pa;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

public class RecordWaterFragment extends Fragment {

    FirebaseFirestore _fbfs = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.record_water_bill, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initSubmitBtn();
    }


    //submit to fire base
    private void initSubmitBtn(){
        String _meter = ((EditText)(getView().findViewById(R.id.water_meter_record_water_bill))).getText().toString();
        String _month = ((EditText)(getView().findViewById(R.id.month_meter_record_water_bill))).getText().toString();
        String _date = ((EditText)(getView().findViewById(R.id.date_meter_record_water_bill))).getText().toString();

        Button _submitBtn = getView().findViewById(R.id.submit_btn_meter_record_water_bill);
        _submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RECORD", "Before up to firebase");
                UpToFireBase();
            }
        });
    }

    private void UpToFireBase(){
        _fbfs.collection("Resident");
    }
}

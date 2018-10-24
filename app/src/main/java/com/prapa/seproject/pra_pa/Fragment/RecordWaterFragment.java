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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prapa.seproject.pra_pa.Bill;
import com.prapa.seproject.pra_pa.R;
import com.prapa.seproject.pra_pa.Room;

public class RecordWaterFragment extends Fragment {

    FirebaseFirestore _fbfs = FirebaseFirestore.getInstance();
    FirebaseAuth _muth = FirebaseAuth.getInstance();

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
        Button _submitBtn = getView().findViewById(R.id.submit_btn_meter_record_water_bill);
        _submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Room _room = new Room("A", 1, 03);

                String _meterstr = ((EditText)(getView().findViewById(R.id.water_meter_record_water_bill))).getText().toString();
                String _month = ((EditText)(getView().findViewById(R.id.month_meter_record_water_bill))).getText().toString();
                String _date = ((EditText)(getView().findViewById(R.id.date_meter_record_water_bill))).getText().toString();
                Log.d("RECORD", "XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                Log.d("RECORD", "xxx : "+_meterstr+_month+_date);

                final int _meter = 23;
//        final String _month = "FEB";
//        final String _date = "10/05/2018";

                final Bill _bill = new Bill(_room,_meter, _month, _date);

                Log.d("RECORD", "Before up to firebase");
                upToFireBase(_room, _bill);
            }
        });
    }

    private void upToFireBase(Room _room, Bill _bill)
    {
        Log.d("RECORD", "ก่อนเข้า"+_fbfs);
        String _number_room = _room.getPhase()+String.valueOf(_room.getFloor())+_room.getNumber_room();

        _fbfs.collection("Resident")
                .document(_muth.getCurrentUser().getUid())
                .collection(_number_room)
                .document("bill")
                .set(_bill)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("RECORD", "SUCCESS");
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("RECORD", "FAILED");
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

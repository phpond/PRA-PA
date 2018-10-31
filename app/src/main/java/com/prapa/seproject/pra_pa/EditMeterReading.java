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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prapa.seproject.pra_pa.Fragment.RecordWaterFragment;
import com.prapa.seproject.pra_pa.Fragment.ViewplanFragment;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditMeterReading extends Fragment {

    FirebaseFirestore db_cloud = FirebaseFirestore.getInstance();


    FirebaseAuth c_auth = FirebaseAuth.getInstance();

    private void GetdataFromFirebase(String _room){

        db_cloud.collection("Resident")
                .document(c_auth.getCurrentUser().getUid())
                .collection(_room)
                .document("bill")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                documentSnapshot.toObject(Bill.class);
                Log.d("get previous bill", "get data from firebase SUCCESS!!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("get previous bill", "get data from firebase FAIL!!");
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_water_bill, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GetdataFromFirebase("A104");

        EditSubmitBtn();
    }




    //edit_submit button on click
    private void EditSubmitBtn(){

        Button edit_submit = getView().findViewById(R.id.submit_btn_meter_edit_water_bill);
        edit_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Room _room = new Room("A", 1, "04");
                //get string
                String _meterStr = ((EditText)(getView().findViewById(R.id.water_meter_edit_water_bill))).getText().toString();
                String _month = ((TextView)(getView().findViewById(R.id.month_meter_edit_water_bill))).getText().toString();
                String _date = ((TextView)(getView().findViewById(R.id.date_meter_edit_water_bill))).getText().toString();
                Bill _bill = new Bill(_room, 120, "FEB", "10/05/2018");

                UpdatetoFireBase(_room, _bill, Integer.parseInt(_meterStr), _month, _date);
            }
        });
    }


    // update to firebase
    private void UpdatetoFireBase(Room _room, Bill _bill, int meter, String month, String date) {

        //replace previous bill by new value
        _bill.setWater_bill(meter);
        _bill.setMonth(month);
        _bill.setRecord_date(date);
        //get room number
        String _number_room = _room.getPhase()+String.valueOf(_room.getFloor())+_room.getNumber_room();

                // Update bill
                db_cloud.collection("Resident")
                .document(c_auth.getCurrentUser().getUid())
                .collection(_number_room)
                .document("bill")
                .set(_bill);


        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_view, new EditMeterReading())
                .addToBackStack(null).commit();
        Log.d("Edit", "updated");
    }
}

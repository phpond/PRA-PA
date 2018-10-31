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
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prapa.seproject.pra_pa.Fragment.ChoosePlanFragment;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditMeterReading extends Fragment {

    FirebaseFirestore db_cloud = FirebaseFirestore.getInstance();
//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference myRef1 = FirebaseDatabase.getInstance().getReference(); //Getting root reference

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
//        c_auth.getCurrentUser().getUid();
//        Log.d("room", "GET DATA OF "+c_auth.getCurrentUser().getUid());
//                final int room_num = 23;
//        final Room _room = new Room("A", 1, 03);
//        int room_num = _room.getNumber_room();
//        getDataFromFirebase(room_num);
        EditSubmitBtn();

    }





    //edit_submit button on click
    private void EditSubmitBtn(){

        Button edit_submit = getView().findViewById(R.id.submit_btn_meter_edit_water_bill);
        edit_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Room _room = new Room("A", 1, "03");
                //get string
                int meter = Integer.parseInt(((EditText)(getView().findViewById(R.id.water_meter_edit_water_bill))).getText().toString());
                String month = ((EditText)(getView().findViewById(R.id.month_meter_edit_water_bill))).getText().toString();
                String date = ((EditText)(getView().findViewById(R.id.date_meter_edit_water_bill))).getText().toString();

                Bill _bill = new Bill(_room, 120, "FEB", "10/05/2018");
                //pack new data into new bill
//                final Bill new_bill = new Bill(_room, meter, month, date);
                UpdatetoFireBase(_room, _bill, meter, month, date);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new ChoosePlanFragment())
                        .addToBackStack(null).commit();
                Log.d("HOME", "Go to Record");
            }
        });
    }
    //get previous bill from db by _room

//    void getDataFromFirebase(Room _room){
//
//    Bill previous_bill = new Bill(_room, 120, "FEB", "10/05/2018");
//    updatetoFireBase(previous_bill, _room);
//    }

    // update to firebase
    private void UpdatetoFireBase(Room _room, Bill _bill, int meter, String month, String date) {
        //replace previous bill by new value
        _bill.setWater_bill(meter);
        _bill.setMonth(month);
        _bill.setRecord_date(date);
        String _number_room = _room.getPhase()+String.valueOf(_room.getFloor())+_room.getNumber_room();
//        myRef1.child()
//        DatabaseReference myRef = myRef1.child("waterbill"); //Write your child reference if any
//        myRef.setValue(new_bill.getWater_bill());

// Update an existing document
        DocumentReference docRef = db_cloud.collection("Resident")
                .document(c_auth.getCurrentUser().getUid())
                .collection(_number_room)
                .document("bill");
        docRef.update("Water_bill", meter);

        //commit to firebase
    }












    








}

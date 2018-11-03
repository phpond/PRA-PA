package com.prapa.seproject.pra_pa.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prapa.seproject.pra_pa.Bill;
import com.prapa.seproject.pra_pa.R;
import com.prapa.seproject.pra_pa.Room;

public class ShowBillFragment extends Fragment {

    private FirebaseFirestore _fbfs = FirebaseFirestore.getInstance();
    private FirebaseAuth _mAuth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_showbill, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Room _room = ResidentChooseFragment._chooseRoom;
        Log.d("SHOW_BILL", ""+_room.getPhase()+_room.getFloor()+_room.getNumber_room());
        getDataFromFirebase(_room.getPhase()+_room.getFloor()+_room.getNumber_room());
    }

    private void setTextOnFragment(Bill _bill){
        TextView _totalPrice  = getView().findViewById(R.id.price_total_show_bill);
//        TextView _name = getView().findViewById(R.id.name_show_bill);
        TextView _room_id = getView().findViewById(R.id.room_id_show_bill);
        TextView _month = getView().findViewById(R.id.month_show_bill);
        TextView _waterMeter = getView().findViewById(R.id.water_meter_show_bill);
        TextView _recordDate = getView().findViewById(R.id.date_show_bill);

        Log.d("SHOW_BILL", ""+_bill);
        try{
            _totalPrice.setText(String.valueOf(_bill.getTotal_price_bill()));
//            _name.setText("Pond Pond");
            _room_id.setText(""+_bill.getRoom().getPhase()+_bill.getRoom().getFloor()+_bill.getRoom().getNumber_room());
            _month.setText(_bill.getMonth()+"/"+_bill.getYear());
            _waterMeter.setText(String.valueOf(_bill.getWater_bill()));
            _recordDate.setText(_bill.getRecord_date());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getDataFromFirebase(String _room){

        Log.d("SHOW_BILL", "Get form DB");
        _fbfs.collection("Resident")
//                .document(_mAuth.getCurrentUser().getUid())
                .document("USER")
                .collection(_room)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                            setTextOnFragment(doc.toObject(Bill.class));
                            Log.d("SHOW_BILL", "SUCCESS : "+doc.toObject(Bill.class).getTotal_price_bill());

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("SHOW_BILL", "get data from firebase FAIL!!");
            }
        });

    }
}
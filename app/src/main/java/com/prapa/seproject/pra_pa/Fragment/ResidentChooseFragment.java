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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prapa.seproject.pra_pa.Bill;
import com.prapa.seproject.pra_pa.R;
import com.prapa.seproject.pra_pa.Room;

import java.util.Calendar;

public class ResidentChooseFragment extends Fragment {

    protected static Room _chooseRoom;


    private FirebaseFirestore _fbfs = FirebaseFirestore.getInstance();
    private FirebaseAuth _mAuth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.resident_chooseroom, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initSubmitBtn();
    }

    void initSubmitBtn(){
        Button _submit = getView().findViewById(R.id.submit_btn_resident);
        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _room = ((EditText)(getView().findViewById(R.id.resident_fill))).getText().toString();
                try {
                    if(_room.isEmpty()){
                        Log.d("RESCHOOSE", "Empty");
                        Toast.makeText(getActivity(), "กรุณาใส่เลขห้อง", Toast.LENGTH_SHORT).show();
                    }
                    else if(!_room.matches("[A-Z]*\\d{3}"))
                    {
                        Toast.makeText(getActivity(), "กรุณาตรวจสอบเลขห้องอีกครั้ง EX. A401", Toast.LENGTH_SHORT).show();
                        Log.d("RESCHOOSE", "WRONG INPUT");

                    }
                    else{
                        char _phase = _room.charAt(0);
                        char _floor = _room.charAt(1);
                        String _numberRoom = _room.substring(2);
                        if(Integer.parseInt(_numberRoom) <= 12){
                            _chooseRoom = new Room(Character.toString(_phase), ((int) _floor)-48, _numberRoom);

                            getDataFromFirebase(_chooseRoom);
                            Log.d("RESCHOOSE", "");
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void getDataFromFirebase(Room _room){

        String _roomNumber = _room.getPhase()+_room.getFloor()+_room.getNumber_room();

        Log.d("RESCHOOSE", "Get form DB " +_roomNumber );
        _fbfs.collection("Resident")
//                .document(_mAuth.getCurrentUser().getUid())
                .document("USER")
                .collection(_roomNumber)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            Toast.makeText(getActivity(), "ห้องนี้ยังไม่มีการบันทึก", Toast.LENGTH_SHORT).show();
                            Log.d("RESCHOOSE", "EMPTY DATA" );
                        }
                        else {
                            for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                            checkUpdatedData(doc.toObject(Bill.class));
                            Log.d("RESCHOOSE", "GOTO CHECK UPDATED" );
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("RESCHOOSE", "get data from firebase FAIL!!");
            }
        });


    }
    private void checkUpdatedData(Bill _bill){

        String _dateBill = "";
        _dateBill = _bill.getMonth()+_bill.getYear();

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;

        String _dateCurrunt = String.valueOf(month)+String.valueOf(year);

        if(!_dateBill.equals(_dateCurrunt) ){
            Toast.makeText(getActivity(), "ในเดือนนี้ยังไม่มีการบันทึก", Toast.LENGTH_SHORT).show();
            Log.d("RESCHOOSE", "DATA NO UPDATE");
             }
        else {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_view, new ShowBillFragment()).addToBackStack(null).commit();
            Log.d("RESCHOOSE", "GOTO SHOW BILL");

        }
    }
}



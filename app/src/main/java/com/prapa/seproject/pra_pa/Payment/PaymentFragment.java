package com.prapa.seproject.pra_pa.Payment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prapa.seproject.pra_pa.Bill;
import com.prapa.seproject.pra_pa.Fragment.HomeFragment;
import com.prapa.seproject.pra_pa.R;

import java.util.ArrayList;

public class PaymentFragment extends Fragment {

    private SharedPreferences _spfr;

    private FirebaseFirestore _fbfs = FirebaseFirestore.getInstance();
    private ArrayList<Bill> _bills = new ArrayList<>();
    
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PaymentAdapter paymentAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.payment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _spfr = getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
        initLogout();
        backBtn();
        initSerch();

    }

    private void initSerch(){
        ImageButton _serchBtn = getView().findViewById(R.id.serch_room_btn);
        _serchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String _room = ((EditText)(getView().findViewById(R.id.serch_room_payment))).getText().toString();
                    if(_room.length() == 4){
                        String _phase = ""+_room.charAt(0);
                        int _floor = Integer.parseInt(""+_room.charAt(1));
                        int _roomId = Integer.parseInt(""+_room.charAt(2)+_room.charAt(3));
                        if(_room.isEmpty()) {
                            Toast.makeText(getActivity(), "กรุณาใส่ข้อมูล", Toast.LENGTH_SHORT).show();
                            Log.d("PAYMENT", "Empty");
                        }else if(!_phase.matches("[A-Z]")){
                            Toast.makeText(getActivity(), "เฟส A-Z เท่านั้น", Toast.LENGTH_SHORT).show();
                            Log.d("PAYMENT", "Phase not matches");
                        }else if(_floor > 8 || _floor < 1){
                            Toast.makeText(getActivity(), "ชั้น 1-8 เท่านั้น", Toast.LENGTH_SHORT).show();
                            Log.d("PAYMENT", "floor not matches");
                        }else if(_roomId > 12 || _roomId < 1){
                            Toast.makeText(getActivity(), "เลขห้อง 1-12 เท่านั้น", Toast.LENGTH_SHORT).show();
                            Log.d("PAYMENT", "room_id not matches");
                        }else{
                            Toast.makeText(getActivity(), "ดึงข้อมูลเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                            Log.d("PAYMENT", "pass");
                            getDataFromFirebase(_room);
                        }
                    }else {
                        Toast.makeText(getActivity(), "กรุณาใส่หมายเลขห้องให้ครบถ้วน", Toast.LENGTH_SHORT).show();
                        Log.d("PAYMENT", "room len < 4");
                    }
                }catch (Exception e){

                }
            }
        });
    }

    private void setRecycleView(){
        recyclerView = getView().findViewById(R.id.recyclerView_list_payment);
        Log.d("PAYMENT", "set recycle prepare... : "+recyclerView + "bills : "+_bills);
        if(recyclerView != null && _bills != null){

            recyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(mLayoutManager);

            paymentAdapter = new PaymentAdapter(_bills, this.getContext());
            recyclerView.setAdapter(paymentAdapter);
            Log.d("PAYMENT", "set recycle success...");
        }else{
            Log.d("RECIPE", "RecyclerView has empty || bills Empty");
        }
    }

    private void getDataFromFirebase(String room){
        _fbfs.collection("Resident")
                .document("USER")
                .collection(room)
                .orderBy("year", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    Bill _bill = doc.toObject(Bill.class);
                    if(_bill.getStatus().equals("ยังไม่ชำระเงิน")){
                        _bills.add(_bill);
                        Log.d("PAYMENT", "get data SUCCESS... : "+doc.toObject(Bill.class).getMonth()
                                +"/"+doc.toObject(Bill.class).getYear());
                    }
                }
                setRecycleView();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("PAYMENT", "get data from firebase FAIL!!");
            }
        });

    }

    private void initLogout(){
        ImageView _logout = getView().findViewById(R.id.logout_payment);
        _logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = _spfr.edit();
                editor.clear();
                editor.commit();
                Log.d("PAYMENT", _spfr.getString("role", "not found"));
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new HomeFragment())
                        .addToBackStack(null).commit();
                Log.d("PAYMENT", "Logout --> Home");
            }
        });
    }

    private void backBtn(){
        ImageView _backBtn = getView().findViewById(R.id.back_btn_payment);
        _backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .popBackStack();
            }
        });
    }
}

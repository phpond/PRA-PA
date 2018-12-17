package com.prapa.seproject.pra_pa.ShowBill;

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
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.Calendar;

public class ShowBillFragment extends Fragment {

    private FirebaseFirestore _fbfs = FirebaseFirestore.getInstance();

    private SharedPreferences _spfr;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ShowBillAdapter showBillAdapter;

    private ArrayList<Bill> _bills = new ArrayList<>();

    private int month;
    private int year;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_showbill, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dateCurrent();
        _spfr = getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
        Log.d("SHOW_BILL", "User room : "+_spfr.getString("room_id", "not found"));
        getDataFromFirebase(_spfr.getString("room_id", "not found"));
    }

    private void setRecycleView(){
        recyclerView = getView().findViewById(R.id.recycle_list_show_bill);
        Log.d("SHOW_BILL", "set recycle prepare... : "+recyclerView + "bills : "+_bills);
        if(recyclerView != null && _bills != null){

            recyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(mLayoutManager);

            showBillAdapter = new ShowBillAdapter(_bills, this.getContext());
            recyclerView.setAdapter(showBillAdapter);
            Log.d("SHOW_BILL", "set recycle success...");
        }else{
            Log.d("RECIPE", "RecyclerView has empty || bills Empty");
        }
    }

    int count = 0;
    private void getDataFromFirebase(String _room){
        Log.d("SHOW_BILL", "Get form DB");
        _fbfs.collection("Resident")
                .document("USER")
                .collection(_room)
                .orderBy("year", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                            if(count == 3){
                                break;
                            }
                            _bills.add(doc.toObject(Bill.class));
                            count++;
                            Log.d("SHOW_BILL", "get data SUCCESS... : "+doc.toObject(Bill.class).getMonth()+"/"+doc.toObject(Bill.class).getYear());
                        }
                        setRecycleView();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("SHOW_BILL", "get data from firebase FAIL!!");
            }
        });
    }

    private void dateCurrent(){
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
    }
}
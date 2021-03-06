package com.prapa.seproject.pra_pa.ShowBill;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prapa.seproject.pra_pa.Bill;
import com.prapa.seproject.pra_pa.Fragment.HomeFragment;
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

    private int price_not_pay = 0;

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
        initLogout();
    }

    private void setRecycleView(){
        TextView not_pay = getView().findViewById(R.id.not_payment_show_bill);
        not_pay.setText(""+price_not_pay);
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
                            if(doc.toObject(Bill.class).getStatus().equals("ยังไม่ชำระเงิน")){
                                price_not_pay += doc.toObject(Bill.class).getTotal_price_bill();
                            }
                            _bills.add(doc.toObject(Bill.class));
                            count++;
                            Log.d("SHOW_BILL", "get data SUCCESS... : "+doc.toObject(Bill.class).getMonth()+"/"+doc.toObject(Bill.class).getYear());
                        }
                        if(_bills.isEmpty()){
                            Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.showBillLayout), "ไม่พบข้อมูล", Snackbar.LENGTH_LONG);
                            snackbar.setAction("refresh", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getActivity().getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.main_view, new ShowBillFragment())
                                            .addToBackStack(null).commit();
                                }
                            });
                            snackbar.show();

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

    private void initLogout(){
        ImageView _logout = getView().findViewById(R.id.logout_show_bill);
        _logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = _spfr.edit();
                editor.clear();
                editor.commit();
                Log.d("SHOW_BILL", _spfr.getString("room_id", "not found"));
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new HomeFragment())
                        .addToBackStack(null).commit();
                Log.d("SHOW_BILL", "Logout --> Home");
            }
        });
    }
}
package com.prapa.seproject.pra_pa.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prapa.seproject.pra_pa.Bill;
import com.prapa.seproject.pra_pa.R;

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
    }

    private void setTextonFragment(){

    }

    private void getDataFromFirebase(String _room){
        Log.d("SHOW_BILL", "Get form DB");
        _fbfs.collection("Resident")
                .document(_mAuth.getCurrentUser().getUid())
                .collection(_room)
                .document("bill")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                documentSnapshot.toObject(Bill.class);
                Log.d("SHOW_BILL", "get data from firebase SUCCESS!!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("SHOW_BILL", "get data from firebase FAIL!!");
            }
        });
    }
}

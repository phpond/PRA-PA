package com.prapa.seproject.pra_pa.Unit;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.prapa.seproject.pra_pa.R;

import java.util.Calendar;

public class EditUnitFragment extends Fragment {
    Calendar cal;
    FirebaseFirestore _fbfs = FirebaseFirestore.getInstance();
    Unit unit;
    EditText unitCurrunt;
    String unitCurruntStr, year, month, noUnit;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_edit_unit, container, false);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            cal = Calendar.getInstance();
            year = String.valueOf(cal.get(Calendar.YEAR));
            month = String.valueOf(cal.get(Calendar.MONTH)+1);


            initBtbEdit();
            getUnitCurrent();



        }

        private void initBtbEdit(){
            Button _editUnitBtn = getView().findViewById(R.id.unit_edit_btn);
            _editUnitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unitCurrunt = getView().findViewById(R.id.per_unit_str);
                    unitCurruntStr= unitCurrunt.getText().toString();
                    if (unitCurruntStr.isEmpty()){
                        Toast.makeText(getActivity(), "กรุณากรอกข้อมูล", Toast.LENGTH_SHORT).show();
                    }
                    else if(!isNumeric(unitCurruntStr))
                    {
                        Toast.makeText(getActivity(), "ตรวจสอบข้อมูล", Toast.LENGTH_SHORT).show();
                        Log.e("EditunitFragment",  unitCurruntStr);
                    }
                    else {
                        setUnitCurruntToFirebase(unitCurruntStr);
                        Log.d("EditunitFragment",  "GOTO SETDATA");
                    }
                }
            });
        }

    private void getUnitCurrent(){
            _fbfs.collection("UnitMeter")
                    .document("UnitMeter")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.d("EditUnitFragment", "... "+documentSnapshot.toObject(Unit.class));
                            if(documentSnapshot.toObject(Unit.class) == null){
                                Log.e("EditUnitFragment","Unknown Error") ;
                            }else{
                                showUnitCurrent(documentSnapshot.toObject(Unit.class).getPerunit());
                                Log.e("EditUnitFragment"," SET UNIT : "+documentSnapshot.toObject(Unit.class).getPerunit()) ;
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("EditUnitFragment","Unknown Error") ;
                }
            });
        }


    private void setUnitCurruntToFirebase(String unitCurruntStr) {

        unit = new Unit(month,year,Float.valueOf(unitCurruntStr));

            _fbfs.collection("UnitMeter")
                    .document("UnitMeter")
                    .set(unit)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                            //getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_view, new EditUnitFragment()).commit();
                            Log.d("EditUnitFragment", "SUCCESS");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                    Log.d("EditUnitFragment", "Unknown Error");

                }
            });
        }

    private boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    private void showUnitCurrent(float u){
            String unitStr = String.valueOf(u);
            unitCurrunt = getView().findViewById(R.id.per_unit_str);
            unitCurrunt.setText(unitStr);
    }

}

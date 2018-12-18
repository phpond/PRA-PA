package com.prapa.seproject.pra_pa.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prapa.seproject.pra_pa.Bill;
import com.prapa.seproject.pra_pa.R;
import com.prapa.seproject.pra_pa.Room;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class EditRecordFragment extends Fragment {

    FirebaseFirestore db_cloud = FirebaseFirestore.getInstance();
    FirebaseFirestore _fbfs = FirebaseFirestore.getInstance();

    private DatePickerDialog.OnDateSetListener mDateDataListener;
    private DatePickerDialog.OnDateSetListener mDateDataListener2;

    private TextView _recordDateBill;
    private TextView _monthBill;

    private Room _room;
    protected static String PHASE_CHOOSE;
    protected static String FLOOR_CHOOSE;

    private SharedPreferences _spfr;
    private ArrayList<Bill> _bills = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_water_bill, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //set room id

        _room = ViewplanFragment._roomOnclick;
        _spfr = getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
        initLogout();
        backBtn();

        Log.d("EDIT", ""+_room.getPhase()+_room.getFloor()+_room.getNumber_room());
        GetDataFromFirebase(_room.getPhase()+_room.getFloor()+_room.getNumber_room());

        String roomNumber = _room.getPhase()+_room.getFloor()+_room.getNumber_room();
        TextView textView = getView().findViewById(R.id.room_id_edit_water_bill);
        textView.setText(roomNumber);
        EditSubmitBtn();
        initDateRecordCalendar();
        initBillCalendar();
        initRecordCalendar();
        getDataFromFirebase(_spfr.getString("room_id", "not found"));

    }

    private void setTextOnFragment(Bill _bill){
        TextView _month = getView().findViewById(R.id.month_meter_edit_water_bill);
        TextView _waterMeter = getView().findViewById(R.id.water_meter_edit_water_bill);
        TextView _recordDate = getView().findViewById(R.id.date_meter_edit_water_bill);

        Log.d("EDIT", ""+_bill);
        try{
            _month.setText(_bill.getMonth()+"/"+_bill.getYear());
            _waterMeter.setText(String.valueOf(_bill.getWater_bill()));
            _recordDate.setText(_bill.getRecord_date());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    int current_meter;
    String current_date;
    String current_year;
    int current_month;

    //edit_submit button on click
    private void EditSubmitBtn(){

        Button edit_submit = getView().findViewById(R.id.submit_btn_meter_edit_water_bill);
        edit_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get string
                String _meterStr = ((EditText)(getView().findViewById(R.id.water_meter_edit_water_bill))).getText().toString();
                String _month = ((TextView)(getView().findViewById(R.id.month_meter_edit_water_bill))).getText().toString();
                String _date = ((TextView)(getView().findViewById(R.id.date_meter_edit_water_bill))).getText().toString();


                String[] _monthYear = _month.split("/");
                current_date = _date;
                current_month = Integer.parseInt(_monthYear[0]);
                current_year = _monthYear[1];
                current_meter = Integer.parseInt(_meterStr);
                CalculateHistoryMeter(_room, Integer.parseInt(_meterStr), current_date, current_year, current_month);

            }
        });
    }


    private void GetDataFromFirebase(String _room){

        db_cloud.collection("Resident")
                .document("USER")
                .collection(_room)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                            setTextOnFragment(doc.toObject(Bill.class));
                            Log.d("Edit", "SUCCESS : "+doc.toObject(Bill.class).getTotal_price_bill());

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Edit", "get data from firebase FAIL!!");
            }
        });

    }


    private void initBillCalendar(){

        _monthBill = getView().findViewById(R.id.month_meter_edit_water_bill);
        ImageView _selectDate = getView().findViewById(R.id.date_bill_edit_water_bill);
        _selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateDataListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateDataListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                _monthBill.setText(String.format("%02d/%d", month+1, year));
                Log.d("Edit", "On date : "+ day +" / "+month+1 + " / "+year);
            }
        };
    }

    private void initRecordCalendar(){

        ImageView _selectDate = getView().findViewById(R.id.date_record_edit_water_bill);
        _selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateDataListener2,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateDataListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                _recordDateBill.setText(String.format("%02d/%02d/%d", day, month+1, year));
                Log.d("Edit", "On date : "+ day +" / "+month+1 + " / "+year);
            }
        };
    }

    private void initDateRecordCalendar(){
        _recordDateBill = getView().findViewById(R.id.date_meter_edit_water_bill);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        _recordDateBill.setText(String.format("%02d/%02d/%d", day, month+1, year));
        Log.d("Edit", "On date : "+ day +" / "+month+1 + " / "+year);
    }

    int count = 0;
    private void getDataFromFirebase(String _room){
        Log.d("GET_BILL", "Get form DB");
        _fbfs.collection("Resident")
                .document("USER")
                .collection(_room)
                .orderBy("year", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                            if(count == 2){
                                break;
                            }
                            _bills.add(doc.toObject(Bill.class));
                            count++;
                            Log.d("GET_BILL", "get data SUCCESS... : "+doc.toObject(Bill.class).getMonth()+"/"+doc.toObject(Bill.class).getYear());
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("GET_BILL", "get data from firebase FAIL!!");
            }
        });
    }

    private int history;
    public void CalculateHistoryMeter(Room room, final int meter, final String current_date, final String current_year, final int current_month) {

        String history_month = "00";
        String history_year = current_year;

        //current month = 1 --> year -1
        if (current_month < 2) {
            history_year = String.valueOf(Integer.parseInt(current_year) - 1);
            Log.d("RECORD", "History year : " + history_year);
        } else {
            history_month = String.format("%02d", current_month - 1);
        }
        Log.d("RECORD", "Room : " + _room.getRoom() + " history month : " + history_month + " year : " + history_year);
        _fbfs.collection("Resident")
                .document("USER")
                .collection(_room.getRoom())
                .document(history_month + "" + history_year)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        try {
                            Bill bill = documentSnapshot.toObject(Bill.class);
                            history = bill.getWater_bill();
                            Log.d("UPDATE", "history in firebase : " + history);

                            Log.d("UPDATE", "History meter : " + history);
                            Bill _bill = new Bill(_room, meter, String.format("%02d", current_month), current_year, current_date, history);
                            Log.d("UPDATE", "Before up to firebase");
                            UpdatetoFireBase(_bill);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("UPDATE", "Error : " + e.getMessage());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("UPDATE", "getData is Fail");
            }
        });

    }
    // update to firebase
    private void UpdatetoFireBase(Bill _bill) {

        String _number_room = _bill.getRoom().getPhase()+String.valueOf(_bill.getRoom().getFloor())+_bill.getRoom().getNumber_room();

        // Update bill
        db_cloud.collection("Resident")
                .document("USER")
                .collection(_number_room)
                .document(_bill.getMonth()+_bill.getYear())
                .set(_bill);
        PHASE_CHOOSE = _room.getPhase();
        FLOOR_CHOOSE = String.valueOf(_room.getFloor());
        goToNextPage();
        Log.d("Edit", "updated"+PHASE_CHOOSE +FLOOR_CHOOSE);
    }

    private void goToNextPage(){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_view, new ViewplanFragment())
                .addToBackStack(null).commit();
        Log.d("Edit", "updated");
    }

    private void initLogout(){
        ImageView _logout = getView().findViewById(R.id.logout_edit_water_bill);
        _logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = _spfr.edit();
                editor.clear();
                editor.commit();
                Log.d("Edit", _spfr.getString("role", "not found"));
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new HomeFragment())
                        .addToBackStack(null).commit();
                Log.d("Edit", "Logout --> Home");
            }
        });
    }

    private void backBtn(){
        ImageView _backBtn = getView().findViewById(R.id.back_btn_edit_water_bill);
        _backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .popBackStack();
            }
        });
    }
}

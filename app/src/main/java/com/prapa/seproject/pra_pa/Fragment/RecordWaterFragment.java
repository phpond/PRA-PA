package com.prapa.seproject.pra_pa.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Calendar;

public class RecordWaterFragment extends Fragment {

    FirebaseFirestore _fbfs = FirebaseFirestore.getInstance();

    private DatePickerDialog.OnDateSetListener mDateDataListener;
    private DatePickerDialog.OnDateSetListener mDateDataListener2;

    private Room _room;

    protected static String PHASE_CHOOSE;
    protected static String FLOOR_CHOOSE;

    private SharedPreferences _spfr;
    private ArrayList<Bill> _bills = new ArrayList<>();

    private TextView _recordDateBill;
    private TextView _monthBill;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.record_water_bill, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //set date
        setDateRecordCalendar();

        _spfr = getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);

        //set room id
        TextView _roomID = getView().findViewById(R.id.room_id_record_water_bill);
        _room = ViewplanFragment._roomOnclick;
        _roomID.setText(_room.getPhase()+_room.getFloor()+_room.getNumber_room());

        //check on click
        initSubmitBtn();
        initBillCalendar();
        initLogout();
        backBtn();

        initRecordCalendar();

        getDataFromFirebase(_spfr.getString("room_id", "not found"));

    }

    int current_meter;
    String current_date;
    String current_year;
    int current_month;
    //submit to fire base
    private void initSubmitBtn(){
        Button _submitBtn = getView().findViewById(R.id.submit_btn_meter_record_water_bill);

        _submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _meterStr = ((EditText)(getView().findViewById(R.id.water_meter_record_water_bill))).getText().toString();
                String _month = ((TextView)(getView().findViewById(R.id.month_meter_record_water_bill))).getText().toString();
                String _date = ((TextView)(getView().findViewById(R.id.date_meter_record_water_bill))).getText().toString();

                if(_meterStr.isEmpty() || _month.isEmpty() || _date.isEmpty()){
                    Toast.makeText(getActivity(), "Please fill information", Toast.LENGTH_SHORT).show();
                    Log.d("RECORD", "Information is empty");
                }else{
                    String[] _monthYear = _month.split("/");
                    current_date = _date;
                    current_month = Integer.parseInt(_monthYear[0]);
                    current_year = _monthYear[1];
                    current_meter = Integer.parseInt(_meterStr);
                    CalculateHistoryMeter(_room, Integer.parseInt(_meterStr), current_date, current_year, current_month);

                }

            }
        });
    }

    private void initBillCalendar(){

        ImageView _selectDate = getView().findViewById(R.id.date_bill_record_water_bill);
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
                Log.d("RECORD", "On date : "+ day +" / "+month+1 + " / "+year);
            }
        };
    }

    private void initRecordCalendar(){

        ImageView _selectDate = getView().findViewById(R.id.date_record_record_water_bill);
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
                Log.d("RECORD", "On date : "+ day +" / "+month+1 + " / "+year);
            }
        };
    }



    private void setDateRecordCalendar(){
        _recordDateBill = getView().findViewById(R.id.date_meter_record_water_bill);
        _monthBill = getView().findViewById(R.id.month_meter_record_water_bill);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        _recordDateBill.setText(String.format("%02d/%02d/%d", day, month+1, year));
        _monthBill.setText(String.format("%02d/%d", month+1, year));
        Log.d("RECORD", "On date : "+ day +" / "+month+1 + " / "+year);
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
//                        setRecycleView();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("GET_BILL", "get data from firebase FAIL!!");
            }
        });
    }

    private int history;
    public void CalculateHistoryMeter(Room room, final int meter, final String current_date, final String current_year, final int current_month){

        String history_month = "00";
        String history_year = current_year;

        //current month = 1 --> year -1
        if(current_month <2){
            history_year = String.valueOf(Integer.parseInt(current_year)-1);
            Log.d("RECORD", "History year : "+history_year);
        }else{
            history_month = String.format("%02d", current_month-1);
        }
        Log.d("RECORD", "Room : "+_room.getRoom()+" history month : "+history_month+" year : "+history_year);
        _fbfs.collection("Resident")
                .document("USER")
                .collection(_room.getRoom())
                .document(history_month+""+history_year)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        try{
                            Bill bill = documentSnapshot.toObject(Bill.class);
                            history = bill.getWater_bill();
                            Log.d("RECORD", "history in firebase : "+history);

                            Log.d("RECORD", "History meter : "+history);
                            Bill _bill = new Bill(_room, meter, String.format("%02d",current_month), current_year, current_date, history);
                            Log.d("RECORD", "Before up to firebase");
                            upToFireBase(_bill);
                        }catch (Exception e){
                            e.printStackTrace();
                            Log.d("RECORD", "Error : "+e.getMessage());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("RECORD", "getData is Fail");
            }
        });


//        for(int i=1; i<=_billDataSet.size(); i++){

//            _billDataSet.get(i).getWater_bill();
//            _billDataSet.get(i).getRecord_date();
//            _billDataSet.get(i).getMonth();
//            _billDataSet.get(i).getYear();
//            if(_billDataSet.get(i).getMonth().equals(current_month)){
//                History = _billDataSet.get(i+1).getWater_bill();
////                current_meter = current_meter - History;
//                Log.d("SHOW_HISTORY", "History meter and Current is : "+History + current_meter);
//            }
//            else
//            {
//                History = _billDataSet.get(i).getWater_bill();
////                current_meter = current_meter - History;
//                Log.d("SHOW_HISTORY", "History meter and Current is : "+History + current_meter);
//            }
//            break;
//        }
//        return history;
    }

    //up data to firebase
    private void upToFireBase(Bill _bill)
    {
        Log.d("RECORD", "Up to firebase"+_fbfs);
        String _number_room = _bill.getRoom().getPhase()+String.valueOf(_bill.getRoom().getFloor())+_bill.getRoom().getNumber_room();
        _fbfs.collection("Resident")
                .document("USER")
                .collection(_number_room)
                .document(_bill.getMonth()+_bill.getYear())
                .set(_bill)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("RECORD", "SUCCESS");
                PHASE_CHOOSE = _room.getPhase();
                FLOOR_CHOOSE = String.valueOf(_room.getFloor());
                goToNextPage();
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("RECORD", "FAILED");
                Toast.makeText(getActivity(), "Not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToNextPage(){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_view, new ChoosePlanFragment())
                .addToBackStack(null).commit();
    }

    private void initLogout(){
        ImageView _logout = getView().findViewById(R.id.logout_record_water);
        _logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = _spfr.edit();
                editor.clear();
                editor.commit();
                Log.d("RECORD", _spfr.getString("role", "not found"));
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new HomeFragment())
                        .addToBackStack(null).commit();
                Log.d("RECORD", "Logout --> Home");
            }
        });
    }

    private void backBtn(){
        ImageView _backBtn = getView().findViewById(R.id.back_btn_record_water);
        _backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .popBackStack();
            }
        });
    }
}

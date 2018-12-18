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

//        TextView _roomID = getView().findViewById(R.id.room_id_record_water_bill);
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
        initBillCalendar();
        initDateRecordCalendar();
        getDataFromFirebase(_spfr.getString("room_id", "not found"));
    }

    private void setTextOnFragment(Bill _bill){
//        TextView _totalPrice  = getView().findViewById(R.id.price_total_show_bill);
//        TextView _name = getView().findViewById(R.id.name_show_bill);
//        TextView _room_id = getView().findViewById(R.id.room_id_show_bill);
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
    String current_month;

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
                current_month = _monthYear[0];
                current_meter = Integer.parseInt(_meterStr);
                int HistoryMeter = CalculateHistoryMeter(_bills, current_meter, current_date, current_month);
                final Bill _bill = new Bill(_room, Integer.parseInt(_meterStr), _monthYear[0], _monthYear[1], _date, HistoryMeter);
                Log.d("Edit", "new bill");

                UpdatetoFireBase(_bill);
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
        final TextView _monthBill = getView().findViewById(R.id.month_meter_edit_water_bill);
        _monthBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
//                showDialogDate(day, month, year);
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
                _monthBill.setText(String.format("%02d/%d", month, year));
                Log.d("Edit", "On date : "+ day +" / "+month + " / "+year);
            }
        };
    }

    private void initDateRecordCalendar(){
        final TextView _recordDateBill = getView().findViewById(R.id.date_meter_edit_water_bill);
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
//                        setRecycleView();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("GET_BILL", "get data from firebase FAIL!!");
            }
        });
    }

    public int CalculateHistoryMeter(ArrayList<Bill> _billDataSet, int current_meter, String current_date, String current_month){
        this.current_meter = current_meter;
        this.current_date = current_date;
        this.current_month = current_month;

        int History = 0;
        for(int i=1; i<=_billDataSet.size(); i++){

//            _billDataSet.get(i).getWater_bill();
//            _billDataSet.get(i).getRecord_date();
//            _billDataSet.get(i).getMonth();
//            _billDataSet.get(i).getYear();
            if(_billDataSet.get(i).getMonth().equals(current_month)){
                History = _billDataSet.get(i+1).getWater_bill();
//                current_meter = current_meter - History;
                Log.d("SHOW_HISTORY", "History meter and Current is : "+History + current_meter);
            }
            else
            {
                History = _billDataSet.get(i).getWater_bill();
//                current_meter = current_meter - History;
                Log.d("SHOW_HISTORY", "History meter and Current is : "+History + current_meter);
            }
            break;
        }
        return History;
    }



    // update to firebase
    private void UpdatetoFireBase(Bill _bill) {

        //replace previous bill by new value
//        _bill.setWater_bill(meter);
//        _bill.setMonth(month);
//        _bill.setRecord_date(date);
        //get room number
        String _number_room = _bill.getRoom().getPhase()+String.valueOf(_bill.getRoom().getFloor())+_bill.getRoom().getNumber_room();

        // Update bill
        db_cloud.collection("Resident")
//                .document(c_auth.getCurrentUser().getUid())
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

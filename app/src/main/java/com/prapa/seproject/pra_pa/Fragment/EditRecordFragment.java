package com.prapa.seproject.pra_pa.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prapa.seproject.pra_pa.Bill;
import com.prapa.seproject.pra_pa.Fragment.RecordWaterFragment;
import com.prapa.seproject.pra_pa.Fragment.ResidentChooseFragment;
import com.prapa.seproject.pra_pa.Fragment.ShowBillFragment;
import com.prapa.seproject.pra_pa.Fragment.ViewplanFragment;
import com.prapa.seproject.pra_pa.R;
import com.prapa.seproject.pra_pa.Room;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class EditRecordFragment extends Fragment {

    FirebaseFirestore db_cloud = FirebaseFirestore.getInstance();
    FirebaseAuth c_auth = FirebaseAuth.getInstance();

    private DatePickerDialog.OnDateSetListener mDateDataListener;
    private DatePickerDialog.OnDateSetListener mDateDataListener2;

    private Room _room;
    protected static String PHASE_CHOOSE;
    protected static String FLOOR_CHOOSE;


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
         _room = new Room("B", 3, "03");
//        _room = ViewplanFragment._roomOnclick;


        Log.d("EDIT", ""+_room.getPhase()+_room.getFloor()+_room.getNumber_room());
        GetDataFromFirebase(_room.getPhase()+_room.getFloor()+_room.getNumber_room());

        EditSubmitBtn();
        initBillCalendar();
        initDateRecordCalendar();
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
                final Bill _bill = new Bill(_room, Integer.parseInt(_meterStr), _monthYear[0], _monthYear[1], _date);
                Log.d("edit", "new bill");

                UpdatetoFireBase(_bill);
            }
        });
    }


    private void GetDataFromFirebase(String _room){

        db_cloud.collection("Resident")
//                .document(c_auth.getCurrentUser().getUid())
//                .collection("A401")
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
                Log.d("RECORD", "On date : "+ day +" / "+month + " / "+year);
            }
        };
    }

    private void initDateRecordCalendar(){
//        final TextView _recordDateBill = getView().findViewById(R.id.date_meter_edit_water_bill);
//        _recordDateBill.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar cal = Calendar.getInstance();
//                int year = cal.get(Calendar.YEAR);
//                int month = cal.get(Calendar.MONTH);
//                int day = cal.get(Calendar.DAY_OF_MONTH);
//                DatePickerDialog dialog = new DatePickerDialog(getContext(),
//                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
//                        mDateDataListener2,
//                        year,month,day);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.show();
//            }
//        });
//        mDateDataListener2 = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                _recordDateBill.setText(String.format("%02d/%02d/%d", day, month, year));
//                Log.d("RECORD", "On date : "+ day +" / "+month + " / "+year);
//            }
//        };
        final TextView _recordDateBill = getView().findViewById(R.id.date_meter_edit_water_bill);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        _recordDateBill.setText(String.format("%02d/%02d/%d", day, month+1, year));
        Log.d("RECORD", "On date : "+ day +" / "+month+1 + " / "+year);
    }

    private void initHomeBtn(){
        ImageButton _homeBtn = getView().findViewById(R.id.home_btn_record_water_bill);
        _homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PHASE_CHOOSE = null;
                FLOOR_CHOOSE = null;
                goToNextPage();
            }
        });
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

//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.main_view, new ViewplanFragment())
//                .addToBackStack(null).commit();
        Log.d("Edit", "updated");
    }


    private void goToNextPage(){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_view, new ChoosePlanFragment())
                .addToBackStack(null).commit();
        Log.d("Edit", "Done");
    }
}

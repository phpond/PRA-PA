package com.prapa.seproject.pra_pa.Fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prapa.seproject.pra_pa.Bill;
import com.prapa.seproject.pra_pa.R;
import com.prapa.seproject.pra_pa.Fragment.ViewplanFragment;
import com.prapa.seproject.pra_pa.Room;

import java.util.Calendar;

public class RecordWaterFragment extends Fragment {

    FirebaseFirestore _fbfs = FirebaseFirestore.getInstance();
    FirebaseAuth _muth = FirebaseAuth.getInstance();

    private DatePickerDialog.OnDateSetListener mDateDataListener;
    private DatePickerDialog.OnDateSetListener mDateDataListener2;

    private Room _room;
//    String[] _month = {"ม.ค.", "ก.พ.", "มี.ค.", "เม.ย.", "พ.ค.", "มิ.ย.", "ก.ค.", "ส.ค.", "ก.ย.", "ต.ค.", "พ.ย.", "ธ.ค."};
//    String[] _month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.record_water_bill, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //set room id
        TextView _roomID = getView().findViewById(R.id.room_id_record_water_bill);
        _room = ViewplanFragment._roomOnclick;
        _roomID.setText(_room.getPhase()+_room.getFloor()+_room.getNumber_room());

        //check on click
        initSubmitBtn();
        initBillCalendar();
        initDateRecordCalendar();
    }

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
                    final Bill _bill = new Bill(_room, Integer.parseInt(_meterStr), _month, _date);
                    Log.d("RECORD", "Before up to firebase");
                    upToFireBase(_bill);
                }

            }
        });
    }

    private void initBillCalendar(){
        final TextView _monthBill = getView().findViewById(R.id.month_meter_record_water_bill);
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
        final TextView _recordDateBill = getView().findViewById(R.id.date_meter_record_water_bill);
        _recordDateBill.setOnClickListener(new View.OnClickListener() {
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
                _recordDateBill.setText(String.format("%02d/%02d/%d", day, month, year));
                Log.d("RECORD", "On date : "+ day +" / "+month + " / "+year);
            }
        };
    }

    //up data to firebase
    private void upToFireBase(Bill _bill)
    {
        Log.d("RECORD", "ก่อนเข้า"+_fbfs);
        String _number_room = _bill.getRoom().getPhase()+String.valueOf(_bill.getRoom().getFloor())+_bill.getRoom().getNumber_room();

        _fbfs.collection("Resident")
                .document(_muth.getCurrentUser().getUid())
                .collection(_number_room)
                .document("bill")
                .set(_bill)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("RECORD", "SUCCESS");
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("RECORD", "FAILED");
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

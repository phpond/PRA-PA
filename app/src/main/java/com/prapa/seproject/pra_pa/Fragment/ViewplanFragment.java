package com.prapa.seproject.pra_pa.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prapa.seproject.pra_pa.Bill;
import com.prapa.seproject.pra_pa.R;
import com.prapa.seproject.pra_pa.Room;

import java.util.Calendar;

public class ViewplanFragment extends Fragment implements View.OnClickListener {

    protected static Room _roomOnclick;

    private FirebaseFirestore _fbfs = FirebaseFirestore.getInstance();
    private SharedPreferences _spfr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_viewplan, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _spfr = getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
        initLogout();
        backBtn();

        ImageView one = getView().findViewById(R.id.room_01);
        one.setOnClickListener(this); // calling onClick() method
        ImageView two = getView().findViewById(R.id.room_02);
        two.setOnClickListener(this);
        ImageView three = getView().findViewById(R.id.room_03);
        three.setOnClickListener(this);
        ImageView four = getView().findViewById(R.id.room_04);
        four.setOnClickListener(this); // calling onClick() method
        ImageView five = getView().findViewById(R.id.room_05);
        five.setOnClickListener(this);
        ImageView six = getView().findViewById(R.id.room_06);
        six.setOnClickListener(this);
        ImageView seven = getView().findViewById(R.id.room_07);
        seven.setOnClickListener(this); // calling onClick() method
        ImageView eight = getView().findViewById(R.id.room_08);
        eight.setOnClickListener(this);
        ImageView nine = getView().findViewById(R.id.room_09);
        nine.setOnClickListener(this);
        ImageView ten = getView().findViewById(R.id.room_010);
        ten.setOnClickListener(this); // calling onClick() method
        ImageView eleven = getView().findViewById(R.id.room_11);
        eleven.setOnClickListener(this);
        ImageView twelve= getView().findViewById(R.id.room_12);
        twelve.setOnClickListener(this);

        ShowPhaseAndFloor();

    }

    @Override
    public void onClick(View v) {


       switch (v.getId()) {

            case R.id.room_01:
                // do your code
                gotoRecordPage("01");
                break;

            case R.id.room_02:
                // do your code
                gotoRecordPage("02");
                break;

            case R.id.room_03:
                // do your code
                gotoRecordPage("03");
                break;

            case R.id.room_04:
                // do your code
                gotoRecordPage("04");
                break;

            case R.id.room_05:
                // do your code
                gotoRecordPage("05");
                break;

            case R.id.room_06:
                // do your code
                gotoRecordPage("06");
                break;

            case R.id.room_07:
                // do your code
                gotoRecordPage("07");
                break;

            case R.id.room_08:
                // do your code
                gotoRecordPage("08");
                break;

            case R.id.room_09:
                // do your code
                gotoRecordPage("09");
                break;

            case R.id.room_010:
                // do your code
                gotoRecordPage("10");
                break;

            case R.id.room_11:
                // do your code
                gotoRecordPage("11");
                break;

            case R.id.room_12:
                // do your code
                gotoRecordPage("12");
                break;

            default:
                break;
        }
    }


        void ShowPhaseAndFloor(){
            String Phase;
            String Floor;
            Room nr;


            Bundle bundle = this.getArguments();
            if (bundle != null) {
                nr = bundle.getParcelable("PhaseAndFloor");


                Phase = nr.getPhase();
                Floor = Integer.toString(nr.getFloor());
                TextView _phaseStr = getView().findViewById(R.id.phase_view_plan);
                TextView _floorStr = getView().findViewById(R.id.floor_view_plan);

                _phaseStr.setText(Phase);
                _floorStr.setText(Floor);

                ShowNumberRoom(Phase, Floor);

                }
        }

        void ShowNumberRoom(String Phase, String Floor){

            Integer[] position = {
                    R.id.top_num_01, R.id.top_num_02,
                    R.id.top_num_03, R.id.top_num_04,
                    R.id.top_num_05, R.id.top_num_06,
                    R.id.top_num_07, R.id.top_num_08,
                    R.id.top_num_09, R.id.top_num_10,
                    R.id.top_num_11, R.id.top_num_12,
            };

            TextView textView;
            int i;
            String PhaseandFloor = Phase+Floor;
            String numberStr = "";

            for (i=0;i<12;i++) {
                if(i<9) {
                    numberStr = PhaseandFloor +"0"+ Integer.toString(i + 1);
                }
                else {
                    numberStr = PhaseandFloor + Integer.toString(i + 1);
                }

                    textView = getView().findViewById(position[i]);
                    textView.setText(numberStr);

            }
        }

        void gotoRecordPage(String NumberRoom){

            Room nr;
            Bundle bundle = this.getArguments();
            if (bundle != null) {

                nr = bundle.getParcelable("PhaseAndFloor");

                String Phase = nr.getPhase();
                int Floor = nr.getFloor();
                nr.setNumber_room(NumberRoom);

                getDataFromFirebase(Phase+Floor+NumberRoom);
                _roomOnclick = nr;
            }
    }


    private void getDataFromFirebase(String _room){

        Log.d("VIEW_PLAN", "Get form DB");
        _fbfs.collection("Resident")
                .document("USER")
                .collection(_room)
                .orderBy("year",Query.Direction.DESCENDING).limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            Log.d("VIEW_PLAN", "EMPTY DATA" );
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_view, new RecordWaterFragment()).addToBackStack(null).commit();
                            Log.d("VIEW_PLAN", "(EMPTY ROOM BILL) GOTO RECORD" );
                        }
                        else {
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                checkUpdatedData(doc.toObject(Bill.class));
                                Log.d("VIEW_PLAN", "GET DATA SUCCESS : " + doc.toObject(Bill.class).getMonth()+ doc.toObject(Bill.class).getYear());

                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "EMPTY", Toast.LENGTH_SHORT).show();

                Log.d("VIEW_PLAN", "get data from firebase FAIL!!");
            }
        });

    }
    private void checkUpdatedData(Bill _bill){

        String _dateBill = "";
        _dateBill = _bill.getMonth()+_bill.getYear();

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;

        String _dateCurrunt = String.valueOf(month)+String.valueOf(year);

        if(_dateBill.equals(_dateCurrunt) )
        {   getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_view, new EditRecordFragment()).addToBackStack(null).commit();
            Log.d("VIEW_PLAN", "DATE_BILL TRUE : GOTO EDIT ON "+_dateCurrunt);
        }
        else {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_view, new RecordWaterFragment()).addToBackStack(null).commit();
            Log.d("VIEW_PLAN", "DATE_BILL FALSE : GOTO REC  ON "+_dateCurrunt);
        }
    }

    private void initLogout(){
        ImageView _logout = getView().findViewById(R.id.logout_view_plan);
        _logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = _spfr.edit();
                editor.clear();
                editor.commit();
                Log.d("VIEW_PLAN", _spfr.getString("role", "not found"));
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new HomeFragment())
                        .addToBackStack(null).commit();
                Log.d("VIEW_PLAN", "Logout --> Home");
            }
        });
    }

    private void backBtn(){
        ImageView _backBtn = getView().findViewById(R.id.icon_back_viewplan);
        _backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .popBackStack();
            }
        });
    }
}

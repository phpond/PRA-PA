package com.prapa.seproject.pra_pa.Fragment;

import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.prapa.seproject.pra_pa.R;
import com.prapa.seproject.pra_pa.Room;

import com.prapa.seproject.pra_pa.Fragment.RecordWaterFragment;

import org.w3c.dom.Text;

import java.util.concurrent.Phaser;


public class ViewplanFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_viewplan, container, false);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ImageView one = getView().findViewById(R.id.top_1_view_plan);
        one.setOnClickListener(this); // calling onClick() method
        ImageView two = getView().findViewById(R.id.top_2_view_plan);
        two.setOnClickListener(this);
        ImageView three = getView().findViewById(R.id.top_3_view_plan);
        three.setOnClickListener(this);
        ImageView four = getView().findViewById(R.id.top_4_view_plan);
        four.setOnClickListener(this); // calling onClick() method
        ImageView five = getView().findViewById(R.id.top_5_view_plan);
        five.setOnClickListener(this);
        ImageView six = getView().findViewById(R.id.top_6_view_plan);
        six.setOnClickListener(this);
        ImageView seven = getView().findViewById(R.id.bottom_1_view_plan);
        seven.setOnClickListener(this); // calling onClick() method
        ImageView eight = getView().findViewById(R.id.bottom_2_view_plan);
        eight.setOnClickListener(this);
        ImageView nine = getView().findViewById(R.id.bottom_3_view_plan);
        nine.setOnClickListener(this);
        ImageView ten = getView().findViewById(R.id.bottom_4_view_plan);
        ten.setOnClickListener(this); // calling onClick() method
        ImageView eleven = getView().findViewById(R.id.bottom_5_view_plan);
        eleven.setOnClickListener(this);
        ImageView twelve= getView().findViewById(R.id.bottom_6_view_plan);
        twelve.setOnClickListener(this);

        ShowPhaseAndFloor();


    }



    @Override
    public void onClick(View v) {


       switch (v.getId()) {

            case R.id.top_1_view_plan:
                // do your code
                gotoRecordPage("01");
                break;

            case R.id.top_2_view_plan:
                // do your code
                gotoRecordPage("02");
                break;

            case R.id.top_3_view_plan:
                // do your code
                gotoRecordPage("03");
                break;

            case R.id.top_4_view_plan:
                // do your code
                gotoRecordPage("04");
                break;

            case R.id.top_5_view_plan:
                // do your code
                gotoRecordPage("05");
                break;

            case R.id.top_6_view_plan:
                // do your code
                gotoRecordPage("06");
                break;

            case R.id.bottom_1_view_plan:
                // do your code
                gotoRecordPage("07");
                break;

            case R.id.bottom_2_view_plan:
                // do your code
                gotoRecordPage("08");
                break;

            case R.id.bottom_3_view_plan:
                // do your code
                gotoRecordPage("09");
                break;

            case R.id.bottom_4_view_plan:
                // do your code
                gotoRecordPage("10");
                break;

            case R.id.bottom_5_view_plan:
                // do your code
                gotoRecordPage("11");
                break;

            case R.id.bottom_6_view_plan:
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
                nr.setNumber_room(Integer.parseInt(NumberRoom));

                Log.d("VIEWPLAN", "GOTO RECORD : Room " + Phase + Floor + NumberRoom);
            }
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_view, new RecordWaterFragment()).commit();
    }
}

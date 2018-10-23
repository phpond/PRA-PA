package com.prapa.seproject.pra_pa;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


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
        //ShowNumberRoom();

    }



    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.top_1_view_plan:
                // do your code
                Toast.makeText(getActivity(), "01", Toast.LENGTH_SHORT).show();
                break;

            case R.id.top_2_view_plan:
                // do your code
                //nr.setNumber("02");
                Toast.makeText(getActivity(), "02", Toast.LENGTH_SHORT).show();
                break;

            case R.id.top_3_view_plan:
                // do your code
                //nr.setNumber("03");
                Toast.makeText(getActivity(), "03", Toast.LENGTH_SHORT).show();
                break;

            case R.id.top_4_view_plan:
                // do your code
                //nr.setNumber("04");
                Toast.makeText(getActivity(), "04", Toast.LENGTH_SHORT).show();
                break;

            case R.id.top_5_view_plan:
                // do your code
                //nr.setNumber("05");
                Toast.makeText(getActivity(), "05", Toast.LENGTH_SHORT).show();
                break;

            case R.id.top_6_view_plan:
                // do your code
                //nr.setNumber("06");
                Toast.makeText(getActivity(), "06", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bottom_1_view_plan:
                // do your code
                //nr.setNumber("07");
                Toast.makeText(getActivity(), "07", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bottom_2_view_plan:
                // do your code
                //nr.setNumber("08");
                Toast.makeText(getActivity(), "08", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bottom_3_view_plan:
                // do your code
                //nr.setNumber("09");
                Toast.makeText(getActivity(), "09", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bottom_4_view_plan:
                // do your code
                //nr.setNumber("10");
                Toast.makeText(getActivity(), "10", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bottom_5_view_plan:
                // do your code
                //nr.setNumber("11");
                Toast.makeText(getActivity(), "11", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bottom_6_view_plan:
                // do your code
                //nr.setNumber("12");
                Toast.makeText(getActivity(), "12", Toast.LENGTH_SHORT).show();
                break;


            default:
                break;
        }
    }

        void ShowNumberRoom(){

            Room nr = getActivity().getIntent().getParcelableExtra("PhaseFloor");
            String _PhaseStr = nr.getPhase();
            EditText editText = getView().findViewById(R.id.editText);
            editText.setText(_PhaseStr);

            Toast.makeText(getActivity(), _PhaseStr, Toast.LENGTH_SHORT).show();

        }
    }

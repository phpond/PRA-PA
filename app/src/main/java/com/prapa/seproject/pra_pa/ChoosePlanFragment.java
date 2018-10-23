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
import android.widget.Toast;

public class ChoosePlanFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chooseplan, container, false);
    }

    @Override

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initSummit();
    }
   void initSummit(){

       Button _btnSummit = getView().findViewById(R.id.submit_btn_choose_plan);
       _btnSummit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent i = new Intent(getActivity(),ViewplanFragment.class);

               Room nr = new Room();

               String _phaseStr = ((EditText)(getView().findViewById(R.id.phase_choose_plan))).getText().toString();
               String _floorStr = ((EditText)(getView().findViewById(R.id.floor_choose_plan))).getText().toString();



               if(_floorStr.isEmpty() || _phaseStr.isEmpty()){
                   Toast.makeText(getActivity(),"กรุณากรอกข้อมูลให้ครบถ้วน",Toast.LENGTH_SHORT).show();
                   Log.d("ChoosePlanFragment : ","PHASE OR FLOOR IS EMPTY");
               }
               else {
                   nr.setPhase(_phaseStr);
                   nr.setFloor(_floorStr);
                   //i.putExtra("PhaseFloor",nr );
                   //String a = nr.getFloor();
                   //startActivity(i);
                   getActivity().startActivity(i);
                   //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_view, new ViewplanFragment()).addToBackStack(null).commit();
                   //Toast.makeText(getActivity(),a,Toast.LENGTH_SHORT).show();
               }
           }
       });


   }

}

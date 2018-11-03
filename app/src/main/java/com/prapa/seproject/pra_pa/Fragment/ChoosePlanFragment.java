package com.prapa.seproject.pra_pa.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.prapa.seproject.pra_pa.R;
import com.prapa.seproject.pra_pa.Room;

public class ChoosePlanFragment extends Fragment {

    private String _phaseStr;
    private String _floorStr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chooseplan, container, false);
    }

    @Override

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(RecordWaterFragment.PHASE_CHOOSE != null || RecordWaterFragment.FLOOR_CHOOSE != null){
            Log.d("ChoosePlanFragment : ", "go to view plan with old phase/floor : "
                    +RecordWaterFragment.PHASE_CHOOSE+"/"+RecordWaterFragment.FLOOR_CHOOSE);
            _phaseStr = RecordWaterFragment.PHASE_CHOOSE;
            _floorStr = RecordWaterFragment.FLOOR_CHOOSE;
            gotoNextPage(_phaseStr, _floorStr);
        }else{
            initSummit();
        }
    }

    void initSummit(){
       Button _btnSummit = getView().findViewById(R.id.submit_btn_choose_plan);
       _btnSummit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               _phaseStr = ((EditText)(getView().findViewById(R.id.phase_choose_plan))).getText().toString();
               _floorStr = ((EditText)(getView().findViewById(R.id.floor_choose_plan))).getText().toString();
               if(_floorStr.isEmpty() || _phaseStr.isEmpty()){
                   Toast.makeText(getActivity(),"กรุณากรอกข้อมูลให้ครบถ้วน",Toast.LENGTH_SHORT).show();
                   Log.d("ChoosePlanFragment : ","PHASE OR FLOOR IS EMPTY");
               }
               else if(!_floorStr.matches("[0-9]") || !_phaseStr.matches("[A-Z]")){
                   Toast.makeText(getActivity(),"กรุณาตรวจสอบข้อมูลอีกครั้ง",Toast.LENGTH_SHORT).show();
                   Log.d("ChoosePlanFragment : ","FLOOR IS'T NUMERIC");

               }
               else {
                   gotoNextPage(_phaseStr,_floorStr);
                   Log.d("ChoosePlanFragment" ,"GO TO PHASE : "+_floorStr +" FLOOR : "+_floorStr);
               }
           }
       });
   }

   void gotoNextPage(String _phaseStr, String _floorStr){

       Room nr = new Room();

       int _floorInt = Integer.parseInt(_floorStr);
       nr.setPhase(_phaseStr.toUpperCase());
       nr.setFloor(_floorInt);
       nr.setNumber_room("0");

       Bundle bundle = new Bundle();
       bundle.putParcelable("PhaseAndFloor", nr);

       Fragment fragmentGet = new ViewplanFragment();
       fragmentGet.setArguments(bundle);
       FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
       FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
       fragmentTransaction.replace(R.id.main_view, fragmentGet);
       fragmentTransaction.addToBackStack(null);
       fragmentTransaction.commit();
   }
}

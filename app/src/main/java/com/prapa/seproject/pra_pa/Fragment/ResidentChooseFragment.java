package com.prapa.seproject.pra_pa.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.prapa.seproject.pra_pa.R;
import com.prapa.seproject.pra_pa.Room;

public class ResidentChooseFragment extends Fragment {

    protected static Room _chooseRoom;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.resident_chooseroom, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initSubmitBtn();
    }

    void initSubmitBtn(){
        Button _submit = getView().findViewById(R.id.submit_btn_resident);
        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _room = ((EditText)(getView().findViewById(R.id.resident_fill))).getText().toString();
                char _phase = _room.charAt(0);
                char _floor = _room.charAt(1);
                String _numberRoom = _room.substring(2);
                try {
                    if(Integer.parseInt(_numberRoom) <= 12){
                        _chooseRoom = new Room(Character.toString(_phase), ((int) _floor)-48, _numberRoom);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_view, new ShowBillFragment()).addToBackStack(null).commit();
                        Log.d("CHOOSE_PLAN", "Go to show bill with room :"+ Character.toString(_phase)+(int) _floor+ _numberRoom);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                Log.d("CHOOSE_PLAN", _phase + " " + _floor + " "+ _numberRoom);
            }
        });
    }
}

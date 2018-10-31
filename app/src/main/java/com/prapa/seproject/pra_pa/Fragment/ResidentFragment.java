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
import android.widget.Toast;

import com.prapa.seproject.pra_pa.R;


public class ResidentFragment extends Fragment {
    protected static String _roomNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.resident_chooseroom, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initSummit();
    }

    private void initSummit()
    {
        Button _btnSummit = getView().findViewById(R.id.submit_btn_resident);
        _btnSummit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _roomStr = ((EditText) (getView().findViewById(R.id.resident_fill))).getText().toString();

                _roomNumber = _roomStr;

                if (_roomStr.isEmpty()) {
                    Toast.makeText(getActivity(),"กรุณากรอกข้อมูลให้ครบถ้วน",Toast.LENGTH_SHORT).show();
                    Log.d("RESIDENT : ","ROOM_NUMBER IS EMPTY");
                } else {
                    Log.d("RESIDENT : ","GO TO SHOW_BILL ROOM: "+_roomNumber);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_view, new ShowBillFragment()).addToBackStack(null).commit();
                }
            }
        });

    }
}



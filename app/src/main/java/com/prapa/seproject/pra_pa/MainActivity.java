package com.prapa.seproject.pra_pa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_water_bill);
        /*if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_view, new ChoosePlanFragment()).commit();
        }*/
    }
}

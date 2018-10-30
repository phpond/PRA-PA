package com.prapa.seproject.pra_pa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.prapa.seproject.pra_pa.Fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_view, new HomeFragment()).addToBackStack(null).commit();
        }
    }
}

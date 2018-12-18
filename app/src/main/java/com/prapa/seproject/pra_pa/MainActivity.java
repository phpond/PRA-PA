package com.prapa.seproject.pra_pa;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.prapa.seproject.pra_pa.Fragment.HomeFragment;
import com.prapa.seproject.pra_pa.Fragment.SearchUsernameFragment;

public class MainActivity extends AppCompatActivity {

    private static int WELCOME_TIMEOUT = 2000;
    protected static int COUNT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(COUNT == 0){
            COUNT++;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent welcome = new Intent();
                    welcome.setClass(MainActivity.this, LogoFragment.class);
                    startActivity(welcome);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            }, WELCOME_TIMEOUT);
            Log.d("Main", "fade | count : "+COUNT);
        }else{
            Log.d("Main", "out fade | count : "+COUNT+" state : "+savedInstanceState);
            COUNT = 0;
            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction().replace(R.id.main_view, new HomeFragment()).commit();
            }
        }

    }
}

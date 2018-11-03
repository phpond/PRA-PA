package com.prapa.seproject.pra_pa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class LogoFragment extends AppCompatActivity {
    private static int WELCOME_TIMEOUT = 4000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo_fragment);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent welcome = new Intent();
                welcome.setClass(LogoFragment.this, MainActivity.class);
                startActivity(welcome);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }, WELCOME_TIMEOUT);

        MainActivity.COUNT = 1;
    }
}

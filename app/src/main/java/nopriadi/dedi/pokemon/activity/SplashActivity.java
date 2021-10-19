/*
 * Created by Dedi Nopriadi on 10/14/21, 1:41 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 10/12/21, 1:41 PM
 */

package nopriadi.dedi.pokemon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import nopriadi.dedi.pokemon.MainActivity;
import nopriadi.dedi.pokemon.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        },2600);
    }
}
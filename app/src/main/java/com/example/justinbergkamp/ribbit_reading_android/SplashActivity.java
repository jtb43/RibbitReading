package com.example.justinbergkamp.ribbit_reading_android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import java.util.concurrent.TimeUnit;
/**
 * Created by codyli on 4/19/18.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        //real janky method of making the splash screen stay on for a bit longer
        try {
            TimeUnit.SECONDS.sleep(1);
        }
        catch(InterruptedException e){
            System.out.print("Error");
        }
        finish();

    }
}

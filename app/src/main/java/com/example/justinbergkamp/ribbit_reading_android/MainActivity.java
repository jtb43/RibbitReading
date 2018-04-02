package com.example.justinbergkamp.ribbit_reading_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void accessSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    };

    public void accessLibrary(View view){
        Intent intent = new Intent(this, LibraryActivity.class);
        startActivity(intent);
    };
}

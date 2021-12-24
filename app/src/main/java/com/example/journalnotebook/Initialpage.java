package com.example.journalnotebook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Initialpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_face);
    }
    //Button INITIAL_PAGEBUTTON = (Button) findViewById(R.id.startbutton);

    public void onClickstart (View view) {
        Log.e("1206-1.1", "1206");
        Intent intent = new Intent(Initialpage.this,MainActivity.class);
        intent.putExtra("data","this is inactivity");
        startActivity(intent);
    }
}

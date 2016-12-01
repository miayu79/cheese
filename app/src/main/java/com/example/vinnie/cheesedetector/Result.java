package com.example.vinnie.cheesedetector;

import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;
import java.io.FileOutputStream;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
public class Result extends AppCompatActivity {
    TextView rr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String value=(mSharedPreference.getString("MyPrefs", "Default_Value"));
//        try{
//        Intent in = getIntent();
//        String mm=in.getStringExtra("id");
//        rr = (TextView) findViewById(R.id.rr);
//        rr.setText(mm);
//        System.out.println("***"+mm);
//        }catch(Exception e){ }
        rr = (TextView) findViewById(R.id.rr);
        rr.setText("Here is the odd one:"+value);

//        try{
//            Bundle extras = getIntent().getExtras();
//            String value="";
//            if (extras != null) {
//
//                value = extras.getString("key");
//                rr.setText("the odd one is: "+value);
//                System.out.println("---"+value);
//                //The key argument here must match that used in the other activity
//            }
//        }catch(Exception e){ }

    }


}

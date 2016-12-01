package com.example.vinnie.cheesedetector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Introduction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Instruction");

        TextView intro1 = (TextView)findViewById(R.id.intro1);
        intro1.setText("1. Allow the MoxStick to warm up for 10 minutes before experimenting.");

        TextView intro2 = (TextView)findViewById(R.id.intro2);
        intro2.setText("2. You will be lead to the bluetooth page first to connect the phone to the pi.");

        TextView intro3 = (TextView)findViewById(R.id.intro3);
        intro3.setText("3. When it is connected, Click ready to start a set of sampling.");

    }
    public void bt(View view){
        Intent intent = new Intent(this, BlueteethActivity.class);
        startActivity(intent);
    }

}

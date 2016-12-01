package com.example.vinnie.cheesedetector;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Chronometer;
import android.widget.Toast;
import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import android.content.SharedPreferences;
import android.os.SystemClock;
import static android.content.SharedPreferences.*;

public class single extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    ConnectedThread ct;
    Chronometer simpleChronometer;
    String FILENAME = "res";
    BluetoothSocket mmSocket=BlueteethActivity.mmSocket;
    private TextView text;
    String h;
    Handler mHandler = new Handler(new IncomingHandlerCallback());
    class IncomingHandlerCallback implements Handler.Callback{
        public String mm;
        @Override

        public boolean handleMessage(Message msg) {
//            String message = (String) msg.obj; //Extract the string from the Message
//            text_2.setText(message);
//            //....
//            mm=message;
            String sentString=msg.getData().getString("what");
            //text.setText(sentString);
            mm=sentString;
            return true;
        }

         public String getMm() {
             return mm;
         }
     };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        text = (TextView)findViewById(R.id.text);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //text.setText(h);
        simpleChronometer = (Chronometer) findViewById(R.id.chronometer);
        //System.out.println("got the mesg1: " + h);
    }


    public void next(View v){
        Intent intent = new Intent(this, single_2.class);
        startActivity(intent);
    }

    public synchronized void receive(View v) {
        simpleChronometer.start();
        ct = new ConnectedThread(BlueteethActivity.mmSocket);
        ct.start();
        //message.setText("hey");

        ct.write("start_1");//start, sensor should start sensing process

    }
    public synchronized void stop(View v) {
        simpleChronometer.stop();
        ct.write("stop_1");//start, sensor should start sensing process
//        try {
//            ct.join(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    //    //=============connectedthread
    private class ConnectedThread extends Thread {
        private boolean mRunning = false;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private final BluetoothSocket ss;

        public ConnectedThread(BluetoothSocket socket) {
            ss=socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;


            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
            mRunning = true;
            byte[] buffer = new byte[1024];
            int begin = 0;
            int bytes = 0;
            while (mRunning) {
                try {



                    bytes = mmInStream.read(buffer);
                    String read = new String(buffer, 0, bytes);
//                    Message msg=Message.obtain();
//                    msg.obj=read;
//                    msg.setTarget(mHandler_2);
//                    msg.sendToTarget();
                    Message m = new Message();
                    Bundle b = new Bundle();
                    b.putString("what", read); // for example
                    m.setData(b);
                    mHandler.sendMessage(m);
                    String sentString=m.getData().getString("what");
                    System.out.print("**************");
                    System.out.println(sentString);

//                    if(sentString.charAt(0)=='^') {
//                        Editor editor = sharedpreferences.edit();
//                        System.out.println("suppose to be here");
//                        editor.putString(MyPREFERENCES, sentString);
//                        editor.apply();
//                    }

                } catch (IOException e) {
                    break;
                }
            }
        }
        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {

            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {

            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
        public void close() {
            mRunning = false;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ct.close();
    }

//    public void bt(View view){
//        Intent intent = new Intent(this, BlueteethActivity.class);
//        startActivity(intent);
//    }
}

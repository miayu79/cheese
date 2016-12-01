package com.example.vinnie.cheesedetector;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class single_2 extends AppCompatActivity {
    ConnectedThread ct_2;
    Chronometer simpleChronometer2;
    ConnectedThread get_res;
    BluetoothSocket mmSocket=BlueteethActivity.mmSocket;
    TextView text_2;
    String h;

    //    Handler mHandler=new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            String message = (String) msg.obj;
//
//        }
//    };
    Handler mHandler_2 = new Handler(new IncomingHandlerCallback());
    class IncomingHandlerCallback implements Handler.Callback{
        public String mm;
        @Override

        public boolean handleMessage(Message msg) {
//            String message = (String) msg.obj; //Extract the string from the Message
//            text_2.setText(message);
//            //....
//            mm=message;
            String sentString=msg.getData().getString("what");
            text_2.setText(sentString);
            mm=sentString;
            return true;
        }
        public String getMm(){
            return mm;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_2);
        text_2=(TextView)findViewById(R.id.text_2);
        simpleChronometer2 = (Chronometer) findViewById(R.id.chronometer2);
        //text_2.setText("s");
        System.out.println("handler&&&");
    }
//    public void mm(View v){
//        Toast.makeText(getApplicationContext(), mHandler_2.getMm(), Toast.LENGTH_LONG).show();
//    }
    public void next(View v){
        Intent intent = new Intent(this, sample3.class);
        startActivity(intent);
    }
//    public void complete(View v){
//        get_res = new ConnectedThread(BlueteethActivity.mmSocket);
//        get_res.start();
//        get_res.write("result");
//        Intent intent = new Intent(this, Result.class);
//        startActivity(intent);
//    }
    public synchronized void receive(View v) {
        simpleChronometer2.start();
        ct_2 = new ConnectedThread(BlueteethActivity.mmSocket);
        ct_2.start();
        //message.setText("hey");

        ct_2.write("start_2");//start, sensor should start sensing process

    }
    public synchronized void stop(View v) {
        simpleChronometer2.stop();
        ct_2.write("stop_2");//start, sensor should start sensing process
        if(ct_2!=null) {
            System.out.println("kill");
            ct_2.interrupt();
        }
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
                    b.putString("what",read); // for example
                    m.setData(b);
                    mHandler_2.sendMessage(m);
                    String sentString=m.getData().getString("what");
                    System.out.print("send to handler22222From pi: ");
                    System.out.println(sentString);

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
        ct_2.close();
    }



}


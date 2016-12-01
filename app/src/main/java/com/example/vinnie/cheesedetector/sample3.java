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
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class sample3 extends AppCompatActivity {
    ConnectedThread ct;
    ConnectedThread get_res;
    Chronometer simpleChronometer3;
    BluetoothSocket mmSocket=BlueteethActivity.mmSocket;
    TextView text_3;
    String h;

    class myHandler extends Handler {
        public String mm;
        @Override

        public void handleMessage(Message msg) {
//            String message = (String) msg.obj; //Extract the string from the Message
//            text_2.setText(message);
//            //....
//            mm=message;
            String sentString=msg.getData().getString("what");
            text_3.setText(sentString);
            mm=sentString;
        }
        public String getMsg(){
            return mm;
        }
    };
    myHandler mHandler_3 = new myHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample3);
        text_3 = (TextView)findViewById(R.id.textView4);
        simpleChronometer3 = (Chronometer) findViewById(R.id.chronometer3);
        //text_3.setText(h);
        System.out.println("got the mesg" + h);
    }

    public void complete(View v){
        get_res = new ConnectedThread(BlueteethActivity.mmSocket);
        get_res.start();
        get_res.write("result");
        //get_res.cancel();
        Intent intent = new Intent(this, Result.class);
        startActivity(intent);
    }

    public synchronized void start(View v) {
        ct = new ConnectedThread(BlueteethActivity.mmSocket);
        simpleChronometer3.start();
        ct.start();
        //message.setText("hey");

        ct.write("start_3");//start, sensor should start sensing process

    }
    public synchronized void end(View v) {
        simpleChronometer3.stop();
        ct.write("stop_3");//start, sensor should start sensing process
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
            mRunning=true;
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
                    mHandler_3.sendMessage(m);

                    System.out.print("send to handlerFrom pi: ");
                    System.out.println(read);

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

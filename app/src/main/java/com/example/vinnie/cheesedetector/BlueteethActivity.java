package com.example.vinnie.cheesedetector;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Set;
import android.view.View;
import android.content.Intent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import java.util.List;
import java.util.Set;
import android.widget.EditText;
import android.os.Bundle;
import android.view.View;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothServerSocket;
import java.io.IOException;
import java.util.UUID;
import android.util.Log;
import android.widget.AdapterView;
import java.io.InputStream;
import java.io.Writer;
import android.util.Log;
import android.os.Handler;
public class BlueteethActivity extends AppCompatActivity {

//    public BlueteethActivity(BluetoothSocket mmSocket) {
//        this.mmSocket = mmSocket;
//    }

    public class thisBroadcastReceiver extends BroadcastReceiver{
        ArrayList found_device = new ArrayList();
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                found_device.add(device.getName() + "\n" + device.getAddress());
                adapter2= new ArrayAdapter(context,android.R.layout.simple_list_item_1, found_device);
                lv.setAdapter(adapter2);

            }
        }
    }
    //Button b1;
    //Button b2;
    public static BluetoothSocket mmSocket;
    private static BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
//    private ConnectedThread ct;
    private ConnectThread mConnectThread;
    Handler h;
    final int RECIEVE_MESSAGE = 1;

    UUID MY_UUID = UUID.fromString("1DED9CEC-D65B-4D26-81AB-24320BBA23B0");
    ArrayAdapter adapter2;
    String NAME = "cheese detector";
    ListView lv;
//    static TextView display;
    BluetoothDevice bluetoothDevice = null;
    //TextView message;
    //private StringBuilder sb = new StringBuilder();
    //private ConnectedThread ct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blueteeth);
        BA = BluetoothAdapter.getDefaultAdapter();
        lv = (ListView)findViewById(R.id.listView);

        adapter2 = new ArrayAdapter
                (this,android.R.layout.simple_list_item_1);
        lv.setAdapter(adapter2);
        //String MAC="B8:27:EB:75:EB:AD";
        //BA.getRemoteDevice(MAC);
        BA.setName("Cheese Detector");
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 1);
            //Toast.makeText(getApplicationContext(),"Turned on",Toast.LENGTH_LONG).show();
        }






//        mConnectThread.interrupt();
//



    }


    public synchronized void connect(View v){
        pairedDevices = BA.getBondedDevices();
        //ArrayList<String> devices = new ArrayList<>();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices)
                bluetoothDevice = bt;
        }
        mConnectThread = new ConnectThread(bluetoothDevice);

        mConnectThread.start();
        if(mmSocket!=null) {
            Toast.makeText(BlueteethActivity.this,
                    "Connected to Pi", Toast.LENGTH_LONG).show();
        }

    }
//    public synchronized void receive(View v){
//        View currentPage = (View) v.getParent();
//        display = (TextView)currentPage.findViewById(R.id.display);
//        ct = new ConnectedThread(mmSocket);
//
//        ct.start();
//        display.setText("hey");
//        ct.write("start");//start, sensor should start sensing process
//
//    }

//    public void cancel(View v) {//stop sensing and close socke
//        ct.cancel();
//    }
    public void ready(View view){
        Intent intent = new Intent(this, single.class);
        startActivity(intent);
    }
    public void off(View v){
        BA.disable();
        adapter2.clear();
        Toast.makeText(getApplicationContext(),"Turned off" ,Toast.LENGTH_LONG).show();
    }
    public void on(View v){
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 1);
            Toast.makeText(getApplicationContext(),"Turned on",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),"Already on",Toast.LENGTH_LONG).show();
        }
    }


    public void list(View v){
        pairedDevices = BA.getBondedDevices();
        ArrayList devices = new ArrayList();
        //Toast.makeText(getApplicationContext(),"Showing Paired Devices",Toast.LENGTH_SHORT).show();
        for(BluetoothDevice bt : pairedDevices)
            devices.add(bt.getName());


        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, devices);
        lv.setAdapter(adapter);
    }


    private class ConnectThread extends Thread {
        //private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        //---------------
//        private InputStream input;
//        private OutputStream output;
//


        //private OutputStream output;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final

            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = mmDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            System.out.println("not found");
            }
            mmSocket = tmp;
            //-------------
//            input=null;
//            output=null;
        }

        //OutputStream temOut = null;

        public void run() {
            // Cancel discovery because it will slow down the connection
            BA.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
                System.out.println("connect");
//                input=mmSocket.getInputStream();
//                output=mmSocket.getOutputStream();


            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }


        }

    }
//
////    //=============connectedthread
//    private class ConnectedThread extends Thread {
//        private final InputStream mmInStream;
//        private final OutputStream mmOutStream;
//        private final BluetoothSocket ss;
//        public ConnectedThread(BluetoothSocket socket) {
//            ss=socket;
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//
//
//            // Get the input and output streams, using temp objects because
//            // member streams are final
//            try {
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
//            } catch (IOException e) {
//            }
//
//            mmInStream = tmpIn;
//            mmOutStream = tmpOut;
//        }
//
//
//    public void run() {
//        byte[] buffer = new byte[1024];
//        int begin = 0;
//        int bytes = 0;
//        while (true) {
//            try {
//
//
//
//                    bytes = mmInStream.read(buffer);
//                    String readmsg = new String(buffer, 0, bytes);
//                System.out.print("From pi: ");
//                System.out.println(readmsg);
//
//                    display.setText(readmsg);
//            } catch (IOException e) {
//                break;
//            }
//        }
//    }
//        /* Call this from the main activity to send data to the remote device */
//        public void write(String message) {
//
//            byte[] msgBuffer = message.getBytes();
//            try {
//                mmOutStream.write(msgBuffer);
//            } catch (IOException e) {
//
//            }
//        }
//
//    public void cancel() {
//        try {
//            mmSocket.close();
//        } catch (IOException e) { }
//    }
//
//    }

//    static String temp;


    Handler mHandler = new myHandler();

    public static class myHandler extends Handler{
        @Override
        public void handleMessage(android.os.Message msg) {
            byte[] writeBuf = (byte[]) msg.obj;
            int begin = (int)msg.arg1;
            int end = (int)msg.arg2;

            switch(msg.what) {
                case 1:
                    String writeMessage = new String(writeBuf);
                    writeMessage = writeMessage.substring(begin, end);

                    //temp=writeMessage;
                    break;
            }
        }
    };








}

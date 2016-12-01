package com.example.vinnie.cheesedetector;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.view.View.OnClickListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    /*declare Blueteeth-------------------------------------*/


    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    static BluetoothSocket mmSocket=BlueteethActivity.mmSocket;
    static class myHandler extends Handler{
        public String mm;
        @Override

        public void handleMessage(Message msg) {
            String message = (String) msg.obj; //Extract the string from the Message
            //text.setText(message);
            //....
            mm=message;
        }
        public String getMsg(){
            return mm;
        }
    };
    static myHandler mHandler = new myHandler();

    //public static ConnectedThread ct = new ConnectedThread(BlueteethActivity.mmSocket);

//    Button b1;
//    Button b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        android.app.FragmentManager fm = getFragmentManager();
//        fm.findFragmentById(R.id.container);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);






    }


    ConnectedThread get_res;
    public synchronized void complete(View view){
        get_res = new ConnectedThread(BlueteethActivity.mmSocket);
        get_res.start();
        get_res.write("result");
        String h=mHandler.getMsg();
        Intent intent = new Intent(this, Result.class);
        intent.putExtra("id", h);
        startActivity(intent);
    }

    //    //=============connectedthread
    private static class ConnectedThread extends Thread {
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
            byte[] buffer = new byte[1024];
            int begin = 0;
            int bytes = 0;
            while (true) {
                try {



                    bytes = mmInStream.read(buffer);
                    String read = new String(buffer, 0, bytes);
                    Message msg=Message.obtain();
                    msg.obj=read;
                    msg.setTarget(mHandler);
                    msg.sendToTarget();
                    //System.out.print("send to targetFrom pi: ");
                    //System.out.println(read);

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

    }



//    public void bt(View view){
//        Intent intent = new Intent(this, BlueteethActivity.class);
//        startActivity(intent);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SAMPLE 1";
                case 1:
                    return "SAMPLE 2";
                case 2:
                    return "SAMPLE 3";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements OnClickListener{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        int ss;
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);


            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            if(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)).equals("Sample: 1")){
                Button b = (Button)rootView.findViewById(R.id.complete);
                b.setVisibility(View.GONE);
            }
            if(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)).equals("Sample: 2")){
                Button b = (Button)rootView.findViewById(R.id.complete);
                b.setVisibility(View.GONE);
            }

            Button receive = (Button) rootView.findViewById(R.id.receive);
            //TextView message = (TextView)rootView.findViewById(R.id.text);

            TextView text = (TextView)rootView.findViewById(R.id.text_2);
            try {
                String h = mHandler.getMsg();
                text.setText(h);
                System.out.println("got the mesg" + h);
            }catch(Exception e){ }



            final Button cancel = (Button) rootView.findViewById(R.id.stop);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view == cancel) {
                        //stop receiving data
                        //stop connected thread here,
                        if (mmSocket != null) {
                            ct.write("stop");
                        }
                    }
                }
            });
            receive.setOnClickListener(this);

            return rootView;
        }
        ConnectedThread ct;
        @Override
        public synchronized void onClick(View v) {
                View currentPage = (View) v.getParent();
                ct = new ConnectedThread(BlueteethActivity.mmSocket);
                ct.start();
                //message.setText("hey");
                
                ct.write("start");//start, sensor should start sensing process


        }




    }
}

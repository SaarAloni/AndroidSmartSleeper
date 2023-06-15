package com.example.atry.activities;

import static android.app.PendingIntent.FLAG_MUTABLE;
import static android.content.Context.ALARM_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atry.R;

import org.chromium.net.CronetEngine;
import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomePageFragment extends Fragment {

    private TextView welcomeTextView;
    private TextView alarmText;
    public static BluetoothSocket mmSocket;
    //    public static ConnectedThread connectedThread;
//    public static CreateConnectThread createConnectThread;

    private static final int LOCATION_PERMISSION_CODE = 121;
    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private BluetoothGatt mGatt;
    private Calendar cal_alarm ;

    private StopAlarm startLight;

    private String time_to_wake ;


    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("TAG", "This is a debug log message.");
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);


        welcomeTextView = view.findViewById(R.id.textViewWelcome);
        alarmText = view.findViewById(R.id.alarmText);

        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String s1 = sh.getString("email", "");
        welcomeTextView.setText("Welcome " + s1);



        Button setAlarm = view.findViewById(R.id.setAlarmButton);
        Button stopAlarm = view.findViewById(R.id.stopAlarmButton);
        Button testAlarm = view.findViewById(R.id.testButton);


        TextView day = view.findViewById(R.id.day);
        TextView action = view.findViewById(R.id.action);
        TextView hour = view.findViewById(R.id.hour);
        TextView date = view.findViewById(R.id.date);

        Executor executor = Executors.newSingleThreadExecutor();
        CronetEngine.Builder myBuilder = new CronetEngine.Builder(getContext());
        CronetEngine cronetEngine = myBuilder.build();

        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                "http://" + getString(R.string.ip) + ":5000/get_alarm?" +
                        "email=" + s1,
                new MyUrlRequestCallback(), executor);

        UrlRequest request = requestBuilder.build();
        request.start();

        stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                stopAlarm(getContext());
            }
        });

        testAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Date dat = new Date();
                Calendar cal_alarm = Calendar.getInstance();
                cal_alarm.setTime(dat);
                cal_alarm.add(Calendar.SECOND, 3);
                setAlarm(getActivity(), cal_alarm);
            }
        });

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Executor executor = Executors.newSingleThreadExecutor();
                CronetEngine.Builder myBuilder = new CronetEngine.Builder(getContext());
                CronetEngine cronetEngine = myBuilder.build();
                UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                        "http://" + getString(R.string.ip) + ":5000/set_alarm?" +
                                "email=" + s1 +
                                "&day=" + day.getText().toString() +
                                "&action=" + action.getText().toString() +
                                "&hour=" + hour.getText().toString() +
                                "&date=" + date.getText().toString(),
                        new MyUrlRequestCallback(), executor);

                UrlRequest request = requestBuilder.build();
                request.start();

            }
        });
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        getContext().registerReceiver(receiver, filter);

//        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
////            return TODO;
//        }
//        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//
//        List<String> s = new ArrayList<String>();
//        for(BluetoothDevice bt : pairedDevices) {
//            Log.d("TAG", "onCreateView: " + bt.getAddress());
//            Log.d("TAG", "onCreateView: " + bt.getName());
//            Log.d("TAG", "onCreateView: " + bt.getUuids()[0]);
//        }
//
//        mBluetoothAdapter.startDiscovery();


        mHandler = new Handler();
        if (!getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getContext(), "BLE Not Supported",
                    Toast.LENGTH_SHORT).show();
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        connectToBle();


//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        CreateConnectThread createConnectThread = new CreateConnectThread(bluetoothAdapter,
//                "88:25:83:F0:51:FC", getContext(), getActivity());
//        createConnectThread.start();

//        Date dat = new Date();
//        Calendar cal_alarm = Calendar.getInstance();
//        cal_alarm.setTime(dat);
//        cal_alarm.set(Calendar.HOUR_OF_DAY, 10);
//        cal_alarm.set(Calendar.MINUTE, 5);
//        cal_alarm.set(Calendar.SECOND, 0);
//        setAlarm(getActivity(), cal_alarm);


        return view;
    }

    private void startScan() {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();
                filters = new ArrayList<ScanFilter>();
            }
            scanLeDevice(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                //Bluetooth not enabled.
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT < 21) {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    } else {
                        mLEScanner.stopScan(mScanCallback);

                    }
                }
            }, SCAN_PERIOD);
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mLEScanner.startScan(filters, settings, mScanCallback);
            }
        } else {
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            } else {
                mLEScanner.stopScan(mScanCallback);
            }
        }
    }


    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.i("callbackType", String.valueOf(callbackType));

            if(result.getDevice().toString().equals("88:25:83:F0:51:FC")) {
                Log.i("result", result.toString());
                BluetoothDevice btDevice = result.getDevice();
                connectToDevice(btDevice);
            }

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                Log.i("ScanResult - Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("onLeScan", device.toString());
                            connectToDevice(device);
                        }
                    });
                }
            };

    public void connectToDevice(BluetoothDevice device) {
        if (mGatt == null) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mGatt = device.connectGatt(getContext(), false, gattCallback);
            scanLeDevice(false);// will stop after first device detection
        }
    }



    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("onConnectionStateChange", "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("gattCallback", "STATE_CONNECTED");
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e("gattCallback", "STATE_DISCONNECTED");
                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();
            Log.i("onServicesDiscovered", services.toString());
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            gatt.readCharacteristic(services.get(1).getCharacteristics().get(0));
//            startLight = new StartLight(gatt, services);
            Blueconnection blueconnection = Blueconnection.getInstance();
            blueconnection.setmGatt(mGatt);
            blueconnection.setmservice(services);
            if (cal_alarm != null) {
                setAlarm(getContext(),cal_alarm);
            }
//            for (BluetoothGattService service: services) {
//                BluetoothGattCharacteristic characteristic = service.getCharacteristics().get(0);
//                byte x = 0;
//                characteristic.setValue(String.valueOf(x));
//                boolean success = mGatt.writeCharacteristic(characteristic);
//            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic
                                                 characteristic, int status) {
            Log.i("onCharacteristicRead", characteristic.toString());
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            gatt.disconnect();
        }
    };



    private void checkLocationPermission() {
        if(isReadStorageAllowed()){
            //If permission is already having then showing the toast
            Toast.makeText(getContext(),"You already have the permission",Toast.LENGTH_LONG).show();
            //Existing the method with return
            startScan();
            return;
        }

        //If the app has not the permission then asking for the permission
        requestStoragePermission();
    }

    //Requesting permission
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION_CODE);
    }

    //We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == LOCATION_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startScan();
                //Displaying a toast
                Toast.makeText(getContext(),"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(getContext(),"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }


    public void connectToBle() {
        checkLocationPermission();
    }

    public void stopAlarm(Context context) {
        Intent intent = new Intent(getContext(), StopAlarm.class);
        intent.setAction("stop");
        intent.putExtra("time", "10000"); // ToDo get time of wake
        PendingIntent pendingIntent = null;
        boolean bool = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bool = (PendingIntent.getBroadcast(
                    getContext().getApplicationContext(), 234324243, intent, FLAG_MUTABLE)!= null);
            pendingIntent = PendingIntent.getBroadcast(
                    getContext().getApplicationContext(), 234324243, intent, FLAG_MUTABLE);
        } else {
            bool = (PendingIntent.getBroadcast(
                    getContext().getApplicationContext(), 234324243, intent, PendingIntent.FLAG_ONE_SHOT)!= null);
            pendingIntent = PendingIntent.getBroadcast(
                    getContext().getApplicationContext(), 234324243, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        if (bool) {
            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                    + (1000), pendingIntent);
        }
    }

    public void setAlarm(Context context, Calendar cal_alarm) {
        Intent intent = new Intent(getContext(), PlayMusic.class);
        intent.setAction("start");
        intent.putExtra("time", time_to_wake);
        PendingIntent pendingIntent = null;
        boolean bool = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bool = (PendingIntent.getBroadcast(
                    getContext().getApplicationContext(), 234324243, intent, FLAG_MUTABLE)!= null);
            pendingIntent = PendingIntent.getBroadcast(
                    getContext().getApplicationContext(), 234324243, intent, FLAG_MUTABLE);
        } else {
            bool = (PendingIntent.getBroadcast(
                    getContext().getApplicationContext(), 234324243, intent, PendingIntent.FLAG_ONE_SHOT)!= null);
            pendingIntent = PendingIntent.getBroadcast(
                    getContext().getApplicationContext(), 234324243, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        if (bool) {
            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
//            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
//                    + (3 * 1000), pendingIntent);
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pendingIntent);
        }


//        Intent myIntent = new Intent(getContext(), PlayMusic.class);
//        myIntent.setAction("stop");
//        PendingIntent pendingIntent2 = null;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            pendingIntent2 = PendingIntent.getBroadcast(
//                    getContext().getApplicationContext(), 234324243, myIntent, FLAG_MUTABLE);
//        } else {
//            pendingIntent2 = PendingIntent.getBroadcast(
//                    getContext().getApplicationContext(), 234324243, myIntent, PendingIntent.FLAG_ONE_SHOT);
//        }
//        AlarmManager alarmManager2 = (AlarmManager)tmp.split("[.]")[0] + tom getContext().getSystemService(ALARM_SERVICE);
//        alarmManager2.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
//                + (30 * 1000), pendingIntent2);
    }

//    public void setLight(Context context) {
//        Intent intent = new Intent(getContext(), StopAlarm.class);
//        Log.d("TAG", "setLight: here " );
//        intent.setAction("start");
//        intent.putExtra("time", "10000"); // ToDo get time of wake
//        PendingIntent pendingIntent = null;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            pendingIntent = PendingIntent.getBroadcast(
//                    getContext().getApplicationContext(), 234324243, intent, FLAG_MUTABLE);
//        } else {
//            pendingIntent = PendingIntent.getBroadcast(
//                    getContext().getApplicationContext(), 234324243, intent, PendingIntent.FLAG_ONE_SHOT);
//        }
//        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
//                + (2 * 1000), pendingIntent);
//
//    }

    public class MyUrlRequestCallback extends UrlRequest.Callback {
        private static final String TAG = "MyUrlRequestCallback";
        private Context context = null;

        @Override
        public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) {
            Log.i(TAG, "onRedirectReceived method called.");
            // You should call the request.followRedirect() method to continue
            // processing the request.
            request.followRedirect();
        }

        @Override
        public void onResponseStarted(UrlRequest request, UrlResponseInfo info) {
            Log.i(TAG, "onResponseStarted method called.");
            // You should call the request.read() method before the request can be
            // further processed. The following instruction provides a ByteBuffer object
            // with a capacity of 102400 bytes for the read() method. The same buffer
            // with data is passed to the onReadCompleted() method.
            request.read(ByteBuffer.allocateDirect(102400));
        }

        @Override
        public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) {
            Log.i(TAG, "onReadCompleted method called.");
            // You should keep reading the request until there's no more data.
            byteBuffer.clear();
            request.read(byteBuffer);
            String res = StandardCharsets.UTF_8.decode(byteBuffer).toString();
            Log.d("tag", res);
            String tmp = res.split("[,]", 0)[0];
            Log.d(TAG, "onReadCompleted: " + tmp);
            if (res.contains(":")) {

                alarmText.setText("Your timer was set to be at " +
                        tmp.split("[.]")[0] + ". Recommended time to go to sleep by the AI " + tmp.split("[.]")[2]);
                int time_too_wake = Integer.parseInt(tmp.split("[.]")[1]);
                time_to_wake = String.valueOf(time_too_wake*1000);
                Log.d(TAG, "onReadCompleted: " + time_too_wake);
                int hour = Integer.parseInt(tmp.split("[.]")[0].split("[:]")[0]);
                int minute = Integer.parseInt(tmp.split("[.]")[0].split("[:]")[1]);
                int seconds = Integer.parseInt(tmp.split("[.]")[0].split("[:]")[2]);
                Date dat = new Date();
                cal_alarm = Calendar.getInstance();
                cal_alarm.setTime(dat);
                if (hour < cal_alarm.get(Calendar.HOUR_OF_DAY)) {
                    cal_alarm.add(Calendar.HOUR_OF_DAY, 24);
                }
                if (hour == cal_alarm.get(Calendar.HOUR_OF_DAY)
                        && minute < cal_alarm.get(Calendar.MINUTE)) {
                    cal_alarm.add(Calendar.HOUR_OF_DAY, 24);
                }

                if (hour == cal_alarm.get(Calendar.HOUR_OF_DAY)
                        && minute == cal_alarm.get(Calendar.MINUTE)
                        && seconds < cal_alarm.get(Calendar.SECOND)) {
                    cal_alarm.add(Calendar.HOUR_OF_DAY, 24);
                }
                cal_alarm.add(Calendar.SECOND, -time_too_wake);
                cal_alarm.set(Calendar.HOUR_OF_DAY, hour);
                cal_alarm.set(Calendar.MINUTE, minute);
                cal_alarm.set(Calendar.SECOND, seconds);
                if(Blueconnection.getInstance().getmGatt() != null) {
                    setAlarm(getActivity(), cal_alarm);
                }

            }
            if (res.contains("not")) {
                alarmText.setText("The alarm was set off ");
            }
            if (res.contains("ok")) {
                Intent intent = new Intent(getContext(), HomePageActivity.class);
                startActivity(intent);
            }
        }

        @Override
        public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
            Log.i(TAG, "onSucceeded method called.");
        }

        @Override
        public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
            // The request has failed. If possible, handle the error.
            Log.e(TAG, "The request failed.", error);
        }

    }



}
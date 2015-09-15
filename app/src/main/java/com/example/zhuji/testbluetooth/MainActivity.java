package com.example.zhuji.testbluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.nfc.Tag;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MainActivity extends Activity {
    private  BluetoothService mBluetoothLeService;
    private String  TAG = "MainActivity";
    private BluetoothAdapter mBluetoothAdapter= null;
    private BluetoothCallback callback = new BluetoothCallback();
    private final MyBroadcastReceiver mGattUpdateReceiver = new MyBroadcastReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        Log.e(" test bluetooth ","ddddddddddddddddddddddddddddddddddddddd");
        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Log.e(" test bluetooth ","eeeeeeeeeeeeeeeeeeeeeeeeeee");
            finish();
            return;
        }
        handler.sendEmptyMessage(1);
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                mBluetoothLeService.startGatt();
            }

        });

    }

    private Timer mTimer ;
    private void setupAutoHeartBeat() {
        mTimer = new Timer();
        mTimer.schedule(new TimeScheduel(), 1000, 1000);
    }
    class TimeScheduel extends TimerTask {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Log.e(TAG, "&&&&& ...TimeScheduel");
            Message msg = mHandler.obtainMessage(1);
            msg.sendToTarget();
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                    mBluetoothLeService.autoSendHeart();
            }
        }
    };



    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
        Log.e(TAG, "onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }


    private  IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SampleGattAttributes.ACTION_GATT_FIRST_CONNECTED);
        intentFilter.addAction(SampleGattAttributes.ACTION_GATT_FIRST_DISCONNECTED);
        intentFilter.addAction(SampleGattAttributes.ACTION_GATT_FIRST_SERVICES_DISCOVERED);
        intentFilter.addAction(SampleGattAttributes.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(SampleGattAttributes.ACTION_FIRST_PRESSURE_SENSOR_DATA);
        intentFilter.addAction(SampleGattAttributes.ACTION_FIRST_POWER_DATA);
        return intentFilter;
    }

    private boolean flag = true;
    private Handler handler  = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if (callback.getBluetoothDevice() == null)
            {
                if (flag)
                {
                    Log.e(" test bluetooth ", "mBluetoothAdapter.startLeScan(callback);");
                    mBluetoothAdapter.startLeScan(callback);
                }
                else
                {
                    mBluetoothAdapter.stopLeScan(callback);
                    Log.e(" test bluetooth ", " mBluetoothAdapter.stopLeScan(callback);");
                }
                flag = !flag;
                this.sendEmptyMessageDelayed(1, 1000);

            }
            else
            {
                if (!flag)
                {
                    Log.e(" test bluetooth ", "mBluetoothAdapter.stopLeScan(callback);");
                    mBluetoothAdapter.stopLeScan(callback);
                }

                process();
            }
        }
    };

    public void process()
    {
        Intent intent = new Intent(this,BluetoothService.class);
        bindService(intent,mServiceConnection,BIND_AUTO_CREATE);
    }


    private final ServiceConnection mServiceConnection = new ServiceConnection()
    {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service)
        {
            Log.e(TAG, "onServiceConnected");
            mBluetoothLeService = ((BluetoothService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize())
            {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            mBluetoothLeService.connect(callback.getBluetoothDevice());
            Log.e(TAG, "success to initialize Bluetooth");
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
            Log.e(TAG, "onServiceDisconnected");
        }
    };

}

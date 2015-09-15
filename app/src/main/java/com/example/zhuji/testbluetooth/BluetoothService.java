package com.example.zhuji.testbluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.UUID;

public class BluetoothService extends Service {

    private final IBinder mBinder = new LocalBinder();
    private BluetoothManager  mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String TAG = "BluetoothService";
    private BluetoothGattObject  LeftDevice = new BluetoothGattObject(this);
    private BluetoothGattObject  RightDevice = new BluetoothGattObject(this);

    public class LocalBinder extends Binder
    {
        public BluetoothService getService()
        {
            return BluetoothService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        LeftDevice.close();
        return super.onUnbind(intent);
    }

    public boolean initialize()
    {
        if (mBluetoothManager == null)
        {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null)
            {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null)
        {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    public void connect(BluetoothDevice device)
    {
        LeftDevice.connect(device);
    }

    public void close()
    {
        LeftDevice.close();
    }

    public void disconnect(BluetoothGatt gatt)
    {
        if (mBluetoothAdapter == null )
        {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        LeftDevice.disconnect();
    }

    public void startGatt(){
       LeftDevice.startGatt();;
    }

    public void autoSendHeart() {
        LeftDevice.autoSendHeart();
    }

}

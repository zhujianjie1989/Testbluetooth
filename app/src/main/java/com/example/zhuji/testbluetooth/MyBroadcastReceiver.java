package com.example.zhuji.testbluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhuji on 2015/9/14.
 */
public class MyBroadcastReceiver extends BroadcastReceiver{
    private boolean mConnected =false;


    @Override
    public void onReceive(Context context, Intent intent)
    {
        final String action = intent.getAction();
        if (SampleGattAttributes.ACTION_GATT_FIRST_CONNECTED.equals(action))
        {
            mConnected = true;
        }
        else if (SampleGattAttributes.ACTION_GATT_FIRST_DISCONNECTED.equals(action))
        {
            mConnected = false;
        }
        else if (SampleGattAttributes.ACTION_GATT_FIRST_SERVICES_DISCOVERED.equals(action))
        {
           // displayGattServices(mBluetoothLeService.getSupportedGattServices(callback.getBluetoothDevice()));
        }
        else if (SampleGattAttributes.ACTION_FIRST_PRESSURE_SENSOR_DATA.equals(action))
        {
            displayPressureSensorData(intent.getStringExtra(SampleGattAttributes.EXTRA_DATA));
        }
        else if (SampleGattAttributes.ACTION_FIRST_POWER_DATA.equals(action))
        {
            displayPowerData(intent.getStringExtra(SampleGattAttributes.EXTRA_DATA));
        }
        else if (SampleGattAttributes.ACTION_DATA_AVAILABLE.equals(action))
        {
            displayData(intent.getStringExtra(SampleGattAttributes.EXTRA_DATA));
        }
    }

    private void displayData(String data) {
        if (data != null) {
            Log.e("displayData", data);
        }
    }
    public int[] sensorData = new int[8];
    private void displayPressureSensorData(String data) {
        String subdata;
        int iValue;
        if (data != null)
        {
            //mSensorUpData.setText(data);
            subdata = data.substring(15,17)+data.substring(12,14);
            iValue = Integer.parseInt(subdata, 16);
            if (iValue>2048)
            {
                sensorData[0] = 0;
            }
            else
            {
                sensorData[0] = 2048 - iValue;
            }

            subdata = data.substring(9,11)+data.substring(6,8);
            iValue = Integer.parseInt(subdata, 16);
            if (iValue>2048)
            {
                sensorData[1] = 0;
            }

            else
            {
                sensorData[1] = 2048 - iValue;
            }
            subdata = data.substring(21,23)+data.substring(18,20);
            iValue = Integer.parseInt(subdata, 16);
            if (iValue>2048)
            {
                sensorData[2] = 0;
            }
            else
            {
                sensorData[2] = 2048 - iValue;
            }
            subdata = data.substring(3,5)+data.substring(0,2);
            iValue = Integer.parseInt(subdata, 16);
            if (iValue>2048)
            {
                sensorData[3] = 0;
            }
            else
            {
                sensorData[3] = 2048 - iValue;
            }
            Log.e("PressureSensorData", "up = " + sensorData[0]+ "left = " + sensorData[1]+"right = " + sensorData[2]+" back = " + sensorData[3]);
        }
    }

    private void displayPowerData(String data) {
        String subdata;

        int iValue;
        if (data != null)
        {
            subdata = data.substring(3,5)+ data.substring(0,2);

            iValue = Integer.parseInt(subdata, 16);
            Log.e("displayPowerData",String.valueOf((double) iValue / 4095 * 1.25 / 3.9 * 13.9)+" V");
            subdata = data.substring(9,11)+data.substring(6,8);
            iValue = Integer.parseInt(subdata, 16);
            Log.e("displayPowerData", "&&&&& mVoltageData" + iValue);
            if (iValue<1000)
                Log.e("displayPowerData", "0.0000 V");
            else
                Log.e("displayPowerData", String.valueOf((double) iValue / 4095 * 1.25 / 3.0 * 13.0) + " V");
        }
    }


}

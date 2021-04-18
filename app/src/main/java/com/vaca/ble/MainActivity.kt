package com.vaca.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ParcelUuid
import android.provider.SyncStateContract
import android.util.Log
import java.util.*
private const val TAG = "ChatServer"
class MainActivity : AppCompatActivity() {
    val SERVICE_UUID: UUID = UUID.fromString("0000b81d-0000-1000-8000-00805f9b34fb")

    /**
     * UUID for the message
     */
    val MESSAGE_UUID: UUID = UUID.fromString("7db3e235-3608-41f3-a03c-955fcbd2ea4b")

    /**
     * UUID to confirm device connection
     */
    val CONFIRM_UUID: UUID = UUID.fromString("36d4dc5c-814b-4097-a5a6-b93b39085928")



    private var advertiser: BluetoothLeAdvertiser? = null



    private val adapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startAdvertisement()
    }











    private fun startAdvertisement() {
        advertiser = adapter.bluetoothLeAdvertiser
        Log.d(TAG, "startAdvertisement: with advertiser $advertiser")

        if (advertiseCallback == null) {
            advertiseCallback = DeviceAdvertiseCallback()
            advertiser?.startAdvertising(advertiseSettings, advertiseData, advertiseCallback)
        }
    }


    private var advertiseSettings: AdvertiseSettings = buildAdvertiseSettings()
    private fun buildAdvertiseSettings(): AdvertiseSettings {
        return AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .setTimeout(0)
                .build()
    }


    private var advertiseData: AdvertiseData = buildAdvertiseData()
    private fun buildAdvertiseData(): AdvertiseData {
        val dataBuilder = AdvertiseData.Builder()
                .addServiceUuid(ParcelUuid(SERVICE_UUID))
                .setIncludeDeviceName(true)


        val failureData = "qqqfuckme";
        dataBuilder.addServiceData(ParcelUuid(SERVICE_UUID), failureData.toByteArray());

        val failureData2 = "123";
        dataBuilder.addServiceData(ParcelUuid(SERVICE_UUID), failureData2.toByteArray());

        val failureData3 = "456";
        dataBuilder.addServiceData(ParcelUuid(SERVICE_UUID), failureData3.toByteArray());
        return dataBuilder.build()
    }



    private var advertiseCallback: AdvertiseCallback? = null
    private class DeviceAdvertiseCallback : AdvertiseCallback() {
        override fun onStartFailure(errorCode: Int) {
            super.onStartFailure(errorCode)
            // Send error state to display
            val errorMessage = "Advertise failed with error: $errorCode"
            Log.d(TAG, "Advertising failed")
            //_viewState.value = DeviceScanViewState.Error(errorMessage)
        }

        override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
            super.onStartSuccess(settingsInEffect)
            Log.d(TAG, "Advertising successfully started")
        }
    }




}
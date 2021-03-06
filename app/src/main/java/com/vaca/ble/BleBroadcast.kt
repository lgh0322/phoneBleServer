package com.vaca.ble

import android.app.Application
import android.bluetooth.*
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import android.os.ParcelUuid
import android.util.Log
import java.util.*

object BleBroadcast {
    private const val TAG = "ChatServer"


    val SERVICE_UUID: UUID = UUID.fromString("0000b81d-0000-1000-8000-00805f9b34fb")
    val MESSAGE_UUID: UUID = UUID.fromString("7db3e235-3608-41f3-a03c-955fcbd2ea4b")
    val CONFIRM_UUID: UUID = UUID.fromString("36d4dc5c-814b-4097-a5a6-b93b39085928")

    private var advertiser: BluetoothLeAdvertiser? = null
    private val adapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var gattServer: BluetoothGattServer? = null
    private var gattServerCallback: BluetoothGattServerCallback? = null
    private lateinit var bluetoothManager: BluetoothManager





    interface WriteUuidGet{
        fun getByteArray(b:ByteArray)
    }


    var writeUuidGet:WriteUuidGet?=null



    fun setupGattServer(app: Application) {
        bluetoothManager = app.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        gattServerCallback = GattServerCallback()

        gattServer = bluetoothManager.openGattServer(
            app,
            gattServerCallback
        ).apply {
            addService(setupGattService())
        }

        startAdvertisement()
    }


    private fun setupGattService(): BluetoothGattService {
        // Setup gatt service
        val service = BluetoothGattService(SERVICE_UUID, BluetoothGattService.SERVICE_TYPE_PRIMARY)
        // need to ensure that the property is writable and has the write permission
        val messageCharacteristic = BluetoothGattCharacteristic(
            MESSAGE_UUID,
            BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE ,
            BluetoothGattCharacteristic.PERMISSION_WRITE
        )
        service.addCharacteristic(messageCharacteristic)
        val confirmCharacteristic = BluetoothGattCharacteristic(
            CONFIRM_UUID,
            BluetoothGattCharacteristic.PROPERTY_NOTIFY,
            BluetoothGattCharacteristic.PERMISSION_WRITE
        )
        service.addCharacteristic(confirmCharacteristic)

        return service
    }


    class GattServerCallback : BluetoothGattServerCallback() {
        override fun onConnectionStateChange(device: BluetoothDevice, status: Int, newState: Int) {
            super.onConnectionStateChange(device, status, newState)
            val isSuccess = status == BluetoothGatt.GATT_SUCCESS
            val isConnected = newState == BluetoothProfile.STATE_CONNECTED
            Log.d(
                TAG,
                "onConnectionStateChange: Server $device ${device.name} success: $isSuccess connected: $isConnected"
            )
            if (isSuccess && isConnected) {

            } else {

            }
        }

        override fun onCharacteristicWriteRequest(
            device: BluetoothDevice,
            requestId: Int,
            characteristic: BluetoothGattCharacteristic,
            preparedWrite: Boolean,
            responseNeeded: Boolean,
            offset: Int,
            value: ByteArray?
        ) {
            super.onCharacteristicWriteRequest(
                device,
                requestId,
                characteristic,
                preparedWrite,
                responseNeeded,
                offset,
                value
            )
            if (characteristic.uuid == MESSAGE_UUID) {
                gattServer?.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, null)
               value?.run {
                   writeUuidGet?.getByteArray(this)
               }
                Log.d(TAG, "onCharacteristicWriteRequest: Have message: \"$len\"")

            }
            if (characteristic.uuid == CONFIRM_UUID) {
                gattServer?.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, null)
                val message = value?.toString(Charsets.UTF_8)
                Log.d(TAG, "onCharacteristicWriteRequest: 777Have message: \"$message\"")

            }
        }
    }


//----------------------------------------------------------advertiseMent


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
    var len=0

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
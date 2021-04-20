package com.vaca.ble.nordic


import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import no.nordicsemi.android.ble.data.Data
import no.nordicsemi.android.ble.observer.ConnectionObserver


class BleDataWorker {
    private var pool: ByteArray? = null
    private val fileChannel = Channel<Int>(Channel.CONFLATED)

    private val connectChannel = Channel<String>(Channel.CONFLATED)
    private var myBleDataManager: BleDataManager? = null
    private val dataScope = CoroutineScope(Dispatchers.IO)
    private val mutex = Mutex()


    var pkgTotal = 0;
    var currentPkg = 0;
    var fileData: ByteArray? = null
    var currentFileName = ""
    var result = 1;
    var currentFileSize = 0


    companion object {

        const val ER2_CMD_GET_INFO = 0xE1
        const val ER2_CMD_RT_DATA = 0x03
        const val ER2_CMD_VIBRATE_CONFIG = 0x00
        const val ER2_CMD_READ_FILE_LIST = 0xF1
        const val ER2_CMD_READ_FILE_START = 0xF2
        const val ER2_CMD_READ_FILE_DATA = 0xF3
        const val ER2_CMD_READ_FILE_END = 0xF4

    }

    data class FileProgress(
        var name: String = "",
        var progress: Int = 0,
        var success: Boolean = false,
    )

    private val comeData = object : BleDataManager.OnNotifyListener {
        override fun onNotify(device: BluetoothDevice?, data: Data?) {
            data?.value?.apply {

            }
            pool?.apply {

            }
        }

    }


    private val connectState = object : ConnectionObserver {
        override fun onDeviceConnecting(device: BluetoothDevice) {

        }

        override fun onDeviceConnected(device: BluetoothDevice) {


        }

        override fun onDeviceFailedToConnect(device: BluetoothDevice, reason: Int) {

        }

        override fun onDeviceReady(device: BluetoothDevice) {

        }

        override fun onDeviceDisconnecting(device: BluetoothDevice) {

        }

        override fun onDeviceDisconnected(device: BluetoothDevice, reason: Int) {

        }

    }




    private fun sendCmd(bs: ByteArray) {
        myBleDataManager?.sendCmd(bs)
    }


    fun initWorker(context: Context, bluetoothDevice: BluetoothDevice?) {
        myBleDataManager = BleDataManager(context)
        myBleDataManager?.setNotifyListener(comeData)
        myBleDataManager?.setConnectionObserver(connectState)
        bluetoothDevice?.let {
            myBleDataManager?.connect(it)
                ?.useAutoConnect(true)
                ?.timeout(10000)
                ?.retry(85, 100)
                ?.done {
                    Log.i("BLE", "连接成功了.>>.....>>>>")
                    dataScope.launch {
                        connectChannel.send("yes")
                    }
                }
                ?.enqueue()
        }
    }


    fun disconnect() {
        myBleDataManager?.disconnect()
        myBleDataManager?.close()
    }

    suspend fun waitConnect() {
        connectChannel.receive()
    }




}
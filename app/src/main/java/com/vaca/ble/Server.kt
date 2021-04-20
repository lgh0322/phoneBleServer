package com.vaca.ble

import android.app.Application
import android.bluetooth.*
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ParcelUuid
import android.provider.SyncStateContract
import android.util.Log
import android.view.View
import java.util.*




//需求， 向朋友展示一张图片
//可以从文件夹获取
//先rgb， 后jpeg


class Server : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BleBroadcast.setupGattServer(application)

    }




}
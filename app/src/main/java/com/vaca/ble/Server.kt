package com.vaca.ble

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


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
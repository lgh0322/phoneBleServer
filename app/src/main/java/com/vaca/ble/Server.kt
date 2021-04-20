package com.vaca.ble

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


//需求， 向朋友展示一张图片
//可以从文件夹获取
//先rgb， 后jpeg

//自由图片、视频、文件、聊天、还有好友哦， 还可以改蓝牙名
//还可以群聊



class Server : AppCompatActivity() {

    var len=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fuck:RGBBackGround=findViewById(R.id.wu)
        BleBroadcast.setupGattServer(application)

        BleBroadcast.writeUuidGet= object : BleBroadcast.WriteUuidGet{
            override fun getByteArray(b: ByteArray) {
                for(k in len until len+b.size){
                    fuck.fuck[k]=b[k-len]
                }
                fuck.invalidate()
                len+=b.size
            }

        }
    }


}
package com.vaca.ble

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)
    }

    fun server(view: View) {
        startActivity(Intent(this, Server::class.java))
    }

    fun nodic(view: View) {

    }
}
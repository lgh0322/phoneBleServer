package com.vaca.ble

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat


class RGBBackGround : View {

    var canvas: Canvas? = null
    private val wavePaint = Paint()
    private val bgPaint = Paint()


    var disp = false

    var drawFra: Int = 1

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
            context,
            attrs,
            defStyle
    ) {
        init()
    }

    private fun init() {
        wavePaint.apply {

            style = Paint.Style.STROKE
            strokeWidth = 5.0f
        }


    }


    lateinit var w: Rect
    var r = 0;
    var g = 0;
    var b = 0;
    val fuck = ByteArray(13068) {
        0.toByte()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (j in 0 until 66) {
            for (k in 0 until 66) {
                val base = (j * 66 + k) * 3
                canvas.drawPoint(k.toFloat(),
                        j.toFloat(),
                        Paint().apply {
                            color =
                                    Color.argb(255,
                                            fuck[base].toUByte().toInt(),
                                            fuck[base + 1].toUByte().toInt(),
                                            fuck[base + 2].toUByte().toInt())
                        })
            }
        }

    }

    override fun onMeasure(width: Int, height: Int) {
        setMeasuredDimension(66, 66)
    }

    private fun getColor(resource_id: Int): Int {
        return ContextCompat.getColor(context, resource_id)
    }
}
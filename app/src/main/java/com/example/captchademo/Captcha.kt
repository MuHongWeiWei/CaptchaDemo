package com.example.captchademo

import android.graphics.*
import java.lang.StringBuilder
import java.util.*

/**
 * Author: Wade
 * E-mail: tony91097@gmail.com
 * Date: 2021/9/22
 */

class Captcha private constructor() {
    companion object {
        val CHARS_NUMBER = arrayListOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        val CHARS_LETTER = arrayListOf(
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
            'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z'
        )
        val CHARS_ALL = arrayListOf(
            '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
            'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z'
        )

        val instance: Captcha by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { Captcha() }
    }

    /**
     * 驗證碼寬度
     */
    private var mWidth = 0

    /**
     * 驗證碼高度
     */
    private var mHeight = 0

    /**
     * 驗證碼背景顏色
     */
    private var mBackgroundColor = 0

    /**
     * 驗證碼長度
     */
    private var mLength = 0

    /**
     * 干擾線的數量
     */
    private var mLineNumber = 0

    /**
     * 驗證碼文字大小
     */
    private var mFontSize = 0

    /**
     * 字體左邊距
     */
    private var mFontPaddingLeft = 0

    /**
     * 字體左邊距範圍值
     */
    private var mFontPaddingLeftRange = 0

    /**
     * 字體上邊距
     */
    private var mFontPaddingTop = 0

    /**
     * 字體上邊距範圍值
     */
    private var mFontPaddingTopRange = 0

    /**
     * 圖片倍率
     */
    private var mScale = 0

    private var mType: TYPE
    private var mRandom: Random
    private lateinit var mCode: String

    sealed class TYPE {
        object NUMBER : TYPE()
        object LETTER : TYPE()
        object CHARS : TYPE()
    }

    init {
        mType = TYPE.CHARS
        mScale = 1
        mWidth = 200
        mHeight = 100
        mBackgroundColor = Color.WHITE
        mLength = 4
        mLineNumber = 5
        mFontSize = 50
        mFontPaddingLeft = 25
        mFontPaddingLeftRange = 20
        mFontPaddingTop = 60
        mFontPaddingTopRange = 20
        mRandom = Random()
    }

    fun setType(type: TYPE): Captcha {
        mType = type
        return instance
    }

    fun setScale(scale: Int): Captcha {
        mScale = scale
        return instance
    }

    fun setSize(width: Int, height: Int): Captcha {
        mWidth = width * mScale
        mHeight = height * mScale
        return instance
    }

    fun setBackgroundColor(color: Int): Captcha {
        mBackgroundColor = color
        return instance
    }

    fun setLength(length: Int): Captcha {
        mLength = length
        return instance
    }

    fun setLineNumber(number: Int): Captcha {
        mLineNumber = number
        return instance
    }

    fun setFontSize(size: Int): Captcha {
        mFontSize = size * mScale
        return instance
    }

    fun setFontPadding(paddingLeft: Int, paddingTop: Int): Captcha {
        mFontPaddingLeft = paddingLeft * mScale
        mFontPaddingTop = paddingTop * mScale
        return instance
    }

    fun setFontPadding(
        paddingLeft: Int,
        paddingLeftRange: Int,
        paddingTop: Int,
        paddingRange: Int
    ): Captcha {
        mFontPaddingLeft = paddingLeft * mScale
        mFontPaddingLeftRange = paddingLeftRange * mScale
        mFontPaddingTop = paddingTop * mScale
        mFontPaddingTopRange = paddingRange * mScale
        return instance
    }

    /**
     * 獲取驗證碼
     */
    fun getCode() = mCode

    fun create(): Bitmap {
        mCode = createCode()

        val bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(mBackgroundColor)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = mFontSize.toFloat()

        var fontPaddingLeft = 0
        for (code in mCode) {
            getRandomTextStyle(paint)
            fontPaddingLeft += getRandomFontPaddingLeft()
            canvas.drawText(
                code.toString(),
                fontPaddingLeft.toFloat(),
                getRandomFontPaddingTop().toFloat(),
                paint
            )
        }

        for (i in 0 until mLineNumber) {
            drawLine(canvas, paint)
        }

        canvas.save()
        canvas.restore()
        return bitmap
    }

    private fun drawLine(canvas: Canvas, paint: Paint) {
        val color = getRandomColor()
        val startX = mRandom.nextInt(mWidth)
        val startY = mRandom.nextInt(mHeight)
        val stopX = mRandom.nextInt(mWidth)
        val stopY = mRandom.nextInt(mHeight)
        paint.strokeWidth = 1f
        paint.color = color
        canvas.drawLine(startX.toFloat(), startY.toFloat(), stopX.toFloat(), stopY.toFloat(), paint)
    }

    private fun getRandomFontPaddingLeft() =
        mFontPaddingLeft + mRandom.nextInt(mFontPaddingLeftRange)

    private fun getRandomFontPaddingTop() = mFontPaddingTop + mRandom.nextInt(mFontPaddingTopRange)

    private fun getRandomTextStyle(paint: Paint) {
        val color = getRandomColor()
        paint.color = color
        paint.isFakeBoldText = mRandom.nextBoolean()
        var skewX = mRandom.nextInt(11) / 10
        skewX = if (mRandom.nextBoolean()) skewX else -skewX
        paint.textSkewX = skewX.toFloat()
    }

    private fun getRandomColor(): Int {
        val red = mRandom.nextInt(256)
        val green = mRandom.nextInt(256)
        val blue = mRandom.nextInt(256)
        return Color.rgb(red, green, blue)
    }

    private fun createCode(): String {
        val buffer = StringBuilder()
        when (mType) {
            TYPE.NUMBER -> {
                for (i in 0 until mLength) {
                    buffer.append(CHARS_NUMBER[mRandom.nextInt(CHARS_NUMBER.size)])
                }
            }
            TYPE.LETTER -> {
                for (i in 0 until mLength) {
                    buffer.append(CHARS_LETTER[mRandom.nextInt(CHARS_LETTER.size)])
                }
            }
            TYPE.CHARS -> {
                for (i in 0 until mLength) {
                    buffer.append(CHARS_ALL[mRandom.nextInt(CHARS_ALL.size)])
                }
            }
        }
        return buffer.toString()
    }
}
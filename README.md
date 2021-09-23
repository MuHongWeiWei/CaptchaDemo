# Android Captcha 防止機器人 生成隨機驗證碼

##### Android Captcha 防止機器人 生成隨機驗證碼，這個隨機驗證碼可以輕易的擋掉那些機器人來做一些事情，此方法是使用0~9和A~Z的所有組合，她可以有效的防止機器人攻擊你的伺服器，隨機驗證碼是你的好幫手來阻止伺服器被機器人攻破。

---

#### 文章目錄
<ol>
    <li><a href="#a">創建驗證碼類別</a></li>
    <li><a href="#b">畫面布局</a></li>
    <li><a href="#c">使用驗證碼</a></li>
    <li><a href="#d">程式範例</a></li>
    <li><a href="#e">效果展示</a></li>
	<li><a href="#f">GitHub</a></li>
</ol>

---

<a id="a"></a>
#### 1.創建驗證碼
```Kotlin
package com.example.test

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
```

<a id="b"></a>
#### 2.畫面布局
```XML
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.appcompat.widget.AppCompatImageView
        android:id="@+id/imageView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        app:layout_constraintStart_toStartOf="@+id/code"
        app:layout_constraintTop_toBottomOf="@+id/code"
        tools:srcCompat="@tools:sample/avatars" />

    <androidx.appcompat.widget.AppCompatEditText
        android:hint="請輸入驗證碼"
        android:maxLength="4"
        android:id="@+id/code"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:layout_marginTop="36dp"
        android:ems="10"
        android:inputType="textPersonName"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <androidx.appcompat.widget.AppCompatButton
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="驗證"
        android:onClick="validate"
        app:layout_constraintBottom_toBottomOf="@+id/code"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toEndOf="@+id/code"
        app:layout_constraintTop_toTopOf="@+id/code" />

    <androidx.appcompat.widget.AppCompatTextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="8dp"
        android:text="看不清楚可點擊圖片更換"
        android:textColor="@color/black"
        android:textSize="16sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toEndOf="@+id/imageView"
        app:layout_constraintTop_toTopOf="@+id/imageView" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

<a id="c"></a>
#### 3.使用驗證碼
```Kotlin
val mCaptcha = Captcha.instance
    .setType(Captcha.TYPE.CHARS)
    .setScale(2)
    .setSize(200, 100)
    .setBackgroundColor(Color.WHITE)
    .setLength(4)
    .setLineNumber(5)
    .setFontSize(50)
    .setFontPadding(25, 20, 60, 20)
```


<a id="d"></a>
#### 4.程式範例
```Kotlin
package com.example.test

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView

class MainActivity : AppCompatActivity() {

    private lateinit var captcha: AppCompatImageView
    private lateinit var code: AppCompatEditText
    private lateinit var mCaptcha: Captcha

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        captcha = findViewById(R.id.imageView)
        code = findViewById(R.id.code)

        createCaptcha()
    }

    private fun createCaptcha() {
        mCaptcha = Captcha.instance
            .setType(Captcha.TYPE.CHARS)
            .setScale(2)
            .setSize(200, 100)
            .setBackgroundColor(Color.WHITE)
            .setLength(4)
            .setLineNumber(5)
            .setFontSize(50)
            .setFontPadding(25, 20, 60, 20)

        captcha.setImageBitmap(mCaptcha.create())

        captcha.setOnClickListener {
            captcha.setImageBitmap(mCaptcha.create())
        }
    }

    fun validate(view: View) {
        if (mCaptcha.getCode().equals(code.text.toString(), true)) {
            Toast.makeText(this, "驗證成功", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "驗證失敗", Toast.LENGTH_SHORT).show()
        }
    }
}
```

<a id="e"></a>
#### 5.效果展示
<a href="https://badgameshow.com/fly/wp-content/uploads/2021/09/20210921_160746.gif"><img src="https://badgameshow.com/fly/wp-content/uploads/2021/09/20210921_160746.gif" width="30%"/></a>

<a id="f"></a>
#### 6.Github
[Android Captcha 防止機器人 生成隨機驗證碼 Github](https://github.com/MuHongWeiWei/CaptchaDemo "Github")

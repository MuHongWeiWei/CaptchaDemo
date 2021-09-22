package com.example.captchademo

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
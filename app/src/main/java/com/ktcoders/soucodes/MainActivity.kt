package com.ktcoders.soucodes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ktcoders.utils.MyExtensions.toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.toast("hi dude")

    }
}
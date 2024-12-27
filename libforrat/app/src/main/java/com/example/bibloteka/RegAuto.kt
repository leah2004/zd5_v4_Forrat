package com.example.bibloteka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class RegAuto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_auto)
        if (savedInstanceState == null) {
            // Set LoginFragment as the default fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, LoginFragment())
                .commit()
        }
    }
}
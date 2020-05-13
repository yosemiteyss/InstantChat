package com.yosemitedev.instantchat.ui.contact

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.yosemitedev.instantchat.InstantChat
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var contactComponent: ContactComponent

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        contactComponent = (application as InstantChat).appComponent
            .contactComponent()
            .create()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
}

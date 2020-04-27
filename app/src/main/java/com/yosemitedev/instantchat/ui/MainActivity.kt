package com.yosemitedev.instantchat.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.databinding.ActivityMainBinding
import com.yosemitedev.instantchat.utils.PreferencesHelper
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var prefsHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
}

package com.yosemitedev.instantchat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yosemitedev.instantchat.R
import dagger.android.support.DaggerAppCompatActivity

class SplashActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)
            .apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            }

        startActivity(intent)
        overridePendingTransition(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
        finish()
    }
}

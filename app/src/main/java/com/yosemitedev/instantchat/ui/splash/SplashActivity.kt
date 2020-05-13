package com.yosemitedev.instantchat.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yosemitedev.instantchat.InstantChat
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.ui.contact.MainActivity

class SplashActivity : AppCompatActivity() {

    lateinit var splashComponent: SplashComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        splashComponent = (application as InstantChat).appComponent.splashComponent().create()
        splashComponent.inject(this)
        super.onCreate(savedInstanceState)
        navigateToMain()
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
            .apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            }

        startActivity(intent)
        overridePendingTransition(
            R.anim.nav_default_enter_anim,
            R.anim.nav_default_exit_anim
        )
        finish()
    }
}

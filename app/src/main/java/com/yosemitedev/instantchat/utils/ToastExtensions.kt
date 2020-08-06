package com.yosemitedev.instantchat.utils

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Toast extension methods for activity and fragments
 */
fun Activity.shortToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.longToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Fragment.shortToast(message: String?) {
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.longToast(message: String?) {
    Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
}

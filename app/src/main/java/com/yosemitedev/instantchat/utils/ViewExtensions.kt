package com.yosemitedev.instantchat.utils

import android.widget.EditText

fun EditText.isEmpty(): Boolean {
    return this.text.toString().isEmpty()
}
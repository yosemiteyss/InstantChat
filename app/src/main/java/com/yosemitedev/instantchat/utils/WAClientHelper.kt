package com.yosemitedev.instantchat.utils

import android.content.Intent
import android.net.Uri
import javax.inject.Inject
import javax.inject.Singleton

private const val WHATSAPP_URL = "https://api.whatsapp.com"

@Singleton
class WAClientHelper @Inject constructor() {

    fun getIntent(
        client: WAClient,
        countryCode: String,
        phoneNum: String
    ): Intent {
        return Intent(Intent.ACTION_VIEW).apply {
            `package` = client.packageName
            data = Uri.parse("$WHATSAPP_URL/send?phone=$countryCode$phoneNum")
        }
    }

    fun getDefaultClient(): WAClient = WAClient.DEFAULT

    fun getClients(): List<WAClient> {
        return listOf(WAClient.DEFAULT, WAClient.BUSINESS)
    }
}

enum class WAClient(val packageName: String) {
    DEFAULT("com.whatsapp"),
    BUSINESS("com.whatsapp.w4b")
}
package com.yosemitedev.instantchat

import android.content.Intent
import android.net.Uri
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WAClientManager @Inject constructor() {

    fun getClients(): List<WAClient> {
        return listOf(
            WAClient.DEFAULT,
            WAClient.BUSINESS
        )
    }

    fun getDefaultClient(): WAClient {
        return WAClient.DEFAULT
    }

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

    companion object {
        private const val WHATSAPP_URL = "https://api.whatsapp.com"
    }
}

enum class WAClient(val packageName: String) {
    DEFAULT("com.whatsapp"),
    BUSINESS("com.whatsapp.w4b")
}
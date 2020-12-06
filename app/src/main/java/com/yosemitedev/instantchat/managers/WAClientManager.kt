package com.yosemitedev.instantchat.managers

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.managers.WAClient.BUSINESS
import com.yosemitedev.instantchat.managers.WAClient.DEFAULT
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WAClientManager @Inject constructor() {

    fun getClients(): List<WAClient> {
        return listOf(
            DEFAULT,
            BUSINESS
        )
    }

    fun getDefaultClient(): WAClient {
        return DEFAULT
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

fun WAClient.getTitle(context: Context): String {
    return when (this) {
        DEFAULT -> context.getString(R.string.waclient_default_title)
        BUSINESS -> context.getString(R.string.waclient_business_title)
    }
}

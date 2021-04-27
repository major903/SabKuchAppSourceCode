package vedam.subkuch.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast

object ShareUtils {
    @JvmStatic
    fun shareMessage(context: Context, message: String, packageName: String?) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"

            if (!packageName.isNullOrBlank()) {
                setPackage(packageName)
            }
        }
        try {
            context.startActivity(Intent.createChooser(sendIntent, "Choose one"))
        } catch (e: Exception) {
            Toast.makeText(context, "Whatsapp Not Installed", Toast.LENGTH_LONG).show()
        }
    }
}
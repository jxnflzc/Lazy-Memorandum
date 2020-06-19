package per.jxnflzc.memorandumkotlin.util

import android.content.Context
import android.content.Intent
import android.net.Uri

class UriUtil {
    companion object {
        fun openPageByUrl(context: Context, url: String) {
            val uri = Uri.parse(url)
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }
}
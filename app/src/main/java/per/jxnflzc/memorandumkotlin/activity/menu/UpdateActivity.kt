package per.jxnflzc.memorandumkotlin.activity.menu

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import kotlinx.android.synthetic.main.activity_update.*
import okio.Okio
import okio.buffer
import okio.source
import per.jxnflzc.memorandumkotlin.ActivityCollector
import per.jxnflzc.memorandumkotlin.R
import java.io.BufferedReader
import java.io.InputStreamReader

class UpdateActivity : AppCompatActivity() {
    companion object {
        fun activityStart(context: Context) {
            val intent = Intent(context, UpdateActivity::class.java).apply {   }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        ActivityCollector.addActivity(this)

        initUpdateInfo()
    }

    private fun initUpdateInfo() {
        val inputStream = resources.openRawResource(R.raw.updateinfo)
        val bufferedSource  = inputStream.source().buffer()
        val sb = bufferedSource.readUtf8()
        txtUpdate.text = Html.fromHtml(sb, Html.FROM_HTML_MODE_LEGACY)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }
}
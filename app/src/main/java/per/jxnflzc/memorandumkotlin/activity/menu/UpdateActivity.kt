package per.jxnflzc.memorandumkotlin.activity.menu

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import io.noties.markwon.Markwon
import io.noties.markwon.image.ImagesPlugin
import io.noties.markwon.image.data.DataUriSchemeHandler
import io.noties.markwon.image.network.NetworkSchemeHandler
import io.noties.markwon.image.network.OkHttpNetworkSchemeHandler
import kotlinx.android.synthetic.main.activity_update.*
import okio.Okio
import okio.buffer
import okio.source
import per.jxnflzc.memorandumkotlin.ActivityCollector
import per.jxnflzc.memorandumkotlin.BaseActivity
import per.jxnflzc.memorandumkotlin.R
import java.io.BufferedReader
import java.io.InputStreamReader

class UpdateActivity : BaseActivity() {
    companion object {
        fun activityStart(context: Context) {
            val intent = Intent(context, UpdateActivity::class.java).apply {   }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        initUpdateInfo()
    }

    private fun initUpdateInfo() {
        val inputStream = resources.openRawResource(R.raw.update_log)
        val bufferedSource  = inputStream.source().buffer()
        val sb = bufferedSource.readUtf8()
        val markdown = Markwon.builder(this)
            .usePlugin(ImagesPlugin.create{ plugin: ImagesPlugin ->
                    plugin.addSchemeHandler(OkHttpNetworkSchemeHandler.create());
            })
            .build()
        markdown.setMarkdown(txtUpdate, sb)
    }
}
package per.jxnflzc.memorandumkotlin.activity.menu

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.gson.JsonParser
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import okhttp3.*
import per.jxnflzc.memorandumkotlin.ActivityCollector
import per.jxnflzc.memorandumkotlin.BaseActivity
import per.jxnflzc.memorandumkotlin.BuildConfig
import per.jxnflzc.memorandumkotlin.R
import per.jxnflzc.memorandumkotlin.util.UpdateUtil
import per.jxnflzc.memorandumkotlin.util.UriUtil
import java.io.IOException
import java.util.concurrent.TimeUnit

class AboutActivity : BaseActivity() {
    companion object {
        fun activityStart(context: Context) {
            val intent = Intent(context, AboutActivity::class.java).apply {   }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val aboutPage = AboutPage(this)
            .isRTL(false)
            .setImage(R.drawable.a11)
            .setDescription(resources.getString(R.string.app_desc))
            .addGroup(resources.getString(R.string.app_info))
            .addItem(Element().setTitle("Version " + BuildConfig.VERSION_NAME).setOnClickListener { UpdateUtil.checkUpdate(this, UpdateUtil.DOWNLOAD) })
            .addItem(Element().setTitle(resources.getString(R.string.update_log)).setOnClickListener { UpdateActivity.activityStart(this) })
            .addItem(Element().setTitle(resources.getString(R.string.historical_version)).setOnClickListener { UriUtil.openPageByUrl(this, "http://fir.jxnflzc.cn/mt2c") })
            .addGroup(resources.getString(R.string.connect_with_me))
            .addWebsite("https://github.com/jxnflzc")
            .addEmail("245186727@qq.com")
            .addGitHub("jxnflzc/Lazy-Memorandum")
            .create()
        setContentView(aboutPage)
    }
}
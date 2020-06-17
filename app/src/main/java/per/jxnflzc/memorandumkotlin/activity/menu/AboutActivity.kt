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
import per.jxnflzc.memorandumkotlin.BuildConfig
import per.jxnflzc.memorandumkotlin.R
import java.io.IOException
import java.util.concurrent.TimeUnit

class AboutActivity : AppCompatActivity() {
    companion object {
        fun activityStart(context: Context) {
            val intent = Intent(context, AboutActivity::class.java).apply {   }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        ActivityCollector.addActivity(this)

        val aboutPage = AboutPage(this)
            .isRTL(false)
            .setImage(R.drawable.a11)
            .setDescription(resources.getString(R.string.app_desc))
            .addGroup(resources.getString(R.string.app_info))
            .addItem(Element().setTitle("Version " + BuildConfig.VERSION_NAME).setOnClickListener { checkVersion() })
            .addItem(Element().setTitle(resources.getString(R.string.update_log)).setOnClickListener { UpdateActivity.activityStart(this) })
            .addGroup(resources.getString(R.string.connect_with_me))
            .addEmail("245186727@qq.com")
            .addGitHub("https://github.com/jxnflzc")
            .create()
        setContentView(aboutPage)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

    private fun checkVersion() {
        try {
            val client = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .build()
            val request = Request.Builder()
                .url("https://api.github.com/repos/jxnflzc/Lazy-Memorandum/releases/latest")
                .get()
                .build()
            val context = this

            val call = client.newCall(request)
            call.enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Looper.prepare()
                    Toast.makeText(applicationContext, R.string.check_version_failed, Toast.LENGTH_SHORT).show()
                    Looper.loop()
                }

                override fun onResponse(call: Call, response: Response) {
                    val result = response.body?.string()
                    val data = JsonParser().parse(result).asJsonObject
                    val internetVersion = data.get("tag_name").asString

                    Looper.prepare()
                    if (internetVersion != BuildConfig.VERSION_NAME) {
                        AlertDialog.Builder(context).apply{
                            setTitle(R.string.find_update)
                            setMessage(R.string.find_update_confirm)
                            setPositiveButton(R.string.ok) { _, _ ->
                                val uri = Uri.parse("https://github.com/jxnflzc/Lazy-Memorandum/releases/latest")
                                startActivity(Intent(Intent.ACTION_VIEW, uri))
                            }
                            setNegativeButton(R.string.cancel, null)
                            create()
                            show()
                        }
                    } else {
                        Toast.makeText(applicationContext, R.string.version_is_newest, Toast.LENGTH_SHORT).show()
                    }
                    Looper.loop()
                }
            })
        } catch (e: IOException) {
            Looper.prepare()
            Toast.makeText(applicationContext, R.string.check_version_failed, Toast.LENGTH_SHORT).show()
            Looper.loop()
        }
    }
}
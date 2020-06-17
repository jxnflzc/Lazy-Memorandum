package per.jxnflzc.memorandumkotlin.activity.menu

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import per.jxnflzc.memorandumkotlin.ActivityCollector
import per.jxnflzc.memorandumkotlin.BuildConfig
import per.jxnflzc.memorandumkotlin.R

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
            .addItem(Element().setTitle("Version " + BuildConfig.VERSION_NAME))
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
}
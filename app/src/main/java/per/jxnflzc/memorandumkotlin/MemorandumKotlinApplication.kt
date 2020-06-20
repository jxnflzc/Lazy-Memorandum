package per.jxnflzc.memorandumkotlin

import android.app.Application
import android.content.Context
import org.litepal.LitePal
import per.jxnflzc.memorandumkotlin.util.LogUtil

class MemorandumKotlinApplication : Application() {
    companion object {
        lateinit var context: Context
        lateinit var logger: LogUtil
    }

    override fun onCreate() {
        super.onCreate()
        LitePal.initialize(this)
        context = applicationContext
        logger = LogUtil(LogUtil.DEBUG)
    }
}
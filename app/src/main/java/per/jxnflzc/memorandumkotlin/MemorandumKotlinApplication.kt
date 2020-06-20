package per.jxnflzc.memorandumkotlin

import android.app.Application
import android.content.Context
import org.litepal.LitePal

class MemorandumKotlinApplication : Application() {
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        LitePal.initialize(this)
        context = applicationContext
    }
}
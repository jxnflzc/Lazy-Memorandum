package per.jxnflzc.memorandumkotlin

import android.app.Application
import android.content.Context
import org.litepal.LitePal

class MemorandumKotlinApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LitePal.initialize(this)
    }
}
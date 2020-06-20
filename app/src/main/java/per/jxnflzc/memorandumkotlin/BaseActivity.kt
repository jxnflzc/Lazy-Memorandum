package per.jxnflzc.memorandumkotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import per.jxnflzc.memorandumkotlin.util.LogUtil

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
        LogUtil.d("BaseActivity", "ActivityCollector.addActivity($this)")
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
        LogUtil.d("BaseActivity", "ActivityCollector.removeActivity($this)")
    }
}
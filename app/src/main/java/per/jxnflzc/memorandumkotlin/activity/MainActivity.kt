package per.jxnflzc.memorandumkotlin.activity

import android.app.*
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.litepal.LitePal
import per.jxnflzc.memorandumkotlin.ActivityCollector
import per.jxnflzc.memorandumkotlin.R
import per.jxnflzc.memorandumkotlin.activity.menu.AboutActivity
import per.jxnflzc.memorandumkotlin.activity.menu.UpdateActivity
import per.jxnflzc.memorandumkotlin.adapter.MemorandumAdapter
import per.jxnflzc.memorandumkotlin.model.EditType
import per.jxnflzc.memorandumkotlin.model.Memorandum
import per.jxnflzc.memorandumkotlin.util.UpdateUtil

class MainActivity : AppCompatActivity() {
    private var memorandumList = ArrayList<Memorandum>()

    companion object {
        fun activityStart(context: Context) {
            val intent = Intent(context, MainActivity::class.java).apply {   }
            context.startActivity(intent)
        }
    }

    //生命周期Create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCollector.addActivity(this)
        LitePal.getDatabase()

        UpdateUtil.checkUpdate(this, UpdateUtil.NOTIFICATION)

        initMemorandumList()
        showMemorandumList()

        btnAdd.setOnClickListener{
            EditMemorandumActivity.activityStart(this, EditType.ADD, requestCode = 1)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            101-> {//1：memorandumList可能会改变，对应requestCode = 1，01：memorandumList发生改变
                Log.d("MainActivity", "initMemorandumList")
                initMemorandumList()
                showMemorandumList()
            }
        }
    }

    //生命周期Destroy
    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

    //创建菜单
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.itemSearch)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                initSearchMemorandumList(query)
                showMemorandumList()
                searchView.clearFocus()
                return false;
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false;
            }
        })
        searchView.setOnCloseListener {
            initMemorandumList()
            showMemorandumList()
            searchView.clearFocus()

            true
        }

        return true
    }

    //菜单按钮选择
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemHelp -> UpdateActivity.activityStart(this)
            R.id.itemAbout -> AboutActivity.activityStart(this)
        }
        return true
    }

    //初始化备忘录列表
    private fun initMemorandumList() {
        memorandumList.clear()//清空所有内容

        memorandumList = LitePal.order("date desc").find(Memorandum::class.java) as ArrayList<Memorandum>
    }

    //初始化搜索出的备忘录列表
    private fun initSearchMemorandumList(query: String) {
        memorandumList.clear()//清空所有内容

        memorandumList = LitePal.where("title like ? or content like ?", "%$query%", "%$query%").order("date desc").find(Memorandum::class.java) as ArrayList<Memorandum>
    }

    //显示备忘录列表
    private fun showMemorandumList() {
        val layoutManager = LinearLayoutManager(this)
        listMemorandum.layoutManager = layoutManager
        val adapter = MemorandumAdapter(memorandumList)
        val context = this

        adapter.setOnRemoveListener(object: MemorandumAdapter.OnRemoveListener {
            override fun onRemove(position: Int) {
                AlertDialog.Builder(context).apply{
                    setTitle(R.string.delete)
                    setMessage(R.string.delete_confirm)
                    setPositiveButton(R.string.ok) { _, _ ->
                        val result = LitePal.delete(Memorandum::class.java, memorandumList[position].id)
                        if (result > 0) {
                            memorandumList.removeAt(position)
                            adapter.notifyDataSetChanged()
                            Toast.makeText(applicationContext, R.string.deleteSuccessful, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, R.string.deleteFailed, Toast.LENGTH_SHORT).show()
                        }
                    }
                    setNegativeButton(R.string.cancel, null)
                    create()
                    show()
                }
            }
        })
        
        listMemorandum.adapter = adapter
    }
}
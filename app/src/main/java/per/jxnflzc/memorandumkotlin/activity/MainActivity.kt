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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.litepal.LitePal
import per.jxnflzc.memorandumkotlin.ActivityCollector
import per.jxnflzc.memorandumkotlin.BaseActivity
import per.jxnflzc.memorandumkotlin.MemorandumKotlinApplication
import per.jxnflzc.memorandumkotlin.MemorandumKotlinApplication.Companion.logger
import per.jxnflzc.memorandumkotlin.R
import per.jxnflzc.memorandumkotlin.activity.menu.AboutActivity
import per.jxnflzc.memorandumkotlin.activity.menu.UpdateActivity
import per.jxnflzc.memorandumkotlin.adapter.MemorandumAdapter
import per.jxnflzc.memorandumkotlin.model.EditType
import per.jxnflzc.memorandumkotlin.model.Memorandum
import per.jxnflzc.memorandumkotlin.util.LogUtil
import per.jxnflzc.memorandumkotlin.util.UpdateUtil
import per.jxnflzc.memorandumkotlin.viewmodel.MainViewModel

class MainActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel

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
        //LitePal.deleteAll(Memorandum::class.java)
        LitePal.getDatabase()

        UpdateUtil.checkUpdate(this, UpdateUtil.NOTIFICATION)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.memorandumList.observe(this, Observer {
            showMemorandumList(it)
        })

        btnAdd.setOnClickListener{
            EditMemorandumActivity.activityStart(this, EditType.ADD, requestCode = 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            101-> {//1：memorandumList可能会改变，对应requestCode = 1，01：memorandumList发生改变
                logger.d("MainActivity", "initMemorandumList")
                viewModel.initMemorandum()
            }
        }
    }

    //创建菜单
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.itemSearch)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchMemorandum(query)
                searchView.clearFocus()
                return false;
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false;
            }
        })
        searchView.setOnCloseListener {
            viewModel.initMemorandum()
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

    //显示备忘录列表
    private fun showMemorandumList(memorandumList: ArrayList<Memorandum>) {
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
                        val result = viewModel.delete(position)
                        if (result > 0) {
                            viewModel.remove(position)
                            adapter.notifyDataSetChanged()
                            Toast.makeText(MemorandumKotlinApplication.context, R.string.deleteSuccessful, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(MemorandumKotlinApplication.context, R.string.deleteFailed, Toast.LENGTH_SHORT).show()
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
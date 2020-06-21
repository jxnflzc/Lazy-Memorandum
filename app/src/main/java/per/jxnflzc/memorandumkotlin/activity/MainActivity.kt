package per.jxnflzc.memorandumkotlin.activity

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.litepal.LitePal
import per.jxnflzc.memorandumkotlin.BaseActivity
import per.jxnflzc.memorandumkotlin.MemorandumKotlinApplication
import per.jxnflzc.memorandumkotlin.MemorandumKotlinApplication.Companion.logger
import per.jxnflzc.memorandumkotlin.R
import per.jxnflzc.memorandumkotlin.activity.edit.EditCatalogActivity
import per.jxnflzc.memorandumkotlin.activity.edit.EditMemorandumActivity
import per.jxnflzc.memorandumkotlin.activity.menu.AboutActivity
import per.jxnflzc.memorandumkotlin.activity.menu.UpdateActivity
import per.jxnflzc.memorandumkotlin.adapter.CatalogAdapter
import per.jxnflzc.memorandumkotlin.adapter.MemorandumAdapter
import per.jxnflzc.memorandumkotlin.model.Catalog
import per.jxnflzc.memorandumkotlin.model.EditType
import per.jxnflzc.memorandumkotlin.model.Memorandum
import per.jxnflzc.memorandumkotlin.util.UpdateUtil
import per.jxnflzc.memorandumkotlin.viewmodel.MainViewModel
import java.util.*
import kotlin.collections.ArrayList

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
        initDataBase()

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        UpdateUtil.checkUpdate(this, UpdateUtil.NOTIFICATION)

        //LitePal.deleteAll(Memorandum::class.java)
        /*for (i in 1..3) {
            val m = Memorandum()
            m.title = "t $i"
            m.content = "c $i"
            m.date = Date()
            m.catalogId = 2
            m.save()
        }*/

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.memorandumList.observe(this, Observer {
            showMemorandumList(it)
        })
        viewModel.catalogList.observe(this, Observer {
            showMCatalogList(it)
        })

        btnAddMemorandum.setOnClickListener{
            EditMemorandumActivity.activityStart(this, EditType.ADD, requestCode = 1)
        }
        btnAddCatalog.setOnClickListener {
            EditCatalogActivity.activityStart(this, EditType.ADD, requestCode = 2)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            101-> {//1：memorandumList可能会改变，对应requestCode = 1，01：memorandumList发生改变
                logger.d("MainActivity", "initMemorandumList")
                viewModel.initMemorandum()
            }
            201-> {//2：catalogList可能会改变，对应requestCode = 1，01：catalogList发生改变
                logger.d("MainActivity", "initCatalogList")
                viewModel.initCatalog()
            }
            202-> {//2：catalogList和memorandumList可能会改变，对应requestCode = 2，01：catalogList和memorandumList发生改变
                logger.d("MainActivity", "initMemorandumList & initCatalogList")
                viewModel.initMemorandum()
                viewModel.initCatalog()
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
            android.R.id.home -> dr.openDrawer(GravityCompat.START)
        }
        return true
    }

    private fun initDataBase() {
        LitePal.getDatabase()
        val sp = getSharedPreferences("database", Context.MODE_PRIVATE)
        val catalogInit = sp.getBoolean("catalog_init", false)
        val editor = sp.edit()
        if (!catalogInit) {
            val catalog = Catalog()
            catalog.save()
            editor.putBoolean("catalog_init", true)
            editor.apply()
        }
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
                        val result = viewModel.deleteMemorandum(position)
                        if (result > 0) {
                            viewModel.removeMemorandum(position)
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

    //显示备忘录列表
    private fun showMCatalogList(catalogList: ArrayList<Catalog>) {
        val layoutManager = LinearLayoutManager(this)
        listCatalog.layoutManager = layoutManager
        val adapter = CatalogAdapter(catalogList)



        adapter.setOnSearchListener(object: CatalogAdapter.OnSearchListener {
            override fun onSearch(catalogId: Long) {
                viewModel.searchMemorandumByCatalogId(catalogId)
            }
        })

        listCatalog.adapter = adapter
    }
}
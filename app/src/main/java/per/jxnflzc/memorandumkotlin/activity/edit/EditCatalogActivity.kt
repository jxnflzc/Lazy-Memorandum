package per.jxnflzc.memorandumkotlin.activity.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_edit_catalog.*
import per.jxnflzc.memorandumkotlin.BaseActivity
import per.jxnflzc.memorandumkotlin.MemorandumKotlinApplication
import per.jxnflzc.memorandumkotlin.R
import per.jxnflzc.memorandumkotlin.factory.EditCatalogViewModelFactory
import per.jxnflzc.memorandumkotlin.model.Catalog
import per.jxnflzc.memorandumkotlin.model.EditType
import per.jxnflzc.memorandumkotlin.viewmodel.edit.EditCatalogViewModel

class EditCatalogActivity : BaseActivity(), View.OnClickListener {
    private var type = EditType.ADD//编辑页面的类型，默认为ADD（添加）
    private lateinit var viewModel: EditCatalogViewModel

    companion object {
        fun activityStart(context: Context, type: EditType, catalog: Catalog = Catalog(), requestCode: Int = 0) {
            val intent = Intent(context, EditCatalogActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable("type", type)
            bundle.putSerializable("catalog", catalog)
            intent.putExtras(bundle)
            if (context is Activity) {
                context.startActivityForResult(intent, requestCode)
            } else {
                context.startActivity(intent)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_catalog)

        val intent = intent
        val bundle = intent.extras
        type = bundle?.getSerializable("type") as EditType
        val catalog = bundle.getSerializable("catalog") as Catalog
        viewModel = ViewModelProvider(this, EditCatalogViewModelFactory(catalog)).get(EditCatalogViewModel::class.java)
        viewModel.catalog.observe(this, Observer {
            showCatalog(it)
        })

        Toast.makeText(MemorandumKotlinApplication.context, "${catalog.id}", Toast.LENGTH_SHORT).show()
        initListener()

        when (type) {
            EditType.EDIT -> {
                btnSave.setText(R.string.change)
                if (catalog.id == 0L || catalog.id == 1L) {
                    btnDelete.visibility = View.GONE
                }
            }
            EditType.ADD -> {
                btnDelete.visibility = View.GONE
            }
        }
    }

    private fun showCatalog(catalog: Catalog) {
        edtName.setText(catalog.name)
    }

    private fun initListener() {
        btnSave.setOnClickListener(this)
        btnDelete.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnSave -> {
                when (type) {
                    EditType.EDIT -> changeCatalog()
                    EditType.ADD -> addCatalog()
                }
            }
            R.id.btnDelete -> deleteCatalog()
        }
    }

    private fun validate(): Boolean {
        if (edtName.text.toString().isBlank()) {
            edtName.error = resources.getString(R.string.validateTitleFailed)
            return false
        }
        return true
    }

    private fun addCatalog() {
        if (validate()) {
            viewModel.setCatalog(edtName.text.toString())

            if (viewModel.save()) {
                setResult(201)
                Toast.makeText(MemorandumKotlinApplication.context, R.string.saveSuccessful, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(MemorandumKotlinApplication.context, R.string.saveFailed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun changeCatalog() {
        if (validate()){
            viewModel.setCatalog(edtName.text.toString())

            if (viewModel.update() > 0) {
                setResult(202)
                Toast.makeText(MemorandumKotlinApplication.context, R.string.changeSuccessful, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(MemorandumKotlinApplication.context, R.string.changeFailed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteCatalog() {
        AlertDialog.Builder(this).apply{
            setTitle(R.string.delete)
            setMessage(R.string.delete_catalog_confirm)
            setPositiveButton(R.string.ok) { _, _ ->
                val result = viewModel.delete()
                if (result > 0) {
                    setResult(202)
                    Toast.makeText(MemorandumKotlinApplication.context, R.string.deleteSuccessful, Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(MemorandumKotlinApplication.context, R.string.deleteFailed, Toast.LENGTH_SHORT).show()
                }
            }
            setNegativeButton(R.string.cancel, null)
            create()
            show()
        }
    }
}
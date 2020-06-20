package per.jxnflzc.memorandumkotlin.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_edit_memorandum.*
import org.litepal.LitePal
import org.litepal.crud.LitePalSupport
import per.jxnflzc.memorandumkotlin.ActivityCollector
import per.jxnflzc.memorandumkotlin.BaseActivity
import per.jxnflzc.memorandumkotlin.MemorandumKotlinApplication
import per.jxnflzc.memorandumkotlin.R
import per.jxnflzc.memorandumkotlin.extend.showDateInfo
import per.jxnflzc.memorandumkotlin.extend.toSimpleString
import per.jxnflzc.memorandumkotlin.model.EditType
import per.jxnflzc.memorandumkotlin.model.Memorandum
import per.jxnflzc.memorandumkotlin.viewmodel.EditViewModel
import per.jxnflzc.memorandumkotlin.viewmodel.MainViewModel
import java.util.*
import kotlin.properties.Delegates

class EditMemorandumActivity : BaseActivity(), View.OnClickListener {
    private var type = EditType.ADD//编辑页面的类型，默认为ADD（添加）
    private val maxNum = 50000//笔记内容的最大字数
    private lateinit var viewModel: EditViewModel

    companion object {
        fun activityStart(context: Context, type: EditType, memorandum: Memorandum = Memorandum(), requestCode: Int = 0) {
            val intent = Intent(context, EditMemorandumActivity::class.java).apply {   }
            val bundle = Bundle()
            bundle.putSerializable("type", type)
            bundle.putSerializable("memorandum", memorandum)
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
        setContentView(R.layout.activity_edit_memorandum)
        ActivityCollector.addActivity(this)

        initListener()
        txtWordNum.text = "0"
        viewModel = ViewModelProvider(this).get(EditViewModel::class.java)

        val intent = intent
        val bundle = intent.extras
        type = bundle?.getSerializable("type") as EditType
        viewModel.memorandum.value = bundle.getSerializable("memorandum") as Memorandum

        when (type) {
            EditType.EDIT -> {
                showMemorandum(viewModel.memorandum.value!!)
            }
            EditType.ADD -> {
                txtWordNum.text = "0/$maxNum"
                btnDelete.visibility = View.GONE
                txtDate.visibility = View.GONE
            }
        }
    }

    private fun showMemorandum(memorandum: Memorandum) {
        edtTitle.setText(memorandum.title)
        edtContent.setText(memorandum.content)
        txtDate.text = memorandum.date.toSimpleString()
        btnSave.setText(R.string.change)
    }

    private fun initListener(){
        btnSave.setOnClickListener(this)
        btnDelete.setOnClickListener(this)

        edtContent.addTextChangedListener(object:TextWatcher{
            var word: CharSequence? = ""

            var start by Delegates.notNull<Int>()
            var end by Delegates.notNull<Int>()

            override fun beforeTextChanged(s: CharSequence?, start: Int, edn: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, edn: Int, after: Int) {
                word = s
            }

            @SuppressLint("StringFormatInvalid")
            override fun afterTextChanged(s: Editable?) {
                txtWordNum.text = String.format("${s?.length.toString()}/$maxNum")
                start = edtContent.selectionStart
                end = edtContent.selectionEnd
                if (s?.length!! > maxNum) {
                    s.delete(start - 1, end)
                    val endPosition = end
                    edtContent.text = s
                    edtContent.setSelection(endPosition)
                    Toast.makeText(MemorandumKotlinApplication.context, getString(R.string.more_than_words, maxNum), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnSave -> {
                when (type) {
                    EditType.EDIT -> changeMemorandum()
                    EditType.ADD -> addMemorandum()
                }
            }
            R.id.btnDelete -> deleteMemorandum()
        }
    }

    private fun validate(): Boolean {
        return (validateTitle() && validateContent())
    }

    private fun validateTitle(): Boolean {
        if (edtTitle.text.toString().isBlank()) {
            edtTitle.error = resources.getString(R.string.validateTitleFailed)
            return false
        }
        return true
    }

    private fun validateContent(): Boolean {
        if (edtContent.text.toString().isBlank()) {
            Toast.makeText(MemorandumKotlinApplication.context, R.string.validateContentFailed, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun changeMemorandum() {
        if (validate()){
            viewModel.memorandum.value?.title = edtTitle.text.toString()
            viewModel.memorandum.value?.content = edtContent.text.toString()
            viewModel.memorandum.value?.date = Date()

            if (viewModel.memorandum.value?.id?.let { viewModel.memorandum.value?.update(it) }!! > 0) {
                setResult(101)
                Toast.makeText(MemorandumKotlinApplication.context, R.string.changeSuccessful, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(MemorandumKotlinApplication.context, R.string.changeFailed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteMemorandum() {
        AlertDialog.Builder(this).apply{
            setTitle(R.string.delete)
            setMessage(R.string.delete_confirm)
            setPositiveButton(R.string.ok) { _, _ ->

                val result =
                    viewModel.memorandum.value?.id?.let {
                        LitePal.delete(Memorandum::class.java,
                            it
                        )
                    }
                if (result ?: 0 > 0) {
                    setResult(101)
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

    private fun addMemorandum() {
        if (validate()){
            viewModel.memorandum.value = Memorandum(edtTitle.text.toString(), edtContent.text.toString())

            if (viewModel.memorandum.value?.save()!!) {
                setResult(101)
                Toast.makeText(MemorandumKotlinApplication.context, R.string.saveSuccessful, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(MemorandumKotlinApplication.context, R.string.saveFailed, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
package per.jxnflzc.memorandumkotlin.viewmodel.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import per.jxnflzc.memorandumkotlin.MemorandumKotlinApplication
import per.jxnflzc.memorandumkotlin.MemorandumKotlinApplication.Companion.logger
import per.jxnflzc.memorandumkotlin.R
import per.jxnflzc.memorandumkotlin.extend.toIdString
import per.jxnflzc.memorandumkotlin.model.Catalog
import per.jxnflzc.memorandumkotlin.model.Memorandum
import per.jxnflzc.memorandumkotlin.service.impl.CatalogServiceImpl
import per.jxnflzc.memorandumkotlin.service.impl.MemorandumServiceImpl
import java.util.*

class EditMemorandumViewModel (memorandum: Memorandum) : ViewModel() {
    var memorandum = MutableLiveData<Memorandum>()

    val catalogList = MutableLiveData<ArrayList<Catalog>>()

    private val memorandumService = MemorandumServiceImpl()

    private val catalogService = CatalogServiceImpl()

    init {
        logger.d("EditViewModel", "init")
        this.memorandum.value = memorandum
        initCatalog()
    }

    fun setMemorandum(title: String, content: String) {
        memorandum.value?.title = title
        memorandum.value?.content = content
        memorandum.value?.date = Date()
    }

    fun setMemorandumCatalog(catalogId: Long) {
        memorandum.value?.catalogId = catalogId
    }

    fun delete(): Int {
        return memorandumService.delete(memorandum.value?.id!!)
    }

    fun update(): Int {
        return memorandumService.update(memorandum.value!!)
    }

    fun save(): Boolean {
        return memorandumService.save(memorandum.value!!)
    }

    private fun initCatalog() {
        catalogList.value = catalogService.getAllCatalog()
    }

    fun getCatalogById(id: Long): Catalog {
        return catalogService.getCatalog(id)
    }
}
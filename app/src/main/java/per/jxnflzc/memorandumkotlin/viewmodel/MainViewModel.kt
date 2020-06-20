package per.jxnflzc.memorandumkotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import per.jxnflzc.memorandumkotlin.model.Catalog
import per.jxnflzc.memorandumkotlin.model.Memorandum
import per.jxnflzc.memorandumkotlin.service.impl.CatalogServiceImpl
import per.jxnflzc.memorandumkotlin.service.impl.MemorandumServiceImpl

class MainViewModel() : ViewModel() {
    val memorandumList = MutableLiveData<ArrayList<Memorandum>>()

    val catalogList = MutableLiveData<ArrayList<Catalog>>()

    private val memorandumService = MemorandumServiceImpl()

    private val catalogService = CatalogServiceImpl()

    init {
        initMemorandum()
        initCatalog()
    }

    fun initMemorandum() {
        memorandumList.value = memorandumService.getAllMemorandum()
    }

    fun searchMemorandum(query: String) {
        memorandumList.value = memorandumService.getSearchMemorandum(query)
    }

    fun searchMemorandumByCatalogId(catalogId: Long) {
        memorandumList.value = if (catalogId == 0L) {
            memorandumService.getAllMemorandum()
        } else {
            memorandumService.getMemorandumByCatalog(catalogId)
        }

    }

    fun deleteMemorandum(position: Int): Int {
        return memorandumService.delete(memorandumList.value?.get(position)?.id!!)
    }

    fun removeMemorandum(position: Int) {
        memorandumList.value?.removeAt(position)
    }

    private fun initCatalog() {
        catalogList.value = catalogService.getAllCatalog()
        val all = Catalog("全部")
        all.id = 0
        catalogList.value?.add(0, all)
    }
}
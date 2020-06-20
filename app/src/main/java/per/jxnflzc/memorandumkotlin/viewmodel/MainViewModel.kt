package per.jxnflzc.memorandumkotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import per.jxnflzc.memorandumkotlin.model.Memorandum
import per.jxnflzc.memorandumkotlin.service.impl.MemorandumServiceImpl

class MainViewModel() : ViewModel() {
    val memorandumList = MutableLiveData<ArrayList<Memorandum>>()

    private val memorandumService = MemorandumServiceImpl()

    fun initMemorandum() {
        memorandumList.value = memorandumService.getAllMemorandum()
    }

    fun searchMemorandum(query: String) {
        memorandumList.value = memorandumService.getSearchMemorandum(query)
    }

    fun delete(position: Int): Int {
        return memorandumService.delete(memorandumList.value?.get(position)?.id!!)
    }

    fun remove(position: Int) {
        memorandumList.value?.removeAt(position)
    }
}
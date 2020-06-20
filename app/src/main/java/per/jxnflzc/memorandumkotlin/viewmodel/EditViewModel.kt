package per.jxnflzc.memorandumkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import per.jxnflzc.memorandumkotlin.MemorandumKotlinApplication.Companion.logger
import per.jxnflzc.memorandumkotlin.extend.toIdString
import per.jxnflzc.memorandumkotlin.model.Memorandum
import per.jxnflzc.memorandumkotlin.service.impl.MemorandumServiceImpl
import java.util.*

class EditViewModel (memorandum: Memorandum) : ViewModel() {
    var memorandum = MutableLiveData<Memorandum>()

    private val memorandumService = MemorandumServiceImpl()

    init {
        logger.d("EditViewModel", "init")
        this.memorandum.value = memorandum
    }

    fun setMemorandum(title: String, content: String) {
        memorandum.value?.title = title
        memorandum.value?.content = content
        memorandum.value?.date = Date()
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
}
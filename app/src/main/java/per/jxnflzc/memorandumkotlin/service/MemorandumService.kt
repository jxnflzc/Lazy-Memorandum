package per.jxnflzc.memorandumkotlin.service

import per.jxnflzc.memorandumkotlin.model.Memorandum

interface MemorandumService {
    fun delete(id: Long): Int

    fun getAllMemorandum(): ArrayList<Memorandum>

    fun getMemorandumByCatalog(catalogId: Long): ArrayList<Memorandum>

    fun getSearchMemorandum(query: String): ArrayList<Memorandum>

    fun update(memorandum: Memorandum): Int

    fun save(memorandum: Memorandum): Boolean
}
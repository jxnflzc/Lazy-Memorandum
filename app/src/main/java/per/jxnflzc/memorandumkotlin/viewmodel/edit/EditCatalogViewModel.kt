package per.jxnflzc.memorandumkotlin.viewmodel.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.litepal.LitePal
import org.litepal.extension.runInTransaction
import per.jxnflzc.memorandumkotlin.MemorandumKotlinApplication.Companion.logger
import per.jxnflzc.memorandumkotlin.extend.toIdString
import per.jxnflzc.memorandumkotlin.model.Catalog
import per.jxnflzc.memorandumkotlin.model.Memorandum
import per.jxnflzc.memorandumkotlin.service.impl.CatalogServiceImpl
import per.jxnflzc.memorandumkotlin.service.impl.MemorandumServiceImpl
import java.util.*

class EditCatalogViewModel (catalog: Catalog) : ViewModel() {
    var catalog = MutableLiveData<Catalog>()

    private val catalogService = CatalogServiceImpl()
    private val memorandumService = MemorandumServiceImpl()

    init {
        logger.d("EditCatalogViewModel", "init")
        this.catalog.value = catalog
    }

    fun setCatalog(name: String) {
        catalog.value?.name = name
    }

    fun save(): Boolean {
        return catalogService.save(catalog.value!!)
    }

    fun update(): Int {
        return catalogService.update(catalog.value!!)
    }

    fun delete(): Int {
        var num = 0

        LitePal.runInTransaction {
            num = catalogService.delete(catalog.value?.id!!)
            var result = true
            logger.d("EditCatalogViewModel", "1st result $result")
            for (m in memorandumService.getMemorandumByCatalog(catalog.value?.id!!)) {
                m.catalogId = 1
                logger.d("EditCatalogViewModel", "\n$m")
                result = result && (m.update(m.id) > 0)
                logger.d("EditCatalogViewModel", "m's result $result")
            }

            logger.d("EditCatalogViewModel", "result ${num > 0}")
            result && (num > 0)
        }
        return num
    }
}
package per.jxnflzc.memorandumkotlin.service

import per.jxnflzc.memorandumkotlin.model.Catalog
import per.jxnflzc.memorandumkotlin.model.Memorandum

interface CatalogService {
    fun getAllCatalog(): ArrayList<Catalog>

    fun getCatalog(id: Long): Catalog
}
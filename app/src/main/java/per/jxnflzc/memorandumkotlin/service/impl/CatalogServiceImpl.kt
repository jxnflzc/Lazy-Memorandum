package per.jxnflzc.memorandumkotlin.service.impl

import org.litepal.LitePal
import per.jxnflzc.memorandumkotlin.model.Catalog
import per.jxnflzc.memorandumkotlin.model.Memorandum
import per.jxnflzc.memorandumkotlin.service.CatalogService

class CatalogServiceImpl : CatalogService {
    override fun getAllCatalog(): ArrayList<Catalog> {
        return LitePal.findAll(Catalog::class.java) as ArrayList<Catalog>
    }

    override fun getCatalog(id: Long): Catalog {
        return LitePal.find(Catalog::class.java, id)
    }
}
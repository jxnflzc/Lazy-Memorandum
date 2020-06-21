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

    override fun update(catalog: Catalog): Int {
        return catalog.update(catalog.id)
    }

    override fun save(catalog: Catalog): Boolean {
        return catalog.save()
    }

    override fun delete(id: Long): Int {
        return LitePal.delete(
            Catalog::class.java,
            id
        )
    }
}
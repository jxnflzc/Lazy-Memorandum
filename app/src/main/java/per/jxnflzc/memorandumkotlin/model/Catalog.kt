package per.jxnflzc.memorandumkotlin.model

import org.litepal.crud.LitePalSupport
import per.jxnflzc.memorandumkotlin.MemorandumKotlinApplication
import per.jxnflzc.memorandumkotlin.R
import java.io.Serializable

class Catalog constructor(): LitePalSupport(), Serializable {
    var id: Long = 0
    var name: String = MemorandumKotlinApplication.context.getString(R.string.no_catalog)

    constructor(name: String): this() {
        this.name = name
    }

    override fun toString(): String {
        return "id: $id\nname: $name"
    }
}
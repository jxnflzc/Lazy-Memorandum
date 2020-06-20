package per.jxnflzc.memorandumkotlin.model

import org.litepal.crud.LitePalSupport
import per.jxnflzc.memorandumkotlin.extend.*
import java.io.Serializable
import java.util.*

class Memorandum constructor(): LitePalSupport(), Serializable {
    var id: Long = 0
    var title: String = ""
    lateinit var date: Date//date初始化在这里会导致无法修改的问题
    var content: String = ""
    var catalogId: Long = 1

    init {
        //date = Date()
        //mid = date.toIdString()
    }

    constructor(title: String, content: String): this() {
        this.date = Date()
        this.title = title
        this.content = content
    }

    override fun toString(): String {
        return "id: $id\ntitle: $title\ndate: $date\ncontent: $content\ncatalogId: $catalogId"
    }
}
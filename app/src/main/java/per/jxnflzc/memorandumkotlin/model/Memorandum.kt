package per.jxnflzc.memorandumkotlin.model

import org.litepal.crud.LitePalSupport
import per.jxnflzc.memorandumkotlin.extend.*
import java.io.Serializable
import java.util.*

class Memorandum constructor(): LitePalSupport(), Serializable {
    var id: Long = 0
    lateinit var mid: String
    var title: String = ""
    lateinit var date: Date//date初始化在这里会导致无法修改的问题
    var content: String = ""

    init {
        //date = Date()
        //mid = date.toIdString()
    }

    constructor(title: String, content: String): this() {
        this.date = Date()
        mid = date.toIdString()
        this.title = title
        this.content = content
    }

    override fun toString(): String {
        return "mid: $mid\ntitle: $title\ndate: $date\ncontent: $content"
    }
}
package per.jxnflzc.memorandumkotlin.service.impl

import org.litepal.LitePal
import per.jxnflzc.memorandumkotlin.extend.toIdString
import per.jxnflzc.memorandumkotlin.model.Memorandum
import per.jxnflzc.memorandumkotlin.service.MemorandumService

class MemorandumServiceImpl: MemorandumService {
    override fun delete(id: Long): Int {
        return LitePal.delete(
            Memorandum::class.java,
            id
        )
    }

    override fun getAllMemorandum(): ArrayList<Memorandum> {
        return LitePal.order("date desc").find(Memorandum::class.java) as ArrayList<Memorandum>
    }

    override fun getSearchMemorandum(query: String): ArrayList<Memorandum> {
        return LitePal.where("title like ? or content like ?", "%$query%", "%$query%").order("date desc").find(Memorandum::class.java) as ArrayList<Memorandum>
    }

    override fun update(memorandum: Memorandum): Int {
        return memorandum.update(memorandum.id)
    }

    override fun save(memorandum: Memorandum): Boolean {
        return memorandum.save()
    }
}
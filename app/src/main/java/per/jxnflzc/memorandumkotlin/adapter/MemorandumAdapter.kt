package per.jxnflzc.memorandumkotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import per.jxnflzc.memorandumkotlin.R
import per.jxnflzc.memorandumkotlin.activity.edit.EditMemorandumActivity
import per.jxnflzc.memorandumkotlin.extend.*
import per.jxnflzc.memorandumkotlin.model.EditType
import per.jxnflzc.memorandumkotlin.model.Memorandum
import per.jxnflzc.memorandumkotlin.service.impl.CatalogServiceImpl

class MemorandumAdapter(private val memorandumList: ArrayList<Memorandum>) :
        RecyclerView.Adapter<MemorandumAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtCatalog: TextView = view.findViewById(R.id.txtCatalog)
        var txtDate: TextView = view.findViewById(R.id.txtDate)
        var txtTitle: TextView = view.findViewById(R.id.txtTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.memorandum_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val memorandum = memorandumList[position]
            EditMemorandumActivity.activityStart(parent.context, EditType.EDIT, memorandum, 1)
        }
        viewHolder.itemView.setOnLongClickListener {
            val position = viewHolder.adapterPosition
            onRemoveListener.onRemove(position)
            true
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val memorandum = memorandumList[position]
        val catalogService = CatalogServiceImpl()
        val catalog = catalogService.getCatalog(memorandum.catalogId)
        if (catalog.id == 0L) {
            memorandum.catalogId = 1
        }
        holder.txtCatalog.text = catalogService.getCatalog(memorandum.catalogId).name
        holder.txtDate.text = memorandum.date.showDateInfo()
        holder.txtTitle.text = memorandum.title
    }

    override fun getItemCount() = memorandumList.size

    interface OnRemoveListener {
        fun onRemove(position: Int)
    }

    private lateinit var onRemoveListener: OnRemoveListener

    fun setOnRemoveListener(onRemoveListener: OnRemoveListener) {
        this.onRemoveListener = onRemoveListener
    }
}
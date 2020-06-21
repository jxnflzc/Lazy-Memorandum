package per.jxnflzc.memorandumkotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import per.jxnflzc.memorandumkotlin.R
import per.jxnflzc.memorandumkotlin.activity.edit.EditCatalogActivity
import per.jxnflzc.memorandumkotlin.activity.edit.EditMemorandumActivity
import per.jxnflzc.memorandumkotlin.model.Catalog
import per.jxnflzc.memorandumkotlin.model.EditType

class CatalogAdapter(private val catalogList: ArrayList<Catalog>) :
        RecyclerView.Adapter<CatalogAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtName: TextView = view.findViewById(R.id.txtName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.catalog_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onSearchListener.onSearch(catalogList[position].id)
        }
        viewHolder.itemView.setOnLongClickListener {
            val position = viewHolder.adapterPosition
            if (catalogList[position].id != 0L) {
                EditCatalogActivity.activityStart(parent.context, EditType.EDIT, catalogList[position], 2)
            }
            true
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val catalog = catalogList[position]
        holder.txtName.text = catalog.name
    }

    override fun getItemCount() = catalogList.size

    interface OnSearchListener {
        fun onSearch(catalogId: Long)
    }

    private lateinit var onSearchListener: OnSearchListener

    fun setOnSearchListener(onSearchListener: OnSearchListener) {
        this.onSearchListener = onSearchListener
    }
}
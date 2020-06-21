package per.jxnflzc.memorandumkotlin.adapter

import android.annotation.SuppressLint
import per.jxnflzc.memorandumkotlin.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import per.jxnflzc.memorandumkotlin.model.Catalog


class CatalogSpinnerAdapter : BaseAdapter {
    lateinit var catalogList: ArrayList<Catalog>
    lateinit var context: Context

    constructor(context: Context, catalogList: ArrayList<Catalog>) {
        this.context = context
        this.catalogList = catalogList
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.catalog_item, viewGroup, false)
        val txtName: TextView = view.findViewById(R.id.txtName)
        val catalog = getItem(position) as Catalog
        catalog.let {
            txtName.text = it.name
        }
        return view
    }

    override fun getItem(position: Int): Any {
        return catalogList[position]
    }

    override fun getItemId(position: Int): Long {
        return catalogList[position].id
    }

    override fun getCount(): Int {
        return catalogList.size
    }
}
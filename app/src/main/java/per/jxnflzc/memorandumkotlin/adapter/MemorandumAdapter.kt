package per.jxnflzc.memorandumkotlin.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import per.jxnflzc.memorandumkotlin.R
import per.jxnflzc.memorandumkotlin.activity.EditMemorandumActivity
import per.jxnflzc.memorandumkotlin.extend.*
import per.jxnflzc.memorandumkotlin.model.EditType
import per.jxnflzc.memorandumkotlin.model.Memorandum
import java.text.SimpleDateFormat
import java.util.*

class MemorandumAdapter(private val memorandumList: MutableList<Memorandum>) :
        RecyclerView.Adapter<MemorandumAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtMid: TextView = view.findViewById(R.id.txtMid)
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

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val memorandum = memorandumList[position]
        holder.txtMid.text = memorandum.mid
        holder.txtDate.text = memorandum.date.showDateInfo()
        holder.txtTitle.text = memorandum.title

    }

    override fun getItemCount() = memorandumList.size
}
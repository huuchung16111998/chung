package com.smile.studio.resoucemanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smile.studio.libsmilestudio.recyclerviewer.OnItemClickListenerRecyclerView
import com.smile.studio.resoucemanager.R
import com.smile.studio.resoucemanager.network.model.Resouce

class ResouceMappingAdapter(val mContext: Context, val mData: ArrayList<Resouce>) : RecyclerView.Adapter<ResouceMappingAdapter.ViewHolder>() {

    var onItemClick: OnItemClickListenerRecyclerView? = null

    fun removeItem(resouce: Resouce) {
        this.mData.remove(resouce)
        notifyDataSetChanged()
    }

    fun addAll(mData: ArrayList<Resouce>) {
        this.mData.addAll(mData)
        notifyDataSetChanged()
    }

    fun clear() {
        this.mData.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(container: ViewGroup, typeView: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.custom_item_resouce, container, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_title.text = mData.get(position).name
        holder.tv_description.text = mData.get(position).detail
        holder.tv_serial.text = mData.get(position).serial
        holder.tv_madein.text = mData.get(position).made_by
        holder.itemView.setOnClickListener {
            if (onItemClick != null) {
                onItemClick?.onClick(it, position)
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_title = view.findViewById<TextView>(R.id.tv_title)
        val tv_description = view.findViewById<TextView>(R.id.tv_description)
        val tv_serial = view.findViewById<TextView>(R.id.tv_serial)
        val tv_madein = view.findViewById<TextView>(R.id.tv_madein)
    }
}
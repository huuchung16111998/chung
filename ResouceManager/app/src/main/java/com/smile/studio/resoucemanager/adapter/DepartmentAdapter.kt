package com.smile.studio.resoucemanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.smile.studio.resoucemanager.R
import com.smile.studio.resoucemanager.network.model.Department

class DepartmentAdapter(val mContext: Context, val mData: ArrayList<Department>) : BaseAdapter() {

    fun addAll(mData: ArrayList<Department>) {
        this.mData.addAll(mData)
        notifyDataSetChanged()
    }

    fun clear() {
        this.mData.clear()
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = null
        var holder: ViewHolder? = null
        if (view == null) {
            //TODO
            view = LayoutInflater.from(mContext).inflate(R.layout.custom_item_spinner, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            //TODO
            holder = view.tag as ViewHolder?
        }
        holder?.tv_title?.text = mData.get(position).name
        return view!!
    }

    override fun getItem(position: Int): Department {
        return mData.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mData.size
    }


    class ViewHolder(val view: View) {
        val tv_title = view.findViewById<TextView>(R.id.tv_title)
    }
}